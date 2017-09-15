package com.ivt.mis.service;

import java.util.List;
import java.util.Vector;

import com.ivt.mis.model.StoreManage;

public interface StoreManageService {

	
	/**
	 * 查看所有库存
	 * 
	 * @return 查询结果集
	 */
	public Vector<StoreManage> retrieveAllStoreManages();

	/**
	 * 根据查询条件查询库存状况
	 * 
	 * @return 查询结果集
	 */
	public Vector<StoreManage> retrieveAllStoreManageByProperties(
			StoreManage storeManage);

	/**
	 * 更新库存，入库则NUMBER为正，出库为负
	 * 
	 * @param storeProcurementId
	 * @param number
	 * @return
	 */
	public boolean modifyStoreTotalQty(String storeProcurementId, int number);

	/**
	 * 重置库存（入库修改库存）
	 * 
	 * @param storeProcurementId
	 * @param number
	 * @return
	 */
	public boolean modifyAndResetStoreTotalQty(String storeProcurementId, int number);
	
	/**
	 * 批量添加新的库存记录
	 * 
	 * @param storeManage
	 *            封装好的StoreManage对象
	 * @return 执行结果
	 */
	boolean saveStoreManage(StoreManage storeManage);

	/**
	 * 批量添加新的库存记录
	 * 
	 * @param storeManage
	 *            封装好的StoreManage对象
	 * @return 执行结果
	 */
	boolean saveBatchStoreManage(List<StoreManage> storeManages);

	/**
	 * 更新库存记录
	 * 
	 * @param storeManage
	 *            封装好的StoreManage对象
	 * @return 执行结果
	 */
	boolean modifyBatchStoreManage(List<StoreManage> storeManages, boolean resetFlag);
	
	/**
	 * 根据入库编号获取库存总量
	 * 
	 * @param storeProcurementId
	 *            入库编号
	 * @return 剩余库存量
	 */
	int getRemainingStoreQty(String storeProcurementId);
	
	

	/**
	 * /** 盘点清理库存为0的数据
	 * 
	 * @param storeProcurementId
	 * @param number
	 * @return
	 */
	public boolean removeStoreManage();
}
