package com.ivt.mis.view.validator;

import java.util.HashMap;
import java.util.Map;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.DataValidator;
import com.ivt.mis.model.FieldDefn;
import com.ivt.mis.model.StoreProcurement;
import com.ivt.mis.service.CustomerService;
import com.ivt.mis.service.ProviderService;
import com.ivt.mis.service.StoreManageService;
import com.ivt.mis.service.StoreProcurementService;
import com.ivt.mis.service.StoreShipmentService;

public class StoreProcurementValidator extends BaseValidator {
	StoreProcurement storeProcurement;
	ProviderService providerService;
	CustomerService customerService;
	StoreProcurementService storeProcurementService;
	StoreShipmentService storeShipmentService;
	StoreManageService storeManageService;

	public StoreProcurementValidator(StoreProcurement storeProcurement,
			ProviderService providerService, CustomerService customerService,
			String editType) {
		this.storeProcurement = storeProcurement;
		this.editType = editType;
		this.providerService = providerService;
		this.customerService = customerService;
		storeProcurementService = CommonFactory.getStoreProcurementService();
		storeShipmentService = CommonFactory.getStoreShipmentService();
		storeManageService = CommonFactory.getStoreManageService();
		this.basePOJO = storeProcurement;
	}

	@Override
	public Map<String, Object[]> validate() {
		Map<String, Object[]> errors = super.validate();

		FieldDefn field = this.storeProcurement.getFieldDefn("providerId");
		if (field.isRequired()
				&& !providerService.isExited(this.storeProcurement
						.getProviderId())) {
			errors.put("providerId", new Object[] { "errors.field.not.exist",
					field.getDescr() });
		}

		field = this.storeProcurement.getFieldDefn("produceTime");
		if (field.isRequired()
				&& !DataValidator.isDate(
						this.storeProcurement.getProduceTime(), "yyyy-MM-dd",
						false)) {
			errors.put("producetime", new Object[] {
					"errors.time.format。invalid", field.getDescr(),
					"2001-12-01" });
		}

		field = this.storeProcurement.getFieldDefn("proNumber");
		if (field.isRequired()) {
			if (this.storeProcurement.getProNumber() == 0){
				errors.put("proNumber", new Object[] {
						"errors.field.required", field.getDescr()});
			}else if (this.storeProcurement.getProNumber() > 0
					&& Constants.STORE_PROCUREMENT_TYPE_RETURN
							.equals(this.storeProcurement.getProType())) {
				errors.put("proNumber", new Object[] {
						"errors.field.store.ship.return", "采购退货", "正数" });
			} else if (this.storeProcurement.getProNumber() < 0
					&& Constants.STORE_PROCUREMENT_TYPE_PRO
							.equals(this.storeProcurement.getProType())) {
				errors.put("proNumber", new Object[] {
						"errors.field.store.ship.return", "采购入库", "负数" });
			} else {
				if (Constants.FORM_TYPE_UPDATE.equals(editType)) {
					int newProNbr = this.storeProcurement.getProNumber();
					int oldProNbr = storeProcurementService
							.getStoreProcurement(this.storeProcurement.getId())
							.getProNumber();
					boolean isShipped = storeShipmentService
							.isExitedInStoreShipment(this.storeProcurement
									.getId());
					if (newProNbr != oldProNbr && isShipped) {
						errors.put("proNumber", new Object[] {
								"errors.field.change.not.allowed", "已经出库",
								"入库数量" });
					}
				}
			}
		}


		if (!DataValidator.isBlankOrNull(this.storeProcurement.getCustomerId()) && !customerService.isExited(this.storeProcurement
						.getCustomerId())) {
			errors.put("customerId", new Object[] { "errors.field.not.exist",
					"预定客户编号" });
		}

		return errors;
	}

	public StoreProcurement getStoreProcurement() {
		return storeProcurement;
	}

	public void setStoreProcurement(StoreProcurement storeProcurement) {
		this.storeProcurement = storeProcurement;
		this.basePOJO = storeProcurement;
	}
}
