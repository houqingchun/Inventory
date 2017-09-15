/**
 * 进货单类DAO
 */
package com.ivt.mis.dao;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ivt.mis.model.Customer;
import com.ivt.mis.model.Product;
import com.ivt.mis.model.Provider;
import com.ivt.mis.model.StoreProcurement;

public class StoreProcurementDAO extends BaseDAO {
	public static final Logger logger = Logger
			.getLogger(StoreProcurementDAO.class);

	public StoreProcurementDAO() {
		super();
	}

	/**
	 * 添加新的进货记录
	 * 
	 * @param storeProcurement
	 *            封装好的StoreProcurement对象
	 * @return 执行结果
	 */
	public boolean addStoreProcurement(StoreProcurement storeProcurement) {
		return this.addObj("addStoreProcurement", storeProcurement);
	}

	/**
	 * 根据ID得到单个对象
	 * 
	 * @param id
	 * @return
	 */
	public StoreProcurement findStoreProcurementById(String id) {
		return (StoreProcurement) this.getObj("findStoreProcurementById", id);
	}

	/**
	 * 批量添加新的进货记录
	 * 
	 * @param storeProcurement
	 *            封装好的StoreProcurement对象
	 * @return 执行结果
	 */
	public boolean addBatchStoreProcurement(
			List<StoreProcurement> storeProcurements) {
		return this.addBatchObj("addStoreProcurement", storeProcurements);
	}

	/**
	 * 更新进货记录
	 * 
	 * @param storeProcurement
	 *            封装好的StoreProcurement对象
	 * @return 执行结果
	 */
	public boolean updateStoreProcurement(StoreProcurement storeProcurement) {
		return this.updateObj("updateStoreProcurement", storeProcurement);
	}

	/**
	 * 更新进货记录
	 * 
	 * @param storeProcurement
	 *            封装好的StoreProcurement对象
	 * @return 执行结果
	 */
	public boolean updateBatchStoreProcurement(
			List<StoreProcurement> storeProcurements) {
		return this.updateBatchObj("updateStoreProcurement", storeProcurements);
	}

	/**
	 * 查询数据库中满足条件的进货记录
	 * 
	 * @param field
	 *            查询的字段
	 * @param value
	 *            满足的值
	 * @return 查询结果
	 */
	public Vector<StoreProcurement> searchStoreProcurementsByProperties(
			StoreProcurement storeProcurement) {
		int totalObjs = this.countObjs("searchStoreProcurementsByPropertiesCount",
				storeProcurement);
		storeProcurement.getPage().setRecordCount(totalObjs);
		return this.getAllObjs("searchStoreProcurementsByProperties",
				storeProcurement);
	}

	/**
	 * 获取所有的进货信息
	 * 
	 * @return 进货信息集合
	 */
	public Vector<StoreProcurement> searchAllStoreProcurements() {
		return this.getAllObjs("searchAllStoreProcurements");
	}
	
	/**
	 * Update product information
	 * @param product
	 * @return
	 */
	public boolean updateProductInfoForProcurement(Product product){
		return this.updateObj("updateProductInfoForProcurement", product);
	}
	
	/**
	 * Update provider infomation
	 * @param provider
	 * @return
	 */
	public boolean updateProviderInfoForProcurement(Provider provider){
		return this.updateObj("updateProviderInfoForProcurement", provider);
	}
	
	/**
	 * Update customer information
	 * @param customer
	 * @return
	 */
	public boolean updateCustomerInfoForProcurement(Customer customer){
		return this.updateObj("updateCustomerInfoForProcurement", customer);
	}
	

	/**
	 * 判断进货编号是否存在
	 * 
	 * @param id
	 * @return
	 */
	public boolean isExited(String id) {
		return this.isExistObj("countStoreProcurement", id);
	}
}
