package com.ivt.mis.view.validator;

import java.util.HashMap;
import java.util.Map;

import com.ivt.mis.common.Constants;
import com.ivt.mis.common.DataValidator;
import com.ivt.mis.common.ValidationManager;
import com.ivt.mis.model.Customization;
import com.ivt.mis.service.CustomizationService;

public class CustomizationValidator extends BaseValidator {
	Customization customization;

	public CustomizationValidator(Customization customization, String editType) {
		this.customization = customization;
		this.editType = editType;
	}

	@Override
	public Map<String, Object[]> validate() {
		HashMap<String, Object[]> errors = new HashMap<String, Object[]>();
		
		if (DataValidator.isBlankOrNull(this.customization.getCompanyName())) {
			errors.put("name", new Object[] { "errors.field.required", "公司名称" });
		}
		if (DataValidator.isBlankOrNull(this.customization.getContacts())) {
			errors.put("contacts", new Object[] { "errors.field.required",
					"联系电话" });
		}
		if (DataValidator.isBlankOrNull(this.customization.getMobile())) {
			errors.put("mobile", new Object[] { "errors.field.required",
					"联系电话" });
		}
		if (!DataValidator.isBlankOrNull(this.customization.getEmail())
				&& !DataValidator.isEmail(customization.getEmail())) {
			errors.put("email", new Object[] { "errors.field.format", "电子邮件" });
		}

		return errors;
	}

}
