package com.ivt.mis.view.validator;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.DataValidator;
import com.ivt.mis.model.FieldDefn;
import com.ivt.mis.model.User;
import com.ivt.mis.service.UserService;
import com.ivt.mis.view.MainFrame;

public class UserValidator extends BaseValidator {
	User user;
	UserService userService;

	public UserValidator(User user, UserService userService, String editType) {
		this.user = user;
		this.userService = userService;
		this.editType = editType;
	}

	@Override
	public Map<String, Object[]> validate() {
		HashMap<String, Object[]> errors = new HashMap<String, Object[]>();

		FieldDefn field = this.user.getFieldDefn("loginName");
		if (Constants.FORM_TYPE_ADD.equals(editType)) {
			if (DataValidator.isBlankOrNull(this.user.getLoginName())) {
				errors.put("loginName", new Object[] { "errors.field.required",
						field.getDescr()});
			} else if (userService.isExited(this.user.getLoginName())) {
				errors.put("loginNameExist", new Object[] {
						"errors.field.exist.already", field.getDescr() });
			}
			
			field = this.user.getFieldDefn("name");
			if (DataValidator.isBlankOrNull(this.user.getName())) {
				errors.put("name", new Object[] { "errors.field.required", field.getDescr()});
			}
		}

		field = this.user.getFieldDefn("passwordOld");
		if (Constants.FORM_TYPE_PWD_CHG.equals(editType)) {
			if (DataValidator.isBlankOrNull(this.user.getPasswordOld())) {
				errors.put("passwordold",
						new Object[] { "errors.field.required", field.getDescr() });
			}else if (!userService.loginCheck(user.getLoginName(), user.getPasswordOld())){
				errors.put("passwordold",
						new Object[] { "errors.password.invalid", field.getDescr() });
			}
		}
		field = this.user.getFieldDefn("password");
		if (DataValidator.isBlankOrNull(this.user.getPassword())) {
			errors.put("password",
					new Object[] { "errors.field.required", field.getDescr()});
		} 
		
		field = this.user.getFieldDefn("passwordConfirm");
		if (DataValidator.isBlankOrNull(this.user.getPasswordConfirm())) {
			errors.put("passwordConfirm", new Object[] {
					"errors.field.required", field.getDescr() });
		} else if (!this.user.getPassword().equals(
				this.user.getPasswordConfirm())) {
			errors.put("passwordNotMatch", new Object[] {
					"errors.password.not.match", field.getDescr() });
		}

		return errors;
	}
}
