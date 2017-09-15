package com.ivt.mis.model;

public class FieldDefn {
	private String fieldName;
	private boolean isRequired;
	private String descr;
	private boolean isVisible;
	
	public FieldDefn(){
		
	}
	
	public FieldDefn(String[] fieldArray){
		setDescr(fieldArray[0]);
		setFieldName(fieldArray[1]);
		setVisible("Y".equalsIgnoreCase(fieldArray[2]));
		setRequired("Y".equalsIgnoreCase(fieldArray[3]));
	}
	
	public String getDescrLong(){
		String t = descr;
		if (this.isRequired){
			t = "(*)" + t;
		}
		
		return t;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public boolean isRequired() {
		return isRequired;
	}
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}
