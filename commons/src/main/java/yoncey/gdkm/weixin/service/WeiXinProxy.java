package yoncey.gdkm.weixin.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

import yoncey.gdkm.weixin.domain.User;
import yoncey.gdkm.weixin.domain.text.TextOutMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WeiXinProxy {

	private static final Logger LOG = LoggerFactory.getLogger(WeiXinProxy.class);
	private ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private AccessTokenManager accessTokenManager;
	private HttpClient httpClient = HttpClient.newBuilder().build();

	public User getUser(String account, String openId) {

		String accessToken = accessTokenManager.getToken(account);

		String url = "https://api.weixin.qq.com/cgi-bin/user/info"//
				+ "?access_token=" + accessToken//
				+ "&openid=" + openId//
				+ "&lang=zh_CN";
		HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
		try {
			HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString(Charset.forName("UTF-8")));
			String body = response.body();
			LOG.trace("调用远程接口返回的内容：\n{}", body);

			if (!body.contains("errcode")) {
				User user = objectMapper.readValue(body, User.class);
				return user;
			}
		} catch (Exception e) {
			LOG.error("调用远程接口出现错误：" + e.getLocalizedMessage(), e);
		}
		return null;
	}

	public void sendText(String account, String openId, String text) {
		TextOutMessage out = new TextOutMessage(openId, text);

		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
		try {
			String json = this.objectMapper.writeValueAsString(out);
			this.post(url, json);
		} catch (JsonProcessingException e) {
			LOG.error("发送消息出现问题：" + e.getLocalizedMessage(), e);
		}
	}

	public void createMenu(String json) {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
		this.post(url, json);
	}

	private void post(String url, String json) {
		LOG.trace("以POST方式发送信息给微信公众号，内容：\n{}", json);
		String accessToken = accessTokenManager.getToken(null);
		url = url + accessToken;
		HttpRequest request = HttpRequest.newBuilder(URI.create(url))
				.POST(BodyPublishers.ofString(json, Charset.forName("UTF-8"))).build();
		CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(request,
				BodyHandlers.ofString(Charset.forName("UTF-8")));
		future.thenAccept(response -> {
			LOG.trace("POST方式发送信息给微信公众号服务器返回的结果：\n{}", response.body());
		});
	}
}
