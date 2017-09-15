/**
 * 工厂类，产生各个服务类的对象
 */
package com.ivt.mis.common;

import org.apache.log4j.Logger;

import com.ivt.mis.service.CodeRuleService;
import com.ivt.mis.service.CustomerService;
import com.ivt.mis.service.CustomizationService;
import com.ivt.mis.service.DataVersionService;
import com.ivt.mis.service.ProductService;
import com.ivt.mis.service.ProviderService;
import com.ivt.mis.service.StoreManageService;
import com.ivt.mis.service.StoreProcurementService;
import com.ivt.mis.service.StoreShipmentService;
import com.ivt.mis.service.UserService;

public class CommonFactory {
	public static final Logger logger = Logger.getLogger(CommonFactory.class);

	/**
	 * 获取ProductService类的对象
	 * 
	 * @return ProductService类的对象
	 */
	public static ProductService getProductService() {
		try {
			return (ProductService) SpringAppContext.getInstance()
					.getApplicationContext().getBean("productService");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	

	/**
	 * 获取CodeRuleService类的对象
	 * 
	 * @return CodeRuleService类的对象
	 */
	public static CodeRuleService getCodeRuleService() {
		try {
			return (CodeRuleService) SpringAppContext.getInstance()
					.getApplicationContext().getBean("codeRuleService");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取ProductService类的对象
	 * 
	 * @return ProductService类的对象
	 */
	public static StoreManageService getStoreManageService() {
		try {
			return (StoreManageService) SpringAppContext.getInstance()
					.getApplicationContext().getBean("storeManageService");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取CustomerServices类的对象
	 * 
	 * @return CustomerServices类的对象
	 */
	public static CustomerService getCustomerService() {
		try {
			return (CustomerService) SpringAppContext.getInstance()
					.getApplicationContext().getBean("customerService");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取CustomizationServices类的对象
	 * 
	 * @return CustomizationServices类的对象
	 */
	public static CustomizationService getCustomizationService() {
		try {
			return (CustomizationService) SpringAppContext.getInstance()
					.getApplicationContext().getBean("customizationService");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取DataVersionService类的对象
	 * 
	 * @return DataVersionService类的对象
	 */
	public static DataVersionService getDataVersionService() {
		try {
			return (DataVersionService) SpringAppContext.getInstance()
					.getApplicationContext().getBean("dataVersionService");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	

	/**
	 * 获取ProviderServices类的对象
	 * 
	 * @return ProviderServices类的对象
	 */
	public static ProviderService getProviderService() {
		try {
			return (ProviderService) SpringAppContext.getInstance()
					.getApplicationContext().getBean("providerService");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取UserServices类的对象
	 * 
	 * @return UserServices类的对象
	 */
	public static UserService getUserService() {
		try {
			return (UserService) SpringAppContext.getInstance()
					.getApplicationContext().getBean("userService");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取StoreProcurementServices类的对象
	 * 
	 * @return StoreProcurementServices类的对象
	 */
	public static StoreProcurementService getStoreProcurementService() {
		try {
			return (StoreProcurementService) SpringAppContext.getInstance()
					.getApplicationContext().getBean("storeProcurementService");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取StoreShipmentService类的对象
	 * 
	 * @return StoreShipmentService类的对象
	 */
	public static StoreShipmentService getStoreShipmentService() {
		try {
			return (StoreShipmentService) SpringAppContext.getInstance()
					.getApplicationContext().getBean("storeShipmentService");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
