package com.ivt.mis.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.ivt.mis.model.CustomizedUnit;
import com.ivt.mis.model.PojoTest;

public class ObjectReflectUtil {
	public static final Logger logger = Logger
			.getLogger(ObjectReflectUtil.class);
	@SuppressWarnings("unchecked")
	public static Method getGetMethod(Class objectClass, String fieldName) {

		StringBuffer sb = new StringBuffer();

		sb.append("get");

		sb.append(fieldName.substring(0, 1).toUpperCase());

		sb.append(fieldName.substring(1));

		try {

			return objectClass.getMethod(sb.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public static Method getSetMethod(Class objectClass, String fieldName) {

		try {

			Class[] parameterTypes = new Class[1];

			Field field = objectClass.getDeclaredField(fieldName);

			parameterTypes[0] = field.getType();

			StringBuffer sb = new StringBuffer();

			sb.append("set");

			sb.append(fieldName.substring(0, 1).toUpperCase());

			sb.append(fieldName.substring(1));

			Method method = objectClass
					.getMethod(sb.toString(), parameterTypes);

			return method;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	public static void invokeSet(Object o, String fieldName, Object value) {

		Method method = getSetMethod(o.getClass(), fieldName);

		try {

			method.invoke(o, new Object[] { value });

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public static Object invokeGet(Object o, String fieldName) {

		Method method = getGetMethod(o.getClass(), fieldName);

		try {
			Object result = method.invoke(o, new Object[0]);
			return result != null ? result : "";

		} catch (Exception e) {

			e.printStackTrace();

		}

		return "";

	}

	/**
	 * 取Bean的属性和值对应关系的MAP
	 * 
	 * @param bean
	 * @return Map
	 */
	public static Object getFieldValue(Object bean, String fieldName1) {
//		logger.debug("FieldName1: " + fieldName1);
		String fieldName;
		String subFieldName;
		if (fieldName1.indexOf(".") > 0){
			fieldName = fieldName1.substring(0,fieldName1.indexOf("."));
			subFieldName = fieldName1.substring(fieldName1.indexOf(".") + 1);
		}else{
			fieldName = fieldName1;
			subFieldName = null;
		}
		
		Class<?> cls = bean.getClass();
		// 取出bean里的所有方法
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();

		for (Field field : fields) {
			try {
				if (fieldName.equalsIgnoreCase(field.getName())) {
					String fieldType = field.getType().getSimpleName();
					String fieldGetName = parGetName(field.getName());
					if (!checkGetMet(methods, fieldGetName)) {
						continue;
					}
					Method fieldGetMet = cls.getMethod(fieldGetName,
							new Class[] {});
					Object fieldVal = fieldGetMet.invoke(bean,
							new Object[] {});
					Object result = null;
					
					if (field.getType().isPrimitive()){
						result = fieldVal;
					}else if (fieldType.equalsIgnoreCase("STRING")) {
						result = fieldVal;
					}else if (fieldType.equalsIgnoreCase("DATE")) {
						result = (Date)fieldVal;
					}else if (fieldType.equalsIgnoreCase("INTEGER") || fieldType.equalsIgnoreCase("INT")) {
						result = Integer.parseInt(String.valueOf(fieldVal));
					}else if (fieldType.equalsIgnoreCase("DOUBLE")) {
						result = Double.parseDouble(String.valueOf(fieldVal));
					}else if (fieldType.equalsIgnoreCase("BOOLEAN")) {
						result = Boolean.valueOf(String.valueOf(fieldVal));
					}else if (fieldType.equalsIgnoreCase("LONG")) {
						result = Long.valueOf(String.valueOf(fieldVal));
					} else {
						Class<?> classObj = Class.forName(field.getType().getName());
//						logger.debug(field.getType().getClass().isInstance(classObj));
						if (field.getType().getClass().isInstance(classObj)){
							Object temp = fieldGetMet.invoke(bean,
									new Object[] {});
//							logger.debug("1>" + temp);
							if (subFieldName != null){
								result = getFieldValue(temp, subFieldName);
							}else{
								result = temp.toString();
							}
						}else{
							logger.info("not supper type" + fieldType);
						}
						
						if (null != fieldVal) {
							result = fieldVal;
						}
					}

					if (result == null || "null".equalsIgnoreCase(result.toString())){
						result = "";
					}
					return result;

				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return "";

	}

	/**
	 * 拼接某属性的 get方法
	 * 
	 * @param fieldName
	 * @return String
	 */
	public static String parGetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		int startIndex = 0;
		if (fieldName.charAt(0) == '_')
			startIndex = 1;
		return "get"
				+ fieldName.substring(startIndex, startIndex + 1).toUpperCase()
				+ fieldName.substring(startIndex + 1);
	}

	/**
	 * 拼接在某属性的 set方法
	 * 
	 * @param fieldName
	 * @return String
	 */
	public static String parSetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		int startIndex = 0;
		if (fieldName.charAt(0) == '_')
			startIndex = 1;
		return "set"
				+ fieldName.substring(startIndex, startIndex + 1).toUpperCase()
				+ fieldName.substring(startIndex + 1);
	}

	/**
	 * 获取存储的键名称（调用parGetName）
	 * 
	 * @param fieldName
	 * @return 去掉开头的get
	 */
	public static String parKeyName(String fieldName) {
		String fieldGetName = parGetName(fieldName);
		if (fieldGetName != null && fieldGetName.trim() != ""
				&& fieldGetName.length() > 3) {
			return fieldGetName.substring(3);
		}
		return fieldGetName;
	}

	/**
	 * set属性的值到Bean
	 * 
	 * @param bean
	 * @param valMap
	 */
	public static void setFieldValue(Object bean, String fieldName1, Object value) {
//		logger.debug("FullFieldName: " + fieldName1);
		String fieldName;
		String subFieldName;
		if (fieldName1.indexOf(".") > 0){
			fieldName = fieldName1.substring(0,fieldName1.indexOf("."));
			subFieldName = fieldName1.substring(fieldName1.indexOf(".") + 1);
		}else{
			fieldName = fieldName1;
			subFieldName = null;
		}
		
//		logger.debug("FieldName: " + fieldName);
//		logger.debug("subFieldName: " + subFieldName);
		Class<?> cls = bean.getClass();
		// 取出bean里的所有方法
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();

		for (Field field : fields) {
			try {

				String fieldSetName = parSetName(field.getName());
				if (!checkSetMet(methods, fieldSetName)) {
					continue;
				}
				Method fieldSetMet = cls.getMethod(fieldSetName,
						field.getType());
//				logger.debug("Field Name:" + field.getName());
//				logger.debug("Field value:" + value);
				if (fieldName.equals(field.getName())) {
					if (null != value) {
						String fieldType = field.getType().getSimpleName();
//						Class<?> fieldType = Class.forName(field.getType().getName());
						
						logger.debug("Field Type:" + fieldType);
						logger.debug("Field Value:" + value);
						logger.debug("Field isPrimitive:" + field.getType().isPrimitive());
						
						if (fieldType.equalsIgnoreCase("STRING")) {
							fieldSetMet.invoke(bean, String.valueOf(value));
						}else if (fieldType.equalsIgnoreCase("DATE")) {
							Date temp = (Date)value;
							fieldSetMet.invoke(bean, temp);
						}else if (fieldType.equalsIgnoreCase("INTEGER") || fieldType.equalsIgnoreCase("INT")) {
							Integer intval = Integer.valueOf(String.valueOf(value));
							fieldSetMet.invoke(bean, intval);
						}else if (fieldType.equalsIgnoreCase("LONG")) {
							Long temp = Long.valueOf(String.valueOf(value));
							fieldSetMet.invoke(bean, temp);
						}else if (fieldType.equalsIgnoreCase("DOUBLE")) {
							Double temp = Double.valueOf(String.valueOf(value));
							fieldSetMet.invoke(bean, temp);
						}else if (fieldType.equalsIgnoreCase("BOOLEAN")) {
							Boolean temp = Boolean.valueOf(String.valueOf(value));
							fieldSetMet.invoke(bean, temp);
						}else {
							Class<?> classObj = Class.forName(field.getType().getName());
//							logger.debug(field.getType().getClass().isInstance(classObj));
							if (field.getType().getClass().isInstance(classObj)){
								Constructor<?> consts = Class.forName(field.getType().getName()).getConstructor();
								Object temp = consts.newInstance();
								if (subFieldName != null){
									setFieldValue(temp, subFieldName,value);
								}
								
								fieldSetMet.invoke(bean, temp);
							}else{
								logger.info("not supper type" + fieldType);
							}
						}
					}

					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

	}

	/**
	 * 格式化string为Date
	 * 
	 * @param datestr
	 * @return date
	 */
	public static Date parseDate(String datestr) {
		if (null == datestr || "".equals(datestr)) {
			return null;
		}
		try {
			String fmtstr = null;
			if (datestr.indexOf(':') > 0) {
				fmtstr = "yyyy-MM-dd HH:mm:ss";
			} else {

				fmtstr = "yyyy-MM-dd";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.CHINA);
			return sdf.parse(datestr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 日期转化为String
	 * 
	 * @param date
	 * @return date string
	 */
	public static String fmtDate(Date date) {
		if (null == date) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.CHINA);
			return sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 判断是否存在某属性的 set方法
	 * 
	 * @param methods
	 * @param fieldSetMet
	 * @return boolean
	 */
	public static boolean checkSetMet(Method[] methods, String fieldSetMet) {
		for (Method met : methods) {
			if (fieldSetMet.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否存在某属性的 get方法
	 * 
	 * @param methods
	 * @param fieldGetMet
	 * @return boolean
	 */
	public static boolean checkGetMet(Method[] methods, String fieldGetMet) {
		for (Method met : methods) {
			if (fieldGetMet.equalsIgnoreCase(met.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException{
		PojoTest sp = new PojoTest();
		sp.setCustomizedUnit(new CustomizedUnit("One"));
		logger.info(sp.getValueAt("customizedUnit.name"));
		//logger.info(sp.getValueAt("ProductBrand"));
//		logger.info(sp.getValueAt("customizedUnit"));
		sp.setValueAt("two", "customizedUnit.name");
		sp.setValueAt(10, "intVal");
		sp.setValueAt(new Integer(20), "intInteger");
		sp.setValueAt(10d, "douVal");
		sp.setValueAt(new Double(30), "douObj");
		sp.setValueAt(new Date(), "date");
//		sp.setValueAt(22, "number");
		logger.info(sp.getValueAt("customizedUnit.name"));
		logger.info(sp.getValueAt("intVal"));
		logger.info(sp.getValueAt("intInteger"));
		logger.info(sp.getValueAt("douVal"));
		logger.info(sp.getValueAt("douObj"));
		logger.info(sp.getValueAt("date"));
	}

}