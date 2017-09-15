package com.ivt.mis.model;

import com.ivt.mis.common.Constants;
import com.ivt.mis.common.ObjectReflectUtil;

public abstract class BasePOJO {
	protected String id;
	protected int version;
	protected PageUtil page;
	
	protected String index;
	//UI列表使用
	protected String[][] columns;
	

	public BasePOJO(){
		page = new PageUtil(Constants.PAGE_SIZE_MAX,0,1);
	}
	
	public FieldDefn getFieldDefn(String fieldName){
		FieldDefn obj = null;
		for (int i = 0; i < columns.length; i++){
			String[] tmp = columns[i];
			if (fieldName.equalsIgnoreCase(tmp[1])){
				obj = new FieldDefn(tmp);
			}
		}
		
		return obj;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof BasePOJO)) {
			return false;
		}

		BasePOJO other = (BasePOJO) o;
		if (!this.id.equals(other.getId())) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		return this.id.hashCode();
	}

	public PageUtil getPage() {
		return page;
	}

	public void setPage(PageUtil page) {
		this.page = page;
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

	public String[][] getColumns() {
		return columns;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
}
