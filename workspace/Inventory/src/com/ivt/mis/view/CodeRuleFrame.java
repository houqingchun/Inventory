package com.ivt.mis.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.StringUtils;

import com.ivt.mis.common.BasicTypeUtils;
import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.swing.IvtJTextField;
import com.ivt.mis.common.swing.IvtLayoutUtilities;
import com.ivt.mis.model.CodeRule;
import com.ivt.mis.service.CodeRuleService;
import com.ivt.mis.view.validator.CodeRuleValidator;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;

public class CodeRuleFrame extends BaseJInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1097690471039328018L;
	private CodeRule codeRule;
	JTextField idField;
	JTextField objectTypeField;
	JComboBox objectNameComboBox;
	JCheckBox autoIncreaseBox;
	JCheckBox availableBox;
	JCheckBox prefixEnabledBox;
	JCheckBox suffixEanbledBox;
	JTextField suffixField;
	JTextField prefixField;
	JTextField nbrOfSeqField;
	JComboBox seqTypeComboBox;
	JTextField seqFormatField;
	JTextField currentSeqField;
	JTextField commentsField;
	JTextField previewField;

	public CodeRuleFrame(CodeRule codeRule, String editType) {
		super("编码规则管理", true, true, false, true);
		this.codeRule = codeRule;
		super.editType = editType;
		super.setFrameId(Constants.MODULE_CODE_RULE);
		//this.setLayer(this.getLayer() + 1);
		Dimension deskPaneDim = MainFrame.desktopPane.getSize();
		this.setSize(580, 320);
		this.setResizable(true);
		Dimension internalDim = this.getSize();
		this.setLocation(
				(deskPaneDim.width - internalDim.width) / 2 + this.getLayer()
						* 5, (deskPaneDim.height - internalDim.height) / 2
						+ this.getLayer() * 5);
		this.getContentPane().add(createJPanel());
	}

	public JPanel createJPanel() {
		JPanel mainContainer = new JPanel();
		mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
		/*
		 * 建立编码添加信息页面
		 */
		JPanel addPanel = new JPanel();
		idField = new IvtJTextField(50);
		objectTypeField = new IvtJTextField(50);
		autoIncreaseBox = new JCheckBox();
		prefixEnabledBox = new JCheckBox("启用前缀");
		suffixEanbledBox = new JCheckBox("启用后缀");
		availableBox = new JCheckBox();
		suffixField = new IvtJTextField(6);
		prefixField = new IvtJTextField(6);
		nbrOfSeqField = new IvtJTextField(2);
		nbrOfSeqField.getDocument().addDocumentListener(
				new NbrOfSeqFieldListener());
		seqFormatField = new IvtJTextField(50);
		currentSeqField = new IvtJTextField(50);
		commentsField = new IvtJTextField(100);
		previewField = new IvtJTextField(100);

		seqTypeComboBox = new JComboBox();
		seqTypeComboBox.addItem("数字递增");
		seqTypeComboBox.addItem("日期时间");
		seqTypeComboBox.addActionListener(new SelectSeqTypeListener());

		objectNameComboBox = new JComboBox();
		objectNameComboBox.addItem("客户编码");
		objectNameComboBox.addItem("供应商编码");
		objectNameComboBox.addItem("产品编码");
		objectNameComboBox.addItem("入库编码");
		objectNameComboBox.addItem("入库批次编码");
		objectNameComboBox.addItem("出库编码");
		objectNameComboBox.addItem("出库批次编码");
		objectNameComboBox.addItem("库存编码");
		objectNameComboBox.addActionListener(new SelectObjectNameListener());

		DefaultFormBuilder builder = IvtLayoutUtilities.getFormBuilder();
		// Fill the table with labels and components.
		CellConstraints cc = new CellConstraints();
		builder.append(new JLabel("编号:"));
		builder.append(idField);
		builder.nextLine();
		builder.append(new JLabel("编码对象:"));
		builder.append(objectNameComboBox);
		builder.append(new JLabel("对象代号:"));
		builder.append(objectTypeField);
		builder.nextLine();
		builder.append(new JLabel("是否启用:"));
		builder.append(availableBox);
		builder.append(new JLabel("是否自动增长:"));
		builder.append(autoIncreaseBox);
		builder.nextLine();
		builder.append(new JLabel("前缀:"));
		builder.append(prefixField, prefixEnabledBox);
		builder.nextLine();

		builder.append(new JLabel("后缀:"));
		builder.append(suffixField, suffixEanbledBox);
		builder.nextLine();
		builder.append(new JLabel("中间序号类型:"));
		builder.append(seqTypeComboBox);
		builder.append(new JLabel("类型格式:"));
		builder.append(seqFormatField);
		builder.nextLine();
		builder.append(new JLabel("序号位数:"));
		builder.append(nbrOfSeqField);
		builder.append(new JLabel("当前序号:"));
		builder.append(currentSeqField);
		builder.nextLine();
		builder.append(new JLabel("备注: "));
		builder.append(commentsField, 5);
		builder.nextLine();
		builder.append(new JLabel("预览: "));
		builder.append(previewField);
		JButton preview = new JButton("预览");
		preview.addActionListener(new CodePreviewListener());
		builder.append(preview);
		builder.append(new JLabel(""));

		addPanel.add(builder.getPanel());

		JPanel buttonsPanel = super.createCRUDButtons(new AddButtonListener(),
				new UpdateButtonListener(), new DeleteButtonListener());

		// 装载数据
		// populateForm();

		// 首次加载
		resetObjectName();

		mainContainer.add(addPanel);
		mainContainer.add(buttonsPanel);

		return mainContainer;
	}

	private void resetObjectName() {
		int index = objectNameComboBox.getSelectedIndex();
		objectNameComboBox.setSelectedIndex(0);
		if (index > 0) {
			objectNameComboBox.setSelectedIndex(index);
		}
		initialCurrentSeqNbr();		
	}

	public CodeRule getCodeRule() {
		return codeRule;
	}

	public void setCodeRule(CodeRule codeRule) {
		this.codeRule = codeRule;
	}

	public String getEditType() {
		return editType;
	}

	public void setEditType(String editType) {
		this.editType = editType;
	}

	public void populateForm() {
		objectTypeField.setEditable(false);
		autoIncreaseBox.setEnabled(false);
		seqFormatField.setEditable(false);
		idField.setEditable(false);
		previewField.setEditable(false);

		if (Constants.FORM_TYPE_UPDATE.equals(editType)) {
			if (codeRule == null) {
				codeRule = new CodeRule();
			}
			idField.setText(codeRule.getId());
			objectNameComboBox.setSelectedItem(codeRule.getObjectName());
			objectTypeField.setText(codeRule.getObjectType());
			availableBox.setSelected(codeRule.getAvailable());
			autoIncreaseBox.setSelected(codeRule.getAutoIncrease());
			prefixEnabledBox.setSelected(codeRule.getPrefixEnabled());
			suffixEanbledBox.setSelected(codeRule.getSuffixEanbled());
			suffixField.setText(codeRule.getSuffix());
			prefixField.setText(codeRule.getPrefix());
			nbrOfSeqField.setText(String.valueOf(codeRule.getNbrOfSeq()));
			seqFormatField.setText(codeRule.getSeqFormat());
			currentSeqField.setText(codeRule.getCurrentSeq());
			currentSeqField.setEditable(false);
			commentsField.setText(codeRule.getComments());
			seqTypeComboBox.setSelectedItem(codeRule.getSeqType());

		} else if (Constants.FORM_TYPE_ADD.equals(editType)) {
			// objectNameComboBox.setSelectedIndex(0);
			// objectTypeField.setText("");
			idField.setText("");
			availableBox.setSelected(true);
			autoIncreaseBox.setSelected(true);
			prefixEnabledBox.setSelected(false);
			suffixEanbledBox.setSelected(false);
			suffixField.setText("");
			prefixField.setText("");
			nbrOfSeqField.setText("12");
			seqFormatField.setText("");
			currentSeqField.setText("");
			currentSeqField.setEditable(false);
			commentsField.setText("");
			seqTypeComboBox.setSelectedIndex(0);
		}
		
		previewCodeSeq();
	}

	public CodeRule buildCodeRule() {
		if (codeRule == null) {
			codeRule = new CodeRule();
		}
		if (codeRule.getId() == null || StringUtils.isBlank(codeRule.getId())) {
			codeRule.setId(BasicTypeUtils.getPremaryKeyID(""));
		}

		codeRule.setObjectName(objectNameComboBox.getSelectedItem().toString());
		codeRule.setObjectType(objectTypeField.getText());
		codeRule.setAutoIncrease(autoIncreaseBox.isSelected());
		codeRule.setAvailable(availableBox.isSelected());
		codeRule.setPrefixEnabled(prefixEnabledBox.isSelected());
		codeRule.setSuffixEanbled(suffixEanbledBox.isSelected());
		codeRule.setAutoIncrease(autoIncreaseBox.isSelected());
		codeRule.setSuffix(suffixField.getText());
		codeRule.setPrefix(prefixField.getText());
		codeRule.setComments(commentsField.getText());
		codeRule.setSeqType(seqTypeComboBox.getSelectedItem().toString());
		codeRule.setSeqFormat(seqFormatField.getText());
		codeRule.setNbrOfSeq(Integer.valueOf(nbrOfSeqField.getText()));
		codeRule.setCurrentSeq(currentSeqField.getText());
		codeRule.setCreateTime(BasicTypeUtils.getCurrentDateTimestamp());

		return codeRule;
	}

	class NbrOfSeqFieldListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			initialCurrentSeqNbr();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			// initialCurrentSeqNbr();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			initialCurrentSeqNbr();
		}

	}

	class SelectSeqTypeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			initialCurrentSeqNbr();
		}
	}

	private String initialCurrentSeqNbr() {
		// 产生类型格式
		int selectIndex = seqTypeComboBox.getSelectedIndex();
		logger.debug("SeqenceType:" + selectIndex);
		if (selectIndex == 0) { // 数字
			nbrOfSeqField.setEditable(true);
			String formatStr = "";
			int c = Integer.valueOf(nbrOfSeqField.getText());
			for (int i = 0; i < c; i++) {
				formatStr += "0";
			}
			seqFormatField.setText(formatStr);
		} else if (selectIndex == 1) { // 日期时间
			nbrOfSeqField.setEditable(false);
			seqFormatField.setText("yyMMddHHmmss");
		}

		// 产生当前序号值
		FieldPosition HELPER_POSITION = new FieldPosition(0);
		NumberFormat numberFormat = null;
		StringBuffer sb = new StringBuffer();
		int seq = 1;
		logger.debug("SeqenceType:" + selectIndex);
		try {
			if (selectIndex == 0) { // 数字自增长
				try {
					seq = Integer.valueOf(codeRule.getCurrentSeq());
					if (seq == 0) {
						seq = 1;
					}
				} catch (Exception e) {
					seq = 1;
				}

				numberFormat = new DecimalFormat(seqFormatField.getText());
				numberFormat.format(seq, sb, HELPER_POSITION);
			} else { // 日期时间
				Date date = new Date();

				SimpleDateFormat formate = new SimpleDateFormat(
						seqFormatField.getText());
				numberFormat = new DecimalFormat("000");
				formate.format(date.getTime(), sb, HELPER_POSITION);

				numberFormat.format(seq, sb, HELPER_POSITION);
			}
		} catch (Exception e) {
			e.printStackTrace();
			showErrorMsg("类型格式不正确");
			return "";
		}
		currentSeqField.setText(sb.toString());

		return sb.toString();
	}
	
	private void previewCodeSeq(){
		String codePreview = "";
		if (prefixEnabledBox.isSelected()) {
			codePreview += prefixField.getText();
		}

		codePreview += initialCurrentSeqNbr();

		if (suffixEanbledBox.isSelected()) {
			codePreview += suffixField.getText();
		}

		previewField.setText(codePreview);
	}

	class CodePreviewListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			previewCodeSeq();
		}

	}

	class SelectObjectNameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int selectIndex = objectNameComboBox.getSelectedIndex();
			logger.debug("ObjectType:" + selectIndex);
			if (selectIndex == 0) {
				objectTypeField.setText(Constants.MODULE_CUSTOMER);
			} else if (selectIndex == 1) {
				objectTypeField.setText(Constants.MODULE_PROVIDER);
			} else if (selectIndex == 2) {
				objectTypeField.setText(Constants.MODULE_PRODUCT);
			} else if (selectIndex == 3) {
				objectTypeField.setText(Constants.MODULE_PROCUREMENT);
			} else if (selectIndex == 4) {
				objectTypeField.setText(Constants.MODULE_PROCUREMENT_BATCH);
			} else if (selectIndex == 5) {
				objectTypeField.setText(Constants.MODULE_SHIPMENT);
			} else if (selectIndex == 6) {
				objectTypeField.setText(Constants.MODULE_SHIPMENT_BATCH);
			} else if (selectIndex == 7) {
				objectTypeField.setText(Constants.MODULE_STORE_MANAGE);
			}

			// 检查数据库，是否存在相应数据，若存在，则填充数据
			CodeRuleService codeRuleService = CommonFactory
					.getCodeRuleService();
			CodeRule codeRuleTmp = codeRuleService
					.getCodeRuleInfoByObjectType(objectTypeField.getText());
			if (codeRuleTmp != null) {
				codeRule = codeRuleTmp;
				editType = Constants.FORM_TYPE_UPDATE;
				populateForm();
				updateBtn.setEnabled(true);
				resetBtn.setEnabled(true);
				saveBtn.setEnabled(false);

			} else {
				codeRule = new CodeRule();
				editType = Constants.FORM_TYPE_ADD;
				populateForm();
				updateBtn.setEnabled(false);
				resetBtn.setEnabled(true);
				saveBtn.setEnabled(true);
			}

			initialCurrentSeqNbr();
		}

	}

	class AddButtonListener extends BaseAddButtonListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			CodeRule codeRule = buildCodeRule();

			CodeRuleService codeRuleService = CommonFactory
					.getCodeRuleService();

			// 格式不正确
			if ("".equals(initialCurrentSeqNbr())) {
				return;
			}

			if (codeRuleService.saveCodeRule(codeRule)) {
				showInfoMsg("添加成功");
			} else {
				showErrorMsg("添加失败");
			}

			resetObjectName();
		}
	}

	class UpdateButtonListener extends BaseUpdateButtonListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			CodeRule codeRule = buildCodeRule();
			CodeRuleService codeRuleService = CommonFactory
					.getCodeRuleService();

			// 格式不正确
			if ("".equals(initialCurrentSeqNbr())) {
				return;
			}
			if (codeRuleService.modifyCodeRule(codeRule)) {
				showInfoMsg("修改成功");
			} else {
				showErrorMsg("修改失败");
			}
			resetObjectName();
		}
	}

	class DeleteButtonListener extends BaseDeleteButtonListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			CodeRuleService codeRuleService = CommonFactory
					.getCodeRuleService();
			if (showConfirmDialog() == JOptionPane.YES_OPTION) {
				if (codeRuleService.removeCodeRule(idField.getText())) {
					showInfoMsg("删除成功");
				} else {
					showErrorMsg("删除失败");
				}
			}
			resetObjectName();
		}
	}
}
