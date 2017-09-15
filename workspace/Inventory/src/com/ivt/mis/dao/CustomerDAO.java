/**
 * 客户DAO类，主要对客户信息进行管理
 */
package com.ivt.mis.dao;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ivt.mis.model.Customer;

public class CustomerDAO extends BaseDAO {

	public static final Logger logger = Logger.getLogger(CustomerDAO.class);

	public CustomerDAO() {
		super();
	}

	/**
	 * 
	 * 向数据库中添加新客户
	 * 
	 * @param customer
	 *            封装好的客户
	 * @return 执行结果
	 */
	public boolean addCustomer(Customer customer) {
		return this.addObj("addCustomer", customer);
	}
	
	/**
	 * 
	 * 向数据库中添加新客户
	 * 
	 * @param customer
	 *            封装好的客户
	 * @return 执行结果
	 */
	public boolean addBatchCustomer(List<Customer> customers) {
		return this.addBatchObj("addCustomer", customers);
	}

	/**
	 * 从数据库中删除指定客户的信息，由于存在外键关系，此处仅设置表中 available=0
	 * 
	 * @param id
	 *            被删除客户的编号
	 * @return 执行结果
	 */
	public boolean deleteCustomer(String id) {
		return this.deleteObj("deleteCustomerById", id);
	}

	/**
	 * 查询数据库中满足条件的客户
	 * 
	 * @param field
	 *            查询的字段
	 * @param value
	 *            满足的值
	 * @return 查询结果
	 */
	public Vector<Customer> searchCustomerByProperties(Customer customer) {
		int totalObjs = this.countObjs("searchCustomerByPropertiesCount",customer);
		customer.getPage().setRecordCount(totalObjs);
		return this.getAllObjs("searchCustomerByProperties", customer);
	}

	/**
	 * 修改客户信息
	 * 
	 * @param customer
	 *            封装好的客户新信息
	 * @return 执行结果
	 */
	public boolean updateCustomer(Customer customer) {
		return this.updateObj("updateCustomer", customer);
	}

	/**
	 * 显示所有的客户
	 * 
	 * @return 客户集合
	 */
	public Vector<Customer> searchAllCustomers() {
		return this.getAllObjs("searchAllCustomers");
	}

	/**
	 * 判断用户是否存在
	 * 
	 * @param id
	 *            查询的客户编号
	 * @return 查询结果
	 */
	public boolean isExited(String id) {
		if (this.findCustomerById(id) != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取特定客户的信息
	 * 
	 * @param id
	 *            客户编号
	 * @return 查询结果
	 */
	public Customer findCustomerById(String id) {
		return (Customer) this.getObj("findCustomerById", id);
	}
	
	/**
	 * 获取所有客户编码列表
	 * 
	 * @param id
	 *            客户编号
	 * @return 查询结果
	 */
	public Vector<String> searchAllCustomerCodes() {
		Vector<Customer> customers = this.getAllObjs("searchAllCustomerCodes");
		Vector<String> results = new Vector<String>();
		for (int i = 0; i < customers.size(); i++){
			results.add(customers.get(i).getId());
		}
		
		return results;
	}
}
