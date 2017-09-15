package com.ivt.mis.common;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;


/**
 * LicenseKeyGenerater license 生成类
 * 
 * @创建日期 2012-4-15
 * @作者 侯青春
 * @版本 1.0
 * @修改历史
 */
public class LicenseKeyGenerater {
    private byte[] priKey;

    private byte[] pubKey;

    /**
     * 生成密钥对
     */
    public void generater() {
        try {

            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            SecureRandom secrand = new SecureRandom();
            secrand.setSeed("www.conwisdom.cn".getBytes()); // 初始化随机产生器
            keygen.initialize(1024, secrand);
            KeyPair keys = keygen.genKeyPair();
            PublicKey pubkey = keys.getPublic();
            PrivateKey prikey = keys.getPrivate();
            pubKey = Base64.encodeBase64Chunked(pubkey.getEncoded());
            priKey = Base64.encodeBase64(prikey.getEncoded());

        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }

    }

    public byte[] getPriKey() {

        return priKey;

    }

    public byte[] getPubKey() {

        return pubKey;

    }

    /**
     * byte转哈希
     * 
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs += ("0" + stmp);
            else
                hs += stmp;
        }
        return hs.toUpperCase();
    }

    /**
     * 哈希转byte
     * 
     * @param b
     * @return
     */
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");

        byte[] b2 = new byte[b.length / 2];

        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    public static void main(String[] args) {
        LicenseKeyGenerater kg = new LicenseKeyGenerater();
        kg.generater();
        
        String cpuNbr = RuntimeUtil.getLocalCPUNbr();
        System.out.println(cpuNbr);
        
        // 公司名/MAC地址/使用天数/是否为试用版本/生成时间
//        String info = "hewei/00-23-CD-AA-C9-54/90/true/2014-8-20";
        String info = "hewei/BFEBFBFF000306A9/30/true/2016-08-26";
//        String info = "hewei/00-00-00-00-00-00/90/true/2014-09-01";
        
        String encodeInfo = new String(Base64.encodeBase64(info.getBytes()));

        byte[] signat = LicenseSignaturer.sign(kg.priKey, info);

        // 生成后即卖给客户，客户注册时将如下信息写入license.properties文件内， key名见license.properties文件

//        System.out.println("pubkey:\n" + new String(kg.getPubKey()));
        System.out.println("sn:\n" + encodeInfo);
//        System.out.println("prikey:\n" + new String(kg.getPriKey()));
        System.out.println("signature:\n" + new String(signat));
        System.out.println(LicenseVerify.verifyLicense());
//        System.out.println("MAC(License):" + LicenseVerify.getSNValue()[1]);
//        System.out.println("MAC(Local):" + MacUtil.getLocalMac());
    }
}
