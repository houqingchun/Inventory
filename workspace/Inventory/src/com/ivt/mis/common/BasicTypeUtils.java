package com.ivt.mis.common;

import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 基础类型的操作，如字符串空白信息的处理，替换，分割，日期，格式转换，MD5加密算法<br>
 * 
 * 因为是工具类，每个操作的方法给出了举例用法，详见每个方法的说明，另后续会不断补充说明
 * 
 * @author 侯青春
 * 
 */
public class BasicTypeUtils {

	/**
	 * 禁止外部实例化
	 */
	private BasicTypeUtils() {

	}

	/**
	 * 常量空白字符串
	 */
	public static final String EMPTY_STR = "";
	public static final String SQL_EMPTY_STR = "null";

	/**
	 * 长日期格式
	 */
	public static final String LONG_DATE_FMT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 短日期格式
	 */
	public static final String SHORT_DATA_FMT = "yyyy-MM-dd";
	/**
	 * 斜杠期格式
	 */
	public static final String IE_DATA_FMT = "yyyy/MM/dd";

	/**
	 * 斜杠期格式
	 */
	public static final String LONG_IE_DATA_FMT = "yyyy/MM/dd HH:mm:ss";

	/** The FieldPosition. */
	private static final FieldPosition HELPER_POSITION = new FieldPosition(0);

	/** This Format for format the number to special format. */
	private final static NumberFormat numberFormat = new DecimalFormat("000");

	/** This int is the sequence number ,the default value is 0. */
	private static final int MAX = 999;

	private static final Map<String, Integer> KEY_MAP = new HashMap<String, Integer>();

	/**
	 * 格式化金额为两位数字
	 * 
	 * @param parml
	 * @return
	 */
	public static String formatNumberWtDigatls(Double parml) {
		if (null == parml) {
			return "";
		}
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		df.applyPattern("###0.00");
		return df.format(parml);
	}

	/**
	 * 根据给定前缀生产主键ID
	 * 
	 * @param preFixStr
	 * @return
	 */
	public static synchronized String getPremaryKeyID(String preFixStr) {
		Date date = new Date();
		SimpleDateFormat formate = new SimpleDateFormat("yyMMddHHmmss");

		StringBuffer sb = new StringBuffer();

		formate.format(date.getTime(), sb, HELPER_POSITION);

		int seq = 0;
		if (KEY_MAP.containsKey(preFixStr)) {
			seq = (Integer) KEY_MAP.get(preFixStr);
		}

		numberFormat.format(seq, sb, HELPER_POSITION);

		if (seq == MAX) {
			seq = 0;
		} else {
			seq++;
		}

		// Update sequence in map
		KEY_MAP.put(preFixStr, seq);

		return preFixStr + sb.toString();
	}

	/**
	 * 取得当前的日期格式的日期
	 * 
	 * @return 返回当前日期格式的日期
	 */
	public static Date getCurrentDateforSQL() {
		return new Date();
	}

	/**
	 * 取得当前的Timestamp给写入数据库使用
	 * 
	 * @return
	 */
	public static Timestamp getCurrentDateTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * 将TimesStamp和当前的日期进行比较
	 * 
	 * @param timestamp
	 * @return 相同返回0，给定日期大于当前日期，返回1，反正返回-1
	 */
	public static int compareDateWtCurrentDate(Timestamp timestamp) {
		Timestamp nowTimestamp = getCurrentDateTimestamp();
		if (nowTimestamp.equals(timestamp)) {
			return 0;
		} else if (nowTimestamp.after(timestamp)) {
			return -1;
		} else {
			return 1;
		}
	}

	/**
	 * 产生简易菜单
	 * 
	 * @param myMenu
	 * @param path
	 * @param url
	 * @return
	 */
	public static String getMenuHyperLink(String[] myMenu, String path,
			String url) {
		String origiaPageURL = myMenu[0];
		myMenu[0] = myMenu[0].substring(0, myMenu[0].indexOf("."));
		if (url.indexOf(myMenu[0]) != -1) {
			return "<a href=" + path + origiaPageURL + " class='current'>"
					+ myMenu[1] + "</a>";
		} else {
			return "<a href=" + path + origiaPageURL + " class='dot'>"
					+ myMenu[1] + "</a>";
		}
	}

