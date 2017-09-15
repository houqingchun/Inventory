package com.ivt.mis.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.ivt.mis.common.Constants;
import com.ivt.mis.common.I18nMsg;

public abstract class BaseJDialog extends JDialog{
	protected String editType;
	protected int rowNbr = 3;

	/**
	 * 展现表单使用
	 * 
	 * @return
	 */
	protected int nextRow() {
		rowNbr = rowNbr + 2;
		return rowNbr;
	}

	protected void resetRow() {
		rowNbr = 3;
	}

	
	public BaseJDialog(JFrame owner, String title, boolean modal){
		super(owner, title, modal);
	}
	
	public BaseJDialog(){
		super();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6103046319930095081L;

	public static String getText(String key) {
		return I18nMsg.getText(key);
	}

	public static String getText(String key, Object[] parameters) {
		List<Object> params = Arrays.asList(parameters);
		return I18nMsg.getText(key, params);
	}
	

	protected void showInfoMsg(String message) {
		JOptionPane.showMessageDialog(MainFrame.desktopPane, message, "消息",
				JOptionPane.INFORMATION_MESSAGE);
	}

	protected void showErrorMsg(String message) {
		JOptionPane.showMessageDialog(MainFrame.desktopPane, message, "错误 ",
				JOptionPane.ERROR_MESSAGE);
	}

	protected void showWarningMsg(String message) {
		JOptionPane.showMessageDialog(MainFrame.desktopPane, message, "警告",
				JOptionPane.WARNING_MESSAGE);
	}
	
	protected void populateForm() {

	}
	

	protected JPanel createCRUDButtons(ActionListener addListener,
			ActionListener updateListener, ActionListener deleteListener) {
		JPanel eventPanel = new JPanel();
		if (Constants.FORM_TYPE_UPDATE.equals(editType)
				&& updateListener != null) {
			JButton eventBtn = new JButton(getText("form.btn.update"));
			eventBtn.addActionListener(updateListener);

			JButton reButton = new JButton(getText("form.btn.reset"));
			reButton.addActionListener(new ResetButtonListener());
			eventPanel.add(eventBtn);
			eventPanel.add(reButton);
		} else if (Constants.FORM_TYPE_DELETE.equals(editType)
				&& deleteListener != null) {
			JButton eventBtn = new JButton(getText("form.btn.delete"));
			eventBtn.addActionListener(deleteListener);
			eventPanel.add(eventBtn);
		} else if (Constants.FORM_TYPE_ADD.equals(editType)
				&& addListener != null) {
			JButton eventBtn = new JButton(getText("form.btn.save"));
			eventBtn.addActionListener(addListener);
			eventPanel.add(eventBtn);
		}

		JButton cancelBtn = new JButton(getText("form.btn.cancel"));
		cancelBtn.addActionListener(new CancelButtonListener());
		eventPanel.add(cancelBtn);

		return eventPanel;
	}

	protected class ResetButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			populateForm();
		}
	}

	protected class CancelButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
}
