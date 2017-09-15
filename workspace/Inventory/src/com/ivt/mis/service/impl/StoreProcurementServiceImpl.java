/**
 * 进货服务类
 */
package com.ivt.mis.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ivt.mis.common.BasicTypeUtils;
import com.ivt.mis.common.Constants;
import com.ivt.mis.dao.CodeRuleDAO;
import com.ivt.mis.dao.StoreManageDAO;
import com.ivt.mis.dao.StoreProcurementDAO;
import com.ivt.mis.model.StoreManage;
import com.ivt.mis.model.StoreProcurement;
import com.ivt.mis.service.CodeRuleService;
import com.ivt.mis.service.StoreProcurementService;
import com.ivt.mis.view.BaseJInternalFrame;

public class StoreProcurementServiceImpl implements StoreProcurementService {

	StoreProcurementDAO storeProcurementDAO;

	StoreManageDAO storeManageDAO;
	
	CodeRuleService codeRuleService;
	public static final Logger logger = Logger
	.getLogger(StoreProcurementServiceImpl.class);

	public StoreProcurementServiceImpl() {
		super();
	}

	public boolean saveStoreProcurement(StoreProcurement storeProcurement) {

		boolean result = false;
//		result = storeProcurementDAO.addStoreProcurement(storeProcurement);
//		// 修改库存量
//		StoreManage storeManage = new StoreManage();
//		storeManage.setId(BasicTypeUtils.getPremaryKeyID("SM"));
//		storeManage.setProductBrand(storeProcurement.getProductBrand());
//		storeManage.setProductCode(storeProcurement.getProductCode());
//		storeManage.setProductId(storeProcurement.getProductId());
//		storeManage.setStoreProcurementId(storeProcurement.getId());
//		storeManage.setTotalNbr(storeProcurement.getProNumber());
//		result &= storeManageDAO.addStoreManage(storeManage);
		return result;
	}

	public Vector<StoreProcurement> retrieveAllStoreProcurements() {

		return storeProcurementDAO.searchAllStoreProcurements();
	}

	public Vector<StoreProcurement> retrieveStoreProcurements(
			StoreProcurement storeProcurement) {
		return storeProcurementDAO.searchStoreProcurementsByProperties(storeProcurement);
	}

	public boolean isExited(String id) {

		return storeProcurementDAO.isExited(id);
	}

	@Override
	public boolean modifyStoreProcurement(StoreProcurement storeProcurement) {
		// storeProcurementDAO=new StoreProcurementDAO();
		// boolean result=false;
		// result=storeProcurementDAO.modifyStoreProcurement(storeProcurement);
		// //修改库存量
		// ProductServiceImpl productServiceImpl=new ProductServiceImpl();
		// productServiceImpl.changeProductNumber(storeProcurement.getProductId(),
		// storeProcurement.getNumber());
		// return result;
		return false;
	}

	@Override
	public boolean saveBatchStoreProcurement(
			List<StoreProcurement> storeProcurements) {

		boolean flag = storeProcurementDAO
				.addBatchStoreProcurement(storeProcurements);
		if (flag) {
			List<StoreManage> storeManages = new ArrayList<StoreManage>();
			List tmpId = this.codeRuleService.getNextObjectCodes(Constants.MODULE_STORE_MANAGE, storeProcurements.size());
			for (int i = 0; i < storeProcurements.size(); i++) {
				logger.debug("Store Manage Id:" + tmpId.get(i));
				StoreProcurement storeProcurement = storeProcurements.get(i);
				StoreManage storeManage = new StoreManage();
				storeManage.setId(tmpId.get(i).toString());
				storeManage.setProductBrand(storeProcurement.getProductBrand());
				storeManage.setProductCode(storeProcurement.getProductCode());
				storeManage.setProductId(storeProcurement.getProductId());
				storeManage.setStoreProcurementId(storeProcurement.getId());
				storeManage.setTotalNbr(storeProcurement.getProNumber());
				storeManages.add(storeManage);
			}
			flag &= storeManageDAO.addBatchStoreManage(storeManages);


			this.codeRuleService.refreshOjbectCode(Constants.MODULE_STORE_MANAGE, storeManages.get(storeManages.size() - 1).getId());
		}
		
		//更新最后一个编码到编码规则内
		this.codeRuleService.refreshOjbectCode(Constants.MODULE_PROCUREMENT, storeProcurements.get(storeProcurements.size() - 1).getId());
		this.codeRuleService.refreshOjbectCode(Constants.MODULE_PROCUREMENT_BATCH, storeProcurements.get(storeProcurements.size() - 1).getGroupId());

		return flag;
	}

	@Override
	public boolean modifyBatchStoreProcurement(
			List<StoreProcurement> storeProcurements) {

		boolean flag = storeProcurementDAO
				.updateBatchStoreProcurement(storeProcurements);

		if (flag) {
			List<StoreManage> storeManages = new ArrayList<StoreManage>();
			for (int i = 0; i < storeProcurements.size(); i++) {
				StoreProcurement storeProcurement = storeProcurements.get(i);
				StoreManage storeManage = new StoreManage();
				storeManage.setId(this.codeRuleService.getNextObjectCode(Constants.MODULE_STORE_MANAGE));
				storeManage.setProductBrand(storeProcurement.getProductBrand());
				storeManage.setProductCode(storeProcurement.getProductCode());
				storeManage.setProductId(storeProcurement.getProductId());
				storeManage.setStoreProcurementId(storeProcurement.getId());
				storeManage.setTotalNbr(storeProcurement.getProNumber());
				storeManages.add(storeManage);
			}
			flag &= storeManageDAO.updateBatchStoreManage(storeManages, true);
		}

		return flag;
	}

	@Override
	public StoreProcurement getStoreProcurement(String id) {
		// TODO Auto-generated method stub
		return storeProcurementDAO.findStoreProcurementById(id);
	}

	public void setStoreProcurementDAO(StoreProcurementDAO storeProcurementDAO) {
		this.storeProcurementDAO = storeProcurementDAO;
	}

	public void setStoreManageDAO(StoreManageDAO storeManageDAO) {
		this.storeManageDAO = storeManageDAO;
	}

	public void setCodeRuleService(CodeRuleService codeRuleService) {
		this.codeRuleService = codeRuleService;
	}

}