	/**
	 * 取得差值日期的时间，共数据库比较使用
	 * 
	 * @param days
	 * @return
	 */
	public static Timestamp getAddDaysTimestamp(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 取得当前给予日期的差值的新日期
	 * 
	 * @param parmlDate
	 * @param days
	 * @return
	 */
	public static Timestamp getAddDaysTimestamp(Timestamp parmlDate, int days) {
		Long newParmlDate = parmlDate.getTime() + days * 24 * 60 * 60 * 1000;
		return new Timestamp(newParmlDate);
	}

	/**
	 * 取得当前今天的最好一刻的Timstamp
	 * 
	 * @return
	 */
	public static Timestamp getThisDayBylastMinuteTimestamp() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);

		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 取得当前短日期的Timestamp
	 * 
	 * @return
	 */
	public static Timestamp getShortFmtCurrentTimestamp() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 取得给定字符串的日前函数 格式：yyyy-MM-dd
	 * 
	 * @param parml
	 *            设定的字符串
	 * @return 返回当前的字符串的日期格式
	 * @throws ParseException
	 *             日期格式不符合当前的格式
	 */
	public static Date getCurrentDateforSQL(String parml) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATE_FMT);
		return sdf.parse(parml);
	}

	/**
	 * 取得当前长格式的日期字符串 <br>
	 * 用法举例： BasicTypeUtils.getLongFmtDate()= "2009-12-22"
	 * 
	 * @return String 返回当前长格式的日期字符串
	 */
	public static String getLongFmtDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATE_FMT);
		return sdf.format(new Date());
	}

	public static String getLongFmtDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATE_FMT);
		return sdf.format(date);
	}

	public static String getFloatFmtTwoNumber(float ff) {
		/*
		 * NumberFormat numFormat = NumberFormat.getNumberInstance();
		 * numFormat.setMaximumFractionDigits(2);
		 */

		DecimalFormat df = new DecimalFormat("####.##");
		return String.valueOf(df.format(ff));
	}

	public static String getShortFmtDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(SHORT_DATA_FMT);
		return sdf.format(date);
	}

	/**
	 * 日期以斜杠的形式显示
	 * 
	 * @param date
	 * @return
	 */
	public static String getIeFmtDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(IE_DATA_FMT);
		return sdf.format(date);
	}

	/**
	 * 日期以斜杠的形式显示(长)
	 * 
	 * @param date
	 * @return
	 */
	public static String getLongIEFmtDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(LONG_IE_DATA_FMT);
		return sdf.format(date);
	}

	/**
	 * 取得当前短格式的日期字符串 <br>
	 * 用法举例： BasicTypeUtils.getShortFmtDate()= "2009-12-22 16:43:00"
	 * 
	 * @return String 返回当前短格式的日期字符串
	 */
	public static String getShortFmtDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(SHORT_DATA_FMT);
		return sdf.format(new Date());
	}

	/**
	 * 整数类型转换字符型<br>
	 * 用法举例： BasicTypeUtils.parseToString(0)= "0"
	 * 
	 * @param parml
	 *            需要转换的整数
	 * 
	 * @return String 整数类型转换字符型
	 */
	public static String parseToString(int parml) {
		return String.valueOf(parml);
	}

	/**
	 * 双精度类型转换字符型<br>
	 * 用法举例： BasicTypeUtils.parseToString(0)= "0"
	 * 
	 * @param parml
	 *            需要转换的双精度
	 * 
	 * @return String 双精度类型转换字符型
	 */
	public static String parseToString(double parml) {
		return String.valueOf(parml);
	}

	/**
	 * 长整类型转换字符型<br>
	 * 用法举例： BasicTypeUtils.parseToString(0)= "0"
	 * 
	 * @param parml
	 *            需要转换的长整数
	 * 
	 * @return String 长整数类型转换字符型
	 */
	public static String parseToString(long parml) {
		return String.valueOf(parml);
	}

	/**
	 * stringBuffer类型转换字符型<br>
	 * 用法举例： BasicTypeUtils.parseToString(0)= "0"
	 * 
	 * @param parml
	 *            需要转换的stringBuffer
	 * 
	 * @return String stringBuffer类型转换字符型
	 */
	public static String parseToString(StringBuffer parml) {
		return String.valueOf(parml);
	}

	/**
	 * 字符串转换为双精度类型 <br>
	 * 用法举例： BasicTypeUtils.parseToDouble("1.0005")= 1.005
	 * 
	 * @param parml
	 *            需要转换的字符串
	 * @return double 返回双精度类型的转换结果
	 */
	public static double parseToDouble(String parml) {
		return NumberUtils.toDouble(parml);
	}

	/**
	 * 字符串转换为长整型 <br>
	 * 用法举例： BasicTypeUtils.parseLong("0")= 0
	 * 
	 * @param parml
	 *            需要转换的字符串
	 * @return String 返回的长整类型的转换结果
	 */
	public static long parseLong(String parml) {
		return NumberUtils.toLong(parml);
	}

	/**
	 * 字符串转换为整数型 <br>
	 * 用法举例： BasicTypeUtils.parseInt("0")= 0
	 * 
	 * @param parml
	 *            需要转换的字符串
	 * @return String 返回的整数类型的转换结果
	 */
	public static int parseInt(String parml) {
		return NumberUtils.toInt(parml);
	}

	/**
	 * 判断当前的字符串是否为空或者为Null<br>
	 * 用法举例： BasicTypeUtils.isNullorBlank("")= true
	 * 
	 * @param parml
	 *            需要检查的字符串
	 * @return Boolean 返回是否为空或者为Null
	 */
	public static boolean isNullorBlank(String parml) {
		return StringUtils.isBlank(parml);
	}

	/**
	 * 计算一个字符或字符串在另外一个字符串出现的次数
	 * 
	 * @return 返回该字符或该字符串在另外一个字符串出现的次数
	 */
	public static int getCount(String str, String sub) {
		return StringUtils.countMatches(str, sub);
	}

	/**
	 * 判断该字符串是否全为汉字
	 * 
	 * @param str
	 * @return
	 */
	public static Boolean isGB2312(String str) {
		for (int i = 0; i < str.length(); i++) {
			String bb = str.substring(i, i + 1);
			// 生成一个Pattern,同时编译一个正则表达式
			boolean cc = java.util.regex.Pattern.matches("[\u4E00-\u9FA5]", bb);
			if (cc == false) {
				return cc;
			}
		}
		return true;

	}

	/**
	 * 对字符串进行取空格处理，对Null并转换为空值 <br>
	 * 用法举例： BasicTypeUtils.notNull("ABC ")= "ABC"
	 * 
	 * @param parml
	 *            需要处理的字符串
	 * @return 对格式处理后的字符串
	 */
	public static String notNull(String parml) {
		return StringUtils.trimToEmpty(parml);
	}

	/**
	 * 判断传入的Long变量是否有值
	 * 
	 * @param longNumber
	 *            需要判断的变量
	 * @return 返回是否为空
	 */
	public static boolean isNullNumber(Long longNumber) {
		return (longNumber == null);
	}

	/**
	 * 产生一个给定长度的字符串 <br>
	 * 用法举例： BasicTypeUtils.leftPad("1",5,"*")= "****5"
	 * 
	 * @param parml
	 *            给定需要处理的字符串
	 * @param size
	 *            需要产生的长度
	 * @return 产生的最终字符串
	 */
	public static String leftPad(String parml, int size, String padChar) {
		return StringUtils.leftPad(parml, size, padChar);
	}

	// /**
	// * 对字符串进行MD5加密 <br>
	// * 用法举例： BasicTypeUtils.MD5("")="d41d8cd98f00b204e9800998ecf8427e"
	// *
	// * @param parml
	// * 需要加密的字符串
	// * @return 对字符串进行MD5加密后的值
	// */
	// public static String MD5(String parml) {
	// return DigestUtils.md5Hex(notNull(parml));
	// }

	/**
	 * 对字符串进行空值分割，并进行第一个字母的大写处理<br>
	 * 用法举例： BasicTypeUtils.leftPad("hello world")= "Hello World"
	 * 
	 * @param parml
	 *            需要首字母处理的字符串
	 * @return 首字母大写处理后的字符串
	 */
	public static String firstCharToUpperCase(String parml) {
		return WordUtils.capitalize(parml);

	}

	/**
	 * 对字符串进行去除HTML的操作 <br>
	 * 用法举例： BasicTypeUtils.leftPad("<a></a>")= "&lt;a&gt;&lt;/a&gt;"
	 * 
	 * @param parml
	 *            需要去除HTML的字符串
	 * @return 去除HTML标签后的字符串
	 */
	public static String escapeHtmlWords(String parml) {
		return StringEscapeUtils.escapeHtml(parml);
	}

	/**
	 * 对字符串进行SQL标记的操作 <br>
	 * 用法举例： BasicTypeUtils.escepeSQLwords("A ' ")= "A '' "
	 * 
	 * @param parml
	 *            需要去除SQL标记的字符串
	 * @return 去除SQL标记后的字符串
	 */
	public static String escepeSQLwords(String parml) {
		return StringEscapeUtils.escapeSql(parml);
	}

	/**
	 * 
	 * 在给定的字符串内进行查找特定字符串，并换成对应的字符串 <br>
	 * 用法举例： BasicTypeUtils.leftPad("abc|ABC|cde","|","*")= "abc*ABC*cde"
	 * 
	 * @param parml
	 * @param searchString
	 * @param replacement
	 * @return
	 */
	public static String replace(String parml, String searchString,
			String replacement) {
		return StringUtils.replace(parml, searchString, replacement);
	}

	/**
	 * 
	 * 对给定的字符串以给定的字符进行分割<br>
	 * 用法举例： BasicTypeUtils.leftPad("abc|ABC|cde","|")={"abc","ABC,"cde"}
	 * 
	 * @param parml
	 * @param separatorChar
	 * @return Array
	 */
	public static String[] split(String parml, String separatorChar) {
		return StringUtils.split(parml, separatorChar);
	}

	/**
	 * 取得一个给定长度的数字字符混合字符串 <br>
	 * 用法举例： BasicTypeUtils.randomAlphanumeric(100)=
	 * "wSDtzWYHp0EDEgvM9NyNbgVYeC5zXDyx8hQqODptHVJyWvzjTa7c01jZJfDK3PXj8hrN3XWALQ3Q129tQtv4OCEdUxFluELYzhBk"
	 * 
	 * @param count
	 *            需要产生随机字符串的长度
	 * @return 产生的给定长度的数字字符随机字符串
	 */
	public static String randomAlphanumeric(int count) {
		return RandomStringUtils.randomAlphanumeric(count);
	}

	/**
	 * 取得一个给定长度的纯数字的随机字符串 <br>
	 * 用法举例： BasicTypeUtils.randomNumeric(100)=
	 * "9811849487110345870189117021421446235298267390404127961970424649355464106939895627617950304876536495"
	 * 
	 * @param count
	 *            需要产生随机数字的长度
	 * @return String 产生的给定长度的数字字符随机字符串
	 */
	public static String randomNumeric(int count) {
		return RandomStringUtils.randomNumeric(count);
	}

	/**
	 * 取得一个给定长度的纯字符的随机字符串 <br>
	 * 用法举例： BasicTypeUtils.randomAlphabetic(100)=
	 * "GeGgIDMKoTamhCERPfQipnDJCIRXLViDJftJElTKPxxUhqzhEkFhRiItujlZmKHFYUGFkdnIhZPFILismVVyVCOlniOTNJhOEglP"
	 * 
	 * @param count
	 *            需要产生随机数字的长度
	 * @return String 产生的给定长度的纯字符型的随机字符串
	 */
	public static String randomAlphabetic(int count) {
		return RandomStringUtils.randomAlphabetic(count);
	}

	/**
	 * 计算当前data进行换算大小，如m,g,t,k等
	 * 
	 * @param dataSize
	 * @return
	 */
	public static String sizeFormat(long dataSize) {
		if (dataSize >= 1073741824) {
			return ((double) Math.round(dataSize / 1073741824d * 100) / 100)
					+ " GB";
		} else if (dataSize >= 1048576) {
			return ((double) Math.round(dataSize / 1048576d * 100) / 100)
					+ " MB";
		} else if (dataSize >= 1024) {
			return ((double) Math.round(dataSize / 1024d * 100) / 100) + " KB";
		} else {
			return dataSize + " Bytes";
		}
	}

	/**
	 * 和系统当前日期比较大小
	 * 
	 * @param dbDate
	 * @return
	 */
	public static Boolean dbDateCompareToSysDate(Date dbDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {
			int num = sdf.parse(sdf.format(dbDate)).compareTo(
					sdf.parse(sdf.format(new Date())));
			if (num == 1) {// 截止日期大于当前系统日期，可以下订单
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static String numberFloatFormet(float number) {
		float d3 = (float) (Math.round((number) * 10000) / 10000.0);
		return String.valueOf(d3);
	}

	public static String numberFloatFormetTwo(float number) {
		float d3 = (float) (Math.round((number) * 100) / 100.0);
		return String.valueOf(d3);
	}

	/**
	 * 得到当前的时间点
	 * 
	 * @return
	 */
	public static String getDateNum() {
		DateFormat format = new SimpleDateFormat("yyMMddHHmmss");
		String formatDate = format.format(new Date());
		return formatDate;
	}
}
