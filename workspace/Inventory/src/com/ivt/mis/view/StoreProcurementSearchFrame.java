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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.ValidationManager;
import com.ivt.mis.model.StoreProcurement;
import com.ivt.mis.service.StoreProcurementService;

public class StoreProcurementSearchFrame extends BaseJInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2876267499885696465L;

	private static StoreProcurementSearchTableModel storeProcurementSearchTableModel;

	private PageBar pageBar = new PageBar();

	private JButton searchBtn;
	
	private InputInforSearchPanel inputInforSearchPanel;
	
	private StoreProcurementSearchFrame storeProcurementSearchFrame;

	public StoreProcurementSearchFrame() {
		super("入库查询", true, true, true, true);
		super.setFrameId(Constants.MODULE_PROCUREMENT_SEARCH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, screenSize.width * 2 / 3 - 10,
				screenSize.height * 2 / 3 - 55);
		inputInforSearchPanel = new InputInforSearchPanel();
		this.setContentPane(inputInforSearchPanel);
		this.storeProcurementSearchFrame = this;
	}
	
	public void callBackRefresh(boolean resetPage) {
		if (resetPage) {
			pageBar.reset();
		}
		searchBtn.doClick();
	}

	class InputInforSearchPanel extends JPanel {
		/**
	 * 
	 */
		private static final long serialVersionUID = 8955660854120397793L;
		JTable table;
		JComboBox comboBox;
		JTextField textField;
		JCheckBox checkBox;
		JTextField textFieldStarttime;
		JTextField textFieldEndtime;

		public InputInforSearchPanel() {
			super(new BorderLayout());
			storeProcurementSearchTableModel = new StoreProcurementSearchTableModel();
			table = new JTable(storeProcurementSearchTableModel);
			
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

			add(tickColumns(storeProcurementSearchTableModel.columnNamesAll, storeProcurementSearchTableModel, table), BorderLayout.LINE_END);
		}
		
		public void resetTableCellRender(){
			int proNumberIndex = storeProcurementSearchTableModel.getFieldColumnIndex("proNumber");
			if (proNumberIndex != -1){
				table.getColumnModel().getColumn(proNumberIndex).setCellRenderer(new TableNumberCellRenderer(storeProcurementSearchTableModel, Constants.NUMBER_TYPE_INT));
			}
			
			int unitPrice = storeProcurementSearchTableModel.getFieldColumnIndex("unitPrice");
			if (unitPrice != -1){
				table.getColumnModel().getColumn(unitPrice).setCellRenderer(new TableNumberCellRenderer(storeProcurementSearchTableModel, Constants.NUMBER_TYPE_DOUBLE));
			}
		}

		public JPanel search() {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			JPanel panel2 = new JPanel(new FlowLayout());
			JPanel panel3 = new JPanel(new FlowLayout());

			JLabel lable = new JLabel("请输入查询条件：");
			panel2.add(lable);

			comboBox = new JComboBox();
			comboBox.addItem("   ----   ");
			comboBox.addItem("产品型号");
			comboBox.addItem("供应商编号");
			comboBox.addItem("预定客户编号");
			comboBox.addItem("品牌");
			comboBox.addItem("业务员");
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
						textFieldStarttime.setText("");
						textFieldEndtime.setText("");
						textFieldStarttime.setEnabled(false);
						textFieldEndtime.setEnabled(false);
					} else {
						textFieldStarttime.setEnabled(true);
						textFieldEndtime.setEnabled(true);
					}
				}
			});
			panel3.add(checkBox);

			JLabel lable1 = new JLabel("从");
			panel3.add(lable1);
			textFieldStarttime = new JTextField();
			textFieldStarttime.setColumns(13);
			textFieldStarttime.setEnabled(false);
			panel3.add(textFieldStarttime);
			JLabel lable2 = new JLabel("到");
			panel3.add(lable2);
			textFieldEndtime = new JTextField();
			textFieldEndtime.setColumns(13);
			textFieldEndtime.setEnabled(false);
			panel3.add(textFieldEndtime);
			// JButton searchAll = new JButton();
			// searchAll.setText("显示全部信息");
			// searchAll.addActionListener(new SearchAllButtonListener());
			JButton updateBtn = new JButton();
			updateBtn.setText("修改");
			updateBtn.addActionListener(new UpdateButtonListener());
			JButton exportBtn = new JButton();
			exportBtn.setText("导出");
			exportBtn.addActionListener(new ExportAllButtonListener(table, "入库信息"));
			JButton printBtn = new JButton();
			printBtn.setText("打印");
			printBtn.addActionListener(new PrintAllButtonListener(table));
			// panel2.add(searchAll);
			panel2.add(updateBtn);
			panel2.add(exportBtn);
			panel2.add(printBtn);

			// panel2.add(new JLabel(ExcelUtil.EXPORT_NOTE));
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
					String groupId = storeProcurementSearchTableModel
							.getValueAt(selectedRowModal, 0).toString();
					StoreProcurementService storeProcurementService = CommonFactory
							.getStoreProcurementService();
					StoreProcurement params = new StoreProcurement();
					params.setGroupId(groupId);
					Vector<StoreProcurement> storeProcurements = storeProcurementService
							.retrieveStoreProcurements(params);
					
					if (!MainFrame.isInternalFrameOpened(Constants.MODULE_PROCUREMENT, true)) {
						StoreProcurementFrame frame = new StoreProcurementFrame(
								storeProcurements, Constants.FORM_TYPE_UPDATE, storeProcurementSearchFrame);
						MainFrame.openNewFrame(frame, true);
					}
				} else {
					showWarningMsg("请先选择要修改的行");
				}
			}
		}

		class SearchAllButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				StoreProcurementService handle = CommonFactory
						.getStoreProcurementService();
				Vector<StoreProcurement> storeProcurementVector = handle
						.retrieveAllStoreProcurements();
				if (storeProcurementVector.size() == 0) {
					showWarningMsg("当前不存在任何入库单");
				} else {
					storeProcurementSearchTableModel
							.updateData(storeProcurementVector);
				}
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
					String beginTime = textFieldStarttime.getText();
					String endTime = textFieldEndtime.getText();
					if (beginTime == null || beginTime.trim().length() == 0) {
						showWarningMsg("请输入开始时间");
						return;
					}
					if (endTime == null || endTime.trim().length() == 0) {
						showWarningMsg("请输入结束时间");
						return;
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
				int selectedIdx = comboBox.getSelectedIndex();
				StoreProcurement storeProcurement = new StoreProcurement();

				storeProcurement.setStartTime("");
				storeProcurement.setEndTime("");
				if (selectedIdx == 1) {
					storeProcurement.setProductCode(value);
				} else if (selectedIdx == 2) {
					storeProcurement.setProviderId(value);
				} else if (selectedIdx == 3) {
					storeProcurement.setCustomerId(value);
				} else if (selectedIdx == 4) {
					storeProcurement.setProductBrand(value);
				} else if (selectedIdx == 5) {
					storeProcurement.setUserId(value);
				}
				if (checkBox.isSelected()) {
					storeProcurement.setStartTime(textFieldStarttime.getText());
					storeProcurement.setEndTime(textFieldEndtime.getText());
				}
				StoreProcurementService handle = CommonFactory
						.getStoreProcurementService();
				storeProcurement.setPage(pageBar.getPage());
				Vector<StoreProcurement> storeProcurementVector = handle
						.retrieveStoreProcurements(storeProcurement);
				if (storeProcurementVector.size() == 0) {
					showWarningMsg("没有满足你条件的入库单");
				}else{
					table.setAutoCreateRowSorter(true);
				}

				storeProcurementSearchTableModel
						.updateData(storeProcurementVector);
				// 更新分页按扭事件信息及显示内容
				pageBar.updateBtnEvent(searchBtn);
				// 自动计算宽度
				fitTableColumns(table);
			}

		}
	}

	class StoreProcurementSearchTableModel extends AbstractTableModel {
		/**
 * 
 */
		private static final long serialVersionUID = -5947294515951962982L;

		public Vector<StoreProcurement> storeProcurementVector = new Vector<StoreProcurement>();

		String[][] columnNames;
		
		String[][] columnNamesAll;
		
		public StoreProcurementSearchTableModel(){
			StoreProcurement tmp = new StoreProcurement();
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
			
			inputInforSearchPanel.resetTableCellRender();
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

		public Object getValueAt(int row, int col) {
			StoreProcurement storeProcurement = null;
			if (storeProcurementVector.size() == 0){
				storeProcurement = new StoreProcurement();
			}else{
				storeProcurement = storeProcurementVector.get(row);
			}
			return storeProcurement.getValueAt(columnNames[col][1]);
		}

		@SuppressWarnings("unchecked")
		public Class getColumnClass(int c) {
			if (storeProcurementVector.size() > 0) {
				return getValueAt(0, c).getClass();
			}
			return null;
		}

		public boolean isCellEditable(int row, int col) {
			return false;
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
