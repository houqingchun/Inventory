package com.ivt.mis.model;

public class CustomizedUnit {
	private String name;
	
	public CustomizedUnit(){
		
	}
	
	public CustomizedUnit(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}
	
}
