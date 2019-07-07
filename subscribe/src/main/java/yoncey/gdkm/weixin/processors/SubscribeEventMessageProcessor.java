package yoncey.gdkm.weixin.processors;

import java.util.Date;

import yoncey.gdkm.weixin.domain.User;
import yoncey.gdkm.weixin.domain.text.EventInMessage;
import yoncey.gdkm.weixin.processors.EventMessageProcessor;
import yoncey.gdkm.weixin.repository.UserRepository;
import yoncey.gdkm.weixin.service.WeiXinProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("subscribeMessageProcessor")
public class SubscribeEventMessageProcessor implements EventMessageProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(SubscribeEventMessageProcessor.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WeiXinProxy weiXinProxy;

	@Override
	public void onMessage(EventInMessage msg) {
		LOG.trace("关注消息处理器: " + msg);
		String account = msg.getToUserName();
		String openId = msg.getFromUserName();
		User user = this.userRepository.findByOpenId(openId);
		if (user == null || user.getStatus() != User.Status.IS_SUBSCRIBE) {
			User wxUser = weiXinProxy.getUser(account, openId);
			if (wxUser == null) {
				return;
			}
			if (user != null) {
				wxUser.setId(user.getId());
				wxUser.setSubTime(user.getSubTime());
			} else {
				wxUser.setSubTime(new Date());
			}
			wxUser.setStatus(User.Status.IS_SUBSCRIBE);
			this.userRepository.save(wxUser);

			this.weiXinProxy.sendText(account, openId, "欢迎关注我的公众号，回复菜单可以获得人工智能菜单");
		}
	}
}
