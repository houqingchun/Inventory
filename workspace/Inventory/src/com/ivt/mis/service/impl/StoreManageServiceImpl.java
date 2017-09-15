package com.ivt.mis.service.impl;

import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ivt.mis.common.Constants;
import com.ivt.mis.dao.StoreManageDAO;
import com.ivt.mis.dao.StoreProcurementDAO;
import com.ivt.mis.model.PageUtil;
import com.ivt.mis.model.StoreManage;
import com.ivt.mis.service.StoreManageService;


public class StoreManageServiceImpl implements StoreManageService {
	StoreManageDAO storeManageDAO;

	public StoreManageServiceImpl() {
		super();
	}
	
	public Vector<StoreManage> retrieveAllStoreManages() {
		

		return storeManageDAO.searchAllStoreManages();
	}

	@Override
	public Vector<StoreManage> retrieveAllStoreManageByProperties(
			StoreManage storeManage) {
		return storeManageDAO.searchStoreManagesByProperties(storeManage);
	}

	@Override
	public boolean modifyStoreTotalQty(String storeProcurementId, int number) {
		

		return storeManageDAO.updateStoreTotalQty(storeProcurementId, number);
	}

	@Override
	public boolean modifyAndResetStoreTotalQty(String storeProcurementId, int number) {
		

		return storeManageDAO.resetStoreTotalQty(storeProcurementId, number);
	}

	@Override
	public boolean removeStoreManage() {
		

		return storeManageDAO.cleanStoreManager();
	}

	@Override
	public boolean saveBatchStoreManage(List<StoreManage> storeManages) {
		

		return storeManageDAO.addBatchStoreManage(storeManages);
	}

	@Override
	public boolean modifyBatchStoreManage(List<StoreManage> storeManages, boolean resetFlag) {
		

		return storeManageDAO.updateBatchStoreManage(storeManages, resetFlag);
	}

	@Override
	public boolean saveStoreManage(StoreManage storeManage) {
		

		return storeManageDAO.addStoreManage(storeManage);
	}

	@Override
	public int getRemainingStoreQty(String storeProcurementId) {
		StoreManage storeManage = new StoreManage();
		storeManage.setStoreProcurementId(storeProcurementId);
		Vector<StoreManage> storeManages = storeManageDAO.searchStoreManagesByProperties(storeManage);
		int total = 0;
		for (int i = 0; i < storeManages.size(); i++){
			StoreManage tmp = storeManages.get(i);
			total += tmp.getTotalNbr();
		}
		
		return total;
	}

	public void setStoreManageDAO(StoreManageDAO storeManageDAO) {
		this.storeManageDAO = storeManageDAO;
	}

}
