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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.model.Customer;
import com.ivt.mis.model.Provider;
import com.ivt.mis.service.ProviderService;
import com.ivt.mis.view.BaseJInternalFrame.ImportButtonListener;

public class ProviderSearchFrame extends BaseJInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4021730167596619673L;
	public static final Logger logger = Logger
			.getLogger(ProviderSearchFrame.class);

	private PageBar pageBar = new PageBar();
	private JButton searchBtn;
	private ProviderSearchFrame providerSearchFrame;

	public ProviderSearchFrame() {
		super("供应商查询", true, true, true, true);
		super.setFrameId(Constants.MODULE_PROVIDER_SEARCH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, screenSize.width * 2 / 3 - 10,
				screenSize.height * 2 / 3 - 55);
		this.setContentPane(new PrivoderInforSearchPanel());
		providerSearchFrame = this;

		//进入时显示所有产品
		searchBtn.doClick();
	}

	public void callBackRefresh(boolean resetPage) {
		if (resetPage) {
			pageBar.reset();
		}
		searchBtn.doClick();
	}
	
	class PrivoderInforSearchPanel extends JPanel {
		/**
	 * 
	 */
		private static final long serialVersionUID = 3657583466293732737L;
		JTable table;
		ProviderTableModel providerTableModel;
		JComboBox comboBox;
		JTextField textField;

		public PrivoderInforSearchPanel() {
			super(new BorderLayout());
			providerTableModel = new ProviderTableModel();
			table = new JTable(providerTableModel);
			// 自动计算宽度
			fitTableColumns(table);
			
			JPanel pane = search();
			this.add(pane, BorderLayout.NORTH);

//			table.setPreferredScrollableViewportSize(new Dimension(500, 70));
			table.setFillsViewportHeight(true);
			table.setAutoCreateRowSorter(true);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			// Create the scroll pane and add the table to it.
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.getViewport().setBackground(Color.WHITE);

			// Add the scroll pane to this panel.
			add(scrollPane, BorderLayout.CENTER);
			add(pageBar.createPageButtons(), BorderLayout.SOUTH);
			add(tickColumns(providerTableModel.columnNamesAll,
					providerTableModel, table), BorderLayout.LINE_END);

			// 自动计算宽度
			fitTableColumns(table);
		}

		public JPanel search() {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout());
			JLabel lable = new JLabel("请选择查询条件：");
			panel.add(lable);

			comboBox = new JComboBox();
			comboBox.addItem("   ----   ");
			comboBox.addItem("供应商编号");
			comboBox.addItem("供应商全称");
			comboBox.addItem("联系人");
			comboBox.setSelectedIndex(0);
			panel.add(comboBox);

			textField = new JTextField();
			textField.setColumns(13);
			panel.add(textField);

			searchBtn = new JButton();
			searchBtn.setText("查询");
			searchBtn.addActionListener(new SearchButtonListener(searchBtn));
			panel.add(searchBtn);

			JButton addBtn = new JButton();
			addBtn.setText("增加");
			addBtn.addActionListener(new ProviderAddListener());
			panel.add(addBtn);

			final JButton updateBtn = new JButton("修改"); // 修改按钮
			updateBtn.addActionListener(new ProviderUpdateListener());
			panel.add(updateBtn);

			final JButton deleteBtn = new JButton("删除"); // 删除按钮
			deleteBtn.addActionListener(new ProviderDeleteListener());
			panel.add(deleteBtn);

			JButton exportBtn = new JButton();
			exportBtn.setText("导出");
			exportBtn.addActionListener(new ExportAllButtonListener(table,"供应商信息"));
			JButton printBtn = new JButton();
			printBtn.setText("打印");
			printBtn.addActionListener(new PrintAllButtonListener(table));

			JButton importBtn = new JButton();
			importBtn.setText(getText("form.btn.import"));
			importBtn.addActionListener(new ImportButtonListener(
					new Provider(), new Vector<Object>()));

			panel.add(exportBtn);
			panel.add(printBtn);
			panel.add(importBtn);
			return panel;
		}

		class SearchButtonListener implements ActionListener {
			JButton searchBtn;

			@Override
			public void actionPerformed(ActionEvent e) {
				String field = comboBox.getSelectedItem().toString();
				String value = textField.getText();
				Provider params = new Provider();
				params.setPage(pageBar.getPage());
				if (field.equals("供应商编号")) {
					params.setId(value);
				} else if (field.equals("供应商全称")) {
					params.setName(value);
				} else if (field.equals("联系人")) {
					params.setContacts(value);
				}

				ProviderService providerService = CommonFactory
						.getProviderService();
				Vector<Provider> providerVector = providerService
						.retrieveProviders(params);
				if (providerVector.size() == 0) {
					showWarningMsg("没有满足你条件的供应商");
				}
				providerTableModel.updateData(providerVector);
				// 更新分页按扭事件信息及显示内容
				pageBar.updateBtnEvent(searchBtn);
				// 自动计算宽度
				fitTableColumns(table);
			}

			public SearchButtonListener(JButton button) {
				searchBtn = button;
			}
		}

		class ProviderTableModel extends AbstractTableModel {
			/**
		 * 
		 */
			private static final long serialVersionUID = 3967252820220188154L;

			Vector<Provider> providerVector = new Vector<Provider>();

			// String[] columnNames = { "供应商编号", "供应商全称", "联系地址", "收货地址",
			// "联系人", "电话号码", "注册地", "注册号", "注册类型", "开户银行", "银行账号" };
			//
			// String[] columnNamesPro = { "id", "name", "address",
			// "shipAddress", "contacts", "mobile", "registryPlace",
			// "registryNumber", "registryType", "bank", "account" };

			String[][] columnNames;

			String[][] columnNamesAll;

			public ProviderTableModel() {
				Provider tmp = new Provider();
				columnNamesAll = tmp.getColumns();
				resetColumns(columnNamesAll);
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

			public void fireTableStructureChanged() {
				this.resetColumns(columnNamesAll);
				super.fireTableStructureChanged();
			}

			public int getColumnCount() {
				return columnNames.length;
			}

			public int getRowCount() {
				return providerVector.size();
			}

			public String getColumnName(int col) {
				return columnNames[col][0];
			}

			public Object getValueAt(int row, int col) {
				Provider provide = null;
				if (providerVector.size() == 0){
					provide = new Provider();
				}else{
					provide = providerVector.get(row);
				}
				return provide.getValueAt(columnNames[col][1]);
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return false;

			}

			// 更新数据
			public void updateData(Vector<Provider> providerVector) {
				this.providerVector = providerVector;
				if (providerVector.size() == 0) {
					this.providerVector = new Vector<Provider>();
				} else {
					fireTableRowsInserted(0, providerVector.size() - 1);
				}
			}
		}
		
		class ProviderAddListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_PROVIDER, false)) {
					ProviderFrame frame = new ProviderFrame(null,
							Constants.FORM_TYPE_ADD, providerSearchFrame);

					MainFrame.openNewFrame(frame, false);
				}
			}
		}

		class ProviderUpdateListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();// 获得选中行的索引
				if (selectedRow != -1) // 是否存在选中行
				{
					int selectedRowModel = table.convertRowIndexToModel(selectedRow);
					// 修改指定的值：
					String providerID = providerTableModel.getValueAt(
							selectedRowModel, 0).toString();
					ProviderService providerService = CommonFactory
							.getProviderService();
					Provider provider = providerService
							.getProviderInfo(providerID);

					if (!MainFrame.isInternalFrameOpened(Constants.MODULE_PROVIDER, false)) {
						ProviderFrame frame = new ProviderFrame(provider,
								Constants.FORM_TYPE_UPDATE, providerSearchFrame);

						MainFrame.openNewFrame(frame, false);
					}
				} else {
					showWarningMsg("请先选择要修改的行");
				}
			}
		}

		class ProviderDeleteListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();// 获得选中行的索引
				if (selectedRow != -1) // 是否存在选中行
				{
					int selectedRowModel = table.convertRowIndexToModel(selectedRow);
					// 修改指定的值：
					String providerID = providerTableModel.getValueAt(
							selectedRowModel, 0).toString();
					ProviderService providerService = CommonFactory
							.getProviderService();
					Provider provider = providerService
							.getProviderInfo(providerID);
					
					if (!MainFrame.isInternalFrameOpened(Constants.MODULE_PROVIDER, false)) {
						ProviderFrame frame = new ProviderFrame(provider,
								Constants.FORM_TYPE_DELETE, providerSearchFrame);

						MainFrame.openNewFrame(frame, false);
					}
				} else {
					showWarningMsg("请先选择要删除的行");
				}
			}
		}
	}

}