package com.ivt.mis.common.swing;

import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class IvtLayoutUtilities {
	public static JPanel getFormLayoutJPanel() {
		FormLayout layout = new FormLayout(
				"right:max(30dlu;pref), 3dlu, 60dlu, 7dlu, right:max(30dlu;pref), 3dlu, 70dlu",
				"p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, p, 3dlu, p, 3dlu, p, 3dlu, p");

		JPanel panel = new JPanel(layout);
		return panel;
	}
	
	public static DefaultFormBuilder getFormBuilder() {
		FormLayout layout = new FormLayout(
				"right:max(40dlu;p), 4dlu, 80dlu, 7dlu, right:max(40dlu;p), 4dlu, 80dlu",
				"");
		
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
//		builder.setDefaultDialogBorder();

		return builder;
	}
}
