package com.ivt.mis.model;

import java.util.List;

public class PurchaseOrder extends BasePOJO implements Cloneable {
	private String id; //采购单号
	private String oppsiteId;//对方单号
	private String orderType;//采购单类型
	private String providerId;//供应商编号
	private String providerName;//供应商名称
	private int available; //是否可用
	private double exchangeRate; //汇率
	private double totalAmount; //总额
	private double depositeRate; //订金比例
	private String invoiceType; //发票类型
	private double invoiceRate; //发票税率
	private String contact; //联系人
	private String telephone; //电话
	private String fax; //传真
	private String email; //邮件
	private String ownerCompanyName; //甲方公司名称
	private String ownerDeptName; //甲方部门名称
	private String ownerSaleMan; //甲方业务员
	private String ownerSignedDate; //甲方签定日期
	private String transportType; //运送方式
	private double transportCost; //运送费用
	private double otherCost; //其他费用
	private double discount; //折扣
	private double deliveryDays; //交付期限
	private String maxDelayDate; //最晚交付日期
	private String payDate; //付款日期
	private String currency; //币别
	private String comments; //备注
	private String createPerson; //创建人
	private String createTime; //创建时间
	private String updatePerson; //更新人
	private String updateTime; //更新时间
	
	private List orderItems; //临时变量，采购单明细
	/**
	 * 列表显示栏位标记 String[]{"显示名称","属性名","是否显示","是否必填"}
	 */
	private String[][] columns = new String[][] {
			new String[] { "序号", "index", "N", "N" },
			new String[] { "采购单号", "id", "Y", "Y" },
			new String[] { "对方单号", "oppsiteId", "Y", "N" },
			new String[] { "采购单类型", "orderType", "Y", "Y" }, 
			new String[] { "供应商编码", "providerId", "Y", "Y" }, 
			new String[] { "供应商名称", "providerName", "Y", "Y" }, 
			new String[] { "汇率", "exchangeRate", "N", "N" },
			new String[] { "总额", "totalAmount", "Y", "N" },
			new String[] { "订金比例", "depositeRate", "Y", "N" },
			new String[] { "发票类型", "invoiceType", "N", "N" }, 
			new String[] { "发票日期", "invoiceRate", "N", "N" },
			new String[] { "联系人", "contact", "Y", "Y" }, 
			new String[] { "电话", "telephone", "Y", "Y" },
			new String[] { "传真", "fax", "Y", "N" },
			new String[] { "电子邮件", "email", "Y", "N" }, 
			new String[] { "甲方公司名称", "ownerCompanyName", "Y", "Y" },
			new String[] { "甲方部门名称", "ownerDeptName", "N", "N" },
			new String[] { "甲方业务员", "ownerSaleMan", "Y", "Y" },
			new String[] { "甲方签定日期", "ownerSignedDate", "N", "N" },
			new String[] { "运送类型", "transportType", "N", "N" },
			new String[] { "运送费用", "transportCost", "N", "N" },
			new String[] { "其他费用", "otherCost", "N", "N" },
			new String[] { "折扣", "discount", "Y", "N" },
			new String[] { "交付期", "deliveryDays", "Y", "Y" },
			new String[] { "最晚交付日期", "maxDelayDate", "N", "N" },
			new String[] { "付款日期", "payDate", "Y", "N" },
			new String[] { "币别", "currency", "Y", "Y" },
			new String[] { "备注", "comments", "Y", "N" },
			new String[] { "创建人", "createPerson", "Y", "N" },
			new String[] { "创建时间", "createTime", "N", "N" },
			new String[] { "更新人", "updatePerson", "N", "N" },
			new String[] { "更新时间", "updateTime", "N", "N" }  };

	public PurchaseOrder clone() {
		PurchaseOrder o = null;
		try {
			o = (PurchaseOrder) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	public String getOppsiteId() {
		return oppsiteId;
	}

	public void setOppsiteId(String oppsiteId) {
		this.oppsiteId = oppsiteId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getDepositeRate() {
		return depositeRate;
	}

	public void setDepositeRate(double depositeRate) {
		this.depositeRate = depositeRate;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public double getInvoiceRate() {
		return invoiceRate;
	}

	public void setInvoiceRate(double invoiceRate) {
		this.invoiceRate = invoiceRate;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOwnerCompanyName() {
		return ownerCompanyName;
	}

	public void setOwnerCompanyName(String ownerCompanyName) {
		this.ownerCompanyName = ownerCompanyName;
	}

	public String getOwnerDeptName() {
		return ownerDeptName;
	}

	public void setOwnerDeptName(String ownerDeptName) {
		this.ownerDeptName = ownerDeptName;
	}

	public String getOwnerSaleMan() {
		return ownerSaleMan;
	}

	public void setOwnerSaleMan(String ownerSaleMan) {
		this.ownerSaleMan = ownerSaleMan;
	}

	public String getOwnerSignedDate() {
		return ownerSignedDate;
	}

	public void setOwnerSignedDate(String ownerSignedDate) {
		this.ownerSignedDate = ownerSignedDate;
	}

	public String getTransportType() {
		return transportType;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	public double getTransportCost() {
		return transportCost;
	}

	public void setTransportCost(double transportCost) {
		this.transportCost = transportCost;
	}

	public double getOtherCost() {
		return otherCost;
	}

	public void setOtherCost(double otherCost) {
		this.otherCost = otherCost;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getDeliveryDays() {
		return deliveryDays;
	}

	public void setDeliveryDays(double deliveryDays) {
		this.deliveryDays = deliveryDays;
	}

	public String getMaxDelayDate() {
		return maxDelayDate;
	}

	public void setMaxDelayDate(String maxDelayDate) {
		this.maxDelayDate = maxDelayDate;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdatePerson() {
		return updatePerson;
	}

	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[][] getColumns() {
		return columns;
	}

	public void setColumns(String[][] columns) {
		this.columns = columns;
	}

	public List getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List orderItems) {
		this.orderItems = orderItems;
	}
}
