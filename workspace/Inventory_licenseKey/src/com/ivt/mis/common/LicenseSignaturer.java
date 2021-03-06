package com.ivt.mis.common;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;


public class LicenseSignaturer {
    public static byte[] sign(byte[] priKeyText, String plainText) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(priKeyText));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey prikey = keyf.generatePrivate(priPKCS8);
            // 用私钥对信息生成数字签名
            Signature signet = java.security.Signature.getInstance("MD5withRSA");
            signet.initSign(prikey);
            signet.update(plainText.getBytes());
            byte[] signed = Base64.encodeBase64Chunked(signet.sign());
            return signed;
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
