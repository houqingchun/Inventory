/**
 * 进货服务接口
 */
package com.ivt.mis.service;

import java.util.List;
import java.util.Vector;

import com.ivt.mis.model.StoreProcurement;

public interface StoreProcurementService
{
	/**
	 * 添加新的进货记录
	 * @param storeProcurement 封装好的StoreProcurement对象
	 * @return 执行结果
	 */
	boolean saveStoreProcurement(StoreProcurement storeProcurement);
	
	/**
	 * 批量添加新的进货记录
	 * @param storeProcurement 封装好的StoreProcurement对象
	 * @return 执行结果
	 */
	boolean saveBatchStoreProcurement(List<StoreProcurement> storeProcurements);
	
	/**
	 * 更新进货记录
	 * @param storeProcurement 封装好的StoreProcurement对象
	 * @return 执行结果
	 */
	boolean modifyBatchStoreProcurement(List<StoreProcurement> storeProcurements);
	
	/**
	 * 更新进货记录
	 * @param storeProcurement 封装好的StoreProcurement对象
	 * @return 执行结果
	 */
	boolean modifyStoreProcurement(StoreProcurement storeProcurement);
	
	/**
	 * 获取所有的进货信息
	 * @return 进货信息集合
	 */
	Vector<StoreProcurement> retrieveAllStoreProcurements();
	/**
	 * 查询数据库中满足条件的进货记录
	 * @param field 查询的字段
	 * @param value 满足的值
	 * @return 查询结果
	 */
	Vector<StoreProcurement> retrieveStoreProcurements(StoreProcurement storeProcurement);
	
/**
 * 根据ID获取对象
 * @param id
 * @return
 */
	StoreProcurement getStoreProcurement(String id);
	
	/**
	 * 判断进货编号是否存在
	 * @param id 
	 * @return
	 */
	boolean isExited(String id);
}
