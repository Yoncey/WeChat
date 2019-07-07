package yoncey.gdkm.weixin.service;

import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXB;

import yoncey.gdkm.weixin.domain.InMessage;
import yoncey.gdkm.weixin.domain.text.*;

public class MessageConvertHelper {

	private static final Map<String, Class<? extends InMessage>> typeMap = new ConcurrentHashMap<>();
	static {
		typeMap.put("text", TextInMessage.class);
		typeMap.put("image", ImageInMessage.class);
		typeMap.put("vioce", TextInMessage.class);
		typeMap.put("video", TextInMessage.class);
		typeMap.put("location", TextInMessage.class);
		typeMap.put("event", EventInMessage.class);
		typeMap.put("link", TextInMessage.class);
		typeMap.put("shortvideo", TextInMessage.class);
	}

	public static Class<? extends InMessage> getClass(String xml) {
		String type = xml.substring(xml.indexOf("<MsgType><![CDATA[") + 18);
		type = type.substring(0, type.indexOf("]"));
		Class<? extends InMessage> c = typeMap.get(type);
		return c;
	}

	public static <T extends InMessage> T convert(String xml) {
		Class<? extends InMessage> c = getClass(xml);
		if (c == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		T msg = (T) JAXB.unmarshal(new StringReader(xml), c);

		return msg;
	}
}
