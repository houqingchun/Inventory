package com.ivt.mis.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.poi.util.IOUtils;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.model.BasePOJO;
import com.ivt.mis.model.Customer;
import com.ivt.mis.model.Product;
import com.ivt.mis.model.Provider;
import com.ivt.mis.service.CustomerService;
import com.ivt.mis.service.ProductService;
import com.ivt.mis.service.ProviderService;
import com.ivt.mis.view.excel.CustomerTemplate;
import com.ivt.mis.view.excel.ProductTemplate;
import com.ivt.mis.view.excel.ProviderTemplate;

public class ExcelDataImportFrame extends BaseJInternalFrame {
	Vector<Object> tableData;

	JTextField excelDataPathField;
	JButton browserBtn;
	JFileChooser excelFileChooser;
	BasePOJO basePOJO;

	JTable table;
	ExcelDataTableModel excelDataTableModel;

	CustomerTemplate customerTemplate;
	ProviderTemplate providerTemplate;
	ProductTemplate productTemplate;
	JButton readBtn;

	public ExcelDataImportFrame(Vector<Object> tableData, BasePOJO basePOJO) {
		super("数据导入", true, true, true, true);
		super.setFrameId(Constants.MODULE_EXCEL_TEMPLATE);
		this.basePOJO = basePOJO;
		if (basePOJO instanceof Customer) {
			this.setTitle("客户数据批量导入");
		} else if (basePOJO instanceof Product) {
			this.setTitle("产品数据批量导入");
		} else if (basePOJO instanceof Provider) {
			this.setTitle("供应商数据批量导入");
		}

		//this.setLayer(this.getLayer() + 1);
		Dimension deskPaneDim = MainFrame.desktopPane.getSize();

		this.setSize(deskPaneDim.width * 3 / 4, deskPaneDim.height * 2 / 3);

		Dimension internalDim = this.getSize();
		this.setLocation(
				(deskPaneDim.width - internalDim.width) / 2 + this.getLayer()
						* 5, (deskPaneDim.height - internalDim.height) / 2
						+ this.getLayer() * 5);

		this.tableData = tableData;

		this.setContentPane(new ExcelDataJPanel(basePOJO));
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6503527518372649059L;

	class ExcelDataJPanel extends JPanel {
		private static final long serialVersionUID = 8225266748173917405L;

		public ExcelDataJPanel(BasePOJO basePOJO) {
			super(new BorderLayout());

			excelDataTableModel = new ExcelDataTableModel(basePOJO);
			table = new JTable(excelDataTableModel);
			// 自动计算宽度
			fitTableColumns(table);

			// 设置序号中间排列
			DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
			rightRenderer.setHorizontalAlignment(JLabel.CENTER);
			rightRenderer.setBackground(this.getBackground());
			table.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);

			// 装载数据
			populateTable();

			table.setFillsViewportHeight(true);
			// table.setAutoCreateRowSorter(true);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.getViewport().setBackground(Color.WHITE);

			JPanel buttonPanel = new JPanel(new FlowLayout());

			// 下载模板
			JButton downloadBtn = new JButton("Excel模板下载");
			downloadBtn.addActionListener(new DownloadButtonListener());

			// 输入域
			excelDataPathField = new JTextField();
			excelDataPathField.setColumns(25);

			// 浏览按扭
			browserBtn = new JButton(getText("form.btn.browser"));
			browserBtn.addActionListener(new DirectoryChooserForExcelData());

			// 读取并预览
			readBtn = new JButton();
			readBtn.setText(getText("form.btn.read.preview"));
			readBtn.addActionListener(new ReadExcelDataForPreview());

			// 数据验证
			JButton dataVerifyBtn = new JButton();
			dataVerifyBtn.setText(getText("form.btn.data.validation"));
			dataVerifyBtn.addActionListener(new ExcelDataVerification());

			// 导入并保存
			JButton importSaveBtn = new JButton();
			importSaveBtn.setText(getText("form.btn.import.save"));
			importSaveBtn.addActionListener(new ExcelDataImportAndSave());

			// 取消
			JButton cancelBtn = new JButton("取消");
			cancelBtn.addActionListener(new CancelButtonListener());

			buttonPanel.add(downloadBtn);
			buttonPanel.add(excelDataPathField);
			buttonPanel.add(excelDataPathField);
			buttonPanel.add(browserBtn);
			// buttonPanel.add(readBtn);
			buttonPanel.add(dataVerifyBtn);
			buttonPanel.add(importSaveBtn);
			buttonPanel.add(cancelBtn);

			add(buttonPanel, BorderLayout.NORTH);
			add(scrollPane, BorderLayout.CENTER);
		}

		private void populateTable() {
			excelDataTableModel.updateData(tableData);
		}

		class DownloadButtonListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser2 = new JFileChooser();
				chooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser2.setDialogTitle("选择保存的目录");
				// 注意两个方法不同
				int returnVal = chooser2.showSaveDialog(null);
				chooser2.setVisible(true);
				if (returnVal == JFileChooser.CANCEL_OPTION) {
					return;
				}
				String savePath = chooser2.getSelectedFile().getAbsolutePath();
				String fileName = "";
				if (basePOJO instanceof Customer) {
					fileName = "客户资料导入模板.xls";
				} else if (basePOJO instanceof Product) {
					fileName = "产品资料导入模板.xls";
				} else if (basePOJO instanceof Provider) {
					fileName = "供应商资料导入模板.xls";
				}

				boolean isDownloadDone = true;

				// 保存信息至文件
				String sourcePath = LicenseRegisterFrame.class.getResource("/")
						.getPath();
				sourcePath = decodePath(sourcePath);
				savePath = decodePath(savePath);
				FileInputStream in = null;
				FileOutputStream out = null;
				try {
					in = new FileInputStream(new File(sourcePath + "/template/"
							+ fileName));
					out = new FileOutputStream(new File(savePath + "/"
							+ fileName));
					IOUtils.copy(in, out);
				} catch (Exception e1) {
					isDownloadDone = false;
					e1.printStackTrace();
				} finally {
					try {
						in.close();
						out.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}

				if (isDownloadDone) {
					showInfoMsg("模板已经下载至" + savePath + "/" + fileName);
				} else {
					showErrorMsg("模板下载失败");
				}

			}

		}

