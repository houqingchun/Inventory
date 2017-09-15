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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.swing.SpringUtilities;
import com.ivt.mis.model.Product;
import com.ivt.mis.model.StoreShipment;
import com.ivt.mis.service.ProductService;

public class ProductSelectionJDialog extends BaseJDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5223649924792699112L;

	private JTable table;

	private ProductTableModel productTableModel ;
	
	private ProductSelectionPanel productSelectionPanel;

	public ProductSelectionJDialog(Component relativeComponent) {
		super(new JFrame(),"请选择产品", true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		

		Image image = null;
		try {
			image = ImageIO
					.read(this.getClass().getResource(Constants.TASK_ICON_PATH));
		} catch (IOException e1) {
			e1.printStackTrace();
		} // 创建图片对象
		this.setIconImage(image);// 设置图标
		
		this.setSize(600, 200);
		
		setLocationRelativeTo(null);

		productSelectionPanel = new ProductSelectionPanel();
		productSelectionPanel.setOpaque(true); // content panes must be opaque

		setContentPane(productSelectionPanel);

		// Display the window.
		pack();
		setVisible(true);
	}

	class ProductSelectionPanel extends JPanel {
		/**
	 * 
	 */
		private static final long serialVersionUID = 2044933397778396918L;
		private JTextField filterText;
		private JCheckBox selectAllBox;
		private TableRowSorter<ProductTableModel> sorter;

		public ProductSelectionPanel() {
			super();
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			// Create a table with a sorter.
			productTableModel = new ProductTableModel();
			sorter = new TableRowSorter<ProductTableModel>(productTableModel);
			table = new JTable(productTableModel);
			table.setRowHeight(Constants.TABLE_DEFAULT_HEIGHT);
			table.setRowSorter(sorter);
			table.setPreferredScrollableViewportSize(new Dimension(600, 200));
			table.setFillsViewportHeight(true);

			ProductService productService = CommonFactory.getProductService();
			Vector<Product> productVector = productService.retrieveAllProducts();
			productTableModel.updateData(productVector);

			table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			// When selection changes, provide user with row numbers for
			// both view and model.
			table.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent event) {
							int rowCount = table.getRowCount();
							StringBuffer buf = new StringBuffer();
							for (int i = 0; i < rowCount; i++) {
								Boolean viewRow = Boolean.parseBoolean(String.valueOf(table.getValueAt(i,
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
			JLabel l1 = new JLabel("快速查找（编号，型号，名称）:", SwingConstants.TRAILING);
			form.add(l1);
			filterText = new JTextField();
			// Whenever filterText changes, invoke newFilter.
			filterText.getDocument().addDocumentListener(
					new DocumentListener() {
						public void changedUpdate(DocumentEvent e) {
							newFilter();
						}

						public void insertUpdate(DocumentEvent e) {
							newFilter();
						}

						public void removeUpdate(DocumentEvent e) {
							newFilter();
						}
					});
			l1.setLabelFor(filterText);
			form.add(filterText);

			SpringUtilities.makeCompactGrid(form, 1, 2, 6, 6, 6, 6);
			add(form);

			JPanel buttonPanel = new JPanel(new SpringLayout());
			buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS)); 
			JButton addToStoreBtn = new JButton("确认");
			addToStoreBtn.addActionListener(new AddToStoreButtonListener());
			buttonPanel.add(addToStoreBtn);
			buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
			JButton cancelBtn = new JButton("取消");
			cancelBtn.addActionListener(new CancelButtonListener());
			buttonPanel.add(cancelBtn);
			buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
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
			RowFilter<ProductTableModel, Object> rf = null;
			// If current expression doesn't parse, don't update.
			try {
				rf = RowFilter.regexFilter(filterText.getText(), 0, 1, 2);
			} catch (java.util.regex.PatternSyntaxException e) {
				return;
			}
			sorter.setRowFilter(rf);
		}
		
		class SelectAllBoxListener implements ItemListener{

			@Override
			public void itemStateChanged(ItemEvent e) {
				int rowCount = table.getRowCount();
				if (selectAllBox.isSelected()){
					for (int i = 0; i < rowCount; i++) {
						table.setValueAt(true, i, table.getColumnCount() - 1);
					}
				}else{
					for (int i = 0; i < rowCount; i++) {
						table.setValueAt(false, i, table.getColumnCount() - 1);
					}
				}
			}
		}
	}
	
	class AddToStoreButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int rowCount = table.getRowCount();
			List<Product> productList = new ArrayList<Product>();
			for (int i = 0; i < rowCount; i++) {
				Boolean viewRow = Boolean.parseBoolean(String.valueOf(table.getValueAt(i,
						table.getColumnCount() - 1)));
				if (viewRow.booleanValue()) {
					productList.add(productTableModel.getRowProduct(table.convertRowIndexToModel(i)).clone());
				}
			}
			if (productList.size() > 0) {
				StoreProcurementFrame.updateStoreTableModal(productList);
				dispose();
			}else{
				showWarningMsg("您没有选择任何产品");
			}
		}
	}

	class ProductTableModel extends AbstractTableModel {
		/**
 * 
 */
		private static final long serialVersionUID = -8051613280247805669L;

		Vector<Product> productVector = new Vector<Product>();
		String[][] columnNames;

		public ProductTableModel() {
			Product tmp = new Product();
			columnNames = tmp.getColumnsForProcurement();
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return productVector.size();
		}
		
		public Product getRowProduct(int row){
			return productVector.get(row);
		}

		public String getColumnName(int col) {
			return columnNames[col][0];
		}

		public Object getValueAt(int row, int col) {
			Product product = productVector.get(row);
			Object value = product.getValueAt(columnNames[col][1]);
			if (String.valueOf(value).equalsIgnoreCase("false") || String.valueOf(value).equalsIgnoreCase("true")){
				return Boolean.parseBoolean(String.valueOf(value));
			}
			return value;
		}
		
		@SuppressWarnings("unchecked")
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			if (col == columnNames.length - 1) {
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
			Product product = productVector.get(row);
			product.setValueAt(value, columnNames[col][1]);
			fireTableCellUpdated(row, col);
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
}
