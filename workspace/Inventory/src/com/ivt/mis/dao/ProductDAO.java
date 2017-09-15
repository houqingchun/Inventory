/**
 * 产品DAO类，主要对产品信息进行管理
 */
package com.ivt.mis.dao;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ivt.mis.model.Product;


public class ProductDAO extends BaseDAO {
	public static final Logger logger = Logger.getLogger(ProductDAO.class);

	public ProductDAO() {
		super();
	}

	/**
	 * 向数据库中添加产品
	 * 
	 * @param product
	 *            封装好的Product对象
	 * @return 执行结果
	 */
	public boolean addProduct(Product product) {
		return this.addObj("addProduct", product);
	}
	

	/**
	 * 向数据库中添加产品
	 * 
	 * @param product
	 *            封装好的Product对象
	 * @return 执行结果
	 */
	public boolean addBatchProduct(List<Product> products) {
		return this.addBatchObj("addProduct", products);
	}
	

	/**
	 * 删除数据库中的产品，由于存在外键关系，此处仅设置表中 available=0
	 * 
	 * @param id
	 *            被删除产品的编号
	 * @return 执行结果
	 */
	public boolean deleteProduct(String id) {
		return this.deleteObj("deleteProductById", id);
	}

	/**
	 * 查询数据库中满足条件的产品
	 * 
	 * @param field
	 *            查询的字段
	 * @param value
	 *            满足的值
	 * @return 查询结果
	 */
	public Vector<Product> searchProductsByProperties(Product product) {
		int totalObjs = this.countObjs("searchProductsByPropertiesCount",product);
		product.getPage().setRecordCount(totalObjs);
		return this.getAllObjs("searchProductsByProperties", product);
	}


	/**
	 * 更新产品信息
	 * @param product
	 * @return
	 */
	public boolean updateProduct(Product product) {
		return this.updateObj("updateProduct", product);
	}

	/**
	 * 获取所有的产品
	 * 
	 * @return 产品的集合
	 */
	public Vector<Product> searchAllProducts() {
		return this.getAllObjs("searchAllProducts");
	}
	
	/**
	 * 获取所有产品型号列表
	 * 
	 * @param id
	 *            产品型号
	 * @return 查询结果
	 */
	public Vector<String> searchAllProductCodes() {
		Vector<Product> products = this.getAllObjs("searchAllProductCodes");
		Vector<String> results = new Vector<String>();
		for (int i = 0; i < products.size(); i++){
			results.add(products.get(i).getProductCode());
		}
		
		return results;
	}


	
	/**
	 * 判断产品是否存在
	 * 
	 * @param id
	 *            查询的产品型号
	 * @return 查询结果
	 */
	public boolean isExited(String id) {
		if (this.findProductById(id) != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取产品的信息
	 * 
	 * @param id
	 *            被查询的产品的编号
	 * @return 查询结果
	 */
	public Product findProductById(String id) {
		return (Product) this.getObj("findProductById", id);
	}

}