		class DirectoryChooserForExcelData implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				excelFileChooser = new JFileChooser();
				FileFilter filter1 = new FileNameExtensionFilter("Excel","xlsx", "xls");
				excelFileChooser.setFileFilter(filter1);
				excelFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				logger.debug("launch chooser...");
				int returnVal = excelFileChooser
						.showOpenDialog(excelDataPathField);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = excelFileChooser.getSelectedFile();
					excelDataPathField.setText(file.getAbsolutePath());
					readBtn.doClick();
				}
			}
		}

		class ReadExcelDataForPreview implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {

				// 自动计算宽度
				fitTableColumns(table);
				
				if (basePOJO instanceof Customer) {
					customerTemplate = new CustomerTemplate(
							decodePath(excelDataPathField.getText()));
					Vector<Object> data = customerTemplate.previewData();
					if (data != null) {
						excelDataTableModel.updateData(data);
					} else {
						showErrorMsg("文件格式不正确");
					}
				} else if (basePOJO instanceof Provider) {
					providerTemplate = new ProviderTemplate(
							decodePath(excelDataPathField.getText()));
					Vector<Object> data = providerTemplate.previewData();
					if (data != null) {
						excelDataTableModel.updateData(data);
					} else {
						showErrorMsg("文件格式不正确");
					}
				} else if (basePOJO instanceof Product) {
					productTemplate = new ProductTemplate(
							decodePath(excelDataPathField.getText()));
					Vector<Object> data = productTemplate.previewData();
					if (data != null) {
						excelDataTableModel.updateData(data);
					} else {
						showErrorMsg("文件格式不正确");
					}
				}
			}
		}

		class ExcelDataVerification implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (basePOJO instanceof Customer) {
					if (customerTemplate == null) {
						showWarningMsg("没有数据需要验证");
					} else {
						String verifyResult = customerTemplate.validateData();
						if (verifyResult.length() > 0) {
							showErrorMsg(verifyResult);
						} else {
							showInfoMsg("数据验证成功");
						}
					}
				} else if (basePOJO instanceof Provider) {
					if (providerTemplate == null) {
						showWarningMsg("没有数据需要验证");
					} else {
						String verifyResult = providerTemplate.validateData();
						if (verifyResult.length() > 0) {
							showErrorMsg(verifyResult);
						} else {
							showInfoMsg("数据验证成功");
						}
					}
				} else if (basePOJO instanceof Product) {
					if (productTemplate == null) {
						showWarningMsg("没有数据需要验证");
					} else {
						String verifyResult = productTemplate.validateData();
						if (verifyResult.length() > 0) {
							showErrorMsg(verifyResult);
						} else {
							showInfoMsg("数据验证成功");
						}
					}
				}

			}
		}

		class ExcelDataImportAndSave implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 自动计算宽度
				fitTableColumns(table);
				
				if (basePOJO instanceof Customer) {
					if (customerTemplate == null) {
						showWarningMsg("没有数据需要导入保存");
					} else {
						// 再次验证
						String verifyResult = customerTemplate.validateData();
						if (verifyResult.length() > 0) {
							showErrorMsg(verifyResult);
						} else {
							CustomerService customerService = CommonFactory
									.getCustomerService();
							if (customerService
									.saveBatchCustomers(new ArrayList(
											customerTemplate.getCustomers()))) {
								showInfoMsg("导入成功");
								dispose();
							} else {
								showErrorMsg("导入失败");
							}
						}
					}
				} else if (basePOJO instanceof Provider) {
					if (providerTemplate == null) {
						showWarningMsg("没有数据需要导入保存");
					} else {
						// 再次验证
						String verifyResult = providerTemplate.validateData();
						if (verifyResult.length() > 0) {
							showErrorMsg(verifyResult);
						} else {
							ProviderService providerService = CommonFactory
									.getProviderService();
							if (providerService
									.saveBatchProviders(new ArrayList(
											providerTemplate.getProviders()))) {
								showInfoMsg("导入成功");
								dispose();
							} else {
								showErrorMsg("导入失败");
							}
						}
					}
				} else if (basePOJO instanceof Product) {
					if (productTemplate == null) {
						showWarningMsg("没有数据需要导入保存");
					} else {
						// 再次验证
						String verifyResult = productTemplate.validateData();
						if (verifyResult.length() > 0) {
							showErrorMsg(verifyResult);
						} else {
							ProductService productService = CommonFactory
									.getProductService();
							if (productService.saveBatchProducts(new ArrayList(
									productTemplate.getProducts()))) {
								showInfoMsg("导入成功");
								dispose();
							} else {
								showErrorMsg("导入失败");
							}
						}
					}
				}

			}
		}
	}

	class ExcelDataTableModel extends AbstractTableModel {

		private static final long serialVersionUID = -7441951137082514125L;

		Vector<Object> objectsVector = new Vector<Object>();
		BasePOJO basePOJO;

		public ExcelDataTableModel(BasePOJO basePOJO) {
			this.basePOJO = basePOJO;
			String[][] columns = basePOJO.getColumns();
			ArrayList<String[]> list = new ArrayList<String[]>();
			for (int i = 0; i < columns.length; i++) {
				String[] subAry = columns[i];
				// if ("Y".equalsIgnoreCase(subAry[2])) {
				list.add(subAry);
				// }
			}

			columnNames = new String[list.size()][3];
			for (int i = 0; i < list.size(); i++) {
				columnNames[i] = list.get(i);
			}
		}

		private String[][] columnNames;

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return objectsVector.size();
		}

		public String getColumnName(int col) {
			return columnNames[col][0];
		}

		public Object getValueAt(int row, int col) {
			BasePOJO object = (BasePOJO) objectsVector.get(row);
			return object.getValueAt(columnNames[col][1]);
		}

		@SuppressWarnings("unchecked")
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			if (col > 0) {
				return true;
			}
			return false;
		}

		/*
		 * Don't need to implement this method unless your table's data can
		 * change.
		 */
		public void setValueAt(Object value, int row, int col) {
			BasePOJO basePOJO = (BasePOJO) objectsVector.get(row);
			basePOJO.setValueAt(value, columnNames[col][1]);
			fireTableCellUpdated(row, col);
		}

		// 更新数据
		public void updateData(Vector<Object> customerVector) {
			this.objectsVector = customerVector;
			if (customerVector.size() == 0) {
				this.objectsVector = new Vector<Object>();
			} else {
				fireTableRowsInserted(0, customerVector.size() - 1);
			}
		}

	}
}
