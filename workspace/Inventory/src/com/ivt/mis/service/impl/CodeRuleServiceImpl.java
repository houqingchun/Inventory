package com.ivt.mis.service.impl;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ivt.mis.common.BasicTypeUtils;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.DataValidator;
import com.ivt.mis.dao.CodeRuleDAO;
import com.ivt.mis.model.CodeRule;
import com.ivt.mis.service.CodeRuleService;
import com.ivt.mis.view.BaseJInternalFrame;

public class CodeRuleServiceImpl implements CodeRuleService {
	public static final Logger logger = Logger
	.getLogger(CodeRuleServiceImpl.class);
	CodeRuleDAO codeRuleDAO;

	@Override
	public boolean saveCodeRule(CodeRule codeRule) {
		if (this.isExited(codeRule.getObjectType())) {
			return modifyCodeRule(codeRule);
		} else {
			return codeRuleDAO.addCodeRule(codeRule);
		}
	}

	@Override
	public boolean removeCodeRule(String id) {
		return codeRuleDAO.deleteCodeRule(id);
	}

	@Override
	public Vector<CodeRule> retrieveCodeRules(CodeRule codeRule) {
		return codeRuleDAO.searchCodeRuleByProperties(codeRule);
	}

	@Override
	public boolean modifyCodeRule(CodeRule codeRule) {
		return codeRuleDAO.updateCodeRule(codeRule);
	}

	@Override
	public boolean isExited(String objectType) {
		return codeRuleDAO.isExited(objectType);
	}

	@Override
	public CodeRule getCodeRuleInfo(String id) {
		return codeRuleDAO.findCodeRuleById(id);
	}

	@Override
	public CodeRule getCodeRuleInfoByObjectType(String objectType) {
		return codeRuleDAO.findCodeRuleByObjectType(objectType);
	}

	public void setCodeRuleDAO(CodeRuleDAO codeRuleDAO) {
		this.codeRuleDAO = codeRuleDAO;
	}

	@Override
	public String getNextObjectCode(String objectType) {
		CodeRule codeRule = this.codeRuleDAO
				.findCodeRuleByObjectType(objectType);
		String newCode = "";
		if (codeRule != null) {
			if (codeRule.getPrefixEnabled()) {
				newCode += codeRule.getPrefix();
			}

			String newSeqNbr = nextSeqNbr(codeRule, 1);
			newCode += newSeqNbr;

			// 更新数据库序号为最新序号
			// codeRule.setCurrentSeq(newSeqNbr);
			// this.codeRuleDAO.updateCurrentSeq(codeRule);

			if (codeRule.getSuffixEanbled()) {
				newCode += codeRule.getSuffix();
			}
		} else {
			// 尚未设定规则，则自动日期时间戳
			newCode = BasicTypeUtils.getPremaryKeyID("DF");
		}

		return newCode;
	}

	@Override
	public boolean refreshOjbectCode(String objectType, String currentCode) {
		CodeRule codeRule = this.codeRuleDAO
				.findCodeRuleByObjectType(objectType);

		if (codeRule != null) {// 存在编码规则
			if (codeRule.getPrefixEnabled()) {
				currentCode = currentCode.replace(codeRule.getPrefix(), "");
			}

			if (codeRule.getSuffixEanbled()) {
				currentCode = currentCode.replace(codeRule.getSuffix(), "");
			}
			
			boolean isGeneratedId = true;
			
			if (Constants.CODE_RULE_SEQ_TYPE_NBR.equals(codeRule.getSeqType())){ //数字类型
				if (!StringUtils.isNumeric(currentCode)){
					logger.debug("Customized ID:" + currentCode);
					isGeneratedId = false;
				}
			}else if (Constants.CODE_RULE_SEQ_TYPE_DATETIME.equals(codeRule.getSeqType())){ //日期类型
				String dateStr = currentCode.substring(0, currentCode.length()-3);
				if (!DataValidator.isDate(dateStr, "yyMMddHHmmss", false)){
					logger.debug("Customized ID:" + currentCode);
					isGeneratedId = false;
				}
			}
				

			//自动生成并且倍数未变
			if (isGeneratedId && currentCode.length() == codeRule.getNbrOfSeq()) {
				// 自动生成的编码,则保存后更新数据库编码
				if (currentCode.compareTo(codeRule.getCurrentSeq()) > 0) {
					// 在于数据库中已经存在值
					codeRule.setCurrentSeq(currentCode);
					return this.codeRuleDAO.updateCodeRule(codeRule);
				} else {
					return false;
				}
			} else {
				// 用户自定义编码,不做更新操作
				return false;
			}

		} else {
			// 不存在编码规则
			return false;
		}
	}

	private String nextSeqNbr(CodeRule codeRule, int increaseNbr) {
		// 产生当前序号值
		FieldPosition HELPER_POSITION = new FieldPosition(0);
		NumberFormat numberFormat = null;
		StringBuffer sb = new StringBuffer();
		int seq = 1;
		if (Constants.CODE_RULE_SEQ_TYPE_NBR.equals(codeRule.getSeqType())) { // 数字自增长
			try {
				seq = Integer.valueOf(codeRule.getCurrentSeq());
				seq = seq + increaseNbr;
			} catch (Exception e) {
				seq = seq + increaseNbr;
			}

			numberFormat = new DecimalFormat(codeRule.getSeqFormat());
			numberFormat.format(seq, sb, HELPER_POSITION);
		} else { // 日期时间
			seq = increaseNbr;
			Date date = new Date();
			SimpleDateFormat formate = new SimpleDateFormat(codeRule
					.getSeqFormat());
			numberFormat = new DecimalFormat("000");
			formate.format(date.getTime(), sb, HELPER_POSITION);

			numberFormat.format(seq, sb, HELPER_POSITION);
		}

		return sb.toString();
	}

	@Override
	public List getNextObjectCodes(String objectType, int count) {
		List codes = new ArrayList();
		CodeRule codeRule = this.codeRuleDAO
				.findCodeRuleByObjectType(objectType);
		for (int i = 0; i < count; i++) {
			String newCode = "";
			if (codeRule != null) {
				if (codeRule.getPrefixEnabled()) {
					newCode += codeRule.getPrefix();
				}

				String newSeqNbr = nextSeqNbr(codeRule, i + 1);
				newCode += newSeqNbr;

				if (codeRule.getSuffixEanbled()) {
					newCode += codeRule.getSuffix();
				}
			} else {
				// 尚未设定规则，则自动日期时间戳
				newCode = BasicTypeUtils.getPremaryKeyID("DF");
			}

			codes.add(newCode);
		}
		return codes;
	}
}
