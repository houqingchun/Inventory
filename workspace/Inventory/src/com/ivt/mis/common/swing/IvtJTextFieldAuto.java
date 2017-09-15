package com.ivt.mis.common.swing;

import java.util.Vector;

public class IvtJTextFieldAuto extends AutoCompletionField {
	
	public IvtJTextFieldAuto(int columns){
		super(columns);
	}
	/**
	 * 设置列表默认值
	 * @param values
	 */
	public void setListValue(Vector<String> values){
		this.setFilter(new DefaultCompletionFilter(values));
	}
}
