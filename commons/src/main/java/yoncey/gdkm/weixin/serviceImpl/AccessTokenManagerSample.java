package yoncey.gdkm.weixin.serviceImpl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import yoncey.gdkm.weixin.domain.ResponseError;
import yoncey.gdkm.weixin.domain.ResponseMessage;
import yoncey.gdkm.weixin.domain.ResponseToken;
import yoncey.gdkm.weixin.service.AccessTokenManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AccessTokenManagerSample implements AccessTokenManager {

	private static final Logger LOG = LoggerFactory.getLogger(AccessTokenManagerSample.class);
	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, ResponseToken> redisTemplate;

	@Override
	public String getToken(String account) throws RuntimeException {
		String key = "wx_access_token";
		ResponseToken token = redisTemplate.boundValueOps(key).get();
		if (token == null) {
			for (int i = 0; i < 10; i++) {
				LOG.trace("数据库里面没有令牌，需要重新获取，正在获取分布式事务锁...");
				Boolean locked = redisTemplate.boundValueOps(key + "_lock").setIfAbsent(new ResponseToken(), 1,
						TimeUnit.MINUTES);
				LOG.trace("获取分布式事务锁结束，结果：{}", locked);
				if (locked != null && locked == true) {
					try {
						token = redisTemplate.boundValueOps(key).get();
						if (token == null) {
							LOG.trace("调用远程接口获取令牌");
							token = getRemoteToken(account);
							redisTemplate.boundValueOps(key).set(token, token.getExpiresIn(), TimeUnit.SECONDS);
						}
					} finally {
						redisTemplate.delete(key + "_lock");
						synchronized (this) {
							this.notifyAll();
						}
					}
					break;
				} else {
					synchronized (this) {
						try {
							this.wait(60 * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		if (token == null) {
			throw new RuntimeException("无法获得访问令牌");
		}
		return token.getToken();
	}

	private ResponseToken getRemoteToken(String account) {
		String appid = "wxb4f0429adc036c5a"; 
		String appSecret = "9b437414f8b21a3b84424f658777aa02";
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"//
				+ "&appid=" + appid//
				+ "&secret=" + appSecret;
		HttpClient hc = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
		ResponseMessage msg;
		try {
			HttpResponse<String> response = hc.send(request, BodyHandlers.ofString(Charset.forName("UTF-8")));
			String body = response.body();
			LOG.trace("调用远程接口的返回值:\n{}", body);
			if (body.contains("errcode")) {
				msg = objectMapper.readValue(body, ResponseError.class);
				msg.setStatus(2);
			} else {
				msg = objectMapper.readValue(body, ResponseToken.class);
				msg.setStatus(1);
			}
			if (msg.getStatus() == 1) {
				return (ResponseToken) msg;
			}
		} catch (Exception e) {
			throw new RuntimeException("获取访问令牌出现问题：" + e.getLocalizedMessage(), e);
		}

		throw new RuntimeException("获取访问令牌出现问题，" + "错误代码=" + ((ResponseError) msg).getErrorCode() + "，错误信息="
				+ ((ResponseError) msg).getErrorMessage());
	}
}
