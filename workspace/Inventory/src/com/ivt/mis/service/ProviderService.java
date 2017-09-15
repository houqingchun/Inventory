/**
 * 供应商服务接口
 */
package com.ivt.mis.service;

import java.util.List;
import java.util.Vector;

import com.ivt.mis.model.Customer;
import com.ivt.mis.model.Provider;

public interface ProviderService
{
	/**
	 * 向数据库中添加新的操作员
	 * @param user 封装好的操作员
	 * @return 执行结果
	 */
	boolean saveProvider(Provider provider);
	
	/**
	 * 向数据库中批量添加新的操作员
	 * @param user 封装好的操作员
	 * @return 执行结果
	 */
	boolean saveBatchProviders(List<Provider> providers);
	/**
	 * 从数据库中删除指定供应商的信息
	 * @param id 被删除供应商的编号
	 * @return 执行结果
	 */
	boolean removeProvider(String id);
	/**
	 * 查询数据库中满足条件的供应商
	 * @param field 查询的字段
	 * @param value 满足的值
	 * @return 查询结果
	 */
	Vector<Provider> retrieveProviders(Provider provider);
	/**
	 * 更新供应商信息
	 * @param customer 封装好的供应商新信息
	 */
	boolean modifyProvider(Provider provider);
	/**
	 * 获取所有供应商信息
	 * @return 供应商集合
	 */
	Vector<Provider> retrieveAllProviders();
	
	/**
	 * 获取所有供应商编码信息
	 * @return 供应商编码集合
	 */
	Vector<String> retrieveAllProviderCodes();
	/**
     * 判断供应商是否存在
     * @param id 查询的用户便还
     * @return 查询结果
     */
   boolean isExited(String id);
   /**
    * 获取特定供应商的信息
    * @param id 供应商编号
    * @return 查询结果
    */
   Provider getProviderInfo(String id);
	
}
