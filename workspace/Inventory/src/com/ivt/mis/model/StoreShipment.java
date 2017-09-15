/**
 * 销售表
 */

package com.ivt.mis.model;

import com.ivt.mis.common.ObjectReflectUtil;

public class StoreShipment extends BasePOJO  implements Cloneable{
	private String id;
	private String groupId;// 组ID，用于区分同批次入库标记
	private String storeProcurementId; // 进货单识别码
	private String productId; // 产品编号
	private String productCode; // 产品型号
	private String productName; // 产品名称
	private String productBrand; // 产品品牌
	private String customerId; // 客户编号
	private String customerName;// 客户名称
	private double unitPrice;// 销售单价
	private String pack; // 包装
	private String miniPack; // 最小包装
	private String produceCode; // 生产批准号
	private String sellCode; // 销售单号
	private int shipNumber; // 数量
	private String createTime; // 时间
	private String userId; // 操作员
	private String comment; // 注释
	private String produceTime; // 生产日期
	private String shipType; //退货，销售
	private String unit; //计量单位
	private String expressNbr; //快递单号
	private String currency; //币别
	private String payDueDate; //应付日期
	private String actPayDueDate; //实付日期


	private String startTime; // 临时变量，数据转换使用
	private String endTime; // 临时变量，数据转换使用

	private CustomizedUnit customizedUnit; // 临时变量，单位下拉列表
	
	/**
	 * 列表显示栏位标记 String[]{"显示名称","属性名","是否显示","是否必填"}
	 */
	private String[][] columns = new String[][] {
			new String[] { "出库批次号", "groupId", "Y", "Y"},
			new String[] { "出库编号", "id", "Y", "Y"},
			new String[] { "入库编号", "storeProcurementId", "Y", "Y"},
			new String[] { "销售单号", "sellCode", "Y", "Y"},
			new String[] { "快递单号", "expressNbr", "Y", "N"},
			new String[] { "产品型号", "productCode", "N", "Y"},
			new String[] { "产品名称", "productName", "Y", "Y"},
			new String[] { "品牌", "productBrand", "Y", "N"},
			new String[] { "生产批准号", "produceCode", "N", "N"},
			new String[] { "包装", "pack", "N", "N"},
			new String[] { "最小包装", "miniPack", "N", "N"},
			new String[] { "数量", "shipNumber", "Y", "Y"},
			new String[] { "单位", "unit", "Y", "Y"},
			new String[] { "单价", "unitPrice", "Y", "Y"},
			new String[] { "币别", "currency", "Y", "Y"},
			new String[] { "客户编号", "customerId", "Y", "Y"},
			new String[] { "客户名称", "customerName", "Y", "Y"},
			new String[] { "生产日期", "produceTime", "N", "N"},
			new String[] { "销售时间", "createTime", "Y", "N"},
			new String[] { "单据类型", "shipType", "Y", "Y"},
			new String[] { "应付日期", "payDueDate", "Y", "Y"},
			new String[] { "实付日期", "actPayDueDate", "Y", "N"},
			new String[] { "注释", "comment", "N", "N"},
			new String[] { "操作员", "userId", "N", "Y"}};
	
	/**
	 * 列表显示栏位标记 String[]{"显示名称","属性名","是否显示","是否必填"}
	 * 用于出库明细
	 */
	private String[][] columnsDetail = new String[][] {
			new String[] { "出库编号", "id", "Y", "Y"},
			new String[] { "产品编号", "productId", "N", "Y"},
			new String[] { "产品型号", "productCode", "N", "Y"},
			new String[] { "产品名称", "productName", "Y", "Y"},
			new String[] { "品牌", "productBrand", "Y", "N"},
			new String[] { "生产批准号", "produceCode", "Y", "N"},
			new String[] { "生产日期", "produceTime", "N", "N"},
			new String[] { "包装", "pack", "N", "N"},
			new String[] { "最小包装", "miniPack", "N", "N"},
			new String[] { "出库数量", "shipNumber", "Y", "Y"},
			new String[] { "单位", "customizedUnit.name", "Y", "Y"},
			new String[] { "单价", "unitPrice", "Y", "Y"}};
	
