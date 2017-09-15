/**
 * 供应商类DAO
 */
package com.ivt.mis.dao;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ivt.mis.common.IbatisUtil;
import com.ivt.mis.model.Provider;

public class ProviderDAO extends BaseDAO {
	SqlMapClient sqlMapClient = IbatisUtil.getSqlMapClient();

	public static final Logger logger = Logger.getLogger(ProviderDAO.class);

	public ProviderDAO() {
		super();
	}

	/**
	 * 添加新供应商
	 * 
	 * @param provider
	 *            封装好的供应商信息
	 * @return 执行结果
	 */
	public boolean addProvider(Provider provider) {
		return super.addObj("addProvider", provider);
	}
	

	/**
	 * 添加新供应商
	 * 
	 * @param provider
	 *            封装好的供应商信息
	 * @return 执行结果
	 */
	public boolean addBatchProvider(List<Provider> providers) {
		return super.addBatchObj("addProvider", providers);
	}
	

	/**
	 * 从数据库中删除指定供应商的信息，由于存在外键关系，此处仅设置表中 available=0
	 * 
	 * @param id
	 *            被删除供应商的编号
	 * @return 执行结果
	 */
	public boolean deleteProvider(String id) {
		return super.deleteObj("deleteProviderById", id);
	}

	/**
	 * 查询数据库中满足条件的供应商
	 * 
	 * @param field
	 *            查询的字段
	 * @param value
	 *            满足的值
	 * @return 查询结果
	 */
	public Vector<Provider> searchProviderByProperties(Provider provider) {
		int totalObjs = this
				.countObjs("searchProviderByPropertiesCount", provider);
		provider.getPage().setRecordCount(totalObjs);
		return this.getAllObjs("searchProviderByProperties", provider);

	}

	/**
	 * 修改供应商信息
	 * 
	 * @param provider
	 *            封装好的供应商新信息
	 * @return 执行结果
	 */
	public boolean updateProvider(Provider provider) {
		return this.updateObj("updateProvider", provider);
	}

	/**
	 * 显示所有的供应商
	 * 
	 * @return 供应商集合
	 */
	public Vector<Provider> searchAllProviders() {
		return this.getAllObjs("searchAllProviders");
	}

	/**
	 * 判断供应商是否存在
	 * 
	 * @param id
	 *            查询的用户便还
	 * @return 查询结果
	 */
	public boolean isExited(String id) {
		if (this.findProviderById(id) != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取特定供应商的信息
	 * 
	 * @param id
	 *            供应商编号
	 * @return 查询结果
	 */
	public Provider findProviderById(String id) {
		return (Provider) this.getObj("findProviderById", id);
	}
	
	/**
	 * 获取所有客户编码列表
	 * 
	 * @param id
	 *            客户编号
	 * @return 查询结果
	 */
	public Vector<String> searchAllProviderCodes() {
		Vector<Provider> providers = this.getAllObjs("searchAllProviderCodes");
		Vector<String> results = new Vector<String>();
		for (int i = 0; i < providers.size(); i++){
			results.add(providers.get(i).getId());
		}
		
		return results;
	}
}
