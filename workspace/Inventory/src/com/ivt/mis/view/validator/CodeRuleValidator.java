package com.ivt.mis.view.validator;

import java.util.HashMap;
import java.util.Map;

import com.ivt.mis.model.CodeRule;

public class CodeRuleValidator extends BaseValidator {
	public CodeRuleValidator(CodeRule codeRule, String editType){
		;
	}
	
	@Override
	public Map<String, Object[]> validate() {
		HashMap<String, Object[]> errors = new HashMap<String, Object[]>();
		return errors;
	}
}
