/**
 *用户操作接口，定义对用户进行的操作 
 */

package com.ivt.mis.service;

import java.util.List;
import java.util.Vector;

import com.ivt.mis.model.Customer;

public interface CustomerService
{
	/**
	 * 向数据库中添加新客户
	 * @param customer 封装好的客户
	 * @return 执行结果
	 */
	boolean saveCustomer(Customer customer);
	
	/**
	 * 向数据库中添加新客户
	 * @param customer 封装好的客户
	 * @return 执行结果
	 */
	boolean saveBatchCustomers(List<Customer> customers);
	
	/**
	 * 从数据库中删除指定客户的信息
	 * @param id 被删除客户的编号
	 * @return 执行结果
	 */
	boolean removeCustomer(String id);
	/**
	 * 查询数据库中满足条件的客户
	 * @param field 查询的字段
	 * @param value 满足的值
	 * @return 查询结果
	 */
	Vector<Customer> retrieveCustomers(Customer customer);
	
	/**
	 * 获取所有客户编码列表
	 * @return
	 */
	Vector<String> retrieveAllCustomerCodes();
	
	/**
	 * 更新客户信息
	 * @param customer 封装好的客户新信息
	 */
	boolean modifyCustomer(Customer customer);
	/**
	 * 获取所有客户信息
	 * @return 客户集合
	 */
	Vector<Customer> retrieveAllCustomers();
	/**
     * 判断用户是否存在
     * @param id 查询的用户便还
     * @return 查询结果
     */
    boolean isExited(String id);
    /**
     * 获取特定客户的信息
     * @param id 客户编号
     * @return 查询结果
     */
    Customer getCustomerInfo(String id);
	
}
