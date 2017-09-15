/**
 *定义常量接口
 */

package com.ivt.mis.common;

public class Constants {
	int PSTM_TYPE = 0;
	int CALL_TYPE = 1;

	public static final String FORM_TYPE_UPDATE = "UPDATE";
	public static final String FORM_TYPE_ADD = "ADD";
	public static final String FORM_TYPE_DELETE = "DELETE";
	public static final String FORM_TYPE_VIEW = "VIEW";
	public static final String FORM_TYPE_ADD_UPDATE = "ADD_UPDATE";
	public static final String FORM_TYPE_PWD_CHG = "PWDCHG"; // 修改密码使用

	public static final int NUMBER_FRACTION_MAX = 3;

	public static final int DB_BATCH_SIZE = 100;

	public static final int PAGE_SIZE_DEFAULT = 25;
	public static final int PAGE_SIZE_MAX = 999999;

	public final static String I18N_FILE_NAME = "config.i18nmsg";
	public final static String POWER_ADMIN = "管理员";
	public final static String POWER_OPERATOR = "操作员";
	public static final String LICENSE_PUBLICK_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNHStWj4YDRQPlR1oQxL3u4dBDfIN7jNMroF/bspA0cE4G1OZz3IgMltkkZR6edh5N2l3MBUUGn8wXWH2n+Sps2IalMjJ6Mko9dab2FP9phmkU1OtDTnFpp0wQ+CLVMcO2/FNKKUfmTjcegmSrWW0LkgXfB4uPfznaqVJMV5KrqQIDAQAB";

	public static final int LICENSE_TRIAL_DAYS = 30;

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

	public static final String STORE_SHIP_TYPE_SELL = "销售出货";

	public static final String STORE_SHIP_TYPE_RETURN = "销售退货";

	public static final String STORE_PROCUREMENT_TYPE_PRO = "采购入库";

	public static final String STORE_PROCUREMENT_TYPE_RETURN = "采购退货";

	public static final String MODULE_PROCUREMENT = "PROCUREMENT";
	public static final String MODULE_PROCUREMENT_SEARCH = "PROCUREMENT_SEARCH";
	public static final String MODULE_PROCUREMENT_BATCH = "PROCUREMENT_BATCH";
	public static final String MODULE_SHIPMENT = "SHIPMENT";
	public static final String MODULE_SHIPMENT_SEARCH = "SHIPMENT_SEARCH";
	public static final String MODULE_SHIPMENT_BATCH = "SHIPMENT_BATCH";
	public static final String MODULE_PRODUCT = "PRODUCT";
	public static final String MODULE_PRODUCT_SEARCH = "PRODUCT_SEARCH";
	public static final String MODULE_PROVIDER = "PROVIDER";
	public static final String MODULE_PROVIDER_SEARCH = "PROVIDER_SEARCH";
	public static final String MODULE_CUSTOMER = "CUSTOMER";
	public static final String MODULE_CUSTOMER_SEARCH = "CUSTOMER_SEARCH";
	public static final String MODULE_STORE_MANAGE = "SOTRE_MANAGE";
	public static final String MODULE_DB_BACKUP = "DB_BACKUP";
	public static final String MODULE_EXCEL_TEMPLATE = "EXCEL_TEMPLATE";
	public static final String MODULE_USER = "USER";
	public static final String MODULE_USER_PWD = "USER_PWD";
	public static final String MODULE_CODE_RULE = "CODE_RULE";
	public static final String MODULE_CONTACTME = "CONTACTME";

	public static final String CODE_RULE_SEQ_TYPE_NBR = "数字递增";
	public static final String CODE_RULE_SEQ_TYPE_DATETIME = "日期时间";
	
	public static final int NUMBER_TYPE_INT = 0;
	public static final int NUMBER_TYPE_DOUBLE = 1;
	public static final int TABLE_DEFAULT_HEIGHT = 25;
	public static final String NUMBER_FORMAT_INT = "#,##0";
	public static final String NUMBER_FORMAT_DOUBLE = "#,##0.0000";
	public static final String NUMBER_FORMAT_DEFAULT = "#,##0.###";
	
}
