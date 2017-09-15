package com.ivt.mis.dao;

import java.util.Vector;

import org.apache.log4j.Logger;

import com.ivt.mis.model.DataVersion;

public class DataVersionDAO extends BaseDAO {
	public static final Logger logger = Logger.getLogger(DataVersionDAO.class);

	public DataVersionDAO() {
		super();
	}
	
	/**
	 * 
	 * 向数据库中添加新数据版本
	 * 
	 * @param dataVersion
	 *            封装好的数据版本
	 * @return 执行结果
	 */
	public boolean addDataVersion(DataVersion dataVersion) {
		return this.addObj("addDataVersion", dataVersion);
	}

	/**
	 * 从数据库中删除指定数据版本的信息，由于存在外键关系，此处仅设置表中 available=0
	 * 
	 * @param id
	 *            被删除数据版本的编号
	 * @return 执行结果
	 */
	public boolean deleteDataVersion(String id) {
		return this.deleteObj("deleteDataVersionById", id);
	}


	/**
	 * 显示所有的数据版本
	 * 
	 * @return 数据版本集合
	 */
	public Vector<DataVersion> searchAllDataVersions() {
		return this.getAllObjs("searchAllDataVersions");
	}
	
	/**
	 * 显示所有的数据版本
	 * 
	 * @return 数据版本集合
	 */
	public DataVersion findDataVersionById(String id) {
		return (DataVersion) this.getObj("findDataVersionById", id);
	}
}
