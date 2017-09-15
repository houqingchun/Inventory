package com.ivt.mis.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.DataValidator;
import com.ivt.mis.common.swing.IvtJTextFieldAuto;
import com.ivt.mis.common.swing.SpringUtilities;
import com.ivt.mis.model.Product;
import com.ivt.mis.model.StoreManage;
import com.ivt.mis.model.StoreProcurement;
import com.ivt.mis.model.StoreShipment;
import com.ivt.mis.service.StoreManageService;
import com.ivt.mis.service.StoreProcurementService;

public class ProductShipmentSelectionJDialog extends BaseJDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5223649924792699112L;

	private JTable table;

	private IvtJTextFieldAuto productCodeField; // 产品型号
	private JTextField productBrandField; // 产品品牌
	private JTextField shipNbrField; // 出库总量
	private JCheckBox selectAllBox; // 全选按扭
	private boolean isStoreEnough; // 库存是否充足标记
	private StoreProcurementTableModel storeProcurementTableModel;// 显示TABLE模型
	private String preOrderedCustomerId;// 预订客户编号（用于默认情况下查询此客户已经预定的库存）

	private ProductShipSelectionPanel productShipSelectionPanel;

	public ProductShipmentSelectionJDialog(Component relativeComponent,
			String preOrderedCustomerId) {
		super(new JFrame(), "请选择出库产品", true);
		this.preOrderedCustomerId = preOrderedCustomerId;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		Image image = null;
		try {
			image = ImageIO.read(this.getClass().getResource(
					Constants.TASK_ICON_PATH));
		} catch (IOException e1) {
			e1.printStackTrace();
		} // 创建图片对象
		this.setIconImage(image);// 设置图标

		this.setSize(800, 200);

		setLocationRelativeTo(null);

		productShipSelectionPanel = new ProductShipSelectionPanel();
		productShipSelectionPanel.setOpaque(true); // content panes must be
													// opaque

		setContentPane(productShipSelectionPanel);

		// Display the window.
		pack();
		setVisible(true);
	}

	class ProductShipSelectionPanel extends JPanel {
		/**
	 * 
	 */
		private static final long serialVersionUID = 2044933397778396918L;
		private TableRowSorter<StoreProcurementTableModel> sorter;

		public ProductShipSelectionPanel() {
			super();
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			// Create a table with a sorter.
			storeProcurementTableModel = new StoreProcurementTableModel();
			sorter = new TableRowSorter<StoreProcurementTableModel>(storeProcurementTableModel);
			table = new JTable(storeProcurementTableModel);
			table.setRowHeight(Constants.TABLE_DEFAULT_HEIGHT);
			// table.setRowSorter(sorter);
			table.setPreferredScrollableViewportSize(new Dimension(800, 200));
			table.setFillsViewportHeight(true);

			StoreProcurementService storeProcurementService = CommonFactory
					.getStoreProcurementService();

			// 默认显示此客户已经预定的库存产品
			StoreProcurement storeProcurement = new StoreProcurement();
			storeProcurement.setCustomerId(preOrderedCustomerId);

			Vector<StoreProcurement> storeProcurementVector = storeProcurementService
					.retrieveStoreProcurements(storeProcurement);
			StoreManageService storeManageService = CommonFactory
					.getStoreManageService();
			Vector<StoreManage> storeManages = storeManageService
					.retrieveAllStoreManages();
			// 自动计算库存，并显示
			storeProcurementTableModel.updateData(calculateStorage(storeManages,
					storeProcurementVector, 999999));

			table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			// When selection changes, provide user with row numbers for
			// both view and model.
			table.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent event) {
							int rowCount = table.getRowCount();
							StringBuffer buf = new StringBuffer();
							for (int i = 0; i < rowCount; i++) {
								Boolean viewRow = Boolean.parseBoolean(String
										.valueOf(table.getValueAt(i,
												table.getColumnCount() - 1)));

								String productCode = (String) table.getValueAt(
										i, 1);
								if (viewRow.booleanValue()) {
									buf.append(productCode + ";");
								}
							}
						}
					});

			// Create the scroll pane and add the table to it.
			JScrollPane scrollPane = new JScrollPane(table);

			// Add the scroll pane to this panel.
			add(scrollPane);

			// Create a separate form for filterText and statusText
			JPanel form = new JPanel(new SpringLayout());
			JLabel productCodeLabel = new JLabel("产品型号:",
					SwingConstants.TRAILING);
			form.add(productCodeLabel);
			productCodeField = new IvtJTextFieldAuto(10); // 自定义输入框，限定只能输入大写字符
			productCodeField.setListValue(CommonFactory.getProductService()
					.retrieveAllProductCodes());
			productCodeLabel.setLabelFor(productCodeField);
			form.add(productCodeField);

			JLabel productBrandLabel = new JLabel("品牌:",
					SwingConstants.TRAILING);
			form.add(productBrandLabel);
			productBrandField = new JTextField();
			productBrandLabel.setLabelFor(productBrandField);
			form.add(productBrandField);

			JLabel shipNbrLabel = new JLabel("出库数量:", SwingConstants.TRAILING);
			form.add(shipNbrLabel);
			shipNbrField = new JTextField();
			shipNbrLabel.setLabelFor(shipNbrField);
			shipNbrField.setText("0");
			form.add(shipNbrField);

			SpringUtilities.makeCompactGrid(form, 1, 6, 6, 6, 6, 6);
			add(form);

			JPanel buttonPanel = new JPanel(new SpringLayout());
			buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
			JButton queryStoreBtn = new JButton("查询库存");
			queryStoreBtn.addActionListener(new QueryStoreButtonListener());
			buttonPanel.add(queryStoreBtn);
			buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
			JButton addToStoreBtn = new JButton("添加到销售单");
			addToStoreBtn.addActionListener(new AddToStoreButtonListener());
			buttonPanel.add(addToStoreBtn);
			buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
			JButton cancelBtn = new JButton("取消");
			cancelBtn.addActionListener(new CancelButtonListener());
			buttonPanel.add(cancelBtn);
			buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
			selectAllBox = new JCheckBox("全选");
			selectAllBox.addItemListener(new SelectAllBoxListener());
			buttonPanel.add(selectAllBox);
			add(buttonPanel);
		}

		/**
		 * Update the row filter regular expression from the expression in the
		 * text box.
		 */
		private void newFilter() {
			RowFilter<StoreProcurementTableModel, Object> rf = null;
			// If current expression doesn't parse, don't update.
			try {
				rf = RowFilter.regexFilter(productCodeField.getText(), 0, 1, 2);
			} catch (java.util.regex.PatternSyntaxException e) {
				return;
			}
			sorter.setRowFilter(rf);
		}

		class SelectAllBoxListener implements ItemListener {

			@Override
			public void itemStateChanged(ItemEvent e) {
				int rowCount = table.getRowCount();
				if (selectAllBox.isSelected()) {
					for (int i = 0; i < rowCount; i++) {
						table.setValueAt(true, i, table.getColumnCount() - 1);
					}
				} else {
					for (int i = 0; i < rowCount; i++) {
						table.setValueAt(false, i, table.getColumnCount() - 1);
					}
				}
			}
		}
	}

	class QueryStoreButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			StoreManageService storeManageService = CommonFactory
					.getStoreManageService();
			StoreProcurementService storeProcurementService = CommonFactory
					.getStoreProcurementService();
			String productCode = productCodeField.getText();
			String productBrand = productBrandField.getText();
			String shipNbrStr = shipNbrField.getText();

			boolean hasError = false;
			StringBuffer errors = new StringBuffer();
			if (DataValidator.isBlankOrNull(productCode)) {
				errors.append("请输入产品型号\n");
				hasError = true;
			}
			// if (DataValidator.isBlankOrNull(shipNbrStr)
			// || !DataValidator.isInt(shipNbrStr)
			// || Integer.valueOf(shipNbrStr).intValue() == 0) {
			// errors.append("出库数量请输入大于0的整数\n");
			// hasError = true;
			// }

			if (!hasError) {
				int shipNbr = Integer.valueOf(shipNbrStr);
				StoreManage storeManage = new StoreManage();
				StoreProcurement storeProcurement = new StoreProcurement();

				storeManage.setProductCode(productCode);
				storeProcurement.setProductCode(productCode);
				if (!DataValidator.isBlankOrNull(productBrand)) {
					storeManage.setProductBrand(productBrand);
					storeProcurement.setProductBrand(productBrand);
				}

				Vector<StoreManage> storeManages = storeManageService
						.retrieveAllStoreManageByProperties(storeManage);

				Vector<StoreProcurement> storeProcurements = storeProcurementService
						.retrieveStoreProcurements(storeProcurement);
				if (storeManages.size() == 0) {
					showWarningMsg("无库存");
					cleanQueryResult();
				} else {

					// 自动计算库存，并显示
					storeProcurementTableModel.updateData(calculateStorage(storeManages,
							storeProcurements, shipNbr));
					// 若当前产品型号对应的库存不足，则提示
					if (!isStoreEnough) {
						showWarningMsg("库存不足");
					}
				}
			} else {
				showErrorMsg(errors.toString());
			}
		}

	}

	/**
	 * 根据入库编号得到本销售单已经分配的库存数量
	 * 
	 * @param storeProcurementId
	 * @return
	 */
	private int getAssignedQty(String storeProcurementId) {
		// 取得销售单中已经分配的库存
		Vector<StoreShipment> assignedQty = StoreShipmentFrame
				.getStoreTableModal();
		int assignedNbr = 0;
		for (int i = 0; i < assignedQty.size(); i++) {
			StoreShipment tmp = assignedQty.get(i);
			if (tmp.getStoreProcurementId().equals(storeProcurementId)) {
				assignedNbr += tmp.getShipNumber();
			}
		}

		return assignedNbr;
	}

	private StoreProcurement getStoreProcurementById(
			Vector<StoreProcurement> storeProcurements, String id) {
		for (int i = 0; i < storeProcurements.size(); i++) {
			StoreProcurement tmp = storeProcurements.get(i);
			if (tmp.getId().equals(id)) {
				return tmp;
			}
		}
		return null;
	}

	/**
	 * 根据总出库量，自动计算需要出库的产品
	 * 
	 * @param storeManages
	 *            根据产品属性查询的库存结果
	 * @param shipNbr
	 *            针对本产品需要出库的总量
	 * @return
	 */
	private Vector<StoreProcurement> calculateStorage(
			Vector<StoreManage> storeManages,
			Vector<StoreProcurement> storeProcurements, int shipNbr) {
		// 计算库存
		Vector<StoreProcurement> calculateResults = new Vector<StoreProcurement>();

		// 库存是否充足标记
		isStoreEnough = false;

		boolean shipEnd = false;
		for (int i = 0; i < storeManages.size(); i++) {
			StoreManage tmp = storeManages.get(i);
			int remainNbr = tmp.getTotalNbr();// 剩余库存

			// 去除本出库单已经分配的库存数量
			remainNbr -= getAssignedQty(tmp.getStoreProcurementId());

			// 已经无库存，则忽略，继续检查下一条
			if (remainNbr == 0) {
				continue;
			}

			int shipNbrRow; // 本次出库量
			if (shipEnd) {
				shipNbrRow = 0;
			} else {
				if (shipNbr > remainNbr) {
					shipNbrRow = remainNbr; // 将剩余库存全部出库
					shipNbr -= shipNbrRow;// 总出库量-本条出库量
				} else {
					shipNbrRow = shipNbr; // 仅出库本次要求的数量
					// 到此，出库完毕，剩余产品不展示
					shipEnd = true;
					isStoreEnough = true;
				}
			}

			// 显示库存分配状况
			StoreProcurement newStoreProcurement = getStoreProcurementById(
					storeProcurements, tmp.getStoreProcurementId());
			if (newStoreProcurement != null) {
				newStoreProcurement.setShipNbr(shipNbrRow); // 本次出库量
				newStoreProcurement.setTotalNbr(remainNbr); // 剩余量(未扣除本次出库量)
				calculateResults.add(newStoreProcurement);
				if (shipNbrRow > 0) {
					newStoreProcurement.setSelected(true);
				}
			}
		}

		return calculateResults;
	}

	/**
	 * 清空查询结果及条件
	 */
	private void cleanQueryResult() {
		// productCodeField.setText("");
		shipNbrField.setText("0");
		storeProcurementTableModel.resetTable();
	}

	class AddToStoreButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int rowCount = table.getRowCount();
			List<StoreProcurement> storeProcurementList = new ArrayList<StoreProcurement>();
			StringBuffer errors = new StringBuffer();
			for (int i = 0; i < rowCount; i++) {
				StoreProcurement temp = storeProcurementTableModel.getRowStoreProcurement(table.convertRowIndexToModel(i)).clone();
				if (!temp.getSelected()) {
					//仅验证选中条目
					continue;
				}
				
				if (temp.getShipNbr() > temp.getTotalNbr()) {
					int index = i + 1;
					errors.append("第 " + index + " 行出库量不能大于库存量\n");
				}
				if (temp.getShipNbr() > 0) {
					storeProcurementList.add(temp.clone());
				}
			}

			if (errors.toString().length() > 0) {
				showErrorMsg(errors.toString());
				return;
			}

			if (storeProcurementList.size() > 0) {
				StoreShipmentFrame.updateStoreTableModal(storeProcurementList);

				// 添加完成后清空上次查询条件及结果
				cleanQueryResult();
			} else {
				showWarningMsg("您没有选择任何产品或本次出库量为0");
			}
		}
	}

	class StoreProcurementTableModel extends AbstractTableModel {
		/**
 * 
 */
		private static final long serialVersionUID = -8051613280247805669L;

		Vector<StoreProcurement> storeProcurementVector = new Vector<StoreProcurement>();

		String[][] columnNames;

		public StoreProcurementTableModel() {
			StoreProcurement tmp = new StoreProcurement();
			columnNames = tmp.getColumnsForShipment();
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return storeProcurementVector.size();
		}
		
		public StoreProcurement getRowStoreProcurement(int row){
			return storeProcurementVector.get(row);
		}

		public Vector<StoreProcurement> getRows() {
			return storeProcurementVector;
		}

		public String getColumnName(int col) {
			return columnNames[col][0];
		}

		public Object getValueAt(int row, int col) {
			StoreProcurement storeProcurement = storeProcurementVector.get(row);
			Object value = storeProcurement.getValueAt(columnNames[col][1]);
			if (String.valueOf(value).equalsIgnoreCase("false")
					|| String.valueOf(value).equalsIgnoreCase("true")) {
				return Boolean.parseBoolean(String.valueOf(value));
			}
			return value;
		}

		/*
		 * Don't need to implement this method unless your table's data can
		 * change.
		 */
		public void setValueAt(Object value, int row, int col) {
			StoreProcurement storeProcurement = storeProcurementVector.get(row);
			storeProcurement.setValueAt(value,
					columnNames[col][1]);
			fireTableCellUpdated(row, col);
		}

		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			if (col >= columnNames.length - 3 && col != columnNames.length - 2) {
				return true;
			} else {
				return false;
			}
		}

		public void resetTable() {
			getRows().removeAllElements();
			fireTableDataChanged();
		}

		// 更新数据
		public void updateData(Vector<StoreProcurement> storeProcurementVector) {
			this.storeProcurementVector = storeProcurementVector;
			if (storeProcurementVector.size() == 0) {
				this.storeProcurementVector = new Vector<StoreProcurement>();
			}

			fireTableDataChanged();
		}
	}
}
