package com.ivt.mis.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

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
import com.ivt.mis.model.Product;
import com.ivt.mis.model.Provider;
import com.ivt.mis.model.StoreProcurement;
import com.ivt.mis.model.StoreShipment;
import com.ivt.mis.service.CustomerService;
import com.ivt.mis.service.ProviderService;
import com.ivt.mis.service.StoreProcurementService;
import com.ivt.mis.view.render.CustomizedUnitCellEditor;
import com.ivt.mis.view.render.CustomizedUnitCellRenderer;
import com.ivt.mis.view.validator.StoreProcurementValidator;

public class StoreProcurementFrame extends BaseJInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3553858497474473395L;

	public CustomerService customerService = CommonFactory.getCustomerService();
	public ProviderService providerServie = CommonFactory.getProviderService();
	public StoreProcurementService storeProcurementService = CommonFactory
			.getStoreProcurementService();

	// 当前页已选择产品列表
	private JTable productTable;

	// 当前页产品列表模型
	private static StoreProcurementTableModel storeProcurementTableModel;

	private Vector<StoreProcurement> updateStoreProcurements; // 批量修改时使用

	// 是否是退货
	private JCheckBox isProcurementReturn;

	StoreProcurementSearchFrame callBackSearchFrame;

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	JTextField groupIDField;// 入库单批次号（同一次入库的唯一标记）
	IvtJTextFieldAuto providerIDField;// 供应商编号
	JTextField providerNameField; // 供应商名称
	JTextField purchaseCodeField; // 采购单号
	JTextField createTimeField;// 进货时间
	JComboBox currencyComboBox; // 币别
	JTextField commentField;// 注释
	JTextField userIdField;// 业务员
	JButton selectProductButton; // 添加产品按扭
	JButton saveToStoreButton;// 入库保存按扭
	JButton updateStoreButton;// 更新按扭
	JButton delProductButton; // 删除产品按扭
	JButton cancelStoreButton;// 取消按扭

	public StoreProcurementFrame(Vector<StoreProcurement> storeProcurements,
			final String editType,
			StoreProcurementSearchFrame storeProcurementSearchFrame) {
		super("进货单", true, true, true, true);
		super.setFrameId(Constants.MODULE_PROCUREMENT);
		this.callBackSearchFrame = storeProcurementSearchFrame;
		this.editType = editType;
		this.updateStoreProcurements = storeProcurements;

		Dimension deskPaneDim = MainFrame.desktopPane.getSize();
		this.setSize(screenSize.width * 2 / 3 - 10,
				screenSize.height * 2 / 3 - 55);
		Dimension internalDim = this.getSize();
		this.setLocation(
				(deskPaneDim.width - internalDim.width) / 2 + this.getLayer()
						* 5, (deskPaneDim.height - internalDim.height) / 2
						+ this.getLayer() * 5);

		this.getContentPane().add(createStoreProcurementPanel());
	}

	private void buildStoreProcurement(StoreProcurement storeProcurement) {
		storeProcurement.setProviderId(providerIDField.getText());
		storeProcurement.setProviderName(providerNameField.getText());
		storeProcurement.setPurchaseCode(purchaseCodeField.getText());
		storeProcurement.setCreateTime(createTimeField.getText());
		storeProcurement.setComment(commentField.getText());
		storeProcurement.setUserId(userIdField.getText());
		storeProcurement.setGroupId(groupIDField.getText());
		storeProcurement.setCurrency(currencyComboBox.getSelectedItem()
				.toString());
		if (isProcurementReturn.isSelected()) {
			storeProcurement
					.setProType(Constants.STORE_PROCUREMENT_TYPE_RETURN);
		} else {
			storeProcurement.setProType(Constants.STORE_PROCUREMENT_TYPE_PRO);
		}

		// Populate customer name with given customer id
		if (!DataValidator.isBlankOrNull(storeProcurement.getCustomerId())) {
			Customer customer = this.customerService
					.getCustomerInfo(storeProcurement.getCustomerId());
			if (customer != null) {
				storeProcurement.setCustomerName(customer.getName());
			}
		}
	}

	public static void updateStoreTableModal(List<Product> newAddedProduct) {
		Vector<StoreProcurement> existingData = storeProcurementTableModel.storeProcurementVector;
		List idTmp = CommonFactory.getCodeRuleService().getNextObjectCodes(
				Constants.MODULE_PROCUREMENT, newAddedProduct.size());
		for (int i = 0; i < newAddedProduct.size(); i++) {
			StoreProcurement temp = new StoreProcurement();
			Product product = newAddedProduct.get(i);
			temp.setProductId(product.getId());
			temp.setProductCode(product.getProductCode());
			temp.setProductName(product.getProductName());
			temp.setProductBrand(product.getBrand());
			temp.setUnit(product.getUnit());
			temp.setPack(product.getPack());
			temp.setMiniPack(product.getMiniPack());
			temp.setId(idTmp.get(i).toString());
			temp.setProduceTime(BasicTypeUtils.getShortFmtDate());
			existingData.add(temp);
		}

		storeProcurementTableModel.updateData(existingData);
	}

	public JPanel createStoreProcurementPanel() {
		JPanel mainContainer = new JPanel();
		mainContainer.setLayout(new BorderLayout());
		// mainContainer.setLayout(new BoxLayout(mainContainer,
		// BoxLayout.Y_AXIS));

		Dimension internalDim = this.getSize();

		JPanel inputTopPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel inputBottomPane = new JPanel(new FlowLayout(FlowLayout.LEFT));

		isProcurementReturn = new JCheckBox("销售退货");
		isProcurementReturn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 检查销售退货状态，并更新出货量为正或为负
				checkProcurementNumberAlongReturnBtn();
			}

		});

		JLabel privoderIDLabel = new JLabel("供应商编号:");
		providerIDField = new IvtJTextFieldAuto(10);
		providerIDField.setListValue(CommonFactory.getProviderService()
				.retrieveAllProviderCodes());
		providerIDField.getDocument().addDocumentListener(
				new ProviderIDChengListener());
		inputTopPane.add(privoderIDLabel);
		inputTopPane.add(providerIDField);

		JLabel privoderNameLabel = new JLabel("供应商名称:");
		providerNameField = new IvtJTextField(18, 50);
		providerNameField.setEditable(false);
		inputTopPane.add(privoderNameLabel);
		inputTopPane.add(providerNameField);

		JLabel purchaseCodeLabel = new JLabel("采购单号:");
		purchaseCodeField = new IvtJTextField(10, 50);
		inputTopPane.add(purchaseCodeLabel);
		inputTopPane.add(purchaseCodeField);

		JLabel currencyLabel = new JLabel("币别:");
		currencyComboBox = super.getCurrency();
		currencyComboBox.setBackground(Color.white);
		inputTopPane.add(currencyLabel);
		inputTopPane.add(currencyComboBox);

		JLabel createTimeLabel = new JLabel("进货时间:");
		createTimeField = new IvtJTextField(12, 20);
		createTimeField.setEditable(false);
		inputTopPane.add(createTimeLabel);
		inputTopPane.add(createTimeField);

		JLabel userLabel = new JLabel("操作员:");
		userIdField = new IvtJTextField(10, 16);
		userIdField.setText(MainFrame.loginname);
		inputTopPane.add(userLabel);
		inputTopPane.add(userIdField);

		JLabel commentLabel = new JLabel("注释:");
		commentField = new IvtJTextField(50, 100);
		inputBottomPane.add(commentLabel);
		inputBottomPane.add(commentField);

		JLabel groupIDLabel = new JLabel("入库批次:");
		groupIDField = new IvtJTextField(15, 30);
		inputBottomPane.add(groupIDLabel);
		inputBottomPane.add(groupIDField);

		JScrollPane productScrollPane = new JScrollPane();
		storeProcurementTableModel = new StoreProcurementTableModel();
		productTable = new JTable(storeProcurementTableModel);
		productTable.setRowHeight(Constants.TABLE_DEFAULT_HEIGHT);
		productTable
				.getColumnModel()
				.getColumn(
						storeProcurementTableModel.getColumnIndex("proNumber"))
				.setCellRenderer(
						new TableNumberCellRenderer(storeProcurementTableModel,
								Constants.NUMBER_TYPE_INT));
		productTable
				.getColumnModel()
				.getColumn(
						storeProcurementTableModel.getColumnIndex("unitPrice"))
				.setCellRenderer(
						new TableNumberCellRenderer(storeProcurementTableModel,
								Constants.NUMBER_TYPE_DOUBLE));

		productTable.setDefaultRenderer(CustomizedUnit.class,
				new CustomizedUnitCellRenderer());
		productTable.setDefaultEditor(CustomizedUnit.class,
				new CustomizedUnitCellEditor(this.getUnitList()));

		productTable.setPreferredScrollableViewportSize(new Dimension(
				internalDim.width - 40, internalDim.height * 3 / 7));
		productScrollPane.setViewportView(productTable);

		productTable
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		// productTable.setBackground(Color.white);
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// buttonPanel.add(isProcurementReturn);
		selectProductButton = new JButton("添加产品");
		selectProductButton.addActionListener(new AddProductButtonListener());
		saveToStoreButton = new JButton("入库保存");
		saveToStoreButton.addActionListener(new SaveToStoreButtonListener());
		updateStoreButton = new JButton("更新");
		updateStoreButton.addActionListener(new UpdateStoreButtonListener());
		cancelStoreButton = new JButton("取消");
		cancelStoreButton.addActionListener(new CancelButtonListener());
		delProductButton = new JButton("删除选中产品");
		delProductButton.addActionListener(new DelProductButtonListener());

		// 装载数据
		populateForm();
		buttonPanel.add(inputBottomPane);
		buttonPanel.add(selectProductButton);
		buttonPanel.add(saveToStoreButton);
		buttonPanel.add(delProductButton);
		buttonPanel.add(updateStoreButton);
		buttonPanel.add(cancelStoreButton);

		mainContainer.add(inputTopPane, BorderLayout.NORTH);
		mainContainer.add(productScrollPane, BorderLayout.CENTER);
		// mainContainer.add(inputBottomPane);
		mainContainer.add(buttonPanel, BorderLayout.SOUTH);

		return mainContainer;
	}

	public void populateForm() {
		if (Constants.FORM_TYPE_UPDATE.equals(editType)) {
			// 取出第一个，填充顶部信息
			StoreProcurement storeProcurement = updateStoreProcurements.get(0);
			providerIDField.setText(storeProcurement.getProviderId(), true);
			providerNameField.setText(storeProcurement.getProviderName());
			purchaseCodeField.setText(storeProcurement.getPurchaseCode());
			createTimeField.setText(storeProcurement.getCreateTime());
			commentField.setText(storeProcurement.getComment());
			userIdField.setText(storeProcurement.getUserId());
			groupIDField.setText(storeProcurement.getGroupId());
			if (currencyComboBox.getSelectedIndex() != -1) {
				currencyComboBox
						.setSelectedItem(storeProcurement.getCurrency());
			}
			groupIDField.setEditable(false);
			createTimeField.setEditable(false);
			userIdField.setEditable(false);

			selectProductButton.setEnabled(false);
			saveToStoreButton.setEnabled(false);
			delProductButton.setEnabled(false);
			isProcurementReturn.setEnabled(false);
			if (Constants.STORE_PROCUREMENT_TYPE_RETURN.equals(storeProcurement
					.getProType())) {
				isProcurementReturn.setSelected(true);
			} else {
				isProcurementReturn.setSelected(false);
			}

			// 添加产品到列表
			storeProcurementTableModel.resetTable();
			storeProcurementTableModel.updateData(updateStoreProcurements);

			providerIDField.setEditable(false);
			providerNameField.setEditable(false);
			commentField.setEditable(false);
			selectProductButton.setEnabled(false);
			saveToStoreButton.setEnabled(false);
			delProductButton.setEnabled(false);
			updateStoreButton.setEnabled(true);
			cancelStoreButton.setEnabled(true);

		} else if (Constants.FORM_TYPE_ADD.equals(editType)) {
			groupIDField.setText(CommonFactory.getCodeRuleService()
					.getNextObjectCode(Constants.MODULE_PROCUREMENT_BATCH));
			logger.debug("groupIDField: " + groupIDField.getText());
			providerNameField.setEditable(false);
			createTimeField.setEditable(false);
			userIdField.setEditable(false);
			groupIDField.setEditable(false);
			isProcurementReturn.setEnabled(true);
			selectProductButton.setEnabled(true);
			saveToStoreButton.setEnabled(true);
			delProductButton.setEnabled(true);
			updateStoreButton.setEnabled(false);
		}
	}

	/**
	 * 若为采购退货，则更新为负数,否则更新为正数
	 */
	private void checkProcurementNumberAlongReturnBtn() {
		Vector<StoreProcurement> rows = storeProcurementTableModel.getRows();
		for (int i = 0; i < rows.size(); i++) {
			StoreProcurement storeProcurement = rows.get(i);
			int nbr = storeProcurement.getProNumber();
			if (isProcurementReturn.isSelected()) {
				if (nbr > 0) {
					storeProcurement.setProNumber(-nbr);
				}
			} else {
				if (nbr < 0) {
					storeProcurement.setProNumber(-nbr);
				}
			}
		}

		storeProcurementTableModel.updateData(rows);
	}

	class AddProductButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			new ProductSelectionJDialog(selectProductButton);
		}
	}

	class DelProductButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] selectedRowNbr = productTable.getSelectedRows();
			ArrayList<StoreProcurement> delObjects = new ArrayList<StoreProcurement>();
			if (selectedRowNbr.length == 0) {
				JOptionPane.showMessageDialog(MainFrame.desktopPane,
						"请选中需要删除的行(可多选)", "警告", JOptionPane.WARNING_MESSAGE);
			} else {
				if (JOptionPane.showConfirmDialog(MainFrame.desktopPane,
						"确认删除所选行吗？", "警告", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

					for (int i = 0; i < selectedRowNbr.length; i++) {
						String id = String.valueOf(storeProcurementTableModel
								.getValueAt(selectedRowNbr[i], 0));
						StoreProcurement delObj = new StoreProcurement(id);
						delObjects.add(delObj);
					}

					storeProcurementTableModel.removeRows(delObjects);
				}
			}
		}
	}

	class UpdateStoreButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Vector<StoreProcurement> storeProcurements = storeProcurementTableModel
					.getRows();
			StoreProcurementValidator storeProcurementValidator = new StoreProcurementValidator(
					null, providerServie, customerService,
					Constants.FORM_TYPE_UPDATE);
			StringBuffer errorsBuf = new StringBuffer();

			if (storeProcurements.size() == 0) {
				errorsBuf.append("您未选择任何产品");
			}

			for (int i = 0; i < storeProcurements.size(); i++) {
				StoreProcurement storeProcurement = storeProcurements.get(i);
				buildStoreProcurement(storeProcurement);
				storeProcurementValidator.setStoreProcurement(storeProcurement);

				String errorString = storeProcurementValidator.getErrorString();
				int rowNbr = i + 1;
				if (errorString != null) {
					errorsBuf.append("第" + rowNbr + "行:\n" + errorString);
				}
			}

			if (errorsBuf.toString().length() > 0) {
				showErrorMsg(errorsBuf.toString());
			} else {
				// Save data into database
				if (storeProcurementService
						.modifyBatchStoreProcurement(new ArrayList(
								storeProcurements))) {
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
			Vector<StoreProcurement> storeProcurements = storeProcurementTableModel
					.getRows();
			StoreProcurementValidator storeProcurementValidator = new StoreProcurementValidator(
					null, providerServie, customerService,
					Constants.FORM_TYPE_ADD);
			StringBuffer errorsBuf = new StringBuffer();

			if (storeProcurements.size() == 0) {
				errorsBuf.append("您未选择任何产品");
			}

			// String groupId = BasicTypeUtils.getPremaryKeyID("GRP");
			for (int i = 0; i < storeProcurements.size(); i++) {
				StoreProcurement storeProcurement = storeProcurements.get(i);
				buildStoreProcurement(storeProcurement);
				storeProcurementValidator.setStoreProcurement(storeProcurement);

				String errorString = storeProcurementValidator.getErrorString();
				int rowNbr = i + 1;
				if (errorString != null) {
					errorsBuf.append("第" + rowNbr + "行:\n" + errorString);
				}
			}

			if (errorsBuf.toString().length() > 0) {
				showErrorMsg(errorsBuf.toString());
			} else {
				// Save data into database
				if (storeProcurementService
						.saveBatchStoreProcurement(new ArrayList(
								storeProcurements))) {
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
					// updateStoreProcurements = new Vector<StoreProcurement>();
					// updateStoreProcurements.addAll(storeProcurements);
					// populateForm();
				} else {
					showErrorMsg("入货单添加失败");
				}
			}
		}
	}

	class ProviderIDChengListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			fetchProviderInfor();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			fetchProviderInfor();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			fetchProviderInfor();
		}

		public void fetchProviderInfor() {
			String id = providerIDField.getText();
			if (id.length() > 3) {
				Provider provider = providerServie.getProviderInfo(id);
				if (provider != null) {
					providerNameField.setText(provider.getName());
					providerNameField.setBackground(getBackground());

					// 自动填写其他相关信息
					createTimeField.setText(BasicTypeUtils.getLongFmtDate());
				} else {
					providerNameField.setText("编号无法识别");
					providerNameField.setBackground(Color.YELLOW);
				}
			} else {
				providerNameField.setText("编号无法识别");
				providerNameField.setBackground(Color.YELLOW);
			}
		}
	}

	class StoreProcurementTableModel extends AbstractTableModel {
		/**
 * 
 */
		private static final long serialVersionUID = 3351544547425285393L;
		Vector<StoreProcurement> storeProcurementVector = new Vector<StoreProcurement>();
		String[][] columnNames;

		public StoreProcurementTableModel() {
			StoreProcurement tmp = new StoreProcurement();
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
			return storeProcurementVector.size();
		}

		public String getColumnName(int col) {
			return columnNames[col][0];
		}

		public Vector<StoreProcurement> getRows() {
			return storeProcurementVector;
		}

		public void removeRows(Collection<?> c) {
			getRows().removeAll(c);
			fireTableDataChanged();
		}

		public void removeRow(int lineNumber) {
			getRows().removeElementAt(lineNumber);
			fireTableDataChanged();
		}

		public void resetTable() {
			getRows().removeAllElements();
			fireTableDataChanged();
		}

		public Object getValueAt(int row, int col) {
			StoreProcurement storeProcurement = storeProcurementVector.get(row);

			return storeProcurement.getValueAt(columnNames[col][1]);
		}

		@SuppressWarnings("unchecked")
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			if (col > 3) {
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
			StoreProcurement storeProcurement = storeProcurementVector.get(row);
			storeProcurement.setValueAt(value, columnNames[col][1]);
			fireTableCellUpdated(row, col);
			checkProcurementNumberAlongReturnBtn();
		}

		// 更新数据
		public void updateData(Vector<StoreProcurement> storeProcurementVector) {
			this.storeProcurementVector = storeProcurementVector;
			if (storeProcurementVector.size() == 0) {
				this.storeProcurementVector = new Vector<StoreProcurement>();
			} else {
				fireTableRowsInserted(0, storeProcurementVector.size() - 1);
			}
		}

	}
}
