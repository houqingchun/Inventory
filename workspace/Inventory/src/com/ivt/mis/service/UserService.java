/**
 * 操作员服务接口
 */
package com.ivt.mis.service;

import java.util.Vector;

import com.ivt.mis.model.User;

public interface UserService
{
	/**
	 * 查询用户名和密码是否正确
	 * @param loginname 用户名
	 * @param password 密码
	 * @return 查询结果
	 */
	boolean loginCheck(String loginname,String password);
	/**
	 * 向数据库中田间新的操作员
	 * @param user 封装好的操作员
	 * @return 执行结果
	 */
	boolean saveUser(User user);
	/**
	 * 删除操作员信息
	 * @param loginname 被删除的操作员名称 
	 * @return 执行结果
	 */
	boolean removeUser(String loginname);
	/**
	 * 修改密码
	 * @param loginname 被修改的用户名
	 * @param password 新密码
	 * @return 执行结果
	 */
	boolean modifyPassword(String loginname,String password);
	/**
	 * 获取用户权限
	 * @param loginname 用户名
	 * @return 查询结果
	 */
	String getPower(String loginname);
	/**
	 * 获取操作员的密码
	 * @param loginname 用户名
	 * @return 密码
	 */
	String getPassword(String loginname);
	/**
     * 判断操作员是否存在
     * @param id 查询的操作员昵称
     * @return 查询结果
     */
    boolean isExited(String loginname);
    /**
     * 获取特定职权的用户名
     * @param type 类型
     * @return 结果集合
     */
    Vector<User> retrieveUsers(String type);
    /**
     * 获取用户的信息
     * @param loginName 用户名
     * @return  用户信息
     */
    User getUserInfo(String loginName);
}
