package com.ivt.mis.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.ExcelUtil;
import com.ivt.mis.common.ValidationManager;
import com.ivt.mis.model.StoreProcurement;
import com.ivt.mis.model.StoreShipment;
import com.ivt.mis.service.StoreProcurementService;
import com.ivt.mis.service.StoreShipmentService;
import com.ivt.mis.view.BaseJInternalFrame.ExportAllButtonListener;
import com.ivt.mis.view.BaseJInternalFrame.PageBar;
import com.ivt.mis.view.BaseJInternalFrame.PrintAllButtonListener;
import com.ivt.mis.view.BaseJInternalFrame.TableNumberCellRenderer;
import com.ivt.mis.view.StoreProcurementSearchFrame.InputInforSearchPanel.UpdateButtonListener;

public class StoreShipmentSearchFrame extends BaseJInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2051772874203415375L;

	private PageBar pageBar = new PageBar();
	private SaleInforSearchPanel saleInforSearchPanel;
	
	private JButton searchBtn;
	private StoreShipmentSearchFrame storeShipmentSearchFrame;
	
	public StoreShipmentSearchFrame() {
		super("销售查询", true, true, true, true);
		super.setFrameId(Constants.MODULE_SHIPMENT_SEARCH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, screenSize.width * 2 / 3 - 10,
				screenSize.height * 2 / 3 - 55);
		
		saleInforSearchPanel = new SaleInforSearchPanel();
		this.setContentPane(saleInforSearchPanel);
		this.storeShipmentSearchFrame = this;
	}
	

	public void callBackRefresh(boolean resetPage) {
		if (resetPage) {
			pageBar.reset();
		}
		searchBtn.doClick();
	}

	class SaleInforSearchPanel extends JPanel {
		/**
	 * 
	 */
		private static final long serialVersionUID = 6621677096630324706L;
		JTable table;
		StoreShipmentTableModel storeShipmentTableModel;
		JComboBox comboBox;
		JTextField textField;
		JCheckBox checkBox;
		JTextField textFieldStartTime;
		JTextField textFieldEndTime;

		public SaleInforSearchPanel() {
			super(new BorderLayout());

			storeShipmentTableModel = new StoreShipmentTableModel();
			table = new JTable(storeShipmentTableModel);
			
			resetTableCellRender();
			
			JPanel pane = search();
			this.add(pane, BorderLayout.NORTH);
			table.setFillsViewportHeight(true);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			

			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.getViewport().setBackground(Color.WHITE);
			add(scrollPane, BorderLayout.CENTER);

			add(pageBar.createPageButtons(), BorderLayout.SOUTH);


			add(tickColumns(storeShipmentTableModel.columnNamesAll, storeShipmentTableModel, table), BorderLayout.LINE_END);
		}
		

		public void resetTableCellRender(){
			int shipNumberIndex = storeShipmentTableModel.getFieldColumnIndex("shipNumber");
			if (shipNumberIndex != -1){
				table.getColumnModel().getColumn(shipNumberIndex).setCellRenderer(new TableNumberCellRenderer(storeShipmentTableModel, Constants.NUMBER_TYPE_INT));
			}
			int unitPrice = storeShipmentTableModel.getFieldColumnIndex("unitPrice");
			if (unitPrice != -1){
				table.getColumnModel().getColumn(unitPrice).setCellRenderer(new TableNumberCellRenderer(storeShipmentTableModel, Constants.NUMBER_TYPE_DOUBLE));
			}
		}


		public JPanel search() {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			JPanel panel2 = new JPanel(new FlowLayout());
			JPanel panel3 = new JPanel(new FlowLayout());

			JLabel lable = new JLabel("请选择查询条件：");
			panel2.add(lable);

			comboBox = new JComboBox();
			comboBox.addItem("   ----   ");
			comboBox.addItem("销售单号");
			comboBox.addItem("操作员");
			comboBox.addItem("客户编号");
			comboBox.addItem("产品型号");
			comboBox.setSelectedIndex(0);
			panel2.add(comboBox);

			textField = new JTextField();
			textField.setColumns(13);
			panel2.add(textField);

			searchBtn = new JButton();
			searchBtn.setText("查询");
			searchBtn.addActionListener(new SearchButtonListener(searchBtn));
			panel2.add(searchBtn);

			checkBox = new JCheckBox("按指定日期查询");

			checkBox.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (!checkBox.isSelected()) {
						textFieldStartTime.setText("");
						textFieldEndTime.setText("");
						textFieldStartTime.setEnabled(false);
						textFieldEndTime.setEnabled(false);
					} else {
						textFieldStartTime.setEnabled(true);
						textFieldEndTime.setEnabled(true);
					}
				}
			});

			panel3.add(checkBox);

			JLabel lable1 = new JLabel("从");
			panel3.add(lable1);
			textFieldStartTime = new JTextField();
			textFieldStartTime.setColumns(13);
			textFieldStartTime.setEnabled(false);
			panel3.add(textFieldStartTime);
			JLabel lable2 = new JLabel("到");
			panel3.add(lable2);
			textFieldEndTime = new JTextField();
			textFieldEndTime.setColumns(13);
			textFieldEndTime.setEnabled(false);
			panel3.add(textFieldEndTime);
			// JButton searchAll = new JButton();
			// searchAll.setText("显示全部信息");
			JButton updateBtn = new JButton();
			updateBtn.setText("修改");
			updateBtn.addActionListener(new UpdateButtonListener());
			JButton exportBtn = new JButton();
			exportBtn.setText("导出");
			exportBtn.addActionListener(new ExportAllButtonListener(table,"出库信息"));
			JButton printBtn = new JButton();
			printBtn.setText("打印");
			printBtn.addActionListener(new PrintAllButtonListener(table));
			// panel2.add(searchAll);
			panel2.add(updateBtn);
			panel2.add(exportBtn);
			panel2.add(printBtn);
			// searchAll.addActionListener(new SearchAllButtonListener());
			panel.add(panel2);
			panel.add(panel3);
			return panel;
		}

		class UpdateButtonListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Constants.POWER_ADMIN.equals(MainFrame.power)) {
					showInfoMsg("此操作需要管理员权限");
					return;
				}
				int selectedRow = table.getSelectedRow();// 获得选中行的索引
				if (selectedRow != -1) // 是否存在选中行
				{
					int selectedRowModal = table
							.convertRowIndexToModel(selectedRow);
					// 修改指定的值：
					String groupId = storeShipmentTableModel.getValueAt(
							selectedRowModal, 0).toString();
					StoreShipmentService storeShipmentService = CommonFactory
							.getStoreShipmentService();
					StoreShipment params = new StoreShipment();
					params.setGroupId(groupId);
					Vector<StoreShipment> storeShipments = storeShipmentService
							.retrieveStoreShipments(params);

					if (!MainFrame.isInternalFrameOpened(Constants.MODULE_SHIPMENT, true)) {
						StoreShipmentFrame frame = new StoreShipmentFrame(
								storeShipments, Constants.FORM_TYPE_UPDATE, storeShipmentSearchFrame);
						MainFrame.openNewFrame(frame, true);
					}
				} else {
					showWarningMsg("请先选择要修改的行");
				}
			}
		}

		class SearchAllButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				StoreShipmentService storeShipmentService = CommonFactory
						.getStoreShipmentService();
				Vector<StoreShipment> storeShipmentVector = storeShipmentService
						.retrieveAllStoreShipments();
				if (storeShipmentVector.size() == 0) {
					showWarningMsg("当前没有任何销售记录");
				}
				storeShipmentTableModel.updateData(storeShipmentVector);
			}
		}

		class SearchButtonListener implements ActionListener {

			JButton searchBtn;

			public SearchButtonListener(JButton button) {
				searchBtn = button;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				String value = textField.getText();
				if (checkBox.isSelected()) {
					String beginTime = textFieldStartTime.getText();
					String endTime = textFieldEndTime.getText();
					if (beginTime == null || beginTime.trim().length() == 0) {
						showWarningMsg("请输入开始时间");
						return;
					}
					if (endTime == null || endTime.trim().length() == 0) {
						showWarningMsg("请输入结束时间");
					}
					if (!ValidationManager.validateDate(beginTime)) {
						showWarningMsg("时间格式不正确!正确格式: yyyy-mm-dd");
						return;
					}
					if (!ValidationManager.validateDate(endTime)) {
						showWarningMsg("时间格式不正确!正确格式: yyyy-mm-dd");
						return;
					}
				}

				StoreShipment storeShipment = new StoreShipment();

				int selectedIdx = comboBox.getSelectedIndex();

				storeShipment.setStartTime("");
				storeShipment.setEndTime("");
				if (selectedIdx == 1) {
					storeShipment.setSellCode(value);
				} else if (selectedIdx == 2) {
					storeShipment.setUserId(value);
				} else if (selectedIdx == 3) {
					storeShipment.setCustomerId(value);
				} else if (selectedIdx == 4) {
					storeShipment.setProductCode(value);
				}
				if (checkBox.isSelected()) {
					storeShipment.setStartTime(textFieldStartTime.getText());
					storeShipment.setEndTime(textFieldEndTime.getText());
				}

				storeShipment.setPage(pageBar.getPage());

				StoreShipmentService storeShipmentService = CommonFactory
						.getStoreShipmentService();
				Vector<StoreShipment> storeShipmentVector = storeShipmentService
						.retrieveStoreShipments(storeShipment);
				if (storeShipmentVector.size() == 0) {
					showWarningMsg("没有满足你条件的销售单");
				}else{
					table.setAutoCreateRowSorter(true);
				}

				storeShipmentTableModel.updateData(storeShipmentVector);
				// 更新分页按扭事件信息及显示内容
				pageBar.updateBtnEvent(searchBtn);
				// 自动计算宽度
				fitTableColumns(table);
			}
		}

		class StoreShipmentTableModel extends AbstractTableModel {
			/**
		 * 
		 */
			private static final long serialVersionUID = -4987078212237612407L;

			Vector<StoreShipment> storeShipmentVector = new Vector<StoreShipment>();
			String[][] columnNames;
			
			String[][] columnNamesAll;
			
			public StoreShipmentTableModel(){
				StoreShipment tmp = new StoreShipment();
				columnNamesAll = tmp.getColumns();
				resetColumns(columnNamesAll);
			}
			
			public void resetColumns(String[][] newColumns){
				ArrayList<String[]> list = new ArrayList<String[]>();
				for (int i = 0; i < newColumns.length; i++){
					String[] subAry = newColumns[i];
					if ("Y".equalsIgnoreCase(subAry[2])){
						list.add(subAry);
					}
				}
				
				columnNames = new String[list.size()][3];
				for (int i = 0; i < list.size(); i++){
					columnNames[i] = list.get(i);
				}								
			}
			
			public int getFieldColumnIndex(String fieldName){
				for (int i = 0; i < columnNames.length; i++){
					String columnKey  = columnNames[i][1];
					if (fieldName.equalsIgnoreCase(columnKey)){
						return i;
					}
				}
				return -1;
			}
			

			public void fireTableStructureChanged(){
				this.resetColumns(columnNamesAll);
				super.fireTableStructureChanged();
				
				saleInforSearchPanel.resetTableCellRender();
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
				} else {
					fireTableRowsInserted(0, storeShipmentVector.size() - 1);
				}
			}

		}
	}
}