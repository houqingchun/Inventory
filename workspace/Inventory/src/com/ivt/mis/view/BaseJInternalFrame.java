package com.ivt.mis.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PrinterException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observer;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.log4j.Logger;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.ExcelUtil;
import com.ivt.mis.common.I18nMsg;
import com.ivt.mis.model.BasePOJO;
import com.ivt.mis.model.CustomizedUnit;
import com.ivt.mis.model.PageUtil;
import com.qt.datapicker.DatePicker;

public abstract class BaseJInternalFrame extends JInternalFrame {
	public static final Logger logger = Logger
			.getLogger(BaseJInternalFrame.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 779483249596835055L;

	protected String editType;
	protected int rowNbr = 3;
	protected JButton updateBtn;
	protected JButton resetBtn;
	protected JButton saveBtn;
	protected JButton deleteBtn;
	protected JButton cancelBtn;
	private String frameId;

	protected Map columnsMap;

	protected static List<CustomizedUnit> listUnit;
	static {
		listUnit = new ArrayList<CustomizedUnit>();
		listUnit.add(new CustomizedUnit("个"));
		listUnit.add(new CustomizedUnit("支"));
		listUnit.add(new CustomizedUnit("卷"));
		listUnit.add(new CustomizedUnit("捆"));
		listUnit.add(new CustomizedUnit("张"));
		listUnit.add(new CustomizedUnit("袋"));
		listUnit.add(new CustomizedUnit("箱"));
		listUnit.add(new CustomizedUnit("盒"));
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof BaseJInternalFrame)) {
			return false;
		}

