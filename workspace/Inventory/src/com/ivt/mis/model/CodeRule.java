package com.ivt.mis.model;

import java.util.Date;

public class CodeRule extends BasePOJO {
	private String objectType; //对象类型
	private String objectName; //对象名称
	private Boolean available;//是否启用
	private Boolean autoIncrease; //是否启动自动编码
	private Boolean prefixEnabled; //是否启用前缀
	private String prefix; //前缀
	private Boolean suffixEanbled; //是否启用后缀
	private String suffix;//后缀
	private int nbrOfSeq;//序列号长度
	private String seqType;//序列号类型 如DATE, DATETIME,TIME,NUMBER
	private String seqFormat;//序列号格式，针对日期时间类有效，如yyMMddHHmmss
	private String currentSeq;//当前序列号
	private String comments;//备注
	private Date createTime;
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public Boolean getAutoIncrease() {
		return autoIncrease;
	}
	public void setAutoIncrease(Boolean autoIncrease) {
		this.autoIncrease = autoIncrease;
	}
	public Boolean getPrefixEnabled() {
		return prefixEnabled;
	}
	public void setPrefixEnabled(Boolean prefixEnabled) {
		this.prefixEnabled = prefixEnabled;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public Boolean getSuffixEanbled() {
		return suffixEanbled;
	}
	public void setSuffixEanbled(Boolean suffixEanbled) {
		this.suffixEanbled = suffixEanbled;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public int getNbrOfSeq() {
		return nbrOfSeq;
	}
	public void setNbrOfSeq(int nbrOfSeq) {
		this.nbrOfSeq = nbrOfSeq;
	}
	public String getSeqType() {
		return seqType;
	}
	public void setSeqType(String seqType) {
		this.seqType = seqType;
	}
	public String getSeqFormat() {
		return seqFormat;
	}
	public void setSeqFormat(String seqFormat) {
		this.seqFormat = seqFormat;
	}
	public String getCurrentSeq() {
		return currentSeq;
	}
	public void setCurrentSeq(String currentSeq) {
		this.currentSeq = currentSeq;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Boolean getAvailable() {
		return available;
	}
	public void setAvailable(Boolean available) {
		this.available = available;
	}
}
