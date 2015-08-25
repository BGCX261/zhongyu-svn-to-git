package com.piaoyou.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TransferMD5 {
	private static String back;
	
	public static String Md5To16(String plainText){
		return Md5(plainText).substring(8,24);
	}
	public static String md5To32(String plainText){
		return Md5(plainText);
	}
	private static String Md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			//System.out.println("result: " + buf.toString());// 32位的加密
			//System.out.println("result: " + buf.toString().substring(8, 24));// 16位的加密
			  back = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return back;
	}

	public static void main(String[] args) {
		System.out.println(TransferMD5.Md5("chaishu@163.com"));
	}
}
