package com.ivt.mis.common;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class RuntimeUtil {
	public static String getLocalMac() {
		InetAddress ia = null;
		try {
			ia = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 获取网卡，获取地址
		byte[] mac = null;
		try {
			mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		} catch (SocketException e) {
			e.printStackTrace();
		}
//		System.out.println("mac数组长度：" + mac.length);
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// 字节转换为整数
			int temp = mac[i] & 0xff;
			String str = Integer.toHexString(temp);
//			System.out.println("每8位:" + str);
			if (str.length() == 1) {
				sb.append("0" + str);
			} else {
				sb.append(str);
			}
		}
		return sb.toString().toUpperCase();
	}
	
	public static String getLocalCPUNbr(){
//		Process process = null;
//		try {
//			process = Runtime.getRuntime().exec(
//			        new String[] { "wmic", "cpu", "get", "ProcessorId" });
//			process.getOutputStream().close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Scanner sc = new Scanner(process.getInputStream());
//		String property = sc.next();
//		String serial = sc.next();
		
		return getLocalMac();
	}
}
