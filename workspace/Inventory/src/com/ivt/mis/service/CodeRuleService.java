/**
 *用户操作接口，定义对用户进行的操作 
 */

package com.ivt.mis.service;

import java.util.List;
import java.util.Vector;

import com.ivt.mis.model.CodeRule;

public interface CodeRuleService
{
	/**
	 * 向数据库中添加新编码规则
	 * @param codeRule 封装好的编码规则
	 * @return 执行结果
	 */
	boolean saveCodeRule(CodeRule codeRule);

	/**
	 * 从数据库中删除指定编码规则的信息
	 * @param id 被删除编码规则的编号
	 * @return 执行结果
	 */
	boolean removeCodeRule(String id);
	/**
	 * 查询数据库中满足条件的编码规则
	 * @param field 查询的字段
	 * @param value 满足的值
	 * @return 查询结果
	 */
	Vector<CodeRule> retrieveCodeRules(CodeRule codeRule);
		
	/**
	 * 更新编码规则信息
	 * @param codeRule 封装好的编码规则新信息
	 */
	boolean modifyCodeRule(CodeRule codeRule);
	
	/**
     * 判断编码规则是否存在
     * @param objectType 对象类型
     * @return 查询结果
     */
    boolean isExited(String objectType);
    /**
     * 获取特定编码规则的信息
     * @param id 编码规则编号
     * @return 查询结果
     */
    CodeRule getCodeRuleInfo(String id);
	
    /**
     * 获取特定编码规则的信息
     * @param objectType 编码规则编号
     * @return 查询结果
     */
    CodeRule getCodeRuleInfoByObjectType(String objectType);
    
    /**
     * 根据对象类型获取下一个编码
     * @param objectType
     * @return
     */
    String getNextObjectCode(String objectType);
    
    /**
     * 根据对象类型获取下一个编码
     * @param objectType
     * @return
     */
    List getNextObjectCodes(String objectType, int count);
    
    /**
     * 根据对象类型和当前对象编码，刷新对象最新编码
     * @param objectType
     * @param currentCode
     * @return
     */
    boolean refreshOjbectCode(String objectType, String currentCode);
}
