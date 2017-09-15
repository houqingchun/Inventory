/**
 * 产品类
 */

package com.ivt.mis.model;

import com.ivt.mis.common.ObjectReflectUtil;

public class Product extends BasePOJO implements Cloneable {
	private String id; // 产品编号
	private String productCode; // 产品型号
	private String productName; // 产品名称
	private String promitCode; // 批准文号
	private String size; // 规格
	private String unit; // 单位
	private String brand; // 品牌
	private String pack; // 包装
	private String miniPack; // 最小包装
	private String manufacturer; // 生产商
	private String description; // 备注
	private int available; // 状态 非0代表可用
	private String category;// 产品类别
	private String ownerId; // 所属
	private String createTime; // 创建时间

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	private boolean selected; // 临时变量，前端展示使用

	private String index;// UI显示 临时使用

	/**
	 * 列表显示栏位标记 String[]{"显示名称","属性名","是否显示","是否必填"}
	 */
	private String[][] columns = new String[][] {
			new String[] { "序号", "index", "N", "N" },
			new String[] { "产品编号", "id", "Y", "Y" },
			new String[] { "产品型号", "productCode", "Y", "Y" },
			new String[] { "产品名称", "productName", "Y", "Y" },
			new String[] { "品牌", "brand", "Y", "N" },
			new String[] { "规格", "size", "Y", "N" },
			new String[] { "包装", "pack", "Y", "N" },
			new String[] { "最小包装", "miniPack", "Y", "N" },
			new String[] { "单位", "unit", "Y", "Y" },
			new String[] { "类别", "category", "Y", "N" },
			new String[] { "批准文号", "promitCode", "Y", "N" },
			new String[] { "生产商", "manufacturer", "Y", "N" },
			new String[] { "备注", "description", "Y", "N" } };
	

	/**
	 * 列表显示栏位标记 String[]{"显示名称","属性名","是否显示","是否必填"}
	 * 用于入库时选择
	 * "产品编号", "产品型号", "产品名称", "品牌", "规格","包装","最小包装",
	//"类别", "单位", "批准文号", "生产商", "选择"
	 */
	private String[][] columnsForProcurement = new String[][] {
			new String[] { "产品编号", "id", "Y", "Y" },
			new String[] { "产品型号", "productCode", "Y", "Y" },
			new String[] { "产品名称", "productName", "Y", "Y" },
			new String[] { "品牌", "brand", "Y", "N" },
			new String[] { "规格", "size", "Y", "N" },
			new String[] { "包装", "pack", "Y", "N" },
			new String[] { "最小包装", "miniPack", "Y", "N" },
			new String[] { "类别", "category", "Y", "N" },
			new String[] { "单位", "unit", "Y", "Y" },			
			new String[] { "批准文号", "promitCode", "Y", "N" },
			new String[] { "生产商", "manufacturer", "Y", "N" },
			new String[] { "选择", "selected", "Y", "N" } };
	
	
	public Product() {
		super();
		super.columns = columns;
	}

	public FieldDefn getFieldDefn(String fieldName) {
		FieldDefn obj = null;
		for (int i = 0; i < columns.length; i++) {
			String[] tmp = columns[i];
			if (fieldName.equalsIgnoreCase(tmp[1])) {
				obj = new FieldDefn(tmp);
			}
		}

		return obj;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getPromitCode() {
		return promitCode;
	}

	public void setPromitCode(String promitCode) {
		this.promitCode = promitCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
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

	/**
	 * 表格控件调用
	 * 
	 * @param columnNumber
	 *            列号
	 * @return 对应列的值
	 */
	public void setValueAt(Object value, String columnName) {
		ObjectReflectUtil.setFieldValue(this, columnName, value);
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIndex() {
		return index;
	}

	public String[][] getColumns() {
		return columns;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Product clone() {
		Product o = null;
		try {
			o = (Product) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public String getMiniPack() {
		return miniPack;
	}

	public void setMiniPack(String miniPack) {
		this.miniPack = miniPack;
	}

	public String[][] getColumnsForProcurement() {
		return columnsForProcurement;
	}

	public void setColumnsForProcurement(String[][] columnsForProcurement) {
		this.columnsForProcurement = columnsForProcurement;
	}

}
