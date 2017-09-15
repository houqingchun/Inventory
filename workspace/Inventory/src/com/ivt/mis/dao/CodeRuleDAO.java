/**
 * 编号规则DAO类，主要对编号规则信息进行管理
 */
package com.ivt.mis.dao;

import java.util.Vector;

import org.apache.log4j.Logger;

import com.ivt.mis.model.CodeRule;

public class CodeRuleDAO extends BaseDAO {

	public static final Logger logger = Logger.getLogger(CodeRuleDAO.class);

	public CodeRuleDAO() {
		super();
	}

	/**
	 * 
	 * 向数据库中添加新编号规则
	 * 
	 * @param codeRule
	 *            封装好的编号规则
	 * @return 执行结果
	 */
	public boolean addCodeRule(CodeRule codeRule) {
		return this.addObj("addCodeRule", codeRule);
	}
	

	/**
	 * 从数据库中删除指定编号规则的信息，由于存在外键关系，此处仅设置表中 available=0
	 * 
	 * @param id
	 *            被删除编号规则的编号
	 * @return 执行结果
	 */
	public boolean deleteCodeRule(String id) {
		return this.deleteObj("deleteCodeRuleById", id);
	}

	/**
	 * 查询数据库中满足条件的编号规则
	 * 
	 * @param field
	 *            查询的字段
	 * @param value
	 *            满足的值
	 * @return 查询结果
	 */
	public Vector<CodeRule> searchCodeRuleByProperties(CodeRule codeRule) {
		int totalObjs = this.countObjs("searchCodeRuleByPropertiesCount",codeRule);
		codeRule.getPage().setRecordCount(totalObjs);
		return this.getAllObjs("searchCodeRuleByProperties", codeRule);
	}

	/**
	 * 修改编号规则信息
	 * 
	 * @param codeRule
	 *            封装好的编号规则新信息
	 * @return 执行结果
	 */
	public boolean updateCodeRule(CodeRule codeRule) {
		return this.updateObj("updateCodeRule", codeRule);
	}
	
	/**
	 * 修改最新序号
	 * 
	 * @param codeRule
	 *            封装好的编号规则新信息
	 * @return 执行结果
	 */
	public boolean updateCurrentSeq(CodeRule codeRule) {
		return this.updateObj("updateCurrentSeq", codeRule);
	}
	
	

	/**
	 * 判断此类编码是否存在
	 * 
	 * @param id
	 *            查询的编号规则编号
	 * @return 查询结果
	 */
	public boolean isExited(String objectType) {
		if (this.isExistObj("countCodeRuleByObjectType", objectType)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 获取特定编号规则的信息
	 * 
	 * @param id
	 *            编号规则编号
	 * @return 查询结果
	 */
	public CodeRule findCodeRuleByObjectType(String objectType) {
		return (CodeRule) this.getObj("searchCodeRuleByObjectType", objectType);
	}

	/**
	 * 获取特定编号规则的信息
	 * 
	 * @param id
	 *            编号规则编号
	 * @return 查询结果
	 */
	public CodeRule findCodeRuleById(String id) {
		return (CodeRule) this.getObj("findCodeRuleById", id);
	}
}
