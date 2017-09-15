package com.ivt.mis.view.validator;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.ivt.mis.common.Constants;
import com.ivt.mis.common.DataValidator;
import com.ivt.mis.common.ValidationManager;
import com.ivt.mis.model.FieldDefn;
import com.ivt.mis.model.Product;
import com.ivt.mis.service.ProductService;
import com.ivt.mis.service.ProviderService;

public class ProductValidator extends BaseValidator {
	Product product;
	ProductService productService;
	ProviderService providerService;

	public ProductValidator(Product product, ProductService productService,
			ProviderService providerService,String editType) {
		this.product = product;
		this.productService = productService;
		this.providerService = providerService;
		this.editType = editType;
		this.basePOJO = product;
	}

	@Override
	public Map<String, Object[]> validate() {
		Map<String, Object[]> errors = super.validate();

		FieldDefn field = this.product.getFieldDefn("id");
		if (field.isRequired() && Constants.FORM_TYPE_ADD.equals(editType)
				&& productService.isExited(this.product.getId())) {
			errors.put("productIdExist", new Object[] {
					"errors.field.exist.already", field.getDescr() });
		}

		return errors;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.basePOJO = product;
		this.product = product;
	}
}
