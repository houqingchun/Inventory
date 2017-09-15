package com.ivt.mis.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;

import com.ivt.mis.model.Customization;

public class ExcelUtil {
	public final static String EXPORT_NOTE = "右击可导出数据..";

	public void addExportExcelPopupMenu(final JTable table,
			Customization customization, String fileName) {
		final JPopupMenu popup = new JPopupMenu();
		// the save JMenuItem and its associated ActionListener
		// JMenuItem save = new JMenuItem("导出数据");
		// save.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent action) {
		// try {
		// ExcelExporter exp = new ExcelExporter();
		// Date currentDate = new Date();
		// File file = new File("ExportData_" + currentDate.getTime() + ".xls");
		// exp.exportTable(table, file);
		// } catch (IOException ex) {
		// System.out.println(ex.getMessage());
		// ex.printStackTrace();
		// }
		// }
		// });
		// popup.add(save);

		// The open JMenuItem and its associated ActionListener
		JMenuItem open = new JMenuItem("导出数据至Excel并打开");
		open.addActionListener(new ExportDataListener(table, customization,
				fileName));
		popup.add(open);

		// the following method only works in JDK 5.0 or greater
		table.setComponentPopupMenu(popup);

		// the following code is needed for JDK 1.4
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				if (popup.isPopupTrigger(event)) {
					popup
							.show(event.getComponent(), event.getX(), event
									.getY());
				}
			}

