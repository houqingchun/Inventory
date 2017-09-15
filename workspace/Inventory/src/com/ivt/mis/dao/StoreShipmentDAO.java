/**
 * 销售进货类DAO
 */
package com.ivt.mis.dao;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ivt.mis.model.Customer;
import com.ivt.mis.model.Product;
import com.ivt.mis.model.Provider;
import com.ivt.mis.model.StoreShipment;

public class StoreShipmentDAO extends BaseDAO {
	public static final Logger logger = Logger
			.getLogger(StoreShipmentDAO.class);

	public StoreShipmentDAO() {
		super();
	}



	/**
	 * 判断产品入库记录是否已经出库
	 * 
	 * @param storeProcurementId
	 *           入库记录编号
	 * @return 查询结果
	 */
	public boolean isExitedInStoreShipment(String storeProcurementId) {
		return this.isExistObj("countStoreShipmentByStoreProcurementId",
				storeProcurementId);
	}

	
	/**
	 * 向数据库中添加新的销售记录
	 * 
	 * @param storeShipment
	 *            封装好的StoreShipment对象
	 * @return 执行结果
	 */
	public boolean addStoreShipment(StoreShipment storeShipment) {
		return this.addObj("addStoreShipment", storeShipment);
	}
	
	/**
	 * Update product information
	 * @param product
	 * @return
	 */
	public boolean updateProductInfoForShipment(Product product){
		return this.updateObj("updateProductInfoForShipment", product);
	}
	
	/**
	 * Update customer information
	 * @param customer
	 * @return
	 */
	public boolean updateCustomerInfoForShipment(Customer customer){
		return this.updateObj("updateCustomerInfoForShipment", customer);
	}

	/**
	 * 获取所有的销售信息
	 * 
	 * @return 销售信息集合
	 */
	public Vector<StoreShipment> searchAllStoreShipment() {
		return this.getAllObjs("searchAllStoreShipment");
	}
	
	public StoreShipment findStoreShipmentById(String id){
		return (StoreShipment) this.getObj("findStoreShipmentById", id);
	}

	/**
	 * 更新出货记录
	 * 
	 * @param storeShipment
	 *            封装好的StoreShipment对象
	 * @return 执行结果
	 */
	public boolean updateBatchStoreShipment(List<StoreShipment> storeShipments) {
		return this.updateBatchObj("updateStoreShipment", storeShipments);
	}

	/**
	 * 批量添加新的出货记录
	 * 
	 * @param storeShipment
	 *            封装好的StoreShipment对象
	 * @return 执行结果
	 */
	public boolean addBatchStoreShipment(List<StoreShipment> storeShipments) {
		return this.addBatchObj("addStoreShipment", storeShipments);
	}

	/**
	 * 查询数据库中满足条件的出货记录
	 * 
	 * @param field
	 *            查询的字段
	 * @param value
	 *            满足的值
	 * @return 查询结果
	 */
	public Vector<StoreShipment> searchStoreShipmentByProperties(StoreShipment storeShipment) {
		int totalObjs = this.countObjs("searchStoreShipmentByPropertiesCount",
				storeShipment);
		storeShipment.getPage().setRecordCount(totalObjs);
		return this.getAllObjs("searchStoreShipmentByProperties", storeShipment);
	}


	/**
	 * 判断销售编号是否存在
	 * 
	 * @param id
	 * @return
	 */
	public boolean isExited(String id) {
		return this.isExistObj("countStoreShipment", id);
	}
}
