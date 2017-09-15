/**
 * 客户服务类
 */
package com.ivt.mis.service.impl;

import java.util.List;
import java.util.Vector;

import com.ivt.mis.common.BasicTypeUtils;
import com.ivt.mis.common.Constants;
import com.ivt.mis.dao.CustomerDAO;
import com.ivt.mis.dao.StoreProcurementDAO;
import com.ivt.mis.dao.StoreShipmentDAO;
import com.ivt.mis.model.Customer;
import com.ivt.mis.service.CodeRuleService;
import com.ivt.mis.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {
	CustomerDAO customerDAO;
	StoreProcurementDAO storeProcurementDAO;
	StoreShipmentDAO storeShipmentDAO;
	CodeRuleService codeRuleService;

	public void setCodeRuleService(CodeRuleService codeRuleService) {
		this.codeRuleService = codeRuleService;
	}

	public CustomerServiceImpl() {
		super();
	}

	public boolean saveCustomer(Customer customer) {
		this.codeRuleService.refreshOjbectCode(Constants.MODULE_CUSTOMER,
				customer.getId());
		customer.setCreateTime(BasicTypeUtils.getLongFmtDate());
		return customerDAO.addCustomer(customer);
	}

	public boolean removeCustomer(String id) {

		return customerDAO.deleteCustomer(id);

	}

	public boolean modifyCustomer(Customer customer) {
		boolean flag = customerDAO.updateCustomer(customer);
		flag &= storeProcurementDAO.updateCustomerInfoForProcurement(customer);
		flag &= storeShipmentDAO.updateCustomerInfoForShipment(customer);
		return flag;
	}

	public Vector<Customer> retrieveAllCustomers() {

		return customerDAO.searchAllCustomers();
	}

	public Vector<Customer> retrieveCustomers(Customer customer) {
		return customerDAO.searchCustomerByProperties(customer);
	}

	public boolean isExited(String id) {

		return customerDAO.isExited(id);
	}

	public Customer getCustomerInfo(String id) {

		return customerDAO.findCustomerById(id);
	}

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	@Override
	public Vector<String> retrieveAllCustomerCodes() {
		return customerDAO.searchAllCustomerCodes();
	}

	public void setStoreProcurementDAO(StoreProcurementDAO storeProcurementDAO) {
		this.storeProcurementDAO = storeProcurementDAO;
	}

	public void setStoreShipmentDAO(StoreShipmentDAO storeShipmentDAO) {
		this.storeShipmentDAO = storeShipmentDAO;
	}

	@Override
	public boolean saveBatchCustomers(List<Customer> customers) {

		String maxId = "";
		for (Customer customer : customers) {
			maxId = maxId.compareTo(customer.getId()) < 0 ? customer.getId()
					: maxId;
			customer.setCreateTime(BasicTypeUtils.getLongFmtDate());
		}
		this.codeRuleService.refreshOjbectCode(Constants.MODULE_CUSTOMER,
				maxId);
		return this.customerDAO.addBatchCustomer(customers);
	}

}
