/**
 * 管理员类DAO
 */
package com.ivt.mis.dao;

import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ivt.mis.model.User;

public class UserDAO extends BaseDAO {
	public static final Logger logger = Logger.getLogger(UserDAO.class);

	public UserDAO() {
		super();
	}

	/**
	 * 查询用户名和密码是否正确
	 * 
	 * @param loginname
	 *            用户名
	 * @param password
	 *            密码
	 * @return 查询结果
	 */
	public boolean loginCheck(String loginname, String password) {
		HashMap map = new HashMap();
		map.put("loginName", loginname);
		map.put("password", password);
		return this.isExistObj("loginCheck", map);
	}

	/**
	 * 向数据库中田间新的操作员
	 * 
	 * @param user
	 *            封装好的操作员
	 * @return 执行结果
	 */
	public boolean addUser(User user) {
		return this.addObj("addUser", user);
	}

	/**
	 * 删除操作员信息
	 * 
	 * @param loginname
	 *            被删除的操作员名称
	 * @return 执行结果
	 */
	public boolean deleteUser(String loginname) {
		// User opt = new User();
		// opt.setLoginName(loginname);
		return this.deleteObj("deleteUser", loginname);
	}

	/**
	 * 修改密码
	 * 
	 * @param loginname
	 *            被修改的用户名
	 * @param password
	 *            新密码
	 * @return 执行结果
	 */
	public boolean updatePassword(String loginname, String password) {
		HashMap map = new HashMap();
		map.put("loginName", loginname);
		map.put("password", password);
		return this.updateObj("updatePassword", map);
	}

	/**
	 * 获取操作员的密码
	 * 
	 * @param loginname
	 *            用户名
	 * @return 密码
	 */
	public String findPassword(String loginname) {

		User opt = (User) this.getObj("findUserById", loginname);
		return opt.getPassword();
	}

	/**
	 * 获取用户权限
	 * 
	 * @param loginname
	 *            用户名
	 * @return 查询结果
	 */
	public String findPowerById(String loginname) {
		User opt = (User) this.getObj("findUserById", loginname);
		return opt.getPower();
	}

	/**
	 * 判断操作员是否存在
	 * 
	 * @param id
	 *            查询的操作员昵称
	 * @return 查询结果
	 */
	public boolean isExited(String loginname) {
		return this.isExistObj("countUserByLoginName", loginname);
	}

	/**
	 * 获取特定职权的用户名
	 * 
	 * @param type
	 *            类型
	 * @return 结果集合
	 */
	public Vector<User> searchUserByType(String type) {
		return this.getAllObjs("searchUserByType", type);
	}

	/**
	 * 获取用户的信息
	 * 
	 * @param loginName
	 *            用户名
	 * @return 用户信息
	 */
	public User findUserInfoById(String loginName) {
		return (User) this.getObj("findUserById", loginName);
	}
}
