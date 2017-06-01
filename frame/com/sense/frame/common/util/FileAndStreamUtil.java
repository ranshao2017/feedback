package com.sense.frame.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件操作工具类
 * 
 * @author gxy
 */
public class FileAndStreamUtil {

	/**
	 * 将byte[]转化成inputStream
	 * 
	 * @param in
	 *            byte[]类型
	 */
	public static InputStream byteTOInputStream(byte[] in) {
		ByteArrayInputStream is = new ByteArrayInputStream(in);
		return is;
	}

	/**
	 * 生成文件
	 * 
	 * @param ins
	 * @param file
	 * @throws BaseException
	 */
	public static void inputstreamtofile(InputStream ins, File file)
			throws Exception {
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		ins.close();
	}

	/**
	 * 生成md5值
	 * 
	 * @param srcFile
	 * @return String
	 * @throws BaseException
	 */
	public static String getFileMD5(String srcFile) throws Exception {
		File file = new File(srcFile);
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			throw new Exception("生成md5值出错！");
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	/**
	 * 将InputStream转换成byte数组
	 * 
	 * @param in
	 *            InputStream
	 * @return byte[]
	 * @throws BaseException
	 * @throws IOException
	 */
	public static byte[] InputStreamTOByte(InputStream in) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[4096];
		int count = -1;
		while ((count = in.read(data, 0, 4096)) != -1)
			outStream.write(data, 0, count);
		data = null;
		return outStream.toByteArray();
	}

	/**
	 * 把文档流内容输出到页面
	 * 
	 * @author qinchao
	 * @date 创建时间 2014-4-8
	 */
	public static void exportDocumentToPage(HttpServletResponse response,
			InputStream inStream, String fileFormat) throws Exception {
		exportDocumentToPageOrDownload(response, inStream, "", fileFormat==null?"":fileFormat.toUpperCase(), true);
	}

	/**
	 * 把文档流内容输出到页面
	 * 
	 * @author qinchao
	 * @date 创建时间 2014-4-8
	 */
	public static void exportDocumentToDownload(HttpServletResponse response,
			InputStream inStream, String fileName,String fileFormat) throws Exception {
		exportDocumentToPageOrDownload(response, inStream, fileName, fileFormat == null ? "" : fileFormat.toUpperCase(), false);
	}
	
	/**
	 * 把文档流内容输出到页面或者页面提示下载
	 * 
	 * @author qinchao
	 * @date 创建时间 2014-4-8
	 */
	private static void exportDocumentToPageOrDownload(
			HttpServletResponse response, InputStream inStream,
			String fileName, String fileFormat, boolean exportToPageMark)
			throws Exception {
		if (response == null || inStream == null) {
			return;
		}

		OutputStream outStream = response.getOutputStream();

		// setContentType 控制哪个插件可以打开内容
		if (exportToPageMark) {
			if ("PDF".equals(fileFormat)) {
				// pdf文件 ,setContentType
				response.setContentType("application/pdf");
			} else if ("DOC".equals(fileFormat)||"DOCX".equals(fileFormat)) {
				// word
				response.setContentType("application/msword");
			} else if ("RAR".equals(fileFormat)) {
				// rar 不能显示在页面，但是可以下载
				return;
			} else if ("EXCEL".equals(fileFormat)) {
				// excel
				response.setContentType("application/vnd.ms-excel");
			} else if ("STREAM".equals(fileFormat)) {
				response.setContentType("application/octet-stream");
			}else if ("IMAGE".equals(fileFormat)) {
				 response.setContentType("image/*"); 
			}
			// 否则就是默认格式
		}

		// 设置返回页面的头 meta 信息,下载附件内容
		if (!exportToPageMark) {
			response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode(fileName, "UTF-8")+ "."+fileFormat);
		}

		if (inStream != null) {
			BufferedInputStream bfins = new BufferedInputStream(inStream);
			BufferedOutputStream bfouts = new BufferedOutputStream(outStream);
			byte[] buffer = new byte[102400];
			int len = 0;
			while ((len = bfins.read(buffer)) != -1) {
				bfouts.write(buffer, 0, len);
				buffer = null;
				buffer = new byte[102400];
			}
			if (bfins != null) {
				bfins.close();
			}
			if (bfouts != null) {
				bfouts.close();
			}
			outStream.flush();
		}

		if (null != inStream) {
			inStream.close();
		}
		if (null != outStream) {
			outStream.close();
		}
	}

}
