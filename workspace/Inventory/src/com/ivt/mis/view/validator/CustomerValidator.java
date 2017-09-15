package com.ivt.mis.view.validator;

import java.util.Map;

import com.ivt.mis.common.Constants;
import com.ivt.mis.common.DataValidator;
import com.ivt.mis.common.ValidationManager;
import com.ivt.mis.model.Customer;
import com.ivt.mis.model.FieldDefn;
import com.ivt.mis.service.CustomerService;

public class CustomerValidator extends BaseValidator {
	Customer customer;
	CustomerService customerService;

	public CustomerValidator(Customer customer,
			CustomerService customerService, String editType) {
		this.customer = customer;
		this.customerService = customerService;
		this.editType = editType;
		this.basePOJO = customer;
	}

	@Override
	public Map<String, Object[]> validate() {
		Map<String, Object[]> errors = super.validate();

		FieldDefn field = this.customer.getFieldDefn("id");
		
		if (field.isRequired() && Constants.FORM_TYPE_ADD.equals(editType)
				&& customerService.isExited(this.customer.getId())) {
			errors.put("numberExist", new Object[] {
					"errors.field.exist.already", field.getDescr() });
		}
		
		field = this.customer.getFieldDefn("zip");
		if (field.isRequired() && !ValidationManager.validateZip(this.customer.getZip())) {
			errors.put("zip", new Object[] { "errors.field.format", field.getDescr() });
		}

		field = this.customer.getFieldDefn("email");
		if (field.isRequired() && !DataValidator.isEmail(customer.getEmail())) {
			errors.put("email", new Object[] { "errors.field.format", field.getDescr() });
		}

		return errors;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.basePOJO = customer;
		this.customer = customer;
	}

}
