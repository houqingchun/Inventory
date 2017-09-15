package com.ivt.mis.view.excel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

public abstract class ExcelTemplate {
	public static final Vector<String> DATA_COLUMNS = new Vector<String>();

	/**
	 * 预览导入的数据
	 * 
	 * @return
	 */
	public Vector<Object> previewData() {
		return null;
	};

	/**
	 * 验证数据
	 * 
	 * @return
	 */
	public String validateData() {
		return null;
	};

	public String getCellStringValue(HSSFCell cell) {
		String cellValue = "";
		if (cell == null){
			cellValue = "";
		}else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			cellValue = cell.getStringCellValue();
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
			cellValue = String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				cellValue = new SimpleDateFormat("yyyy-MM-dd").format(cell
						.getDateCellValue());

			} else {
				cellValue = String.valueOf(cell.getNumericCellValue());
			}

		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_ERROR) {
			cellValue = String.valueOf(cell.getErrorCellValue());
		}

		return cellValue;
	}

	public boolean getCellBooleanValue(HSSFCell cell) {
		boolean cellValue = Boolean.FALSE;
		if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			cellValue = Boolean.valueOf(cell.getStringCellValue());
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
			cellValue = cell.getBooleanCellValue();
		}

		return cellValue;
	}

	public double getCellDoubleValue(HSSFCell cell) {
		double cellValue = 0;
		if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			cellValue = Double.valueOf(cell.getStringCellValue());
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			cellValue = cell.getNumericCellValue();
		}

		return cellValue;
	}

	public int getCellIntValue(HSSFCell cell) {
		int cellValue = 0;
		if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			cellValue = Integer.valueOf(cell.getStringCellValue());
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			cellValue = (int) cell.getNumericCellValue();
		}

		return cellValue;
	}

	public Date getCellDateValue(HSSFCell cell) {
		Date cellValue = null;
		if (HSSFDateUtil.isCellDateFormatted(cell)) {
			cellValue = cell.getDateCellValue();
		}

		return cellValue;
	}
}
