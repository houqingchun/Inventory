package com.ivt.mis.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observer;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;

import com.ivt.mis.common.BasicTypeUtils;
import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.DataValidator;
import com.ivt.mis.common.swing.IvtJTextField;
import com.ivt.mis.common.swing.IvtJTextFieldAuto;
import com.ivt.mis.model.Customer;
import com.ivt.mis.model.CustomizedUnit;
import com.ivt.mis.model.StoreProcurement;
import com.ivt.mis.model.StoreShipment;
import com.ivt.mis.service.CustomerService;
import com.ivt.mis.service.StoreShipmentService;
import com.ivt.mis.view.render.CustomizedUnitCellEditor;
import com.ivt.mis.view.render.CustomizedUnitCellRenderer;
import com.ivt.mis.view.render.ObservingTextField;
import com.ivt.mis.view.validator.StoreShipmentValidator;
import com.qt.datapicker.DatePicker;

public class StoreShipmentFrame extends BaseJInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3553858497474473395L;

	public CustomerService customerService = CommonFactory.getCustomerService();
	public CustomerService customerServie = CommonFactory.getCustomerService();
	public StoreShipmentService storeShipmentService = CommonFactory
			.getStoreShipmentService();

	// 当前页已选择产品列表
	private JTable productTable;

	// 当前页产品列表模型
	private static StoreShipmentTableModel storeShipmentTableModel;
	// 销售单汇总表模型
	private static StoreShipmentSummaryTableModel storeShipmentSummaryTableModel;

	// 是否是退货
	private static JCheckBox isShipReturn;

	StoreShipmentSearchFrame callBackSearchFrame;

	private Vector<StoreShipment> updateStoreShipments; // 批量修改时使用

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	JTextField groupIDField;// 出库单批次号（同一次出库的唯一标记）
	IvtJTextFieldAuto customerIDField;// 客户编号
	JTextField customerNameField; // 客户名称
	JTextField sellCodeField; // 销售单号
	JTextField expressNbrField; // 快递单号
	JComboBox currencyComboBox; // 币别
	JTextField payDueDateField; // 应付日期
	JButton payDueDateFieldBtn; // 应付日期控件
	JTextField actPayDueDateField; // 实付日期
	JButton actPayDueDateFieldBtn; // 实付日期控件
	JTextField createTimeField;// 销售时间
	JTextField commentField;// 注释
	JTextField userIdField;// 业务员
	JTextField totalAmountField; // 销售单总额
	JButton selectProductButton; // 添加产品按扭
	JButton saveToStoreButton;// 出库保存按扭
	JButton updateStoreButton;// 更新按扭
	JButton delProductButton; // 删除产品按扭
	JButton cancelStoreButton;// 取消按扭

	private void buildStoreShipment(StoreShipment storeShipment) {
		storeShipment.setCustomerId(customerIDField.getText());
		storeShipment.setCustomerName(customerNameField.getText());
		storeShipment.setSellCode(sellCodeField.getText());
		storeShipment.setActPayDueDate(actPayDueDateField.getText());
		storeShipment.setPayDueDate(payDueDateField.getText());
		storeShipment.setCreateTime(createTimeField.getText());
		storeShipment.setComment(commentField.getText());
		storeShipment.setExpressNbr(expressNbrField.getText());
		storeShipment.setUserId(userIdField.getText());
		storeShipment.setGroupId(groupIDField.getText());
		if (isShipReturn.isSelected()) {
			storeShipment.setShipType(Constants.STORE_SHIP_TYPE_RETURN);
		} else {
			storeShipment.setShipType(Constants.STORE_SHIP_TYPE_SELL);
		}
		if (currencyComboBox.getSelectedIndex() != -1) {
			storeShipment.setCurrency(currencyComboBox.getSelectedItem()
					.toString());
		}

		// Populate customer name with given customer id
		if (!DataValidator.isBlankOrNull(storeShipment.getCustomerId())) {
			Customer customer = this.customerService
					.getCustomerInfo(storeShipment.getCustomerId());
			if (customer != null) {
				storeShipment.setCustomerName(customer.getName());
			}
		}
	}

	public StoreShipmentFrame(Vector<StoreShipment> storeShipments,
			final String editType,
			StoreShipmentSearchFrame storeShipmentSearchFrame) {
		super("销售单", true, true, true, true);
		super.setFrameId(Constants.MODULE_SHIPMENT);
		this.callBackSearchFrame = storeShipmentSearchFrame;
		this.editType = editType;
		this.updateStoreShipments = storeShipments;

		Dimension deskPaneDim = MainFrame.desktopPane.getSize();
		this.setSize(deskPaneDim.width * 2 / 3 - 10,
				deskPaneDim.height * 2 / 3 - 55);
		Dimension internalDim = this.getSize();
		this.setLocation(
				(deskPaneDim.width - internalDim.width) / 2 + this.getLayer()
						* 5, (deskPaneDim.height - internalDim.height) / 2
						+ this.getLayer() * 5);

		this.getContentPane().add(createStoreShipmentPanel());
	}

	/**
	 * 获取列表显示的内容
	 * 
	 * @return
	 */
	public static Vector<StoreShipment> getStoreTableModal() {
		return storeShipmentTableModel.getRows();
	}

	/**
	 * 更新列表显示内容
	 * 
	 * @param storeProcurements
	 */
	public static void updateStoreTableModal(
			List<StoreProcurement> storeProcurements) {
		Vector<StoreShipment> existingData = storeShipmentTableModel.storeShipmentVector;
		List idTmp = CommonFactory.getCodeRuleService().getNextObjectCodes(
				Constants.MODULE_SHIPMENT, storeProcurements.size());
		for (int i = 0; i < storeProcurements.size(); i++) {
			StoreShipment temp = new StoreShipment();
			StoreProcurement storeProcurement = storeProcurements.get(i);
			temp.setStoreProcurementId(storeProcurement.getId());
			temp.setProductId(storeProcurement.getProductId());
			temp.setProductCode(storeProcurement.getProductCode());
			temp.setProductName(storeProcurement.getProductName());
			temp.setProductBrand(storeProcurement.getProductBrand());
			temp.setProduceCode(storeProcurement.getProduceCode());
			temp.setProduceTime(storeProcurement.getProduceTime());
			temp.setPack(storeProcurement.getPack());
			temp.setMiniPack(storeProcurement.getMiniPack());
			temp.setShipNumber(storeProcurement.getShipNbr());
			temp.setUnit(storeProcurement.getUnit());
			logger.debug("INVTUnit: " + temp.getCustomizedUnit().toString());

			int shipNbr = temp.getShipNumber();

			if (isShipReturn.isSelected()) {
				if (shipNbr > 0) {
					temp.setShipNumber(-shipNbr);
				}
			} else {
				if (shipNbr < 0) {
					temp.setShipNumber(-shipNbr);
				}
			}

			temp.setId(idTmp.get(i).toString());
			existingData.add(temp);
		}

		storeShipmentTableModel.updateData(existingData);
	}

	public JPanel createStoreShipmentPanel() {
		JPanel mainContainer = new JPanel();
		// mainContainer.setLayout(new BoxLayout(mainContainer,
		// BoxLayout.Y_AXIS));
		mainContainer.setLayout(new BorderLayout());
		Dimension internalDim = this.getSize();

		isShipReturn = new JCheckBox();
		isShipReturn.setBackground(Color.white);
		isShipReturn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 检查销售退货状态，并更新出货量为正或为负
				checkShipNumberAlongReturnBtn();
			}

		});

		JPanel inputTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputTopPanel.setPreferredSize(new Dimension(800, 80));
		JPanel inputTopPanelSub1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel inputTopPanelSub2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JLabel privoderIDLabel = new JLabel("客户编号:");
		// customerIDField = new IvtJTextField(10, 16, true);
		customerIDField = new IvtJTextFieldAuto(10);
		customerIDField.setListValue(CommonFactory.getCustomerService()
				.retrieveAllCustomerCodes());
		customerIDField.getDocument().addDocumentListener(
				new CustomerIDChengListener());
		inputTopPanelSub1.add(privoderIDLabel);
		inputTopPanelSub1.add(customerIDField);

		JLabel privoderNameLabel = new JLabel("客户名称:");
		customerNameField = new IvtJTextField(18, 50);
		customerNameField.setEditable(false);
		inputTopPanelSub1.add(privoderNameLabel);
		inputTopPanelSub1.add(customerNameField);

		JLabel createTimeLabel = new JLabel("销售时间:");
		createTimeField = new IvtJTextField(12, 20);
		createTimeField.setEditable(false);
		inputTopPanelSub1.add(createTimeLabel);
		inputTopPanelSub1.add(createTimeField);

		JLabel userLabel = new JLabel("操作员:");
		userIdField = new IvtJTextField(10, 16);
		userIdField.setText(MainFrame.loginname);
		inputTopPanelSub1.add(userLabel);
		inputTopPanelSub1.add(userIdField);

		JLabel sellCodeLabel = new JLabel("销售单号:");
		sellCodeField = new IvtJTextField(10, 50);
		inputTopPanelSub2.add(sellCodeLabel);
		inputTopPanelSub2.add(sellCodeField);

		JLabel expressNbrLabel = new JLabel("快递单号:");
		expressNbrField = new IvtJTextField(10, 50);
		inputTopPanelSub2.add(expressNbrLabel);
		inputTopPanelSub2.add(expressNbrField);

		JLabel payDueDateLabel = new JLabel("应付日期:");
		payDueDateField = new ObservingTextField(12, 20);