		BaseJInternalFrame other = (BaseJInternalFrame) o;
		if (this.frameId != other.frameId) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		return this.frameId.hashCode();
	}

	public static String getText(String key) {
		return I18nMsg.getText(key);
	}

	public static String getText(String key, Object[] parameters) {
		List<Object> params = Arrays.asList(parameters);
		return I18nMsg.getText(key, params);
	}

	protected String decodePath(String path) {
		try {
			return URLDecoder.decode(path, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 展现表单使用
	 * 
	 * @return
	 */
	protected int nextRow() {
		rowNbr = rowNbr + 2;
		return rowNbr;
	}

	protected void resetRow() {
		rowNbr = 3;
	}

	protected static JComboBox getIvtRegistryType() {
		JComboBox cb = new JComboBox();
		cb.addItem("个人独资/合伙制/股份制");
		cb.addItem("上市公司");
		cb.addItem("港澳台企业");
		cb.addItem("欧美/日资企业");
		cb.addItem("其他类型");

		return cb;
	}

	protected static List<CustomizedUnit> getUnitList() {

		return listUnit;
	}
	
	protected static JComboBox getUnits() {
		JComboBox cb = new JComboBox();
		for (int i = 0; i < listUnit.size(); i++){
			cb.addItem(listUnit.get(i).getName());
		}
		
		return cb;
	}


	protected static JComboBox getIvtLevelState() {
		JComboBox cb = new JComboBox();
		cb.addItem("A-非常重要");
		cb.addItem("B-重要");
		cb.addItem("C-普通");
		cb.addItem("D-不重要");

		return cb;
	}

	protected static JComboBox getCurrency() {
		JComboBox cb = new JComboBox();
		cb.addItem("人民币(CNY)");
		cb.addItem("港元(HKD)");
		cb.addItem("美元(USD)");
		cb.addItem("欧元(EUR)");
		cb.addItem("日元(JPY)");

		return cb;
	}

	protected JPanel createCRUDButtons(ActionListener addListener,
			ActionListener updateListener, ActionListener deleteListener) {
		JPanel eventPanel = new JPanel();
		if (Constants.FORM_TYPE_UPDATE.equals(editType)
				&& updateListener != null) {
			updateBtn = new JButton("更新");
			updateBtn.addActionListener(updateListener);

			resetBtn = new JButton("重置");
			resetBtn.addActionListener(new ResetButtonListener());
			eventPanel.add(updateBtn);
			eventPanel.add(resetBtn);
		} else if (Constants.FORM_TYPE_DELETE.equals(editType)
				&& deleteListener != null) {
			deleteBtn = new JButton("删除");
			deleteBtn.addActionListener(deleteListener);
			eventPanel.add(deleteBtn);
		} else if (Constants.FORM_TYPE_ADD.equals(editType)
				&& addListener != null) {
			saveBtn = new JButton("保存");
			saveBtn.addActionListener(addListener);
			eventPanel.add(saveBtn);
		} else if (Constants.FORM_TYPE_ADD_UPDATE.equals(editType)) {
			updateBtn = new JButton("更新");
			updateBtn.addActionListener(updateListener);

			resetBtn = new JButton("重置");
			resetBtn.addActionListener(new ResetButtonListener());
			eventPanel.add(updateBtn);
			eventPanel.add(resetBtn);

			saveBtn = new JButton("保存");
			saveBtn.addActionListener(addListener);
			eventPanel.add(saveBtn);
		}

		cancelBtn = new JButton("取消");
		cancelBtn.addActionListener(new CancelButtonListener());
		eventPanel.add(cancelBtn);

		return eventPanel;
	}

	protected void fitTableColumns(JTable myTable) {
		// JTableHeader header = myTable.getTableHeader();
		// int rowCount = myTable.getRowCount();
		//
		// Enumeration columns = myTable.getColumnModel().getColumns();
		//
		// while(columns.hasMoreElements()){
		// TableColumn column = (TableColumn)columns.nextElement();
		// int col =
		// header.getColumnModel().getColumnIndex(column.getIdentifier());
		// int width = (int)myTable.getTableHeader().getDefaultRenderer()
		// .getTableCellRendererComponent(myTable, column.getIdentifier()
		// , false, false, -1, col).getPreferredSize().getWidth();
		// for(int row = 0; row<rowCount; row++){
		// int preferedWidth = (int)myTable.getCellRenderer(row,
		// col).getTableCellRendererComponent(myTable,
		// myTable.getValueAt(row, col), false, false, row,
		// col).getPreferredSize().getWidth();
		// width = Math.max(width, preferedWidth);
		// }
		// header.setResizingColumn(column); // 此行很重要
		// column.setWidth(width+myTable.getIntercellSpacing().width + 10);
		// }
		TableColumnAdjuster adjuster = new TableColumnAdjuster(myTable, 10);
//		adjuster.setOnlyAdjustLarger(true);
		adjuster.adjustColumns();
	}

	protected JPanel tickColumns(final String[][] columns,
			final AbstractTableModel tableModel, final JTable table) {
		JPanel internalPanel = new JPanel();
		internalPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		internalPanel.setLayout(new BoxLayout(internalPanel, BoxLayout.Y_AXIS));
		columnsMap = new LinkedHashMap();
		for (int i = 0; i < columns.length; i++) {
			String[] tmp = columns[i];
			// 临时序号不用显示
			if ("INDEX".equalsIgnoreCase(tmp[1])) {
				continue;
			}
			boolean isShown = tmp[2].equalsIgnoreCase("Y") ? true : false;
			columnsMap.put(tmp[0], i);
			JCheckBox cbx = new JCheckBox(tmp[0], isShown);

			// 主键不可选
			if ("ID".equalsIgnoreCase(tmp[1])
					|| "GROUPID".equalsIgnoreCase(tmp[1])) {
				cbx.setEnabled(false);
			}

			cbx.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					JCheckBox source = (JCheckBox) e.getSource();
					Set entrySet = columnsMap.entrySet();
					Iterator<Entry> items = entrySet.iterator();
					while (items.hasNext()) {
						Entry entry = items.next();
						if (entry.getKey().equals(source.getText())) {
							int index = (Integer) entry.getValue();
							if (e.getStateChange() == ItemEvent.SELECTED) {
								columns[index][2] = "Y";
							} else if (e.getStateChange() == ItemEvent.DESELECTED) {
								columns[index][2] = "N";
							}
						}
					}

					tableModel.fireTableStructureChanged();

					fitTableColumns(table);
				}

			});
			internalPanel.add(cbx);
		}
		return internalPanel;
	}

	protected class PageBar {

		private PageUtil page;
		private JButton currentSearchButton;
		JButton firstPageBtn;
		JButton prePageBtn;
		JButton nextPageBtn;
		JButton lastPageBtn;
		JComboBox pageSizeBox;

		private JTextField pageInfoField;
		private PrePageButtonListerner prePageBtnEvent;
		private NextPageButtonListerner nextPageBtnEvent;
		private FirstPageButtonListerner firstPageBtnEvent;
		private LastPageButtonListerner lastPageBtnEvent;

		public PageBar() {
			page = new PageUtil(Constants.PAGE_SIZE_DEFAULT, 0, 1);
			pageInfoField = new JTextField(4);
			prePageBtnEvent = new PrePageButtonListerner();
			nextPageBtnEvent = new NextPageButtonListerner();
			firstPageBtnEvent = new FirstPageButtonListerner();
			lastPageBtnEvent = new LastPageButtonListerner();
		}

		/**
		 * 设置分布前事件状态
		 * 
		 * @param nextBtn
		 * @param preBtn
		 * @param button
		 */
		protected void updateBtnEvent(JButton button) {
			currentSearchButton = button;
			pageInfoField.setText(page.getCurrentPage() + "/"
					+ page.getPageCount());
			updatePageBtnStatus();
		}

		public JPanel createPageButtons() {
			JPanel panel = new JPanel();
			JLabel pageInfo = new JLabel("当前页/总页数");

			pageInfoField.setText(page.getCurrentPage() + "/"
					+ page.getPageCount());
			pageInfoField.setEditable(false);

			JLabel pageSizeBoxLabel = new JLabel("每页显示数量");
			pageSizeBox = new JComboBox();
			pageSizeBox.addItem(10);
			pageSizeBox.addItem(25);
			pageSizeBox.addItem(50);
			pageSizeBox.addItem(100);
			pageSizeBox.addItem(1000);
			pageSizeBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					page.setPageSize(Integer.parseInt(pageSizeBox
							.getSelectedItem().toString()));
					// 重新查询
					if (currentSearchButton != null) {
						page.setCurrentPage(1);
						// 重新查询结果
						currentSearchButton.doClick();
					}
				}
			});

			pageSizeBox.setSelectedItem(Constants.PAGE_SIZE_DEFAULT);

			firstPageBtn = new JButton();
			firstPageBtn.setText("首页");
			firstPageBtn.addActionListener(firstPageBtnEvent);
			prePageBtn = new JButton();
			prePageBtn.setText("上页");
			prePageBtn.addActionListener(prePageBtnEvent);
			nextPageBtn = new JButton();
			nextPageBtn.setText("下页");
			nextPageBtn.addActionListener(nextPageBtnEvent);
			lastPageBtn = new JButton();
			lastPageBtn.setText("最后");
			lastPageBtn.addActionListener(lastPageBtnEvent);

			panel.add(pageInfo);
			panel.add(pageInfoField);
			panel.add(pageSizeBoxLabel);
			panel.add(pageSizeBox);
			panel.add(firstPageBtn);
			panel.add(prePageBtn);
			panel.add(nextPageBtn);
			panel.add(lastPageBtn);

			return panel;
		}

		private void updatePageBtnStatus() {
			if (page.getCurrentPage() == 1) {
				firstPageBtn.setEnabled(false);
				prePageBtn.setEnabled(false);
				if (page.getPageCount() == 1) {
					nextPageBtn.setEnabled(false);
					lastPageBtn.setEnabled(false);
				} else {
					nextPageBtn.setEnabled(true);
					lastPageBtn.setEnabled(true);
				}
			} else if (page.getCurrentPage() == page.getPageCount()) {
				nextPageBtn.setEnabled(false);
				lastPageBtn.setEnabled(false);
				if (page.getPageCount() == 1) {
					nextPageBtn.setEnabled(false);
					lastPageBtn.setEnabled(false);
				} else {
					firstPageBtn.setEnabled(true);
					prePageBtn.setEnabled(true);
				}
			} else {
				nextPageBtn.setEnabled(true);
				lastPageBtn.setEnabled(true);
				firstPageBtn.setEnabled(true);
				prePageBtn.setEnabled(true);
			}
		}

		protected class PrePageButtonListerner implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				logger.debug("Click pre page button");
				if (currentSearchButton != null) {
					page.setCurrentPage(page.getCurrentPage() - 1);
					logger.debug("Nevigate to previous page"
							+ (page.getCurrentPage() - 1));

					updatePageBtnStatus();
					// 重新查询结果
					currentSearchButton.doClick();
				}
			}

			public void setPage(PageUtil pageUtil) {
				page = pageUtil;
			}
		}

		protected class FirstPageButtonListerner implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				logger.debug("Click pre page button");
				if (currentSearchButton != null) {
					page.setCurrentPage(1);
					logger.debug("Nevigate to previous page"
							+ (page.getCurrentPage() - 1));
					updatePageBtnStatus();
					// 重新查询结果
					currentSearchButton.doClick();
				}
			}
		}

		protected class LastPageButtonListerner implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				logger.debug("Click pre page button");
				if (currentSearchButton != null) {
					page.setCurrentPage(page.getPageCount());
					logger.debug("Nevigate to previous page"
							+ (page.getCurrentPage() - 1));
					updatePageBtnStatus();
					// 重新查询结果
					currentSearchButton.doClick();
				}
			}
		}

		protected class NextPageButtonListerner implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				logger.debug("Click next page button");
				if (currentSearchButton != null) {
					page.setCurrentPage(page.getCurrentPage() + 1);
					logger.debug("Nevigate to next page"
							+ (page.getCurrentPage() + 1));
					updatePageBtnStatus();
					// 重新查询结果
					currentSearchButton.doClick();
				}
			}
		}

		public PageUtil getPage() {
			return page;
		}

		public void setPage(PageUtil page) {
			this.page = page;
		}

		public JButton getCurrentSearchButton() {
			return currentSearchButton;
		}

		public void setCurrentSearchButton(JButton currentSearchButton) {
			this.currentSearchButton = currentSearchButton;
		}

		public void reset() {
			this.page.setCurrentPage(1);
		}
	}

	protected void populateForm() {

	}

	public BaseJInternalFrame(String title, boolean resizable,
			boolean closable, boolean maximizable, boolean iconifiable) {
		super(title, resizable, closable, maximizable, iconifiable);
	}

	protected class ResetButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			populateForm();
		}
	}

	protected class CancelButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

	protected class ImportButtonListener implements ActionListener {
		private BasePOJO basePOJO;
		private Vector<Object> tableData;

		public ImportButtonListener(BasePOJO basePOJO, Vector<Object> tableData) {
			this.basePOJO = basePOJO;
			this.tableData = tableData;
		}

		public void actionPerformed(ActionEvent e) {
			// CustomerDataExcel excel = new
			// CustomerDataExcel("D:\\客户资料导入模板.xls");
			if (!MainFrame
					.isInternalFrameOpened(Constants.MODULE_EXCEL_TEMPLATE, false)) {
				ExcelDataImportFrame frame = new ExcelDataImportFrame(
						tableData, basePOJO);
				MainFrame.openNewFrame(frame, false);
			}
		}
	}

	protected class ExportAllButtonListener implements ActionListener {
		private JTable table;
		private String fileName;

		public void actionPerformed(ActionEvent e) {
			ExcelUtil excelUtil = new ExcelUtil();
			excelUtil
					.addExportExcelListener(table, CommonFactory
							.getCustomizationService().getCustomizationInfo(),
							fileName);
		}

		public ExportAllButtonListener(JTable table, String fileName) {
			this.table = table;
			this.fileName = fileName;
		}
	}

	protected class PrintAllButtonListener implements ActionListener {
		private JTable table;

		public void actionPerformed(ActionEvent e) {
			try {
				MessageFormat headerFormat = new MessageFormat("Page {0}");
				MessageFormat footerFormat = new MessageFormat("- {0} -");
				table.print(JTable.PrintMode.FIT_WIDTH, headerFormat,
						footerFormat);

			} catch (PrinterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		public PrintAllButtonListener(JTable table) {
			this.table = table;
		}
	}

	protected void showInfoMsg(String message) {
		JOptionPane.showMessageDialog(MainFrame.desktopPane, message, "消息",
				JOptionPane.INFORMATION_MESSAGE);
	}

	protected void showErrorMsg(String message) {
		JOptionPane.showMessageDialog(MainFrame.desktopPane, message, "错误 ",
				JOptionPane.ERROR_MESSAGE);
	}

	protected void showWarningMsg(String message) {
		JOptionPane.showMessageDialog(MainFrame.desktopPane, message, "警告",
				JOptionPane.WARNING_MESSAGE);
	}

	protected int showConfirmDialog() {
		return JOptionPane.showConfirmDialog(MainFrame.desktopPane, "确认删除吗？",
				"警告", JOptionPane.YES_NO_OPTION);
	}

	protected abstract class BaseAddButtonListener implements ActionListener {

	}

	protected abstract class BaseUpdateButtonListener implements ActionListener {

	}

	protected abstract class BaseDeleteButtonListener implements ActionListener {
	}

	protected class SelectDateListener implements ActionListener {
		JTextField dateField;
		public SelectDateListener(JTextField dateField){
			this.dateField = dateField;
		}
		public void actionPerformed(ActionEvent e) {
			DatePicker dp = new DatePicker((Observer) dateField,
					Locale.CHINA);
			// previously selected date
			Date selectedDate = dp.parseDate(dateField.getText());
			dp.setSelectedDate(selectedDate);
			dp.start(dateField);
		};
	}

	class TableNumberCellRenderer extends DefaultTableCellRenderer {
		// private int precision = 3;
		private Number numberValue;
		// private NumberFormat nf;

		DecimalFormat numberFormat = new DecimalFormat();

		/**
		 * 
		 */
		private static final long serialVersionUID = 8614834008762461362L;

		private AbstractTableModel tableModel;

		public TableNumberCellRenderer(AbstractTableModel tableModel, int type) {
			super();
			this.tableModel = tableModel;

			setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

			if (Constants.NUMBER_TYPE_INT == type) {
				numberFormat.applyPattern(Constants.NUMBER_FORMAT_INT);
			} else if (Constants.NUMBER_TYPE_DOUBLE == type) {
				numberFormat.applyPattern(Constants.NUMBER_FORMAT_DOUBLE);
			} else {
				numberFormat.applyPattern(Constants.NUMBER_FORMAT_DEFAULT);
			}
			// nf = NumberFormat.getNumberInstance();
			// nf.setMinimumFractionDigits(3);
			// nf.setMaximumFractionDigits(3);
			// nf.setRoundingMode(RoundingMode.HALF_UP);
		}

		@Override
		public void setValue(Object value) {
			if ((value != null) && (value instanceof Number)) {
				numberValue = (Number) value;
				value = numberFormat.format(numberValue.doubleValue());
			}
			super.setValue(value);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int col) {

			int modelRow = table.convertRowIndexToModel(row);
			int modelCol = table.convertColumnIndexToModel(col);

			// Cells are by default rendered as a JLabel.
			JLabel l = (JLabel) super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, modelRow, modelCol);

			// Get the status for the current row.
			tableModel = (AbstractTableModel) table.getModel();
			if (Double.valueOf(String.valueOf(tableModel.getValueAt(modelRow,
					modelCol))) < 0) {
				l.setForeground(Color.RED);
			} else {
				l.setForeground(Color.BLACK);
			}
			l.setHorizontalAlignment(JLabel.RIGHT);

			// Return the JLabel which renders the cell.
			return l;
		}
	}

	public String getEditType() {
		return editType;
	}

	public void setEditType(String editType) {
		this.editType = editType;
	}

	public String getFrameId() {
		return frameId;
	}

	public void setFrameId(String frameId) {
		this.frameId = frameId;
	}
}
