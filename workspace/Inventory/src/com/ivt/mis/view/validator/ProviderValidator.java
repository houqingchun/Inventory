package com.ivt.mis.view.validator;

import java.util.Map;

import com.ivt.mis.common.Constants;
import com.ivt.mis.common.DataValidator;
import com.ivt.mis.common.ValidationManager;
import com.ivt.mis.model.FieldDefn;
import com.ivt.mis.model.Provider;
import com.ivt.mis.service.ProviderService;

public class ProviderValidator extends BaseValidator {
	Provider provider;
	ProviderService providerService;

	public ProviderValidator(Provider provider,
			ProviderService providerService, String editType) {
		this.provider = provider;
		this.providerService = providerService;
		this.editType = editType;
		this.basePOJO = provider;
	}

	@Override
	public Map<String, Object[]> validate() {
		Map<String, Object[]> errors = super.validate();

		FieldDefn field = this.provider.getFieldDefn("id");
		
		if (field.isRequired() && Constants.FORM_TYPE_ADD.equals(editType)
				&& providerService.isExited(this.provider.getId())) {
			errors.put("numberExist", new Object[] {
					"errors.field.exist.already", field.getDescr() });
		}

		field = this.provider.getFieldDefn("zip");
		if (field.isRequired() && !ValidationManager.validateZip(this.provider.getZip())) {
			errors.put("zip", new Object[] { "errors.field.format", field.getDescr() });
		}

		field = this.provider.getFieldDefn("email");
		if (field.isRequired() && !DataValidator.isEmail(provider.getEmail())) {
			errors.put("email", new Object[] { "errors.field.format", field.getDescr() });
		}

		return errors;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.basePOJO = provider;
		this.provider = provider;
	}

}
