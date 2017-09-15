/**
 * 管理员类
 */

package com.ivt.mis.model;

public class User extends BasePOJO
{
	private String loginName;  //用户名
	private String password;  //密码
	
	private String name;  //姓名
	private String power;  //权限
	private int available; //状态
	private String createTime; //创建时间
	private String passwordConfirm;  //临时变量 密码确认
	private String passwordOld;  //临时变量 旧密码
	
	/**
	 * 列表显示栏位标记 String[]{"显示名称","属性名","是否显示","是否必填"}
	 */
    String[][] columns = new String[][] {
			new String[] { "登录名", "loginName", "Y" ,"Y"},
			new String[] { "密码", "password", "Y" ,"Y"},
			new String[] { "姓名", "name", "Y" ,"Y"},
			new String[] { "权限", "power", "Y" ,"Y"},
			new String[] { "状态", "available", "Y" ,"Y"},
			new String[] { "创建时间", "createTime", "Y" ,"Y"},
			new String[] { "确认密码", "passwordConfirm", "Y" ,"Y"},
			new String[] { "旧密码", "passwordOld", "Y" ,"Y"}};
	
	public User()
	{
		super();
		super.columns = columns;
	}
	
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getLoginName()
	{
		return loginName;
	}
	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getPower()
	{
		return power;
	}
	public void setPower(String power)
	{
		this.power = power;
	}
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	public String getPasswordOld() {
		return passwordOld;
	}
	public void setPasswordOld(String passwordOld) {
		this.passwordOld = passwordOld;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}
}
