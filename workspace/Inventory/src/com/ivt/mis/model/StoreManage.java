package com.ivt.mis.model;

import com.ivt.mis.common.ObjectReflectUtil;

public class StoreManage extends BasePOJO {
	private String id;
	private int totalNbr;
	private String productId;
	private String storeProcurementId;
	private String productCode;
	private String productBrand;

	private String index;// UI显示 临时使用
	
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String[][] getColumns() {
		return columns;
	}

	public void setColumns(String[][] columns) {
		this.columns = columns;
	}

	public StoreManage() {
		super();
		super.columns = columns;
	}
	

	/**
	 * 列表显示栏位标记 String[]{"显示名称","属性名","是否显示","是否必填"}
	 */
	String[][] columns = new String[][] {
			new String[] { "序号", "index", "N", "N" },
			new String[] { "库存编号", "id", "Y", "Y" },
			new String[] { "入库编号", "storeProcurementId", "Y", "Y" },
			new String[] { "产品编号", "productId", "Y", "Y" },
			new String[] { "产品型号", "productCode", "Y", "Y" },
			new String[] { "品牌", "productBrand", "Y", "Y" },
			new String[] { "库存量", "totalNbr", "Y", "Y" }
			};

	public int getTotalNbr() {
		return totalNbr;
	}

	public void setTotalNbr(int totalNbr) {
		this.totalNbr = totalNbr;
	}

	public String getStoreProcurementId() {
		return storeProcurementId;
	}

	public void setStoreProcurementId(String storeProcurementId) {
		this.storeProcurementId = storeProcurementId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductBrand() {
		return productBrand;
	}

	public void setProductBrand(String productBrand) {
		this.productBrand = productBrand;
	}

	/**
	 * 表格控件调用
	 * 
	 * @param columnNumber
	 *            列号
	 * @return 对应列的值
	 */
	public Object getValueAt(String columnName) {
		return ObjectReflectUtil.getFieldValue(this, columnName);
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
