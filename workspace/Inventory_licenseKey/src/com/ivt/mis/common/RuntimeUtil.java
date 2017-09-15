package com.ivt.mis.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(
			        new String[] { "wmic", "cpu", "get", "ProcessorId" });
			process.getOutputStream().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Scanner sc = new Scanner(process.getInputStream());
		String property = sc.next();
		String serial = sc.next();
		
		return serial;
	}
	
	public static String getSerialNumber(String drive) {
		  String result = "";
		    try {
		      File file = File.createTempFile("realhowto",".vbs");
		      file.deleteOnExit();
		      FileWriter fw = new java.io.FileWriter(file);

		      String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
		                  +"Set colDrives = objFSO.Drives\n"
		                  +"Set objDrive = colDrives.item(\"" + drive + "\")\n"
		                  +"Wscript.Echo objDrive.SerialNumber";  // see note
		      fw.write(vbs);
		      fw.close();
		      Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
		      BufferedReader input =
		        new BufferedReader
		          (new InputStreamReader(p.getInputStream()));
		      String line;
		      while ((line = input.readLine()) != null) {
		         result += line;
		      }
		      input.close();
		    }
		    catch(Exception e){
		        e.printStackTrace();
		    }
		    return result.trim();
		  }


	
	public static void main(String[] args){
		System.out.println(getSerialNumber("C"));
	}
}
