package com.ivt.mis.service.impl;

import java.util.Vector;

import org.apache.log4j.Logger;

import com.ivt.mis.dao.PurchaseOrderDAO;
import com.ivt.mis.model.PurchaseOrder;
import com.ivt.mis.service.PurchaseOrderService;

public class PurchaseOrderServiceImpl implements PurchaseOrderService {
	public static final Logger logger = Logger
			.getLogger(PurchaseOrderServiceImpl.class);
	PurchaseOrderDAO purchaseOrderDAO;
	@Override
	public boolean savePurchaseOrder(PurchaseOrder purchaseOrder) {
		return purchaseOrderDAO.addPurchaseOrder(purchaseOrder);
	}

	@Override
	public boolean removePurchaseOrder(String id) {
		return purchaseOrderDAO.deletePurchaseOrder(id);
	}

	@Override
	public Vector<PurchaseOrder> retrievePurchaseOrders(
			PurchaseOrder purchaseOrder) {
		return purchaseOrderDAO.searchPurchaseOrderByProperties(purchaseOrder);
	}

	@Override
	public boolean modifyPurchaseOrder(PurchaseOrder purchaseOrder) {
		return purchaseOrderDAO.updatePurchaseOrder(purchaseOrder);
	}

	@Override
	public boolean isExited(String id) {
		return purchaseOrderDAO.isExited(id);
	}

	@Override
	public PurchaseOrder getPurchaseOrderInfo(String id) {
		return purchaseOrderDAO.findPurchaseOrderById(id);
	}

	public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
		this.purchaseOrderDAO = purchaseOrderDAO;
	}
}
