package yoncey.gdkm.weixin.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "wx_user")
public class User {

	public static enum Status {
		IS_SUBSCRIBE, IS_UNSUBSCRIBE;
	}

	@Id
	@Column(length = 36)
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@GeneratedValue(generator = "uuid2")
	private String id;

	@Enumerated(EnumType.STRING)
	private Status status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date subTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date unsubTime;

	private String subscribe;
	@JsonProperty("openid")
	private String openId;
	@JsonProperty("nickname")
	private String nickName;
	private String sex;
	private String city;
	private String country;
	private String province;
	private String language;
	@JsonProperty("headimgurl")
	private String headImageURL;
	@JsonProperty("subscribe_time")
	private long subscribeTime;
	@JsonProperty("unionid")
	private String unionId;
	private String remark;
	@JsonProperty("groupid")
	private String groupId;
	@JsonProperty("tagid_list")
	@Transient
	private String[] tagIdList;
	@JsonProperty("subscribe_scene")
	private String subscribeScene;
	@JsonProperty("qr_scene")
	private String qrScene;
	@JsonProperty("qr_scene_str")
	private String qrSceneStr;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getSubTime() {
		return subTime;
	}

	public void setSubTime(Date subTime) {
		this.subTime = subTime;
	}

	public Date getUnsubTime() {
		return unsubTime;
	}

	public void setUnsubTime(Date unsubTime) {
		this.unsubTime = unsubTime;
	}

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getHeadImageURL() {
		return headImageURL;
	}

	public void setHeadImageURL(String headImageURL) {
		this.headImageURL = headImageURL;
	}

	public long getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(long subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String[] getTagIdList() {
		return tagIdList;
	}

	public void setTagIdList(String[] tagIdList) {
		this.tagIdList = tagIdList;
	}

	public String getSubscribeScene() {
		return subscribeScene;
	}

	public void setSubscribeScene(String subscribeScene) {
		this.subscribeScene = subscribeScene;
	}

	public String getQrScene() {
		return qrScene;
	}

	public void setQrScene(String qrScene) {
		this.qrScene = qrScene;
	}

	public String getQrSceneStr() {
		return qrSceneStr;
	}

	public void setQrSceneStr(String qrSceneStr) {
		this.qrSceneStr = qrSceneStr;
	}

}
