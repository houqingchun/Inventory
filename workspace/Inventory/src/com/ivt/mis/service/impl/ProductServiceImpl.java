/**
 * 产品服务类
 */
package com.ivt.mis.service.impl;

import java.util.List;
import java.util.Vector;

import com.ivt.mis.common.BasicTypeUtils;
import com.ivt.mis.common.Constants;
import com.ivt.mis.dao.ProductDAO;
import com.ivt.mis.dao.StoreManageDAO;
import com.ivt.mis.dao.StoreProcurementDAO;
import com.ivt.mis.dao.StoreShipmentDAO;
import com.ivt.mis.model.Product;
import com.ivt.mis.service.CodeRuleService;
import com.ivt.mis.service.ProductService;

public class ProductServiceImpl implements ProductService {

	ProductDAO productDAO;
	StoreManageDAO storeManageDAO; 
	StoreProcurementDAO storeProcurementDAO;
	StoreShipmentDAO storeShipmentDAO;
	CodeRuleService codeRuleService;

	public void setCodeRuleService(CodeRuleService codeRuleService) {
		this.codeRuleService = codeRuleService;
	}

	public ProductServiceImpl() {
		super();
	}

	public boolean saveProduct(Product product) {
		this.codeRuleService.refreshOjbectCode(Constants.MODULE_PRODUCT, product.getId());
		product.setCreateTime(BasicTypeUtils.getLongFmtDate());
		return productDAO.addProduct(product);
	}

	public boolean removeProduct(String id) {

		return productDAO.deleteProduct(id);
	}

	public Vector<Product> retrieveProducts(Product product) {
		return productDAO.searchProductsByProperties(product);
	}

	public Vector<Product> retrieveAllProducts() {

		return productDAO.searchAllProducts();
	}

	public boolean isExited(String id) {

		return productDAO.isExited(id);
	}

	public Product getProductInfo(String id) {

		return productDAO.findProductById(id);
	}

	@Override
	public boolean modifyProduct(Product product) {
		boolean flag = productDAO.updateProduct(product);
		flag &= storeManageDAO.updateProductInfoForStoreManager(product);
		flag &= storeProcurementDAO.updateProductInfoForProcurement(product);
		flag &= storeShipmentDAO.updateProductInfoForShipment(product);
		
		return flag;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	@Override
	public Vector<String> retrieveAllProductCodes() {
		// TODO Auto-generated method stub
		return productDAO.searchAllProductCodes();
	}

	public void setStoreManageDAO(StoreManageDAO storeManageDAO) {
		this.storeManageDAO = storeManageDAO;
	}

	public void setStoreProcurementDAO(StoreProcurementDAO storeProcurementDAO) {
		this.storeProcurementDAO = storeProcurementDAO;
	}

	public void setStoreShipmentDAO(StoreShipmentDAO storeShipmentDAO) {
		this.storeShipmentDAO = storeShipmentDAO;
	}

	@Override
	public boolean saveBatchProducts(List<Product> products) {
		String maxId = "";
		for (Product product:products){
			maxId = maxId.compareTo(product.getId()) < 0? product.getId():maxId;
			product.setCreateTime(BasicTypeUtils.getLongFmtDate());
		}
		this.codeRuleService.refreshOjbectCode(Constants.MODULE_PRODUCT, maxId);
		return productDAO.addBatchProduct(products);
	}
}
