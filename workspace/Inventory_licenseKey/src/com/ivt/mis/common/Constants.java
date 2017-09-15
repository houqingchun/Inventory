/**
 *定义常量接口
 */

package com.ivt.mis.common;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Constants {
	int PSTM_TYPE = 0;
	int CALL_TYPE = 1;

	public static final String FORM_TYPE_UPDATE = "UPDATE";
	public static final String FORM_TYPE_ADD = "ADD";
	public static final String FORM_TYPE_DELETE = "DELETE";
	public static final String FORM_TYPE_VIEW = "VIEW";
	public static final String FORM_TYPE_PWD_CHG = "PWDCHG"; // 修改密码使用

	public static final int NUMBER_FRACTION_MAX = 3;

	public static final int DB_BATCH_SIZE = 100;
	
	public static final int PAGE_SIZE_DEFAULT = 30;
	public static final int PAGE_SIZE_MAX = 999999;

	public final static String I18N_FILE_NAME = "config.i18nmsg";
	public final static String POWER_ADMIN = "管理员";
	public final static String POWER_OPERATOR = "操作员";
	public static final String LICENSE_PUBLICK_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNHStWj4YDRQPlR1oQxL3u4dBDfIN7jNMroF/bspA0cE4G1OZz3IgMltkkZR6edh5N2l3MBUUGn8wXWH2n+Sps2IalMjJ6Mko9dab2FP9phmkU1OtDTnFpp0wQ+CLVMcO2/FNKKUfmTjcegmSrWW0LkgXfB4uPfznaqVJMV5KrqQIDAQAB";
	
	public static final int LICENSE_TRIAL_DAYS = 90;
	
	public static boolean LICENSE_IS_TRIAL = false;
	
	public static final String TASK_ICON_PATH = "/com/ivt/mis/view/images/task.jpg";
	
	public static boolean COMPANY_INFO_IS_MAINTAINED = false;
	
	public static String TOOLS_TITLE_SUFFIX = "";
	
	public static String TOOLS_TITLE_PREFIX = "";
	
	public static String IVT_VERSION = "1.00";
	
	public static boolean IS_UPGRADE_MODE = false;
	
	public static final String LICENSE_INVALID = "LICENSE_INVALID";
	public static final String LICENSE_CODE_NOT_MATCH = "LICENSE_CODE_NOT_MATCH";
	public static final String LICENSE_NOT_REGIST = "LICENSE_NOT_REGIST";
	public static final String LICENSE_VALID = "LICENSE_VALID";
	public static final String LICENSE_EXPIRED = "LICENSE_EXPIRED";
}
