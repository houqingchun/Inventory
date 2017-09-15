package com.ivt.mis.service;

import java.util.Vector;

import com.ivt.mis.model.PurchaseOrder;

public interface PurchaseOrderService {

	/**
	 * 向数据库中添加新采购单
	 * 
	 * @param purchaseOrder
	 *            封装好的purchaseOrder对象
	 * @return 执行结果
	 */
	boolean savePurchaseOrder(PurchaseOrder purchaseOrder);

	/**
	 * 从数据库中删除采购单
	 * 
	 * @param id
	 *            被删除采购单的编号
	 * @return 执行结果
	 */
	boolean removePurchaseOrder(String id);

	/**
	 * 查询数据库中满足条件的采购单
	 * 
	 * @param field
	 *            查询的字段
	 * @param value
	 *            满足的值
	 * @return 查询结果
	 */
	Vector<PurchaseOrder> retrievePurchaseOrders(PurchaseOrder purchaseOrder);

	/**
	 * 更新采购单信息
	 * 
	 * @param purchaseOrder
	 * @return
	 */
	public boolean modifyPurchaseOrder(PurchaseOrder purchaseOrder);



	/**
	 * 判断采购单是否存在
	 * 
	 * @param id
	 *            查询的采购单型号
	 * @return 查询结果
	 */
	boolean isExited(String id);

	/**
	 * 获取采购单的信息
	 * 
	 * @param id
	 *            被查询的采购单的编号
	 * @return 查询结果
	 */
	PurchaseOrder getPurchaseOrderInfo(String id);
}
