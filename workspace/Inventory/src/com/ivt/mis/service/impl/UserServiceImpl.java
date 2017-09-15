/**
 * 操作员服务类
 */
package com.ivt.mis.service.impl;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ivt.mis.common.BasicTypeUtils;
import com.ivt.mis.dao.UserDAO;
import com.ivt.mis.model.User;
import com.ivt.mis.service.UserService;

public class UserServiceImpl implements UserService {
	private UserDAO userDAO;

	public UserServiceImpl() {
		super();
	}

	public boolean saveUser(User user) {
		user.setCreateTime(BasicTypeUtils.getLongFmtDate());
		return userDAO.addUser(user);
	}

	public boolean removeUser(String loginname) {

		return userDAO.deleteUser(loginname);
	}

	public boolean loginCheck(String loginname, String password) {
		//
		return userDAO.loginCheck(loginname, password);
	}

	public boolean modifyPassword(String loginname, String password) {

		return userDAO.updatePassword(loginname, password);
	}

	public String getPower(String loginname) {
		return userDAO.findPowerById(loginname);
	}

	public String getPassword(String loginname) {

		return userDAO.findPassword(loginname);
	}

	public boolean isExited(String loginname) {

		return userDAO.isExited(loginname);
	}

	public Vector<User> retrieveUsers(String type) {

		return userDAO.searchUserByType(type);
	}

	public User getUserInfo(String loginName) {

		return userDAO.findUserInfoById(loginName);
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
