/**
 * 销售服务接口
 */
package com.ivt.mis.service;

import java.util.List;
import java.util.Vector;

import com.ivt.mis.model.StoreShipment;

public interface StoreShipmentService
{
	/**
	 * 向数据库中添加新的销售记录
	 * @param storeShipment 封装好的StoreShipment对象
	 * @return 执行结果
	 */
	boolean saveStoreShipment(StoreShipment storeShipment);
	
	/**
	 * 根据ID获取对象
	 * @param id
	 * @return
	 */
	StoreShipment getStoreShipment(String id);


	/**
	 * 判断产品入库记录是否已经出库
	 * 
	 * @param storeProcurementId
	 *           入库记录编号
	 * @return 查询结果
	 */
	boolean isExitedInStoreShipment(String storeProcurementId);
	
	/**
	 * 批量添加新的出货记录
	 * @param storeShipment 封装好的StoreShipment对象
	 * @return 执行结果
	 */
	boolean saveBatchStoreShipment(List<StoreShipment> storeShipments);
	
	/**
	 * 更新出货记录
	 * @param storeShipment 封装好的StoreShipment对象
	 * @return 执行结果
	 */
	boolean modifyBatchStoreShipment(List<StoreShipment> storeShipments);
	
	/**
	 * 获取所有的销售信息
	 * @return 销售信息集合
	 */
	Vector<StoreShipment> retrieveAllStoreShipments();
	
	/**
	 * 查询数据库中满足条件的销售记录
	 * @param field 查询的字段
	 * @param value 满足的值
	 * @return 查询结果
	 */
	public Vector<StoreShipment> retrieveStoreShipments(StoreShipment storeShipment);
	
	/**
	 * 判断销售编号是否存在
	 * @param id 
	 * @return
	 */
	boolean isExited(String id);
}
