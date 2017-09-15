package com.ivt.mis.common;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18nMsg {
	public static String getText(String key, Object[] parameters){
		List<Object> params = Arrays.asList(parameters);
		return getText(key, params);
	}
	
	
	public static String getText(String key, List<Object> parameters){
		Locale locale = Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle(Constants.I18N_FILE_NAME);
		String kesMsg = bundle.getString(key);
		
		int paramCount = parameters.size();
		String msg = "";
		if (paramCount == 1){
			msg = MessageFormat.format(kesMsg, parameters.get(0));
		}else if (paramCount == 2){
			msg = MessageFormat.format(kesMsg, parameters.get(0), parameters.get(1));
		}else if (paramCount == 3){
			msg = MessageFormat.format(kesMsg, parameters.get(0), parameters.get(1), parameters.get(2));
		}else if (paramCount == 4){
			msg = MessageFormat.format(kesMsg, parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3));
		}else if (paramCount == 5){
			msg = MessageFormat.format(kesMsg, parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3), parameters.get(4));
		}else if (paramCount == 6){
			msg = MessageFormat.format(kesMsg, parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3), parameters.get(4), parameters.get(5));
		}else{
			msg = MessageFormat.format(kesMsg, parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3), parameters.get(4), parameters.get(5), parameters);
		}
        
        
        return msg;
	}
	
	public static String getText(String key){
		Locale locale = Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle(Constants.I18N_FILE_NAME,locale);
		return bundle.getString(key);
	}
}
