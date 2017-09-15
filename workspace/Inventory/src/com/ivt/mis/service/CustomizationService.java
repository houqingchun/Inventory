package com.ivt.mis.service;

import com.ivt.mis.model.Customization;

public interface CustomizationService {
	/**
	 * 向数据库中添加新配置
	 * 
	 * @param customization
	 *            封装好的配置
	 * @return 执行结果
	 */
	boolean saveCustomization(Customization customization);

	/**
	 * 更新配置信息
	 * 
	 * @param customization
	 *            封装好的配置新信息
	 */
	boolean modifyCustomization(Customization customization);

	/**
	 * 判断用户是否存在
	 * 
	 * @return 查询结果
	 */
	boolean isExited();

	/**
	 * 获取特定配置的信息
	 * 
	 * @param id
	 *            配置编号
	 * @return 查询结果
	 */
	Customization getCustomizationInfo();
}
