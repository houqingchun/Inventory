package com.ivt.mis.model;

public class PurchaseOrderItem extends BasePOJO implements Cloneable {
	private String id ;//编码
	private String purchaseOrderId;//采购单编号
	private String productId;//产品编号
	private int buyNbr;//采购数量
	private String unit;//单位
	private double unitPrice;//单价
	private double totalAmount;//总额
	private int deliveryDays;//交付期限
	private String comments;//备注

	/**
	 * 列表显示栏位标记 String[]{"显示名称","属性名","是否显示","是否必填"}
	 */
	private String[][] columns = new String[][] {
			new String[] { "序号", "id", "N", "Y" },
			new String[] { "采购单号", "purchaseOrderId", "Y", "Y" },
			new String[] { "产品编码", "productId", "Y", "Y" },
			new String[] { "采购数量", "buyNbr", "Y", "Y" },
			new String[] { "单位", "unit", "Y", "Y" },
			new String[] { "单价", "unitPrice", "Y", "N" },
			new String[] { "交付期", "deliveryDays", "Y", "N" },
			new String[] { "备注", "comments", "Y", "N" }
	};
	
	public PurchaseOrderItem clone() {
		PurchaseOrderItem o = null;
		try {
			o = (PurchaseOrderItem) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(String purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getBuyNbr() {
		return buyNbr;
	}

	public void setBuyNbr(int buyNbr) {
		this.buyNbr = buyNbr;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getDeliveryDays() {
		return deliveryDays;
	}

	public void setDeliveryDays(int deliveryDays) {
		this.deliveryDays = deliveryDays;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String[][] getColumns() {
		return columns;
	}

	public void setColumns(String[][] columns) {
		this.columns = columns;
	}
}
