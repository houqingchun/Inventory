package com.ivt.mis.model;

import java.util.Date;

import com.ivt.mis.common.ObjectReflectUtil;

public class PojoTest {
	private String name;
	private String sex;
	private int intVal;
	private Integer intInteger;
	private double douVal;
	private Double douObj;
	private Date date;
	private Number number;
	private CustomizedUnit customizedUnit;
	public String getName() {
		return name;
	}
	public PojoTest(){
	}
	public PojoTest(String name){
		this.name = name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public CustomizedUnit getCustomizedUnit() {
		return customizedUnit;
	}
	public void setCustomizedUnit(CustomizedUnit customizedUnit) {
		this.customizedUnit = customizedUnit;
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
	public int getIntVal() {
		return intVal;
	}
	public void setIntVal(int intVal) {
		this.intVal = intVal;
	}
	public Integer getIntInteger() {
		return intInteger;
	}
	public void setIntInteger(Integer intInteger) {
		this.intInteger = intInteger;
	}
	public double getDouVal() {
		return douVal;
	}
	public void setDouVal(double douVal) {
		this.douVal = douVal;
	}
	public Double getDouObj() {
		return douObj;
	}
	public void setDouObj(Double douObj) {
		this.douObj = douObj;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Number getNumber() {
		return number;
	}
	public void setNumber(Number number) {
		this.number = number;
	}
}
