package com.ivt.mis.common.swing;

import javax.swing.JTextField;

public class IvtJTextField extends JTextField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 333839359573585639L;

	public IvtJTextField(int columns, int maxLength) {
		super(columns);
		this.setDocument(new LimitedDocument(maxLength));
	}

	public IvtJTextField(int maxLength) {
		this.setDocument(new LimitedDocument(maxLength));
	}

	public IvtJTextField(int columns, int maxLength, boolean isUpperCase) {
		super(columns);
		this.setDocument(new LimitedDocument(maxLength, isUpperCase));
	}

	public IvtJTextField(int maxLength, boolean isUpperCase) {
		this.setDocument(new LimitedDocument(maxLength, isUpperCase));
	}

	private int maxLength = 5;

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getMaxLength() {
		return this.maxLength;
	}
}
