package yoncey.gdkm.weixin.processors;

import yoncey.gdkm.weixin.domain.text.EventInMessage;

public interface EventMessageProcessor {

	void onMessage(EventInMessage msg);
}
