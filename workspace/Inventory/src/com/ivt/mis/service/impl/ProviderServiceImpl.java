/**
 * 供应商服务类
 */
package com.ivt.mis.service.impl;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ivt.mis.common.BasicTypeUtils;
import com.ivt.mis.common.Constants;
import com.ivt.mis.dao.ProviderDAO;
import com.ivt.mis.dao.StoreProcurementDAO;
import com.ivt.mis.model.PageUtil;
import com.ivt.mis.model.Provider;
import com.ivt.mis.service.CodeRuleService;
import com.ivt.mis.service.ProviderService;

public class ProviderServiceImpl implements ProviderService {
	public static final Logger logger = Logger
			.getLogger(ProviderServiceImpl.class);
	ProviderDAO providerDAO;

	StoreProcurementDAO storeProcurementDAO;

	CodeRuleService codeRuleService;

	public void setCodeRuleService(CodeRuleService codeRuleService) {
		this.codeRuleService = codeRuleService;
	}

	public ProviderServiceImpl() {
		super();
	}

	public boolean saveProvider(Provider provider) {
		this.codeRuleService.refreshOjbectCode(Constants.MODULE_PROVIDER,
				provider.getId());
		provider.setCreateTime(BasicTypeUtils.getLongFmtDate());
		return providerDAO.addProvider(provider);
	}

	public boolean removeProvider(String id) {

		return providerDAO.deleteProvider(id);
	}

	public boolean modifyProvider(Provider provider) {
		boolean flag = providerDAO.updateProvider(provider);
		flag &= storeProcurementDAO.updateProviderInfoForProcurement(provider);
		return flag;
	}

	public Vector<Provider> retrieveAllProviders() {
		logger.debug("creat new dao");

		logger.debug("query all provider");
		Vector<Provider> result = providerDAO.searchAllProviders();
		logger.debug("query all provider complete");
		return result;
	}

	public Vector<Provider> retrieveProviders(Provider provider) {
		if (provider.getPage() == null) {
			provider.setPage(new PageUtil(Constants.PAGE_SIZE_MAX, 0, 1));
		}
		return providerDAO.searchProviderByProperties(provider);
	}

	public boolean isExited(String id) {

		return providerDAO.isExited(id);
	}

	public Provider getProviderInfo(String id) {

		return providerDAO.findProviderById(id);
	}

	public void setProviderDAO(ProviderDAO providerDAO) {
		this.providerDAO = providerDAO;
	}

	@Override
	public Vector<String> retrieveAllProviderCodes() {
		return providerDAO.searchAllProviderCodes();
	}

	public void setStoreProcurementDAO(StoreProcurementDAO storeProcurementDAO) {
		this.storeProcurementDAO = storeProcurementDAO;
	}

	@Override
	public boolean saveBatchProviders(List<Provider> providers) {

		String maxId = "";
		for (Provider provider : providers) {
			maxId = maxId.compareTo(provider.getId()) < 0 ? provider.getId()
					: maxId;
			provider.setCreateTime(BasicTypeUtils.getLongFmtDate());
		}
		this.codeRuleService.refreshOjbectCode(Constants.MODULE_PROVIDER,
				maxId);
		return providerDAO.addBatchProvider(providers);
	}

}
