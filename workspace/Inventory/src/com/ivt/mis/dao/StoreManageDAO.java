package com.ivt.mis.dao;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ivt.mis.model.Product;
import com.ivt.mis.model.StoreManage;


public class StoreManageDAO extends BaseDAO {
	public static final Logger logger = Logger.getLogger(StoreManageDAO.class);
	/**
	 * 查询库存的产品信息
	 * 
	 * @return 查询结果集
	 */
	public Vector<StoreManage> searchAllStoreManages() {
		return this.getAllObjs("searchAllStoreManages");
	}
	
	
	
	public StoreManageDAO() {
		super();
	}

	/**
	 * 增加库存信息
	 * 
	 * @return 查询结果集
	 */
	public boolean addStoreManage(StoreManage storeManage) {
		return this.addObj("addStoreManage", storeManage);
	}

	/**
	 * 判断产品记录是否存在与ivt_storemanage表中
	 * 
	 * @param productID
	 *            查询的产品型号
	 * @return 查询结果
	 */
	private boolean isExitedInStorage(String storeProcurementId) {
		return this.isExistObj("countStoreManageByStoreProcurementId",
				storeProcurementId);
	}

	/**
	 * 批量添加新的库存记录
	 * 
	 * @param storeProcurement
	 *            封装好的StoreManage对象
	 * @return 执行结果
	 */
	public boolean addBatchStoreManage(List<StoreManage> storeManages) {
		return this.addBatchObj("addStoreManage", storeManages);
	}

	/**
	 * 更新库存记录
	 * 
	 * @param storeManages
	 * @param resetFlag 若重置库存则为true,若更新，则为false
	 * @return
	 */
	public boolean updateBatchStoreManage(List<StoreManage> storeManages,
			boolean resetFlag) {
		if (resetFlag){
			return this.updateBatchObj("resetStoreManageTotalNbr", storeManages);	
		}else{
			return this.updateBatchObj("updateStoreTotalQty", storeManages);
		}
		
	}
	

	/**
	 * Update product information
	 * @param product
	 * @return
	 */
	public boolean updateProductInfoForStoreManager(Product product){
		return this.updateObj("updateProductInfoForStoreManager", product);
	}

	/**
	 * 根据查询条件查询库存状况
	 * 
	 * @return 查询结果集
	 */
	public Vector<StoreManage> searchStoreManagesByProperties(
			StoreManage storeManage) {
		int totalObjs = this.countObjs("searchStoreManagesByPropertiesCount",storeManage);
		storeManage.getPage().setRecordCount(totalObjs);
		return this.getAllObjs("searchStoreManagesByProperties", storeManage);
	}

	/**
	 * 根据入库编号更新总库存量，若入库（退）情况，NUMBER为负数
	 * 
	 * @param storeProcurementId
	 * @param number
	 * @return
	 */
	public boolean resetStoreTotalQty(String storeProcurementId, int number) {
		StoreManage param = new StoreManage();
		param.setStoreProcurementId(storeProcurementId);
		param.setTotalNbr(number);

		return this.updateObj("resetStoreManageTotalNbr", param);
	}

	/**
	 * 根据出库，修改剩余库存量，若为出库（退）的，NUMBER为负数
	 * 
	 * @param storeProcurementId
	 * @param number
	 * @return
	 */
	public boolean updateStoreTotalQty(String storeProcurementId, int number) {
		StoreManage param = new StoreManage();
		param.setStoreProcurementId(storeProcurementId);
		param.setTotalNbr(number);

		return this.updateObj("updateStoreTotalQty", param);
	}

	/**
	 * 盘点清理库存为0的数据
	 * 
	 * @param storeProcurementId
	 * @param number
	 * @return
	 */
	public boolean cleanStoreManager() {
		return this.deleteObj("cleanStoreManager");
	}
}
