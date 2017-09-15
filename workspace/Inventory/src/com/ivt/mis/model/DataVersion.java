package com.ivt.mis.model;

public class DataVersion extends BasePOJO {
	private String dataPath; //备份或还原文件绝对位置
	private String dataType; //备份或还原
	private String comments; //备注
	private String createTime; //创建时间

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
}
