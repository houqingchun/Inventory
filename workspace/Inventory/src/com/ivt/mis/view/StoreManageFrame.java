package com.ivt.mis.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import com.ivt.mis.common.BasicTypeUtils;
import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.model.StoreManage;
import com.ivt.mis.service.StoreManageService;

public class StoreManageFrame extends BaseJInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3186098763947365239L;

	public static final Logger logger = Logger
			.getLogger(BaseJInternalFrame.class);
	
	private PageBar pageBar = new PageBar();

	public StoreManageFrame() {
		super("库存盘点", true, true, true, true);
		super.setFrameId(Constants.MODULE_STORE_MANAGE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, screenSize.width * 2 / 3-10,
				screenSize.height * 2 / 3-55);
		this.getContentPane().add(new StoreHouseInfoPanel());
	}

	class StoreHouseInfoPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5951794082137193061L;
		private Vector<StoreManage> storeManagees = new Vector<StoreManage>();
		JTable table;
		StorehouseTableModel storehouseTableModel;

		public StoreHouseInfoPanel() {
			super(new BorderLayout());
			storehouseTableModel = new StorehouseTableModel();
			table = new JTable(storehouseTableModel);
			table.getColumnModel().getColumn(storehouseTableModel.getColumnIndex("totalNbr")).setCellRenderer(new TableNumberCellRenderer(storehouseTableModel, Constants.NUMBER_TYPE_INT));
			table.setFillsViewportHeight(true);
			table.setAutoCreateRowSorter(true);
			table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			JPanel pane = search();
			add(pane, BorderLayout.NORTH);

			// Create the scroll pane and add the table to it.
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.getViewport().setBackground(Color.WHITE);
			// Add the scroll pane to this panel.
			add(scrollPane, BorderLayout.CENTER);
			
			add(pageBar.createPageButtons()
					, BorderLayout.SOUTH);

			add(tickColumns(storehouseTableModel.columnNamesAll,
					storehouseTableModel, table), BorderLayout.LINE_END);
		}
		
		public JPanel search() {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout());
			JLabel lable = new JLabel("盘点员：");
			panel.add(lable);
			JTextField textField = new JTextField();
			textField.setEditable(false);
			textField.setText(MainFrame.loginname);
			textField.setColumns(13);
			panel.add(textField);
			JLabel lable1 = new JLabel("盘点时间：");
			panel.add(lable1);
			JTextField textField1 = new JTextField();
			textField1.setEditable(false);

			textField1.setText(BasicTypeUtils.getLongFmtDate());
			textField1.setColumns(13);
			panel.add(textField1);

			final JButton button = new JButton();
			button.setText("盘点");
			panel.add(button);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					logger.debug("click Check Inventory");
					StoreManageService storeManageService = CommonFactory
							.getStoreManageService();
					StoreManage params = new StoreManage();
					params.setPage(pageBar.getPage());
					storeManagees = storeManageService
							.retrieveAllStoreManageByProperties(params);
					if (storeManagees.size() == 0) {
						showWarningMsg("没有产品");
					} else {
						storehouseTableModel.addAllStoreManage(storeManagees);
					}

					// 更新分页按扭事件信息及显示内容
					pageBar.updateBtnEvent(button);
					// 自动计算宽度
					fitTableColumns(table);
				}
			});

			JButton exportBtn = new JButton();
			exportBtn.setText("导出");
			exportBtn.addActionListener(new ExportAllButtonListener(table,"库存盘点"));
			JButton printBtn = new JButton();
			printBtn.setText("打印");
			printBtn.addActionListener(new PrintAllButtonListener(table));

			panel.add(exportBtn);
			panel.add(printBtn);

			return panel;
		}

		class StorehouseTableModel extends AbstractTableModel {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6142875767324267595L;
			private Vector<StoreManage> storeManageVector = new Vector<StoreManage>();
			String[][] columnNames;

			String[][] columnNamesAll;

			public StorehouseTableModel() {
				StoreManage tmp = new StoreManage();
				columnNamesAll = tmp.getColumns();
				resetColumns(columnNamesAll);
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
			
			public void resetColumns(String[][] newColumns) {
				ArrayList<String[]> list = new ArrayList<String[]>();
				for (int i = 0; i < newColumns.length; i++) {
					String[] subAry = newColumns[i];
					if ("Y".equalsIgnoreCase(subAry[2])) {
						list.add(subAry);
					}
				}

				columnNames = new String[list.size()][3];
				for (int i = 0; i < list.size(); i++) {
					columnNames[i] = list.get(i);
				}
			}



			public void addAllStoreManage(Vector<StoreManage> storeManagees) {
				this.storeManageVector = storeManagees;
				fireTableRowsInserted(0, storeManagees.size() - 1);
			}

//			private String[] columnNames = { "库存编号", "入库编号", "产品编号", "产品型号",
//					"品牌", "库存量" };
//			private String[] columnProNames = { "id", "storeProcurementId",
//					"productId", "productCode", "productBrand", "totalNbr" };

			public int getColumnCount() {
				return columnNames.length;
			}

			public int getRowCount() {
				return storeManageVector.size();
			}

			public String getColumnName(int col) {
				return columnNames[col][0];
			}

			public void fireTableStructureChanged() {
				this.resetColumns(columnNamesAll);
				super.fireTableStructureChanged();
			}
			
			public Object getValueAt(int row, int col) {
				StoreManage storeManage = null;
				if (storeManageVector.size() == 0){
					storeManage = new StoreManage();
				}else{
					storeManage = storeManageVector.get(row);
				}
				
				return storeManage.getValueAt(columnNames[col][1]);
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return false;
			}
		}
	}
}
