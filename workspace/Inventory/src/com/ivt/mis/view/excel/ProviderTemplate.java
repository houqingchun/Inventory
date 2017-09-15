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
import com.ivt.mis.model.Provider;
import com.ivt.mis.view.MainFrame;
import com.ivt.mis.view.validator.ProviderValidator;

public class ProviderTemplate extends ExcelTemplate {

	public static final Logger logger = Logger
			.getLogger(ProviderTemplate.class);
	private String filePath;
	private Vector<Object> providers = new Vector<Object>();

	public ProviderTemplate(String filePath) {
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
				Provider provider = new Provider();
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
				provider.setIndex(String.valueOf(seq));
				provider.setId(this.getCellStringValue(row.getCell(index++)));
				provider.setName(this.getCellStringValue(row.getCell(index++)));
				provider.setContacts(this.getCellStringValue(row
						.getCell(index++)));
				provider.setTelephone(this.getCellStringValue(row
						.getCell(index++)));
				provider.setMobile(this.getCellStringValue(row.getCell(index++)));
				provider.setFax(this.getCellStringValue(row.getCell(index++)));
				provider.setEmail(this.getCellStringValue(row.getCell(index++)));
				provider.setAddress(this.getCellStringValue(row
						.getCell(index++)));
				provider.setShipAddress(this.getCellStringValue(row
						.getCell(index++)));
				provider.setZip(this.getCellStringValue(row.getCell(index++)));
				provider.setBank(this.getCellStringValue(row.getCell(index++)));
				provider.setAccount(this.getCellStringValue(row
						.getCell(index++)));
				provider.setRegistryPlace(this.getCellStringValue(row
						.getCell(index++)));
				provider.setRegistryNumber(this.getCellStringValue(row
						.getCell(index++)));
				provider.setRegistryType(this.getCellStringValue(row
						.getCell(index++)));
				provider.setLevelState(this.getCellStringValue(row
						.getCell(index++)));
				provider.setVersion(1);
				provider.setAvailable(1);
				provider.setOwnerId(MainFrame.loginname);
				providers.add(provider);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return providers;
	}

	@Override
	public String validateData() {
		ProviderValidator providerValidator = new ProviderValidator(null,
				CommonFactory.getProviderService(), Constants.FORM_TYPE_ADD);
		Set<String> ids = new HashSet<String>();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < providers.size(); i++) {
			Provider provider = (Provider) providers.get(i);
			providerValidator.setProvider(provider);
			String result = providerValidator.getErrorString();
			if (ids.contains(provider.getId())) {
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

			ids.add(provider.getId());
		}
		logger.debug(buf.toString());

		return buf.toString();
	}

	public Vector<Object> getProviders() {
		return providers;
	}

	public void setProviders(Vector<Object> providers) {
		this.providers = providers;
	}
}