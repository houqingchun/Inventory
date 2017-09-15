package com.ivt.mis.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;

import com.ivt.mis.common.BasicTypeUtils;
import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.DataValidator;
import com.ivt.mis.common.swing.IvtLayoutUtilities;
import com.ivt.mis.model.DataVersion;
import com.ivt.mis.service.DataVersionService;
import com.jgoodies.forms.builder.DefaultFormBuilder;

public class DataBackupRestoreFrame extends BaseJInternalFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 227476496174155305L;

	JTabbedPane backupRestoreTab;

	JTextField idField;
	JTextField dataPathField;
	JTextField dataTypeField;
	JTextField commentsField;
	JTextField createTimeField;

	JTextField restoreDataPathField;
	JTextField restoreCommentsField;
	JTextField restoreCreateTimeField;

	JButton backupBtn;
	JButton backupCancelBtn;
	JButton restoreBtn;
	JButton restoreCancelBtn;
	JPanel backupPanel;
	JPanel restorePanel;

	JButton browserBtn;
	JButton restoreBrowserBtn;
	JFileChooser fileChooser;
	JFileChooser restoreFileChooser;

	public DataBackupRestoreFrame() {
		super("数据备份与恢复管理", true, true, false, true);
		super.setFrameId(Constants.MODULE_DB_BACKUP);

		// 数据备份
		backupRestoreTab = new JTabbedPane();
		//this.setLayer(this.getLayer() + 1);
		Dimension deskPaneDim = MainFrame.desktopPane.getSize();
		this.setSize(500, 170);
		this.setResizable(false);
		Dimension internalDim = this.getSize();
		this.setLocation(
				(deskPaneDim.width - internalDim.width) / 2 + this.getLayer()
						* 5, (deskPaneDim.height - internalDim.height) / 2
						+ this.getLayer() * 5);

		dataPathField = new JTextField();
		dataTypeField = new JTextField();
		createTimeField = new JTextField();
		commentsField = new JTextField();

		// 设置默认数据
		dataTypeField.setText("备份");
		dataTypeField.setEditable(false);
		createTimeField.setText(BasicTypeUtils.getLongFmtDate());
		createTimeField.setEditable(false);

		backupBtn = new JButton(getText("form.btn.backup"));
		backupCancelBtn = new JButton(getText("form.btn.cancel"));
		browserBtn = new JButton(getText("form.btn.browser"));
		browserBtn.addActionListener(new DirectoryChooser());
		backupBtn.addActionListener(new BackupButtonListener());

		// Create a file chooser
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(null);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		DefaultFormBuilder builder = IvtLayoutUtilities.getFormBuilder();
		// Fill the table with labels and components.
		builder.append(new JLabel("备份路径"));
		builder.append(dataPathField, 3);
		builder.append(browserBtn);
		// panel.add(new JLabel("备份时间:"), cc.xy(1, rowNbr));
		// panel.add(createTimeField, cc.xyw(3, rowNbr, 5));
		// nextRow();
		builder.append(new JLabel("备注"));
		builder.append(commentsField, 5);

		backupPanel = new JPanel();
		restorePanel = new JPanel();

		backupPanel.setLayout(new FlowLayout());
		backupPanel.add(builder.getPanel());
		backupPanel.add(backupBtn);
		backupPanel.add(backupCancelBtn);

		// 还原TAB
		restoreCreateTimeField = new JTextField();
		restoreCommentsField = new JTextField();
		restoreDataPathField = new JTextField();
		restoreCreateTimeField.setText(BasicTypeUtils.getLongFmtDate());
		restoreCreateTimeField.setEditable(false);

		restoreBtn = new JButton(getText("form.btn.restore"));
		restoreCancelBtn = new JButton(getText("form.btn.cancel"));
		restoreBrowserBtn = new JButton(getText("form.btn.browser"));
		restoreBrowserBtn.addActionListener(new DirectoryChooserForRestore());

		
		// Create a file chooser
		restoreFileChooser = new JFileChooser();
		restoreFileChooser.setCurrentDirectory(null);
		restoreBtn.addActionListener(new RestoreButtonListener());

		DefaultFormBuilder builderRestore = IvtLayoutUtilities.getFormBuilder();
		// Fill the table with labels and components.
		builderRestore.append(new JLabel("恢复路径"));
		builderRestore.append(restoreDataPathField, 3);
		builderRestore.append(restoreBrowserBtn);
		builderRestore.nextLine();
		builderRestore.append(new JLabel("备注"));
		builderRestore.append(restoreCommentsField,5);

		restorePanel.add(builderRestore.getPanel());
		restorePanel.add(restoreBtn);
		restorePanel.add(restoreCancelBtn);

		backupPanel.setVisible(true);
		restorePanel.setVisible(true);
		backupRestoreTab.add("数据备份", backupPanel);
		backupRestoreTab.add("数据恢复", restorePanel);

		this.getContentPane().add(backupRestoreTab);

		backupCancelBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		restoreCancelBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	class BackupButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (DataValidator.isBlankOrNull(dataPathField.getText())) {
				showWarningMsg("请选择备份目录");
				return;
			}
			boolean isSuccess = true;
			// 获得当前DB文件
			File dbFile = new File("ivt.db");
			// 生成备份文件名（时间戳）
			String newFileName = dataPathField.getText() + "/ivt_"
					+ BasicTypeUtils.getPremaryKeyID("") + ".db";
			try {
				FileUtils.copyFile(dbFile, new File(newFileName));
				
				DataVersionService dataVersionService = CommonFactory
						.getDataVersionService();
				DataVersion dataVersion = new DataVersion();
				dataVersion.setDataType("备份");
				dataVersion.setDataPath(newFileName);
				dataVersion.setComments(commentsField.getText());
				dataVersion.setId(CommonFactory.getCodeRuleService().getNextObjectCode(Constants.MODULE_DB_BACKUP));
				dataVersion.setCreateTime(BasicTypeUtils.getLongFmtDate());
				isSuccess &= dataVersionService.saveDataVersion(dataVersion);
				
			} catch (IOException e1) {
				showErrorMsg(e1.getMessage());
				isSuccess &= false;
				return;
			}
			if (isSuccess) {
				showInfoMsg("数据备份" + getText("form.event.success", new String[]{getText("form.btn.save")}));
				dispose();
			}
		}

	}

	class RestoreButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (DataValidator.isBlankOrNull(restoreDataPathField.getText())) {
				showWarningMsg("请选择需要还原的数据文件");
				return;
			}
			boolean isSuccess = true;
			// 获得当前DB文件
			File dbFile = new File("ivt.db");
			// 取得还原文件名
			String newFileName = restoreDataPathField.getText();
			
			try {
				FileUtils.copyFile(new File(newFileName), dbFile);
				
				DataVersionService dataVersionService = CommonFactory
						.getDataVersionService();
				DataVersion dataVersion = new DataVersion();
				dataVersion.setDataType("恢复");
				dataVersion.setDataPath(newFileName);
				dataVersion.setComments(restoreCommentsField.getText());
				dataVersion.setId(CommonFactory.getCodeRuleService().getNextObjectCode(Constants.MODULE_DB_BACKUP));
				dataVersion.setCreateTime(BasicTypeUtils.getLongFmtDate());
				isSuccess &= dataVersionService.saveDataVersion(dataVersion);
			} catch (IOException e1) {
				showErrorMsg(e1.getMessage());
				isSuccess &= false;
				return;
			}
			if (isSuccess) {
				showInfoMsg("数据恢复" + getText("form.event.success", new String[]{getText("form.btn.save")}));
				dispose();
			}
		}

	}

	class DirectoryChooser implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int returnVal = fileChooser.showOpenDialog(dataPathField);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				dataPathField.setText(file.getAbsolutePath());
			}
		}
	}
	
	class DirectoryChooserForRestore implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int returnVal = restoreFileChooser.showOpenDialog(restoreDataPathField);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = restoreFileChooser.getSelectedFile();
				restoreDataPathField.setText(file.getAbsolutePath());
			}
		}
	}
}