	/**
	 * 列表显示栏位标记 String[]{"显示名称","属性名","是否显示","是否必填"}
	 * 用于出库汇总
	 */
	private String[][] columnsSummary = new String[][] {
			new String[] { "产品编号", "productId", "N", "Y"},
			new String[] { "产品型号", "productCode", "N", "Y"},
			new String[] { "产品名称", "productName", "Y", "Y"},
			new String[] { "品牌", "productBrand", "Y", "N"},
			new String[] { "出库总量", "shipNumber", "Y", "Y"}};
	
	//"产品型号", "产品名称", "出库总量"
	public StoreShipment() {
		super();
		super.columns = columns;
	}

	public StoreShipment(String id){
		this.id = id;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof StoreShipment)) {
			return false;
		}

		StoreShipment other = (StoreShipment) o;
		if (this.id != other.id) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		return this.id.hashCode();
	}

	
	/**
	 * 表格控件调用
	 * 
	 * @param columnNumber
	 *            列号
	 * @return 对应列的值
	 */
	public Object getValueAt(String columnName) {
		if (columnName.equalsIgnoreCase("customizedUnit")){
			return this.getCustomizedUnit();
		}else{
			return ObjectReflectUtil.getFieldValue(this, columnName);
		}
		
	}

	/**
	 * 表格控件调用
	 * 
	 * @param columnNumber
	 *            列号
	 * @return 对应列的值
	 */
	public void setValueAt(Object value, String columnName) {
		ObjectReflectUtil.setFieldValue(this, columnName, String.valueOf(value));
	}

	public String getStoreProcurementId() {
		return storeProcurementId;
	}

	public void setStoreProcurementId(String storeProcurementId) {
		this.storeProcurementId = storeProcurementId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public String getProduceCode() {
		return produceCode;
	}

	public void setProduceCode(String produceCode) {
		this.produceCode = produceCode;
	}

	public String getSellCode() {
		return sellCode;
	}

	public void setSellCode(String sellCode) {
		this.sellCode = sellCode;
	}

	public int getShipNumber() {
		return shipNumber;
	}

	public void setShipNumber(int shipNumber) {
		this.shipNumber = shipNumber;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getProduceTime() {
		return produceTime;
	}

	public void setProduceTime(String produceTime) {
		this.produceTime = produceTime;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getProductBrand() {
		return productBrand;
	}

	public void setProductBrand(String productBrand) {
		this.productBrand = productBrand;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
		this.customizedUnit = new CustomizedUnit(unit);
	}

	public String[][] getColumns() {
		return columns;
	}

	public void setColumns(String[][] columns) {
		this.columns = columns;
	}

	public String getExpressNbr() {
		return expressNbr;
	}

	public void setExpressNbr(String expressNbr) {
		this.expressNbr = expressNbr;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPayDueDate() {
		return payDueDate;
	}

	public void setPayDueDate(String payDueDate) {
		this.payDueDate = payDueDate;
	}

	public String getActPayDueDate() {
		return actPayDueDate;
	}

	public void setActPayDueDate(String actPayDueDate) {
		this.actPayDueDate = actPayDueDate;
	}

	public CustomizedUnit getCustomizedUnit() {
		return this.customizedUnit;
	}
	public void setCustomizedUnit(CustomizedUnit customizedUnit) {
		this.customizedUnit = customizedUnit;
		this.unit = customizedUnit.getName();
	}


	public StoreShipment clone() {
		StoreShipment o = null;
		try {
			o = (StoreShipment) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	public String getMiniPack() {
		return miniPack;
	}

	public void setMiniPack(String miniPack) {
		this.miniPack = miniPack;
	}

	public String[][] getColumnsDetail() {
		return columnsDetail;
	}

	public void setColumnsDetail(String[][] columnsDetail) {
		this.columnsDetail = columnsDetail;
	}

	public String[][] getColumnsSummary() {
		return columnsSummary;
	}

	public void setColumnsSummary(String[][] columnsSummary) {
		this.columnsSummary = columnsSummary;
	}
}
