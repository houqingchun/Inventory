/**
 * 销售服务类
 */
package com.ivt.mis.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.ivt.mis.common.Constants;
import com.ivt.mis.dao.StoreManageDAO;
import com.ivt.mis.dao.StoreShipmentDAO;
import com.ivt.mis.model.StoreManage;
import com.ivt.mis.model.StoreShipment;
import com.ivt.mis.service.CodeRuleService;
import com.ivt.mis.service.StoreShipmentService;

public class StoreShipmentServiceImpl implements StoreShipmentService {
	StoreShipmentDAO storeShipmentDAO;

	StoreManageDAO storeManageDAO;

	CodeRuleService codeRuleService;

	public void setCodeRuleService(CodeRuleService codeRuleService) {
		this.codeRuleService = codeRuleService;
	}

	public StoreShipmentServiceImpl() {
		super();
	}

	public boolean saveStoreShipment(StoreShipment storeShipment) {

		boolean result = true;
//		result = storeShipmentDAO.addStoreShipment(storeShipment);
//		// 修改库存量
//		StoreManageService storeManageService = new StoreManageServiceImpl();
//		storeManageService.modifyStoreTotalQty(
//				storeShipment.getStoreProcurementId(),
//				-storeShipment.getShipNumber());
		return result;
	}

	public Vector<StoreShipment> retrieveAllStoreShipments() {

		return storeShipmentDAO.searchAllStoreShipment();
	}

	public boolean isExited(String id) {

		return storeShipmentDAO.isExited(id);
	}

	@Override
	public boolean saveBatchStoreShipment(List<StoreShipment> storeShipments) {
		boolean flag = storeShipmentDAO.addBatchStoreShipment(storeShipments);
		if (flag) {
			List<StoreManage> storeManages = new ArrayList<StoreManage>();
			for (int i = 0; i < storeShipments.size(); i++) {
				StoreShipment storeShipment = storeShipments.get(i);
				StoreManage storeManage = new StoreManage();
				storeManage.setStoreProcurementId(storeShipment.getStoreProcurementId());;
				storeManage.setTotalNbr(-storeShipment.getShipNumber());
				storeManages.add(storeManage);
			}
			flag &= storeManageDAO.updateBatchStoreManage(storeManages, false);
		}
		

		//更新最后一个编码到编码规则内
		this.codeRuleService.refreshOjbectCode(Constants.MODULE_SHIPMENT, storeShipments.get(storeShipments.size() - 1).getId());
		this.codeRuleService.refreshOjbectCode(Constants.MODULE_SHIPMENT_BATCH, storeShipments.get(storeShipments.size() - 1).getGroupId());

		return flag;
	}

	@Override
	public boolean modifyBatchStoreShipment(List<StoreShipment> storeShipments) {
		//获取DB中原有出货量，以便根据差额更新库存
		StoreShipment params = new StoreShipment();
		params.setGroupId(storeShipments.get(0).getGroupId());
		List<StoreShipment> oldStoreShipments = storeShipmentDAO.searchStoreShipmentByProperties(params);
		
		boolean flag = storeShipmentDAO.updateBatchStoreShipment(storeShipments);
		
		
		if (flag) {
			List<StoreManage> storeManages = new ArrayList<StoreManage>();
			for (int i = 0; i < storeShipments.size(); i++) {
				StoreShipment storeShipment = storeShipments.get(i);
				StoreManage storeManage = new StoreManage();
				storeManage.setStoreProcurementId(storeShipment.getStoreProcurementId());
				
				//根据原始数额计算库存差额
				for (int j = 0; j < oldStoreShipments.size(); j++){
					StoreShipment tmp = oldStoreShipments.get(j);
					if (tmp.getId().equals(storeShipment.getId())){
						storeManage.setTotalNbr(-(storeShipment.getShipNumber() - tmp.getShipNumber()));
						break;
					}
				}
				
				storeManages.add(storeManage);
			}
			flag &= storeManageDAO.updateBatchStoreManage(storeManages, false);
		}

		return flag;
	}

	@Override
	public Vector<StoreShipment> retrieveStoreShipments(StoreShipment storeShipment) {
		return storeShipmentDAO.searchStoreShipmentByProperties(storeShipment);
	}

	@Override
	public boolean isExitedInStoreShipment(String storeProcurementId) {
		return storeShipmentDAO.isExitedInStoreShipment(storeProcurementId);
	}

	@Override
	public StoreShipment getStoreShipment(String id) {
		return storeShipmentDAO.findStoreShipmentById(id);
	}

	public void setStoreShipmentDAO(StoreShipmentDAO storeShipmentDAO) {
		this.storeShipmentDAO = storeShipmentDAO;
	}

	public void setStoreManageDAO(StoreManageDAO storeManageDAO) {
		this.storeManageDAO = storeManageDAO;
	}
}
