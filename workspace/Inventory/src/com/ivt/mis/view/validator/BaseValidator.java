package com.ivt.mis.view.validator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.ivt.mis.common.Constants;
import com.ivt.mis.common.DataValidator;
import com.ivt.mis.common.I18nMsg;
import com.ivt.mis.model.BasePOJO;
import com.ivt.mis.model.FieldDefn;

/**
 * 
 * action验证的标准接口，初始化一个Map保存所有要验证的信息
 * 
 * @author 侯青春
 * 
 * 
 */
public abstract class BaseValidator {
	public String editType;
	protected BasePOJO basePOJO;

	public Map<String, Object[]> validate() {
		HashMap<String, Object[]> errors = new HashMap<String, Object[]>();

		for (int i = 0; i < basePOJO.getColumns().length; i++) {
			FieldDefn field = new FieldDefn(basePOJO.getColumns()[i]);
			if (field.isRequired()) {
				if (DataValidator.isBlankOrNull(basePOJO.getValueAt(
						field.getFieldName()).toString())) {
					errors.put(field.getFieldName(), new Object[] {
							"errors.field.required", field.getDescr() });
				}
			}
		}

		return errors;
	}

	public String getErrorString() {
		Map<String, Object[]> errors = this.validate();

		if (!errors.isEmpty()) {
			StringBuffer errorBuf = new StringBuffer();
			Set entity = errors.entrySet();
			Iterator items = entity.iterator();

			while (items.hasNext()) {
				Entry entry = (Entry) items.next();
				Object[] errorMsg = (Object[]) entry.getValue();

				String keyMsg = I18nMsg.getText(errorMsg[0].toString(), Arrays
						.asList(errorMsg).subList(1, errorMsg.length));
				errorBuf.append(keyMsg + "\n");
			}
			return errorBuf.toString();
		}

		return null;
	}
}
