/**
 * 产品操作接口
 */

package com.ivt.mis.service;

import java.util.List;
import java.util.Vector;

import com.ivt.mis.model.Product;

public interface ProductService
{
	
	/**
	 * 向数据库中添加新产品
	 * @param product  封装好的product对象
	 * @return  执行结果
	 */
	boolean saveProduct(Product product);
	

	/**
	 * 向数据库中添加新产品
	 * @param product  封装好的product对象
	 * @return  执行结果
	 */
	boolean saveBatchProducts(List<Product> products);
	
	
	/**
	 * 从数据库中删除产品
	 * @param id  被删除产品的编号
	 * @return  执行结果
	 */
	boolean removeProduct(String id);
	/**
	 * 查询数据库中满足条件的产品
	 * @param field 查询的字段
	 * @param value 满足的值
	 * @return 查询结果
	 */
	Vector<Product> retrieveProducts(Product product);
	
	/**
	 * 更新产品信息
	 * @param product
	 * @return
	 */
	public boolean modifyProduct(Product product);
	/**
	 * 获取所有的产品
	 * @return 产品的集合
	 */
	Vector<Product> retrieveAllProducts();
	
	/**
	 * 获取所有的产品型号
	 * @return 产品型号集合
	 */
	Vector<String> retrieveAllProductCodes();
	
	/**
     * 判断产品是否存在
     * @param id 查询的产品型号
     * @return 查询结果
     */
    boolean isExited(String id);
    /**
     * 获取产品的信息
     * @param id 被查询的产品的编号
     * @return 查询结果
     */
    Product getProductInfo(String id);
}
