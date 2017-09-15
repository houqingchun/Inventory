package com.ivt.mis.common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

/**
 * License 验证类
 * 
 * @创建日期 2012-4-15
 * @作者 侯青春
 * @版本 1.0
 * @修改历史
 */
public class LicenseVerify {
	private LicenseVerify() {
	}

	/**
	 * 解码SN
	 * 
	 * @param sn
	 * @return
	 */
	private static String decodeSn(String sn) {
		return new String(Base64.decodeBase64(sn.getBytes()));
	}

	/**
	 * 验证是否有效的license，若无效，应该跳转至license注册页，注册后写入license.properties注册信息
	 * 
	 * @param pubKeyText
	 * @param sn
	 * @param signText
	 * @return
	 */
	public static boolean verify(byte[] pubKeyText, String sn, byte[] signText) {
		try {
			// 解密由base64编码的公钥,并构造X509EncodedKeySpec对象
			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(
					Base64.decodeBase64(pubKeyText));
			// RSA对称加密算法
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			// 取公钥匙对象
			PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
			// 解密由base64编码的数字签名
			byte[] signed = Base64.decodeBase64(signText);
			Signature signatureChecker = Signature.getInstance("MD5withRSA");
			signatureChecker.initVerify(pubKey);
			signatureChecker.update(decodeSn(sn).getBytes());

			// 验证签名是否正常
			if (signatureChecker.verify(signed)) {
				return true;
			} else {
				return false;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将字串集合转换为字串
	 * 
	 * @param strList
	 * @return
	 */
	private static String convertToString(List<String> strList) {
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < strList.size(); i++) {
			bf.append(strList.get(i));
		}

		return bf.toString();
	}

	/**
	 * 取得SN值《"Hengtong/00-21-CC-6D-A2-D1/30/true/2012-6-20"》
	 * 
	 * @return
	 */
	public static String[] getSNValue() {
		String sn = null;
		try {
			sn = convertToString(IOUtils.readLines(new InputStreamReader(
					LicenseVerify.class.getResourceAsStream("/sn.properties"))));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (sn != null) {
			// 返回可注册用户量
			return decodeSn(sn).split("/");
		} else {
			return null;
		}
	}

	/**
	 * 验证License是否有效，或是否已经过期
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String verifyLicense() {
		String sn = null;
		String signText = null;
		String result = null;
		try {

			sn = convertToString(IOUtils.readLines(new InputStreamReader(
					LicenseVerify.class.getResourceAsStream("/sn.properties"))));
			signText = convertToString(IOUtils.readLines(new InputStreamReader(
					LicenseVerify.class
							.getResourceAsStream("/signature.properties"))));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (sn != null && signText != null) {
			// 是否是有效的license
			boolean isValid = verify(Constants.LICENSE_PUBLICK_KEY.getBytes(),
					sn, signText.getBytes());
			if (isValid) {

				String[] snValue = getSNValue();
				
				// 检查机器码
				boolean isTrial = Boolean.valueOf(snValue[3]);
				if (!isTrial) {
					// 正版
					String regMac = snValue[1];
					String localCpuNbr = RuntimeUtil.getLocalCPUNbr();
					if (regMac.equals(localCpuNbr)) {
						result = Constants.LICENSE_VALID;
					} else {
						// 注册时机器码地址与本机不符
						result = Constants.LICENSE_CODE_NOT_MATCH;
					}
				} else {					
					// 试用版
					// 检查判断是否过期(联网)
					String startDate = snValue[4];
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date sDate = sf.parse(startDate);
						Date networkDate = LicenseVerify.getNetworkDate();
						Calendar cal = Calendar.getInstance();
						cal.setTime(sDate);
						cal.add(Calendar.DAY_OF_MONTH,
								Constants.LICENSE_TRIAL_DAYS);
						Date expiredDate = cal.getTime();
												
						if (networkDate.before(expiredDate)) {
							result = Constants.LICENSE_VALID;
						} else {
							// 过期
							result = Constants.LICENSE_EXPIRED;
						}
					} catch (ParseException e) {
						e.printStackTrace();
						// 无效license
						result = Constants.LICENSE_INVALID;
					}
				}
			} else {
				// License 无效
				result = Constants.LICENSE_INVALID;
			}
		} else {
			// 未购买license
			result = Constants.LICENSE_NOT_REGIST;
		}
		return result;
	}

	public static Date getNetworkDate() {
		URL url;
		URLConnection uc = null;
		try {
			url = new URL("http://www.baidu.com");
			// 取得资源对象
			uc = url.openConnection();
			uc.connect(); // 发出连接
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		long ld = uc.getDate(); // 取得网站日期时间
		Date date = new Date(ld); // 转换为标准时间对象
		return date;
	}
}
