/**
 * 进货类
 */
package com.ivt.mis.model;

import com.ivt.mis.common.ObjectReflectUtil;

public class StoreProcurement extends BasePOJO implements Cloneable{
	private String id;
	private String providerId; // 供应商编号
	private String providerName; // 供应商名称
	private String customerId; // 预定客户编号
	private String customerName; // 预定客户名称
	private String productId; // 产品编号
	private String productCode; // 产品型号
	private String productName;// 产品名称
	private String productBrand;// 品牌
	private int proNumber; // 数量
	private double unitPrice; // 单价
	private String pack; // 包装
	private String miniPack; // 最小包装
	private String produceCode; // 生产批准号
	private String purchaseCode; // 采购单号
	private String userId; // 业务员
	private String dayCode; // 生产序列号
	private String createTime; // 录入时间
	private String comment; // 注释
	private String produceTime; // 生产日期
	private String groupId;// 组ID，用于区分同批次入库标记
	private String proType; //退货，采购
	private String unit; //计量单位
	private String currency; //币别

	// 如下字段暂时未用
	private String numberStr; // 临时变量，数据转换使用
	private String unitPriceStr;// 临时变量，数据转换使用
	private String startTime; // 临时变量，数据转换使用
	private String endTime; // 临时变量，数据转换使用
	private CustomizedUnit customizedUnit; // 临时变量，单位下拉列表
	
	private int totalNbr; //临时变量 库存量
	private int shipNbr; //临时变量 出库量
	private boolean selected; // 临时变量，前端展示使用
	
	/**
	 * 列表显示栏位标记 String[]{"显示名称","属性名","是否显示","是否必填"}
	 * 用于查询时显示
	 */
	private String[][] columns = new String[][] {
			new String[] { "入库批次号", "groupId", "Y", "Y"},
			new String[] { "入库编号", "id", "Y", "Y"},
			new String[] { "产品型号", "productCode", "Y", "Y"},
			new String[] { "产品名称", "productName", "Y", "Y"},
			new String[] { "品牌", "productBrand", "Y", "N"},
			new String[] { "采购单号", "purchaseCode", "Y", "Y"},
			new String[] { "生产批准号", "produceCode", "N", "N"},
			new String[] { "包装", "pack", "Y", "N"},
			new String[] { "最小包装", "miniPack", "Y", "N"},
			new String[] { "数量", "proNumber", "Y", "Y"},
			new String[] { "单位", "unit", "Y", "Y"},
			new String[] { "单价", "unitPrice", "Y", "Y"},
			new String[] { "币别", "currency", "Y", "Y"},
			new String[] { "供应商编号", "providerId", "Y", "Y"},
			new String[] { "供应商名称", "providerName", "Y", "Y"},
			new String[] { "预定客户编号", "customerId", "N", "N"},
			new String[] { "预定客户名称", "customerName", "N", "N"},
			new String[] { "生产日期", "produceTime", "Y", "N"},
			new String[] { "入库时间", "createTime", "Y", "N"},
			new String[] { "注释", "comment", "Y", "N"},
			new String[] { "操作员", "userId", "N", "Y"}};
	
	
	/**
	 * 列表显示栏位标记 String[]{"显示名称","属性名","是否显示","是否必填"}
	 * 用于入库时栏位
	 */
	private String[][] columnsDetail = new String[][] {
			new String[] { "入库编号", "id", "Y", "Y"},
			new String[] { "产品编号", "productId", "Y", "Y"},
			new String[] { "产品型号", "productCode", "Y", "Y"},
			new String[] { "产品名称", "productName", "Y", "Y"},
			new String[] { "品牌", "productBrand", "Y", "N"},
			new String[] { "包装", "pack", "N", "N"},
			new String[] { "最小包装", "miniPack", "Y", "N"},
			new String[] { "生产批准号", "produceCode", "N", "N"},
			new String[] { "入库数量", "proNumber", "Y", "Y"},
			new String[] { "单位", "customizedUnit.name", "Y", "Y"},
			new String[] { "单价", "unitPrice", "Y", "Y"},
			new String[] { "预定客户编号", "customerId", "N", "N"},
			new String[] { "生产日期", "produceTime", "Y", "N"}};
	
