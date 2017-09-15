package com.ivt.mis.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.BoxLayout;
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
import com.ivt.mis.model.Customer;
import com.ivt.mis.service.CustomerService;

public class CustomerSearchFrame extends BaseJInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5942849872577412198L;

	private PageBar pageBar = new PageBar();

	private JButton searchBtn;

	private CustomerSearchFrame customerSearchFrame;

	public CustomerSearchFrame() {
		super("客户信息查询", true, true, true, true);
		super.setFrameId(Constants.MODULE_CUSTOMER_SEARCH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, screenSize.width * 2 / 3 - 10,
				screenSize.height * 2 / 3 - 55);
		this.setContentPane(new CustomerInforSearchPanel());

		customerSearchFrame = this;

		//进入时显示所有产品
		searchBtn.doClick();
	}

	public void callBackRefresh(boolean resetPage) {
		if (resetPage) {
			pageBar.reset();
		}
		searchBtn.doClick();
	}

	class CustomerInforSearchPanel extends JPanel {
		/**
	 * 
	 */
		private static final long serialVersionUID = 8225266748173917405L;
		JTable table;
		CustomerTableModel customerTableModel;
		JComboBox comboBox;
		JTextField textField;

		public CustomerInforSearchPanel() {
			super(new BorderLayout());

			customerTableModel = new CustomerTableModel();
			table = new JTable(customerTableModel);
			// 自动计算宽度
			fitTableColumns(table);
			
			JPanel pane = search();
			add(pane, BorderLayout.NORTH);

			table.setFillsViewportHeight(true);
			table.setAutoCreateRowSorter(true);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.getViewport().setBackground(Color.WHITE);
			
			add(scrollPane, BorderLayout.CENTER);
			add(pageBar.createPageButtons(), BorderLayout.SOUTH);
			add(tickColumns(customerTableModel.columnNamesAll,
					customerTableModel, table), BorderLayout.LINE_END);
		}

		public JPanel search() {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout());
			JLabel lable = new JLabel("查询条件");
			panel.add(lable);

			comboBox = new JComboBox();
			comboBox.addItem("   ----   ");
			comboBox.addItem("客户编号");
			comboBox.addItem("客户名称");
			comboBox.addItem("联系人");
			comboBox.setSelectedIndex(0);
			panel.add(comboBox);

			textField = new JTextField();
			textField.setColumns(13);
			panel.add(textField);

			searchBtn = new JButton();
			searchBtn.setText(getText("form.btn.search"));
			searchBtn.addActionListener(new SearchButtonListener(searchBtn));

			panel.add(searchBtn);

			JButton addBtn = new JButton();
			addBtn.setText(getText("form.btn.new"));
			addBtn.addActionListener(new CustomerAddListener());
			panel.add(addBtn);

			final JButton updateBtn = new JButton(getText("form.btn.update")); // 修改按钮
			updateBtn.addActionListener(new CustomerUpdateListener());
			panel.add(updateBtn);

			final JButton deleteBtn = new JButton(getText("form.btn.delete")); // 删除按钮
			deleteBtn.addActionListener(new CustomerDeleteListener());
			panel.add(deleteBtn);

			JButton exportBtn = new JButton();
			exportBtn.setText(getText("form.btn.export"));
			exportBtn.addActionListener(new ExportAllButtonListener(table, "客户信息"));

			JButton printBtn = new JButton();
			printBtn.setText(getText("form.btn.print"));
			printBtn.addActionListener(new PrintAllButtonListener(table));

			JButton importBtn = new JButton();
			importBtn.setText(getText("form.btn.import"));
			importBtn.addActionListener(new ImportButtonListener(
					new Customer(), new Vector<Object>()));

			panel.add(exportBtn);
			panel.add(printBtn);
			panel.add(importBtn);
			return panel;
		}

		class SearchButtonListener implements ActionListener {
			JButton searchBtnInternal;

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectIndex = comboBox.getSelectedIndex();
				String value = textField.getText();
				Customer params = new Customer();
				params.setPage(pageBar.getPage());
				if (selectIndex == 1) {
					params.setId(value);
				} else if (selectIndex == 2) {
					params.setName(value);
				} else if (selectIndex == 3) {
					params.setContacts(value);
				}
				CustomerService customerService = CommonFactory
						.getCustomerService();

				Vector<Customer> customerVector = customerService
						.retrieveCustomers(params);
				if (customerVector.size() == 0) {
					showWarningMsg(getText("form.search.no.result"));
				}

				customerTableModel.updateData(customerVector);
				// 更新分页按扭事件信息及显示内容
				pageBar.updateBtnEvent(searchBtnInternal);
				// 自动计算宽度
				fitTableColumns(table);
			}

			public SearchButtonListener(JButton button) {
				searchBtnInternal = button;
			}
		}

		class CustomerTableModel extends AbstractTableModel {
			/**
		 * 
		 */
			private static final long serialVersionUID = -7441951137082514125L;

			Vector<Customer> customerVector = new Vector<Customer>();

			String[][] columnNames;

			String[][] columnNamesAll;

			public CustomerTableModel() {
				Customer tmp = new Customer();
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
				return customerVector.size();
			}

			public String getColumnName(int col) {
				return columnNames[col][0];
			}

			public Object getValueAt(int row, int col) {
				Customer customer = null;
				if (customerVector.size() == 0) {
					customer = new Customer();
				} else {
					customer = customerVector.get(row);
				}

				return customer.getValueAt(columnNames[col][1]);
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return false;
			}

			// 更新数据
			public void updateData(Vector<Customer> customerVector) {
				this.customerVector = customerVector;
				if (customerVector.size() == 0) {
					this.customerVector = new Vector<Customer>();
				} else {
					fireTableRowsInserted(0, customerVector.size() - 1);
				}
			}
		}

		class CustomerAddListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_CUSTOMER, false)) {
					CustomerFrame frame = new CustomerFrame(null,
							Constants.FORM_TYPE_ADD, customerSearchFrame);
					MainFrame.openNewFrame(frame, false);
				}
			}
		}

		class CustomerUpdateListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();// 获得选中行的索引
				if (selectedRow != -1) // 是否存在选中行
				{
					int selectedRowModel = table.convertRowIndexToModel(selectedRow);
					// 修改指定的值：
					String customerID = customerTableModel.getValueAt(
							selectedRowModel, 0).toString();
					CustomerService customerService = CommonFactory
							.getCustomerService();
					Customer customer = customerService
							.getCustomerInfo(customerID);
					
					if (!MainFrame.isInternalFrameOpened(Constants.MODULE_CUSTOMER, false)) {
						CustomerFrame frame = new CustomerFrame(customer,
								Constants.FORM_TYPE_UPDATE, customerSearchFrame);
						MainFrame.openNewFrame(frame, false);
					}

				} else {
					showWarningMsg(getText("form.select.required"));
				}
			}
		}

		class CustomerDeleteListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();// 获得选中行的索引
				if (selectedRow != -1) // 是否存在选中行
				{
					int selectedRowModel = table.convertRowIndexToModel(selectedRow);
					// 修改指定的值：
					String customerID = customerTableModel.getValueAt(
							selectedRowModel, 0).toString();
					CustomerService customerService = CommonFactory
							.getCustomerService();
					Customer customer = customerService
							.getCustomerInfo(customerID);
					
					if (!MainFrame.isInternalFrameOpened(Constants.MODULE_CUSTOMER, false)) {
						CustomerFrame frame = new CustomerFrame(customer,
								Constants.FORM_TYPE_DELETE, customerSearchFrame);
						MainFrame.openNewFrame(frame, false);
					}
				} else {
					showWarningMsg(getText("form.select.required"));
				}
			}
		}
	}
}
