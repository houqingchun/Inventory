/**
 * 
 */
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
import com.ivt.mis.model.Customer;
import com.ivt.mis.view.MainFrame;
import com.ivt.mis.view.validator.CustomerValidator;

/**
 * @author qhou
 *
 */
public class CustomerTemplate extends ExcelTemplate {

	public static final Logger logger = Logger
			.getLogger(CustomerTemplate.class);
	private String filePath;
	private Vector<Object> customers = new Vector<Object>();

	public CustomerTemplate(String filePath) {
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

			// 从第1行开始取数据
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Customer customer = new Customer();
				// 取得一行
				HSSFRow row = sheet.getRow(i);

				// 若第一列为空，则继续读取下一行
				if (row.getCell(0) == null || DataValidator.isBlankOrNull(row.getCell(0)
						.getStringCellValue())) {
					continue;
				}
				int seq = i;
				int index = 0; //Cell序号
				customer.setIndex(String.valueOf(seq));
				customer.setId(this.getCellStringValue(row.getCell(index++)));
				customer.setName(this.getCellStringValue(row.getCell(index++)));
				customer.setContacts(this.getCellStringValue(row.getCell(index++)));
				customer.setTelephone(this.getCellStringValue(row.getCell(index++)));
				customer.setMobile(this.getCellStringValue(row.getCell(index++)));
				customer.setFax(this.getCellStringValue(row.getCell(index++)));
				customer.setEmail(this.getCellStringValue(row.getCell(index++)));
				customer.setAddress(this.getCellStringValue(row.getCell(index++)));
				customer.setShipAddress(this.getCellStringValue(row.getCell(index++)));
				customer.setZip(this.getCellStringValue(row.getCell(index++)));
				customer.setBank(this.getCellStringValue(row.getCell(index++)));
				customer.setAccount(this.getCellStringValue(row.getCell(index++)));
				customer.setRegistryPlace(this.getCellStringValue(row.getCell(index++)));
				customer.setRegistryNumber(this.getCellStringValue(row.getCell(index++)));
				customer.setRegistryType(this.getCellStringValue(row.getCell(index++)));
				customer.setLevelState(this.getCellStringValue(row.getCell(index++)));
				customer.setComments(this.getCellStringValue(row.getCell(index++)));
				customer.setVersion(1);
				customer.setAvailable(1);
				customer.setOwnerId(MainFrame.loginname);
				customers.add(customer);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return customers;
	}

	@Override
	public String validateData() {
		CustomerValidator customerValidator = new CustomerValidator(null,
				CommonFactory.getCustomerService(), Constants.FORM_TYPE_ADD);
		Set<String> ids = new HashSet<String>();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < customers.size(); i++) {
			Customer customer = (Customer) customers.get(i);
			customerValidator.setCustomer(customer);
			String result = customerValidator.getErrorString();
			if (ids.contains(customer.getId())) {
				if (result == null) {
					result = "";
				}
				result += I18nMsg.getText("errors.field.duplicate",
						new String[] { "客户编号" }) + "\n";
			}
			if (result != null) {
				int index = i + 1;
				buf.append("第" + index + "行" + result);
			}

			ids.add(customer.getId());
		}
		logger.debug(buf.toString());

		return buf.toString();
	}

	public Vector<Object> getCustomers() {
		return customers;
	}

	public void setCustomers(Vector<Object> customers) {
		this.customers = customers;
	}

	// public static void main(String[] args) {
	// CustomerTemplate excel = new CustomerTemplate("D:\\客户资料导入模板.xls");
	// excel.previewData();
	// excel.validateData();
	// }

}