//		payDueDateField.setEditable(false);
		payDueDateField.setBackground(Color.white);
		payDueDateFieldBtn = new JButton("");
		actPayDueDateFieldBtn = new JButton("");
		
		try {
			Image img = ImageIO.read(getClass().getResource("/com/ivt/mis/view/images/calendar.jpg"));
			ImageIcon calIcon = new ImageIcon(img);
			
			payDueDateFieldBtn.setIcon(calIcon);
			payDueDateFieldBtn.setBorderPainted(false);
			actPayDueDateFieldBtn.setIcon(calIcon);
			actPayDueDateFieldBtn.setBorderPainted(false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		payDueDateFieldBtn.addActionListener(new SelectDateListener(
				payDueDateField));
		inputTopPanelSub2.add(payDueDateLabel);
		inputTopPanelSub2.add(payDueDateField);
		inputTopPanelSub2.add(payDueDateFieldBtn);

		JLabel actPayDueDateLabel = new JLabel("实付日期:");
		actPayDueDateField = new ObservingTextField(12, 20);
		actPayDueDateField.setEditable(false);
		actPayDueDateField.setBackground(Color.white);
		
		actPayDueDateFieldBtn.addActionListener(new SelectDateListener(
				actPayDueDateField));

		inputTopPanelSub2.add(actPayDueDateLabel);
		inputTopPanelSub2.add(actPayDueDateField);
		inputTopPanelSub2.add(actPayDueDateFieldBtn);

		JLabel totalAmountLabel = new JLabel("总额:");
		totalAmountField = new JTextField(12);
		totalAmountField.setEditable(false);
		totalAmountField.setHorizontalAlignment(JTextField.RIGHT);
		currencyComboBox = super.getCurrency();
		currencyComboBox.setBackground(Color.white);
		inputTopPanelSub2.add(totalAmountLabel);
		inputTopPanelSub2.add(totalAmountField);
		inputTopPanelSub2.add(currencyComboBox);

		inputTopPanelSub2.add(isShipReturn);
		inputTopPanelSub2.add(new JLabel("销售退货"));

		inputTopPanel.add(inputTopPanelSub1);
		inputTopPanel.add(Box.createVerticalGlue());
		inputTopPanel.add(inputTopPanelSub2);

		JPanel inputBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JLabel commentLabel = new JLabel("注释:");
		commentField = new IvtJTextField(50, 100);
		inputBottomPanel.add(commentLabel);
		inputBottomPanel.add(commentField);

		JLabel groupIDLabel = new JLabel("出库批次:");
		groupIDField = new IvtJTextField(15, 30, true);
		inputBottomPanel.add(groupIDLabel);
		inputBottomPanel.add(groupIDField);

		// 创建水平Box组件
		Box subBox = Box.createVerticalBox();

		JScrollPane productDetailScrollPane = new JScrollPane();
		storeShipmentTableModel = new StoreShipmentTableModel();
		productTable = new JTable(storeShipmentTableModel);
		productTable.setRowHeight(Constants.TABLE_DEFAULT_HEIGHT);
		productTable
				.getColumnModel()
				.getColumn(storeShipmentTableModel.getColumnIndex("shipNumber"))
				.setCellRenderer(
						new TableNumberCellRenderer(storeShipmentTableModel,
								Constants.NUMBER_TYPE_INT));
		productTable
				.getColumnModel()
				.getColumn(storeShipmentTableModel.getColumnIndex("unitPrice"))
				.setCellRenderer(
						new TableNumberCellRenderer(storeShipmentTableModel,
								Constants.NUMBER_TYPE_DOUBLE));
		productTable.setDefaultRenderer(CustomizedUnit.class,
				new CustomizedUnitCellRenderer());
		productTable.setDefaultEditor(CustomizedUnit.class,
				new CustomizedUnitCellEditor(this.getUnitList()));

		productTable.setToolTipText("销售出库明细");

		productTable.setPreferredScrollableViewportSize(new Dimension(
				internalDim.width - 40, internalDim.height * 2 / 3));
		productDetailScrollPane.setViewportView(productTable);
		productTable
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JScrollPane productSumScrollPane = new JScrollPane();
		storeShipmentSummaryTableModel = new StoreShipmentSummaryTableModel();
		JTable productTableSum = new JTable(storeShipmentSummaryTableModel);
		productTableSum.setRowHeight(Constants.TABLE_DEFAULT_HEIGHT);
		productTableSum.setToolTipText("销售出库汇总");
		productTableSum
				.getColumnModel()
				.getColumn(storeShipmentSummaryTableModel.getColumnIndex("shipNumber"))
				.setCellRenderer(
						new TableNumberCellRenderer(
								storeShipmentSummaryTableModel,
								Constants.NUMBER_TYPE_INT));

		productTableSum.setPreferredScrollableViewportSize(new Dimension(
				internalDim.width - 40, internalDim.height * 1 / 3));
		productSumScrollPane.setViewportView(productTableSum);
		productTableSum
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selectProductButton = new JButton("添加产品");
		selectProductButton.addActionListener(new AddProductButtonListener());
		saveToStoreButton = new JButton("出库保存");
		saveToStoreButton.addActionListener(new SaveToStoreButtonListener());
		updateStoreButton = new JButton("更新");
		updateStoreButton.addActionListener(new UpdateStoreButtonListener());
		cancelStoreButton = new JButton("取消");
		cancelStoreButton.addActionListener(new CancelButtonListener());
		delProductButton = new JButton("删除选中产品");
		delProductButton.addActionListener(new DelProductButtonListener());

		buttonPanel.add(inputBottomPanel);
		buttonPanel.add(selectProductButton);
		buttonPanel.add(saveToStoreButton);
		buttonPanel.add(delProductButton);
		buttonPanel.add(updateStoreButton);
		buttonPanel.add(cancelStoreButton);

		subBox.add(productDetailScrollPane);
		subBox.add(Box.createRigidArea(new Dimension(10, 10)));
		subBox.add(productSumScrollPane);

		mainContainer.add(inputTopPanel, BorderLayout.NORTH);
		mainContainer.add(subBox, BorderLayout.CENTER);
		mainContainer.add(buttonPanel, BorderLayout.SOUTH);

		// 装载数据
		populateForm();

		return mainContainer;
	}

	/**
	 * 汇总出库明细
	 */
	private void summarizeStoreShipment() {
		Vector<StoreShipment> storeShipments = storeShipmentTableModel
				.getRows();
		Vector<StoreShipment> storeShipmentsSummary = new Vector<StoreShipment>();
		
		//销售单总额
		double totalAmount = 0;
		
		for (int i = 0; i < storeShipments.size(); i++) {
			StoreShipment storeShipment = storeShipments.get(i);
			StoreShipment storeShipmentNew = getObjFromSummaryList(
					storeShipmentsSummary, storeShipment.getProductId());
			if (storeShipmentNew != null) {
				storeShipmentNew.setShipNumber(storeShipmentNew.getShipNumber()
						+ storeShipment.getShipNumber());
			} else {
				storeShipmentsSummary.add(storeShipment.clone());
			}
			

			totalAmount += storeShipment.getUnitPrice() * storeShipment.getShipNumber();
		}

		//更新出库汇总
		storeShipmentSummaryTableModel.updateData(storeShipmentsSummary);
		
		//更新销售单总额

		DecimalFormat numberFormat = new DecimalFormat();
		numberFormat.applyPattern(Constants.NUMBER_FORMAT_DEFAULT); 
		logger.debug("Total Amount:" + totalAmount);
		logger.debug("Total Amount(format):" + numberFormat.format(totalAmount));
		totalAmountField.setText(numberFormat.format(totalAmount));
		totalAmountField.setForeground(Color.blue);
		
	}

	/**
	 * 根据产品型号取出汇总内对应的对象
	 * 
	 * @param storeShipments
	 * @param productId
	 * @return
	 */
	private StoreShipment getObjFromSummaryList(
			Vector<StoreShipment> storeShipments, String productId) {
		for (int i = 0; i < storeShipments.size(); i++) {
			StoreShipment storeShipment = storeShipments.get(i);
			if (productId.equals(storeShipment.getProductId())) {
				return storeShipment;
			}
		}

		return null;
	}

	public void populateForm() {
		if (Constants.FORM_TYPE_UPDATE.equals(editType)) {
			// 取出第一个，填充顶部信息
			StoreShipment storeShipment = updateStoreShipments.get(0);
			customerIDField.setText(storeShipment.getCustomerId(), true);
			customerNameField.setText(storeShipment.getCustomerName());
			sellCodeField.setText(storeShipment.getSellCode());
			payDueDateField.setText(storeShipment.getPayDueDate());
			actPayDueDateField.setText(storeShipment.getActPayDueDate());
			createTimeField.setText(storeShipment.getCreateTime());
			commentField.setText(storeShipment.getComment());
			expressNbrField.setText(storeShipment.getExpressNbr());
			userIdField.setText(storeShipment.getUserId());
			groupIDField.setText(storeShipment.getGroupId());
//			payDueDateFieldBtn.setEnabled(false);
			groupIDField.setEnabled(false);
			selectProductButton.setEnabled(false);
			saveToStoreButton.setEnabled(false);
			delProductButton.setEnabled(false);
			isShipReturn.setEnabled(false);

			currencyComboBox.setSelectedItem(storeShipment.getCurrency());

			if (Constants.STORE_SHIP_TYPE_RETURN.equals(storeShipment
					.getShipType())) {
				isShipReturn.setSelected(true);
			} else {
				isShipReturn.setSelected(false);
			}

			// 添加产品到列表
			storeShipmentTableModel.resetTable();
			storeShipmentTableModel.updateData(updateStoreShipments);

			customerNameField.setEditable(false);
			customerIDField.setEnabled(false);
			createTimeField.setEnabled(false);
			commentField.setEnabled(true);
			expressNbrField.setEnabled(true);
			userIdField.setEnabled(false);
			selectProductButton.setEnabled(false);
			saveToStoreButton.setEnabled(false);
			delProductButton.setEnabled(false);
			updateStoreButton.setEnabled(true);
			cancelStoreButton.setEnabled(true);

		} else if (Constants.FORM_TYPE_ADD.equals(editType)) {
			groupIDField.setText(CommonFactory.getCodeRuleService()
					.getNextObjectCode(Constants.MODULE_SHIPMENT_BATCH));
			customerNameField.setEditable(false);
			createTimeField.setEnabled(false);
			userIdField.setEnabled(false);
			groupIDField.setEnabled(false);

			isShipReturn.setEnabled(true);
			selectProductButton.setEnabled(true);
			saveToStoreButton.setEnabled(true);
			delProductButton.setEnabled(true);
			updateStoreButton.setEnabled(false);
		}
	}

	/**
	 * 若为销售退货，则更新为负数,否则更新为正数
	 */
	private void checkShipNumberAlongReturnBtn() {
		Vector<StoreShipment> rows = storeShipmentTableModel.getRows();
		for (int i = 0; i < rows.size(); i++) {
			StoreShipment storeShipment = rows.get(i);
			int shipNbr = storeShipment.getShipNumber();
			if (isShipReturn.isSelected()) {
				if (shipNbr > 0) {
					storeShipment.setShipNumber(-shipNbr);
				}
			} else {
				if (shipNbr < 0) {
					storeShipment.setShipNumber(-shipNbr);
				}
			}
		}

		storeShipmentTableModel.updateData(rows);
	}

	class AddProductButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (DataValidator.isBlankOrNull(customerIDField.getText())) {
				JOptionPane.showMessageDialog(MainFrame.desktopPane, "请输入客户编号",
						"警告", JOptionPane.WARNING_MESSAGE);
			} else {
				new ProductShipmentSelectionJDialog(selectProductButton,
						customerIDField.getText());
			}

		}
	}

	class DelProductButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] selectedRowNbr = productTable.getSelectedRows();

			ArrayList<StoreShipment> delObjects = new ArrayList<StoreShipment>();
			if (selectedRowNbr.length == 0) {
				JOptionPane.showMessageDialog(MainFrame.desktopPane,
						"请选中需要删除的行", "警告", JOptionPane.WARNING_MESSAGE);
			} else {
				if (JOptionPane.showConfirmDialog(MainFrame.desktopPane,
						"确认删除所选行吗？", "警告", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

					// storeShipmentTableModel.removeRow(selectedRowNbr);
					for (int i = 0; i < selectedRowNbr.length; i++) {
						String id = String.valueOf(storeShipmentTableModel
								.getValueAt(selectedRowNbr[i], 0));
						StoreShipment delObj = new StoreShipment(id);
						delObjects.add(delObj);
					}

					storeShipmentTableModel.removeRows(delObjects);
				}
			}
		}
	}

	class UpdateStoreButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Vector<StoreShipment> storeShipments = storeShipmentTableModel
					.getRows();
			StoreShipmentValidator storeShipmentValidator = new StoreShipmentValidator(
					null, customerServie, Constants.FORM_TYPE_UPDATE);
			StringBuffer errorsBuf = new StringBuffer();

			if (storeShipments.size() == 0) {
				errorsBuf.append("您未选择任何产品");
			}

			for (int i = 0; i < storeShipments.size(); i++) {
				StoreShipment storeShipment = storeShipments.get(i);
				buildStoreShipment(storeShipment);
				storeShipmentValidator.setStoreShipment(storeShipment);

				String errorString = storeShipmentValidator.getErrorString();
				int rowNbr = i + 1;
				if (errorString != null) {
					errorsBuf.append("第" + rowNbr + "行:\n" + errorString);
				}
			}

			if (errorsBuf.toString().length() > 0) {
				showErrorMsg(errorsBuf.toString());
			} else {
				// Save data into database
				if (storeShipmentService
						.modifyBatchStoreShipment(new ArrayList(storeShipments))) {
					showInfoMsg("入货单更新成功");
					// 关闭并刷新父页面
					if (callBackSearchFrame != null) {
						logger.debug("call back method execute");
						callBackSearchFrame.callBackRefresh(false);
					}
					dispose();
				} else {
					showErrorMsg("入货单更新失败");
				}
			}
		}
	}

	class SaveToStoreButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Vector<StoreShipment> storeShipments = storeShipmentTableModel
					.getRows();
			StoreShipmentValidator storeShipmentValidator = new StoreShipmentValidator(
					null, customerServie, Constants.FORM_TYPE_ADD);
			StringBuffer errorsBuf = new StringBuffer();

			if (storeShipments.size() == 0) {
				errorsBuf.append("您未选择任何产品");
			}

			// String groupId = BasicTypeUtils.getPremaryKeyID("GRP");
			for (int i = 0; i < storeShipments.size(); i++) {
				StoreShipment storeShipment = storeShipments.get(i);
				buildStoreShipment(storeShipment);
				storeShipmentValidator.setStoreShipment(storeShipment);

				String errorString = storeShipmentValidator.getErrorString();
				int rowNbr = i + 1;
				if (errorString != null) {
					errorsBuf.append("第" + rowNbr + "行:\n" + errorString);
				}
			}

			if (errorsBuf.toString().length() > 0) {
				showErrorMsg(errorsBuf.toString());
			} else {
				// Save data into database
				if (storeShipmentService.saveBatchStoreShipment(new ArrayList(
						storeShipments))) {
					showInfoMsg("入货单添加成功");
					// 关闭并刷新父页面
					if (callBackSearchFrame != null) {
						logger.debug("call back method execute");
						callBackSearchFrame.callBackRefresh(false);
					}
					dispose();
					// storeTableModel.resetTable();
					// 切换至修改模式
					// editType = Constants.FORM_TYPE_UPDATE;
					// updateStoreShipments = new Vector<StoreShipment>();
					// updateStoreShipments.addAll(storeShipments);
					// populateForm();
				} else {
					showErrorMsg("入货单添加失败");
				}
			}
		}
	}

	class CustomerIDChengListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			fetchCustomerInfor();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			fetchCustomerInfor();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			fetchCustomerInfor();
		}

		public void fetchCustomerInfor() {
			String id = customerIDField.getText();
			if (id.length() > 3) {
				Customer customer = customerServie.getCustomerInfo(id);
				if (customer != null) {
					customerNameField.setText(customer.getName());
					customerNameField.setBackground(getBackground());

					// 自动填写其他相关信息
					createTimeField.setText(BasicTypeUtils.getLongFmtDate());
				} else {
					customerNameField.setText("编号无法识别");
					customerNameField.setBackground(Color.YELLOW);
				}
			} else {
				customerNameField.setText("编号无法识别");
				customerNameField.setBackground(Color.YELLOW);
			}
		}
	}

	class StoreShipmentTableModel extends AbstractTableModel {
		/**
 * 
 */
		private static final long serialVersionUID = 3351544547425285393L;
		Vector<StoreShipment> storeShipmentVector = new Vector<StoreShipment>();

		String[][] columnNames;

		public StoreShipmentTableModel() {
			StoreShipment tmp = new StoreShipment();
			columnNames = tmp.getColumnsDetail();
		}

		public int getColumnIndex(String columnName) {
			int index = 0;

			for (int i = 0; i < columnNames.length; i++) {
				if (columnNames[i][1].equalsIgnoreCase(columnName)) {
					index = i;
					break;
				}
			}
			return index;
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return storeShipmentVector.size();
		}

		public String getColumnName(int col) {
			return columnNames[col][0];
		}

		public Vector<StoreShipment> getRows() {
			return storeShipmentVector;
		}

		public void removeRow(int lineNumber) {
			getRows().removeElementAt(lineNumber);
			fireTableDataChangedLocal();
		}

		public void removeRows(Collection<?> c) {
			getRows().removeAll(c);
			fireTableDataChangedLocal();
		}

		public void resetTable() {
			getRows().removeAllElements();
			fireTableDataChangedLocal();
		}

		public Object getValueAt(int row, int col) {
			StoreShipment storeShipment = storeShipmentVector.get(row);

			return storeShipment.getValueAt(columnNames[col][1]);
		}

		@SuppressWarnings("unchecked")
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			if (col >= columnNames.length - 3) {
				return true;
			} else {
				return false;
			}
		}

		/*
		 * Don't need to implement this method unless your table's data can
		 * change.
		 */
		public void setValueAt(Object value, int row, int col) {
			StoreShipment storeShipment = storeShipmentVector.get(row);
			storeShipment.setValueAt(value, columnNames[col][1]);
			fireTableCellUpdated(row, col);
			fireTableDataChangedLocal();
			checkShipNumberAlongReturnBtn();
		}

		public void fireTableDataChangedLocal() {
			fireTableDataChanged();
			// 更新Summary
			summarizeStoreShipment();
		}

		// 更新数据
		public void updateData(Vector<StoreShipment> storeShipmentVector) {
			this.storeShipmentVector = storeShipmentVector;
			if (storeShipmentVector.size() == 0) {
				this.storeShipmentVector = new Vector<StoreShipment>();
			} else {
				fireTableRowsInserted(0, storeShipmentVector.size() - 1);
			}

			// 汇总出库
			fireTableDataChangedLocal();
		}

	}

	class StoreShipmentSummaryTableModel extends AbstractTableModel {
		/**
 * 
 */
		private static final long serialVersionUID = 3351544547425285393L;
		Vector<StoreShipment> storeShipmentVector = new Vector<StoreShipment>();

		String[][] columnNames;

		public StoreShipmentSummaryTableModel() {
			StoreShipment tmp = new StoreShipment();
			columnNames = tmp.getColumnsSummary();
		}
		
		public int getColumnIndex(String columnName) {
			int index = 0;

			for (int i = 0; i < columnNames.length; i++) {
				if (columnNames[i][1].equalsIgnoreCase(columnName)) {
					index = i;
					break;
				}
			}
			return index;
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return storeShipmentVector.size();
		}

		public String getColumnName(int col) {
			return columnNames[col][0];
		}

		public Vector<StoreShipment> getRows() {
			return storeShipmentVector;
		}

		public void removeRow(int lineNumber) {
			getRows().removeElementAt(lineNumber);
			fireTableDataChanged();
		}

		public void removeRows(Collection<?> c) {
			getRows().removeAll(c);
			fireTableDataChanged();
		}

		public void resetTable() {
			getRows().removeAllElements();
			fireTableDataChanged();
		}

		public Object getValueAt(int row, int col) {
			StoreShipment storeShipment = storeShipmentVector.get(row);

			return storeShipment.getValueAt(columnNames[col][1]);
		}

		@SuppressWarnings("unchecked")
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			return false;
		}

		// 更新数据
		public void updateData(Vector<StoreShipment> storeShipmentVector) {
			this.storeShipmentVector = storeShipmentVector;
			if (storeShipmentVector.size() == 0) {
				this.storeShipmentVector = new Vector<StoreShipment>();
			}
			fireTableDataChanged();
		}
	}

}