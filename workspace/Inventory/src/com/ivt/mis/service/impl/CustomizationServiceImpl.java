package com.ivt.mis.service.impl;

import com.ivt.mis.dao.CustomizationDAO;
import com.ivt.mis.model.Customization;
import com.ivt.mis.service.CustomizationService;

public class CustomizationServiceImpl implements CustomizationService {

	CustomizationDAO customizationDAO;

	@Override
	public boolean saveCustomization(Customization customization) {
		return customizationDAO.addCustomization(customization);
	}

	@Override
	public boolean modifyCustomization(Customization customization) {
		return customizationDAO.updateCustomization(customization);
	}

	@Override
	public boolean isExited() {
		return customizationDAO.isExited();
	}

	@Override
	public Customization getCustomizationInfo() {
		return customizationDAO.findCustomization();
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

}
