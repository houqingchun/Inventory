package com.ivt.mis.view.excel;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.DataValidator;
import com.ivt.mis.common.I18nMsg;
import com.ivt.mis.model.Product;
import com.ivt.mis.view.MainFrame;
import com.ivt.mis.view.validator.ProductValidator;

public class ProductTemplate extends ExcelTemplate {

	public static final Logger logger = Logger
			.getLogger(ProductTemplate.class);
	private String filePath;
	private Vector<Object> products = new Vector<Object>();

	public ProductTemplate(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public Vector<Object> previewData() {

		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		try {
			fs = new POIFSFileSystem(new FileInputStream(filePath));
			wb = new HSSFWorkbook(fs);

			HSSFSheet sheet = null;
			// 第1个sheet
			sheet = wb.getSheetAt(0);

			// 从第八行开始取数据
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Product product = new Product();
				// 取得一行
				HSSFRow row = sheet.getRow(i);
				// 共15列

				// 若第一列为空，则继续读取下一行
				if (row.getCell(0) == null
						|| DataValidator.isBlankOrNull(row.getCell(0)
								.getStringCellValue())) {
					continue;
				}
				int seq = i;
				int index = 0; // Cell序号
				product.setIndex(String.valueOf(seq));
				product.setId(this.getCellStringValue(row.getCell(index++)));
				product.setProductCode(this.getCellStringValue(row.getCell(index++)));
				product.setProductName(this.getCellStringValue(row.getCell(index++)));
				product.setBrand(this.getCellStringValue(row.getCell(index++)));
				product.setSize(this.getCellStringValue(row.getCell(index++)));
				product.setUnit(this.getCellStringValue(row.getCell(index++)));
				product.setCategory(this.getCellStringValue(row.getCell(index++)));
				product.setPromitCode(this.getCellStringValue(row.getCell(index++)));
				product.setManufacturer(this.getCellStringValue(row.getCell(index++)));
				product.setDescription(this.getCellStringValue(row.getCell(index++)));
				product.setVersion(1);
				product.setAvailable(1);
				product.setOwnerId(MainFrame.loginname);
				products.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return products;
	}

	@Override
	public String validateData() {
		ProductValidator productValidator = new ProductValidator(null,
				CommonFactory.getProductService(), CommonFactory.getProviderService(),Constants.FORM_TYPE_ADD);
		Set<String> ids = new HashSet<String>();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < products.size(); i++) {
			Product product = (Product) products.get(i);
			productValidator.setProduct(product);
			String result = productValidator.getErrorString();
			if (ids.contains(product.getId())) {
				if (result == null) {
					result = "";
				}
				result += I18nMsg.getText("errors.field.duplicate",
						new String[] { "供应商编号" }) + "\n";
			}
			if (result != null) {
				int index = i + 1;
				buf.append("第" + index + "行" + result);
			}

			ids.add(product.getId());
		}
		logger.debug(buf.toString());

		return buf.toString();
	}

	public Vector<Object> getProducts() {
		return products;
	}

	public void setProducts(Vector<Object> products) {
		this.products = products;
	}
}