package com.ivt.mis.view.validator;

import java.util.HashMap;
import java.util.Map;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.DataValidator;
import com.ivt.mis.model.FieldDefn;
import com.ivt.mis.model.StoreShipment;
import com.ivt.mis.service.CustomerService;
import com.ivt.mis.service.StoreManageService;
import com.ivt.mis.service.StoreShipmentService;

public class StoreShipmentValidator extends BaseValidator {
	StoreShipment storeShipment;
	CustomerService customerService;
	StoreShipmentService storeShipmentService;
	StoreManageService storeManageService;

	public StoreShipmentValidator(StoreShipment storeShipment,
			CustomerService customerService, String editType) {
		this.storeShipment = storeShipment;
		this.customerService = customerService;
		this.editType = editType;
		storeShipmentService = CommonFactory.getStoreShipmentService();
		storeManageService = CommonFactory.getStoreManageService();
		this.basePOJO = storeShipment;
	}

	@Override
	public Map<String, Object[]> validate() {
		Map<String, Object[]> errors = super.validate();

		FieldDefn field = this.storeShipment.getFieldDefn("customerId");
		if (field.isRequired()
				&& !customerService
						.isExited(this.storeShipment.getCustomerId())) {
			errors.put("customerIdNotExist", new Object[] {
					"errors.field.not.exist", field.getDescr() });
		}

		field = this.storeShipment.getFieldDefn("shipNumber");
		if (field.isRequired()) {
			if (this.storeShipment.getShipNumber() == 0) {
				errors.put("shipNumber", new Object[] {
						"errors.field.required", field.getDescr() });
			} else if (this.storeShipment.getShipNumber() > 0
					&& Constants.STORE_SHIP_TYPE_RETURN
							.equals(this.storeShipment.getShipType())) {
				errors.put("shipNumber", new Object[] {
						"errors.field.store.ship.return", "销售退货", "正数" });
			} else if (this.storeShipment.getShipNumber() < 0
					&& Constants.STORE_SHIP_TYPE_SELL.equals(this.storeShipment
							.getShipType())) {
				errors.put("shipNumber", new Object[] {
						"errors.field.store.ship.return", "销售出货", "负数" });
			} else {
				if (Constants.FORM_TYPE_UPDATE.equals(editType)) {
					int newShipNbr = this.storeShipment.getShipNumber();
					int oldShipNbr = storeShipmentService.getStoreShipment(
							this.storeShipment.getId()).getShipNumber();
					int remainingNbr = storeManageService
							.getRemainingStoreQty(this.storeShipment
									.getStoreProcurementId());
					if (newShipNbr != oldShipNbr && newShipNbr > remainingNbr) {
						errors.put("shipNumber", new Object[] {
								"errors.field.store.ship.update",
								String.valueOf(newShipNbr),
								String.valueOf(remainingNbr) });
					}
				} else if (Constants.FORM_TYPE_ADD.equals(editType)) {
					int newShipNbr = this.storeShipment.getShipNumber();
					int remainingNbr = storeManageService
							.getRemainingStoreQty(this.storeShipment
									.getStoreProcurementId());
					if (newShipNbr > remainingNbr) {
						errors.put("shipNumber", new Object[] {
								"errors.field.store.ship.update",
								String.valueOf(newShipNbr),
								String.valueOf(remainingNbr) });
					}
				}
			}
		}

		return errors;
	}

	public StoreShipment getStoreShipment() {
		return storeShipment;
	}

	public void setStoreShipment(StoreShipment storeShipment) {
		this.storeShipment = storeShipment;
		this.basePOJO =  storeShipment;
	}
}
