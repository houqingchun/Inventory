package com.ivt.mis.service;

import java.util.Vector;

import com.ivt.mis.model.DataVersion;

public interface DataVersionService {
	/**
	 * 向数据库中添加新数据版本
	 * 
	 * @param dataVersion
	 *            封装好的数据版本
	 * @return 执行结果
	 */
	boolean saveDataVersion(DataVersion dataVersion);

	/**
	 * 从数据库中删除指定数据版本的信息
	 * 
	 * @param id
	 *            被删除数据版本的编号
	 * @return 执行结果
	 */
	boolean removeDataVersion(String id);

	/**
	 * 获取所有数据版本信息
	 * 
	 * @return 数据版本集合
	 */
	Vector<DataVersion> retrieveAllDataVersions();

	/**
	 * 获取特定数据版本的信息
	 * 
	 * @param id
	 *            数据版本编号
	 * @return 查询结果
	 */
	DataVersion getDataVersion(String id);
}
