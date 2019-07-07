package yoncey.gdkm.weixin.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class InMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("ToUserName")
	private String toUserName;

	@JsonProperty("FromUserName")
	private String fromUserName;

	@JsonProperty("CreateTime")
	private long createTime;

	@JsonProperty("MsgType")
	private String msgType;

	@JsonProperty("MsgId")
	private Long msgId;

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	abstract public String toString();
}
