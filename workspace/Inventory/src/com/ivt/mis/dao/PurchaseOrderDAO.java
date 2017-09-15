package com.ivt.mis.dao;

import java.util.Vector;

import org.apache.log4j.Logger;

import com.ivt.mis.model.PurchaseOrder;

public class PurchaseOrderDAO extends BaseDAO {
	public static final Logger logger = Logger
			.getLogger(PurchaseOrderDAO.class);

	public PurchaseOrderDAO() {
		super();
	}

	/**
	 * 向数据库中添加采购单
	 * 
	 * @param purchaseOrder
	 *            封装好的PurchaseOrder对象
	 * @return 执行结果
	 */
	public boolean addPurchaseOrder(PurchaseOrder purchaseOrder) {
		boolean flag = this.addObj("addPurchaseOrder", purchaseOrder);
		flag &= this.addBatchObj("addPurchaseOrderItem",
				purchaseOrder.getOrderItems());
		return flag;
	}

	/**
	 * 删除数据库中的采购单，由于存在外键关系，此处仅设置表中 available=0
	 * 
	 * @param id
	 *            被删除采购单的编号
	 * @return 执行结果
	 */
	public boolean deletePurchaseOrder(String id) {
		return this.deleteObj("deletePurchaseOrderById", id);
	}

	/**
	 * 查询数据库中满足条件的采购单
	 * 
	 * @param field
	 *            查询的字段
	 * @param value
	 *            满足的值
	 * @return 查询结果
	 */
	public Vector<PurchaseOrder> searchPurchaseOrderByProperties(
			PurchaseOrder purchaseOrder) {
		int totalObjs = this.countObjs("searchPurchaseOrderByPropertiesCount",
				purchaseOrder);
		purchaseOrder.getPage().setRecordCount(totalObjs);
		return this
				.getAllObjs("searchPurchaseOrderByProperties", purchaseOrder);
	}

	/**
	 * 更新采购单信息
	 * 
	 * @param purchaseOrder
	 * @return
	 */
	public boolean updatePurchaseOrder(PurchaseOrder purchaseOrder) {
		boolean flag = this.updateObj("updatePurchaseOrder", purchaseOrder);
		flag &= this.updateBatchObj("updatePurchaseOrderItem",
				purchaseOrder.getOrderItems());

		return flag;
	}

	/**
	 * 获取所有的采购单
	 * 
	 * @return 采购单的集合
	 */
	public Vector<PurchaseOrder> searchAllPurchaseOrders() {
		return this.getAllObjs("searchAllPurchaseOrders");
	}

	/**
	 * 判断采购单是否存在
	 * 
	 * @param id
	 *            查询的采购单型号
	 * @return 查询结果
	 */
	public boolean isExited(String id) {
		return this.isExistObj("countPurchaseOrderById", id);
	}

	/**
	 * 获取采购单的信息
	 * 
	 * @param id
	 *            被查询的采购单的编号
	 * @return 查询结果
	 */
	public PurchaseOrder findPurchaseOrderById(String id) {
		PurchaseOrder p = (PurchaseOrder) this.getObj("findPurchaseOrderById",
				id);
		p.setOrderItems(this.getAllObjs("findPurchaseOrderItemByOrderId",
				p.getId()));
		return p;
	}
}
