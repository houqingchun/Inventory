package com.ivt.mis.service.impl;

import java.util.Vector;

import com.ivt.mis.dao.DataVersionDAO;
import com.ivt.mis.model.DataVersion;
import com.ivt.mis.service.DataVersionService;

public class DataVersionServiceImpl implements DataVersionService {
	DataVersionDAO dataVersionDAO;

	@Override
	public boolean saveDataVersion(DataVersion dataVersion) {
		return dataVersionDAO.addDataVersion(dataVersion);
	}

	@Override
	public boolean removeDataVersion(String id) {
		return dataVersionDAO.deleteDataVersion(id);
	}

	@Override
	public Vector<DataVersion> retrieveAllDataVersions() {
		return dataVersionDAO.searchAllDataVersions();
	}

	@Override
	public DataVersion getDataVersion(String id) {
		return dataVersionDAO.findDataVersionById(id);
	}

	public void setDataVersionDAO(DataVersionDAO dataVersionDAO) {
		this.dataVersionDAO = dataVersionDAO;
	}

}
