/**
 * 供应商类
 */

package com.ivt.mis.model;

import com.ivt.mis.common.ObjectReflectUtil;
import com.ivt.mis.common.ValidationManager;

public class Provider extends BasePOJO {
	public Provider() {
		super();
		super.columns = columns;
	}
	private String id; //供应商编号
	private String name; // 全称
	private String zip; // 邮编
	private String address; // 公司地址
	private String shipAddress; // 收货地址
	private String telephone; // 移动电话
	private String fax; // 公司传真
	private String contacts; // 联系人
	private String mobile; // 联系手机
	private String email; // 联系人邮箱
	private String bank; // 开户银行
	private String account; // 开户账号
	private int available; // 状态 非0代表可用
	private String registryPlace; // 注册地
	private String registryNumber; // 注册号
	private String registryType; // 注册类型
	private String ownerId; // 创建人
	private String levelState;// 级别
	private String createTime; //创建时间
	

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	private String index;// UI显示 临时使用
	
	/**
	 * 列表显示栏位标记 String[]{"显示名称","属性名","是否显示","是否必填"}
	 */
	private String[][] columns = new String[][] {
			new String[] { "序号", "index", "N", "N"},
			new String[] { "供应商编号", "id", "Y", "Y"},
			new String[] { "供应商名称", "name", "Y", "Y"},
			new String[] { "联系人", "contacts", "Y", "Y"},
			new String[] { "联系电话", "telephone", "Y", "Y"},
			new String[] { "移动电话", "mobile", "Y", "N"},
			new String[] { "传真号码", "fax", "Y", "N"},
			new String[] { "电子邮箱", "email", "Y", "N"},
			new String[] { "联系地址", "address", "Y", "N"},
			new String[] { "收货地址", "shipAddress", "Y", "N"},
			new String[] { "邮政编码", "zip", "Y", "N"},
			new String[] { "开户银行", "bank", "Y", "N"},
			new String[] { "开户帐号", "account", "Y", "N"},
			new String[] { "注册地", "registryPlace", "Y", "N"},
			new String[] { "注册号", "registryNumber", "Y", "N"},
			new String[] { "注册类型", "registryType", "Y", "N"},
			new String[] { "供应商级别", "levelState", "Y", "N"}};
	
	/**
	 * 表格控件调用
	 * 
	 * @param columnNumber
	 *            列号
	 * @return 对应列的值
	 */
	public Object getValueAt(String columnName) {
		return ObjectReflectUtil.getFieldValue(this, columnName);
	}

	/**
	 * 表格控件调用
	 * 
	 * @param columnNumber
	 *            列号
	 * @return 对应列的值
	 */
	public void setValueAt(Object value, String columnName) {
		ObjectReflectUtil.setFieldValue(this, columnName, value);
	}
	
	public String getRegistryPlace() {
		return registryPlace;
	}

	public void setRegistryPlace(String registryPlace) {
		this.registryPlace = registryPlace;
	}

	public String getRegistryNumber() {
		return registryNumber;
	}

	public void setRegistryNumber(String registryNumber) {
		this.registryNumber = registryNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	public int getAvailable() {
		return available;
	}

	public String getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}

	public String getRegistryType() {
		return registryType;
	}

	public void setRegistryType(String registryType) {
		this.registryType = registryType;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getLevelState() {
		return levelState;
	}

	public void setLevelState(String levelState) {
		this.levelState = levelState;
	}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		public String[][] getColumns() {
			return columns;
		}
}
