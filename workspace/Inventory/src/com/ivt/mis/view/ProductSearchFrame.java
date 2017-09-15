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

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.model.Product;
import com.ivt.mis.service.ProductService;

public class ProductSearchFrame extends BaseJInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5889114756306718910L;

	private PageBar pageBar = new PageBar();
	private JButton searchBtn;
	
	private ProductSearchFrame productSearchFrame;
	
	public ProductSearchFrame() {
		super("产品查询", true, true, true, true);
		super.setFrameId(Constants.MODULE_PRODUCT_SEARCH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, screenSize.width * 2 / 3 - 10,
				screenSize.height * 2 / 3 - 55);
		this.setContentPane(new ProductInforSearchPanel());
		productSearchFrame = this;
		
		//进入时显示所有产品
		searchBtn.doClick();
	}

	public void callBackRefresh(boolean resetPage) {
		if (resetPage) {
			pageBar.reset();
		}
		searchBtn.doClick();
	}
	
	class ProductInforSearchPanel extends JPanel {
		/**
	 * 
	 */
		private static final long serialVersionUID = 8751685545637285873L;
		JTable table;
		ProductTableModel productTableModel;
		JComboBox comboBox;
		JTextField textField;

		public ProductInforSearchPanel() {
			super(new BorderLayout());
			
			productTableModel = new ProductTableModel();
			table = new JTable(productTableModel);
			
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
			add(tickColumns(productTableModel.columnNamesAll,
					productTableModel, table), BorderLayout.LINE_END);

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
			comboBox.addItem("产品编码");
			comboBox.addItem("产品型号");
			comboBox.addItem("品牌");
			comboBox.addItem("生产商");
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
			addBtn.addActionListener(new ProductAddListener());
			panel.add(addBtn);

			final JButton updateBtn = new JButton("修改"); // 修改按钮
			updateBtn.addActionListener(new ProductUpdateListener());
			panel.add(updateBtn);

			final JButton deleteBtn = new JButton("删除"); // 删除按钮
			deleteBtn.addActionListener(new ProductDeleteListener());
			panel.add(deleteBtn);
			
			JButton exportBtn = new JButton();
			exportBtn.setText("导出");
			exportBtn.addActionListener(new ExportAllButtonListener(table,"产品信息"));
			JButton printBtn = new JButton();
			printBtn.setText("打印");
			printBtn.addActionListener(new PrintAllButtonListener(table));
			
			JButton importBtn = new JButton();
			importBtn.setText(getText("form.btn.import"));
			importBtn.addActionListener(new ImportButtonListener(
					new Product(), new Vector<Object>()));
			
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
				Product params = new Product();
				params.setPage(pageBar.getPage());
				
				if (field.equals("产品型号")) {
					params.setProductCode(value);
				} else if (field.equals("产品编码")) {
					params.setId(value);
				} else if (field.equals("品牌")) {
					params.setBrand(value);
				} else if (field.equals("生产商")) {
					params.setManufacturer(value);
				}
				ProductService productService = CommonFactory
						.getProductService();
				Vector<Product> productVector = productService
						.retrieveProducts(params);
				if (productVector.size() == 0) {
					showWarningMsg("没有满足你条件的产品");
				}
				productTableModel.updateData(productVector);
				// 更新分页按扭事件信息及显示内容
				pageBar.updateBtnEvent(searchBtn);
				// 自动计算宽度
				fitTableColumns(table);

			}

			public SearchButtonListener(JButton button){
				searchBtn = button;
			}
		}

		class ProductTableModel extends AbstractTableModel {
			/**
		 * 
		 */
			private static final long serialVersionUID = -8051613280247805669L;

			Vector<Product> productVector = new Vector<Product>();

//			private String[] columnNames = { "产品编号", "产品型号", "产品名称", "品牌",
//					"规格", "类别", "批准文号","生产商" };
//			private String[] columnProName = {"id","productCode","productName","brand","size","category","promitCode","manufacturer"};

			String[][] columnNames;

			String[][] columnNamesAll;

			public ProductTableModel() {
				Product tmp = new Product();
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
				return productVector.size();
			}

			public String getColumnName(int col) {
				return columnNames[col][0];
			}

			public Object getValueAt(int row, int col) {
				Product product = null;
				if (productVector.size() == 0){
					product = new Product();
				}else{
					product = productVector.get(row);
				}
				
				return product.getValueAt(columnNames[col][1]);
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return false;

			}

			// 更新数据
			public void updateData(Vector<Product> productVector) {
				this.productVector = productVector;
				if (productVector.size() == 0) {
					this.productVector = new Vector<Product>();
				} else {
					fireTableRowsInserted(0, productVector.size() - 1);
				}
			}
		}
		
		class ProductAddListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_PRODUCT, false)) {
					ProductFrame frame = new ProductFrame(null,
							Constants.FORM_TYPE_ADD, productSearchFrame);
					MainFrame.openNewFrame(frame, false);
				}
			}
		}

		class ProductUpdateListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();// 获得选中行的索引
				if (selectedRow != -1) // 是否存在选中行
				{
					int selectedRowModel = table.convertRowIndexToModel(selectedRow);
					// 修改指定的值：
					String productID = productTableModel.getValueAt(selectedRowModel, 0)
							.toString();
					ProductService productService = CommonFactory
							.getProductService();
					Product product = productService.getProductInfo(productID);
					

					if (!MainFrame.isInternalFrameOpened(Constants.MODULE_PRODUCT, false)) {
						ProductFrame frame = new ProductFrame(product,
								Constants.FORM_TYPE_UPDATE,productSearchFrame);
						MainFrame.openNewFrame(frame, false);
						productTableModel.fireTableDataChanged();
					}
				} else {
					showWarningMsg("请先选择要修改的行");
				}
			}
		}

		class ProductDeleteListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();// 获得选中行的索引
				if (selectedRow != -1) // 是否存在选中行
				{
					int selectedRowModel = table.convertRowIndexToModel(selectedRow);
					// 修改指定的值：
					String productID = productTableModel.getValueAt(selectedRowModel, 0)
							.toString();
					ProductService productService = CommonFactory
							.getProductService();
					Product product = productService.getProductInfo(productID);

					if (!MainFrame.isInternalFrameOpened(Constants.MODULE_PRODUCT, false)) {
						ProductFrame frame = new ProductFrame(product,
								Constants.FORM_TYPE_DELETE,productSearchFrame);

						MainFrame.openNewFrame(frame, false);
					}
				} else {
					showWarningMsg("请先选择要删除的行");
				}
			}
		}
	}

}
