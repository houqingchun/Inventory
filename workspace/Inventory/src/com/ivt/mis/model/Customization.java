package com.ivt.mis.model;

public class Customization extends BasePOJO {
	private String companyName;
	private String contacts;
	private String mobile;
	private String fax;
	private String address;
	private String email;
	private String logoPath;
	private String sn;
	private String signature;
	private int isTrial;
	private int expiredDays;
	private String comments;
	private String createTime;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getIsTrial() {
		return isTrial;
	}

	public void setIsTrial(int isTrial) {
		this.isTrial = isTrial;
	}

	public int getExpiredDays() {
		return expiredDays;
	}

	public void setExpiredDays(int expiredDays) {
		this.expiredDays = expiredDays;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
}