	/**
	 * 列表显示栏位标记 String[]{"显示名称","属性名","是否显示","是否必填"}
	 * 用于出库时栏位
	 * 入库编号", "产品编号", "产品型号", "产品名称", "品牌",
				"生产批准号", "生产日期", "包装", "最小包装","剩余库存", "本次出库量", "单位", "选择" 
	 */
	private String[][] columnsForShipment = new String[][] {
			new String[] { "入库编号", "id", "Y", "Y"},
			new String[] { "产品编号", "productId", "Y", "Y"},
			new String[] { "产品型号", "productCode", "Y", "Y"},
			new String[] { "产品名称", "productName", "Y", "Y"},
			new String[] { "品牌", "productBrand", "Y", "Y"},
			new String[] { "包装", "pack", "N", "N"},
			new String[] { "最小包装", "miniPack", "Y", "N"},
			new String[] { "生产批准号", "produceCode", "N", "N"},
			new String[] { "生产日期", "produceTime", "Y", "N"},
			new String[] { "剩余数量", "totalNbr", "Y", "Y"},
			new String[] { "本次出库数量", "shipNbr", "Y", "Y"},
			new String[] { "单位", "unit", "Y", "Y"},
			new String[] { "选择", "selected", "Y", "Y"}};
	public StoreProcurement(){
		super();
		super.columns = columns;;
	}
	public StoreProcurement(String id){
		this.id = id;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof StoreProcurement)) {
			return false;
		}

		StoreProcurement other = (StoreProcurement) o;
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
//		if (columnName.equalsIgnoreCase("customizedUnit")){
//			return this.getcustomizedUnit();
//		}else{
			return ObjectReflectUtil.getFieldValue(this, columnName);
//		}
		
	}

	/**
	 * 表格控件调用
	 * 
	 * @param columnNumber
	 *            列号
	 * @return 对应列的值
	 */
	public void setValueAt(Object value, String columnName) {
//		if (columnName.equalsIgnoreCase("customizedUnit")){
//			this.setCustomizedUnit((CustomizedUnit)value);
//		}else{
			ObjectReflectUtil.setFieldValue(this, columnName, value);
//		}
		
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
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

	public String getNumberStr() {
		return numberStr;
	}

	public void setNumberStr(String numberStr) {
		this.numberStr = numberStr;
	}

	public String getUnitPriceStr() {
		return unitPriceStr;
	}

	public void setUnitPriceStr(String unitPriceStr) {
		this.unitPriceStr = unitPriceStr;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProduceCode() {
		return produceCode;
	}

	public void setProduceCode(String produceCode) {
		this.produceCode = produceCode;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getProNumber() {
		return proNumber;
	}

	public void setProNumber(int proNumber) {
		this.proNumber = proNumber;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getDayCode() {
		return dayCode;
	}

	public void setDayCode(String dayCode) {
		this.dayCode = dayCode;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductBrand() {
		return productBrand;
	}

	public void setProductBrand(String productBrand) {
		this.productBrand = productBrand;
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
	public int getTotalNbr() {
		return totalNbr;
	}
	public void setTotalNbr(int totalNbr) {
		this.totalNbr = totalNbr;
	}
	public int getShipNbr() {
		return shipNbr;
	}
	public void setShipNbr(int shipNbr) {
		this.shipNbr = shipNbr;
	}
	public boolean getSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getProType() {
		return proType;
	}
	public void setProType(String proType) {
		this.proType = proType;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
		this.customizedUnit = new CustomizedUnit(this.unit);
	}
	public String[][] getColumns() {
		return columns;
	}
	public void setColumns(String[][] columns) {
		this.columns = columns;
	}
	public String getMiniPack() {
		return miniPack;
	}
	public void setMiniPack(String miniPack) {
		this.miniPack = miniPack;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public CustomizedUnit getCustomizedUnit() {
		if (this.customizedUnit == null){
			return new CustomizedUnit("");
		}
		return this.customizedUnit;
	}
	public void setCustomizedUnit(CustomizedUnit customizedUnit) {
		this.customizedUnit = customizedUnit;
		this.unit = customizedUnit.getName();
	}

	public StoreProcurement clone() {
		StoreProcurement o = null;
		try {
			o = (StoreProcurement) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}
	public String[][] getColumnsDetail() {
		return columnsDetail;
	}
	public void setColumnsDetail(String[][] columnsDetail) {
		this.columnsDetail = columnsDetail;
	}
	public String[][] getColumnsForShipment() {
		return columnsForShipment;
	}
	public void setColumnsForShipment(String[][] columnsForShipment) {
		this.columnsForShipment = columnsForShipment;
	}
}
