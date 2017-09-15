package com.ivt.mis.view.render;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.ivt.mis.model.CustomizedUnit;

public class CustomizedUnitCellEditor extends AbstractCellEditor implements
		TableCellEditor, ActionListener {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5609196651745759309L;
	private CustomizedUnit customizedUnit;
	private List<CustomizedUnit> listCustomizedUnit;
	
	public CustomizedUnitCellEditor(List<CustomizedUnit> listCustomizedUnit) {
		this.listCustomizedUnit = listCustomizedUnit;
	}
	
	@Override
	public Object getCellEditorValue() {
		return this.customizedUnit;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (value instanceof CustomizedUnit) {
			this.customizedUnit = (CustomizedUnit) value;
		}
		
		JComboBox comboCustomizedUnit = new JComboBox();
		comboCustomizedUnit.setEditable(true);
		
		for (CustomizedUnit customizedUnit : listCustomizedUnit) {
			comboCustomizedUnit.addItem(customizedUnit);
		}
		
		comboCustomizedUnit.setSelectedItem(customizedUnit.getName());
		comboCustomizedUnit.addActionListener(this);
		
		comboCustomizedUnit.setBackground(table.getBackground());
 		
		return comboCustomizedUnit;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JComboBox combCustomizedUnit = (JComboBox) event.getSource();
		try{
			this.customizedUnit = (CustomizedUnit) combCustomizedUnit.getSelectedItem();
		}catch(ClassCastException e){
			this.customizedUnit = new CustomizedUnit(combCustomizedUnit.getSelectedItem().toString());
			boolean flag = false;
			for (int i = 0; i < listCustomizedUnit.size(); i++){
				CustomizedUnit tmp = listCustomizedUnit.get(i);
				if (this.customizedUnit.getName().equals(tmp.getName())){
					flag = true;
					break;
				}
			}

			if (!flag){
				listCustomizedUnit.add(this.customizedUnit);
			}
			
		}
	}
}
