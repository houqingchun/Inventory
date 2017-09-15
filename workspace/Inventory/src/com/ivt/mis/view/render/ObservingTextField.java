package com.ivt.mis.view.render;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextField;

import com.ivt.mis.common.swing.IvtJTextField;
import com.qt.datapicker.DatePicker;

public class ObservingTextField extends IvtJTextField implements Observer {

	public ObservingTextField(int columns, int maxLength) {
		super(columns, maxLength);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5049448379094801945L;

	@Override
	public void update(Observable o, Object arg) {
		Calendar calendar = (Calendar) arg;
		DatePicker dp = (DatePicker) o;
		setText(dp.formatDate(calendar, "yyyy-MM-dd"));
	}

}
