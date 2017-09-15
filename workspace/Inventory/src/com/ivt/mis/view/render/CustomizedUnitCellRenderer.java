package com.ivt.mis.view.render;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.ivt.mis.model.CustomizedUnit;

public class CustomizedUnitCellRenderer extends DefaultTableCellRenderer {

 	public Component CustomizedUnitCellRenderer(JTable table, Object value, 
 			boolean isSelected, boolean hasFocus, int row, int column) {
 		if (value instanceof CustomizedUnit) {
 			CustomizedUnit unit = (CustomizedUnit) value;
 			setText(unit.getName());
 		}
 		
 		if (isSelected) {
 			setBackground(table.getSelectionBackground());
 		} else {
 			setBackground(table.getSelectionForeground());
 		}
 		
 		return this;
 	}
}