			public void mouseReleased(MouseEvent event) {
				if (popup.isPopupTrigger(event)) {
					popup
							.show(event.getComponent(), event.getX(), event
									.getY());
				}
			}
		});

	}

	class ExportDataListener implements ActionListener {

		Customization customization = null;
		JTable table;
		String fileName;

		public ExportDataListener(JTable table, Customization customization,
				String fileName) {
			this.table = table;
			this.customization = customization;
			this.fileName = fileName;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				// Note that i'm actually saving the file first
				ExcelExporter exp = new ExcelExporter();
				Date currentDate = new Date();
				String tempPath = "./temp";
				File dir = new File(tempPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// File file = new File(tempPath + "/ExportData_"
				// + currentDate.getTime() + ".xls");
				File file = new File(tempPath + "/" + fileName + "_"
						+ currentDate.getTime() + ".xls");

				exp.exportTable(table, file, this.customization, fileName);
				ExcelOpener opn = new ExcelOpener();
				opn.openTable(file);
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}

		}

	}

	/**
	 * 添加导出按扭Listener
	 * 
	 * @param table
	 */
	public void addExportExcelListener(JTable table,
			Customization customization, String fileName) {
		try {
			// Note that i'm actually saving the file first
			ExcelExporter exp = new ExcelExporter();
			Date currentDate = new Date();
			String tempPath = "./temp";
			File dir = new File(tempPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(tempPath + "/" + fileName + "_"
					+ currentDate.getTime() + ".xls");

			exp.exportTable(table, file, customization, fileName);
			ExcelOpener opn = new ExcelOpener();
			opn.openTable(file);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	class ExcelExporter {
		public ExcelExporter() {
		}

		public void exportTableTxt(JTable table, File file) throws IOException {
			TableModel model = table.getModel();
			FileWriter out = new FileWriter(file);

			for (int i = 0; i < model.getColumnCount(); i++) {
				out.write(model.getColumnName(i) + "\t");
			}
			out.write("\n");

			for (int i = 0; i < model.getRowCount(); i++) {
				for (int j = 0; j < model.getColumnCount(); j++) {
					// I added this check for the ISBN conversion
					if (j == 0) {
						// the book Title
						out.write(model.getValueAt(i, j).toString() + "\t");
					} else {
						/*
						 * the ISBN Number Note that I added a \" to the front
						 * of the string and a \t followed by a closing \" to
						 * let Excel know that this field is to be converted as
						 * text
						 */
						out.write("\"" + model.getValueAt(i, j).toString()
								+ "\t" + "\"");
					}
				}
				out.write("\n");
			}
			out.close();
			System.out.println("write to " + file);
		}

		private void autoSetColumnWidth(HSSFSheet sheet, int startRow,
				int maxRowNum, int maxColumn) {
			// 获取当前列的宽度，然后对比本列的长度，取最大值
			for (int columnNum = 0; columnNum <= maxColumn; columnNum++) {
				sheet.autoSizeColumn(columnNum);
				
				int columnWidth = sheet.getColumnWidth(columnNum) / 256;
				
				for (int rowNum = startRow; rowNum <= maxRowNum; rowNum++) {
					HSSFRow currentRow;
					// 当前行未被使用过
					if (sheet.getRow(rowNum) == null) {
						currentRow = sheet.createRow(rowNum);
					} else {
						currentRow = sheet.getRow(rowNum);
					}

					if (currentRow.getCell(columnNum) != null) {
						HSSFCell currentCell = currentRow.getCell(columnNum);
						int length = 0;
						try {
							length = currentCell.toString().getBytes("UTF-8").length;
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						if (columnWidth < length) {
							columnWidth = length;
						}
					}
				}
				sheet.setColumnWidth(columnNum, columnWidth * 256);
			}
		}

		@SuppressWarnings("deprecation")
		public void exportTable(JTable table, File file,
				Customization customization, String fileName)
				throws IOException {
			TableModel model = table.getModel();

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFCellStyle styleHeader = wb.createCellStyle();
			HSSFFont fontHeader = wb.createFont();
			fontHeader.setFontName(HSSFFont.FONT_ARIAL);
			fontHeader.setFontHeightInPoints((short) 10);
			fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			styleHeader.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			styleHeader.setFont(fontHeader);
			styleHeader.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

			HSSFCellStyle styleTitle = wb.createCellStyle();
			HSSFFont font = wb.createFont();
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints((short) 12);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setColor(HSSFColor.BLUE.index);
			styleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			styleTitle.setFillForegroundColor(HSSFColor.CORNFLOWER_BLUE.index);

			styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			styleTitle.setFont(font);
			styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

			try {
				HSSFSheet sheet = wb.createSheet(fileName);
				sheet.setGridsPrinted(true);
				sheet.setAutobreaks(true);
				sheet.setFitToPage(true);
				HSSFRow row = null;
				HSSFCell cell = null;

				// 录入公司相关住处
				int rowNum = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0);
				cell.setCellValue("公司名称:");
				cell.setCellStyle(styleHeader);
				cell = row.createCell(1);
				cell.setCellValue(customization.getCompanyName());
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0);
				cell.setCellValue("联系人:");
				cell.setCellStyle(styleHeader);
				cell = row.createCell(1);
				cell.setCellValue(customization.getContacts());
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0);
				cell.setCellValue("联系电话:");
				cell.setCellStyle(styleHeader);
				cell = row.createCell(1);
				cell.setCellValue(customization.getMobile());
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0);
				cell.setCellValue("电子邮箱:");
				cell.setCellStyle(styleHeader);
				cell = row.createCell(1);
				cell.setCellValue(customization.getEmail());
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0);
				cell.setCellValue("传真:");
				cell.setCellStyle(styleHeader);
				cell = row.createCell(1);
				cell.setCellValue(customization.getFax());

				// 表头
				row = sheet.createRow(rowNum++);
				row.setHeightInPoints(22);

				cell = row.createCell(0);
				cell.setCellStyle(styleTitle);

				cell.setCellValue(fileName);
				// 合并单元格
				CellRangeAddress region1 = new CellRangeAddress(rowNum - 1,
						(short) rowNum - 1, 0,
						(short) model.getColumnCount() - 1);
				sheet.addMergedRegion(region1);

				// Write table header
				row = sheet.createRow(rowNum++);
				for (int i = 0; i < model.getColumnCount(); i++) {
					cell = row.createCell(i);
					String value = String.valueOf((String) model
							.getColumnName(i));
					cell.setCellValue(value);
					cell.setCellStyle(styleHeader);
				}

				// Write data by row
				for (int i = 0; i < table.getRowCount(); i++) {
					row = sheet.createRow(i + rowNum);
					for (int j = 0; j < table.getColumnCount(); j++) {
						String value = String.valueOf(table.getValueAt(i, j));
						cell = row.createCell(j);
						cell.setCellValue(value);
					}
				}

				// 调整列宽
				autoSetColumnWidth(sheet, rowNum - 1, rowNum - 1
						+ table.getRowCount(), model.getColumnCount());

				FileOutputStream out = new FileOutputStream(file);
				wb.write(out);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("write to " + file.getAbsolutePath());
		}
	}

	class ExcelOpener {
		public ExcelOpener() {
		}

		public void openTable(File file) throws IOException {
			Runtime run = Runtime.getRuntime();
			// I make the assumption that the client has Excel and
			// the file type .XLS is associated with Excel

			// This is a simple check to find out the operating system
			String lcOSName = System.getProperty("os.name").toLowerCase();
			boolean MAC_OS_X = lcOSName.startsWith("mac os x");
			if (MAC_OS_X) {
				run.exec("open " + file);
			} else {
				run.exec("cmd.exe /c start " + file);
			}
			System.out.println(file + " opened");
		}
	}

}
