package com.ivt.mis.dao;

import org.apache.log4j.Logger;

import com.ivt.mis.model.Customization;

public class CustomizationDAO extends BaseDAO {
	public static final Logger logger = Logger.getLogger(CustomizationDAO.class);

	public CustomizationDAO() {
		super();
	}


	/**
	 * 
	 * 向数据库中添加客户配置信息
	 * 
	 * @param customization
	 *            封装好的客户
	 * @return 执行结果
	 */
	public boolean addCustomization(Customization customization) {
		return this.addObj("addCustomization", customization);
	}
	

	/**
	 * 修改配置信息
	 * 
	 * @param customization
	 *            封装好的配置新信息
	 * @return 执行结果
	 */
	public boolean updateCustomization(Customization customization) {
		return this.updateObj("updateCustomization", customization);
	}
	
	/**
	 * 判断用户是否存在
	 * 
	 * @param id
	 *            查询的配置编号
	 * @return 查询结果
	 */
	public boolean isExited() {
		return this.isExistObj("countCustomization", "1");
	}
	
	/**
	 * 获取特定配置的信息
	 * 
	 * @param id
	 *            配置编号
	 * @return 查询结果
	 */
	public Customization findCustomization() {
		return (Customization) this.getObj("findCustomization","1");
	}
}
