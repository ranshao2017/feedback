package com.sense.frame.common.util;

import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * 类描述:commons-codec是Apache下面的一个加解密开发包
 * 
 * @author qinchao 创建时间 2013-12-13
 */
public class Base64Util {

	/**
	 * 转base64字符串为字节数组
	 * 
	 * @return
	 * @throws Exception
	 */
	public static byte[] parseBase64StrToByte(String base64Str)
			throws Exception {
		// 将base64转换为byte[]
		Base64 base64 = new Base64();
		byte[] b = base64Str.getBytes();
		b = base64.decode(b);
		return b;
	}

	/**
	 * 将base64转化成BMP格式进行保存，完成后返回路径
	 * 
	 * @param base64
	 *            base64类型
	 */
	public static String saveBase64ToBMPWithType(String base64Str, String path,
			String type) throws Exception {

		// 将base64转换为byte[]
		Base64 base64 = new Base64();
		byte[] b = base64Str.getBytes();
		b = base64.decode(b);
		/*
		 * String path="d:\\"+DateUtil.dateToString(new Date(),
		 * "yyMMddHHmmssSSS")+ StringUtil.randomString(5)+".bmp";
		 */
		InputStream fileInput = FileAndStreamUtil.byteTOInputStream(b);
		if (type.equals("2")) {
			BmpUtils.savePicNoHead(fileInput, path);
		} else {
			BmpUtils.savePicAppendHead(fileInput, path);
		}
		return path;
	}

	/**
	 * 
	 * 创建日期2011-4-25上午10:12:38 修改日期 作者： 使用Base64加密算法加密字符串 return
	 */
	public static String encodeStr(String plainText) {
		String rtnString = "";
		if (plainText != null && plainText != "") {
			byte[] b = plainText.getBytes();
			Base64 base64 = new Base64();
			b = base64.encode(b);

			rtnString = new String(b);
		}

		return rtnString;
	}

	/**
	 * 
	 * 创建日期2011-4-25上午10:15:11 修改日期 作者： 使用Base64解密 return
	 */
	public static String decodeStr(String encodeStr) {
		String rtnStr = "";
		if (encodeStr != null && encodeStr != "") {
			byte[] b = encodeStr.getBytes();
			Base64 base64 = new Base64();
			b = base64.decode(b);
			rtnStr = new String(b);
		}

		return rtnStr;
	}

	/**
	 * 生成授权码
	 */
	public static String generateAuthCode(String plainText) {
		int[] randonEncodeOrder = { 6, 2, 1, 4, 8, 0, 3, 9, 5, 7 };
		String cliper = encodeStr(plainText); // BASE64加密

		// 加密之后，打乱顺序，防止解密
		int length = cliper.length();
		String tmpStr;
		int leftLen = length;
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i * 10 < length; i++) {
			if (leftLen > 10) {
				tmpStr = cliper.substring(i * 10, i * 10 + 10);
				for (int j = 0; j < 10; j++) {
					sbf.append(tmpStr.substring(randonEncodeOrder[j],
							randonEncodeOrder[j] + 1));
				}
				leftLen = leftLen - 10;
			} else {
				tmpStr = cliper.substring(i * 10, i * 10 + leftLen);
				sbf.append(tmpStr);
			}
		}
		return sbf.toString();
	}

	/**
	 * 解密授权码
	 */
	public static String decodeAuthCode(String cliperText) {
		// 把乱序的密码整理成真正的密文
		int[] randonDecodeOrder = { 5, 2, 1, 6, 3, 8, 0, 9, 4, 7 };
		int length = cliperText.length();
		String tmpStr;
		int leftLen = length;
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i * 10 < length; i++) {
			if (leftLen > 10) {
				tmpStr = cliperText.substring(i * 10, i * 10 + 10);

				for (int j = 0; j < 10; j++) {
					sbf.append(tmpStr.substring(randonDecodeOrder[j],
							randonDecodeOrder[j] + 1));
				}

				leftLen = leftLen - 10;

			} else {
				tmpStr = cliperText.substring(i * 10, i * 10 + leftLen);

				sbf.append(tmpStr);
			}

		}

		return decodeStr(sbf.toString()); // BASE64解密
	}

	public static void main(String[] args) {
		String plainT = "HSUEAMSINTF-HITOO2016";
		System.out.println("原文：" + plainT);

//		String cliper = Base64Util.encodeStr(plainT);
//		System.out.println("BASE64加密后：" + cliper);

		String cliperDisOrder = Base64Util.generateAuthCode(plainT);
		System.out.println("BASE64加密,并打乱顺序\n给用户的授权码：" + cliperDisOrder);

		System.out.println("解密授权码:" + Base64Util.decodeAuthCode(cliperDisOrder));
	}
}
