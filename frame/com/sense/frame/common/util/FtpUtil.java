package com.sense.frame.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.sense.frame.base.BusinessException;

public class FtpUtil {
	private String ftpIP = "";// FTP服务器的IP地址
	private int ftpPort = -1;// FTP服务器的端口号
	private String ftpUser = "";// FTP服务器的用户
	private String ftpPsw = "";// FTP服务器的登陆密码
	private FTPClient ftpClient = null;// FTP客户端

	private int bufferSize = 524288;//每次上传缓冲区大小

	/**
	 * 构造方法（不指定服务器端口号）
	 */
	public FtpUtil(String ftpIP, String ftpUser, String ftpPsw) {
		this.ftpIP = ftpIP;
		this.ftpUser = ftpUser;
		this.ftpPsw = ftpPsw;
	}

	/**
	 * 构造方法（指定服务器端口号）
	 */
	public FtpUtil(String ftpIP, int ftpPort, String ftpUser, String ftpPsw) {
		this.ftpIP = ftpIP;
		this.ftpPort = ftpPort;
		this.ftpUser = ftpUser;
		this.ftpPsw = ftpPsw;
	}

	/**
	 * 构造方法（指定服务器端口号和每次上传缓冲区大小）
	 */
	public FtpUtil(String ftpIP, int ftpPort, String ftpUser, String ftpPsw,
			int bufferSize) {
		this.ftpIP = ftpIP;
		this.ftpPort = ftpPort;
		this.ftpUser = ftpUser;
		this.ftpPsw = ftpPsw;
		this.bufferSize = bufferSize;
	}

	/**
	 * 连接FTP，实例化FTPClient
	 * 
	 * @throws Exception
	 */
	public boolean connect() throws Exception {
		try {
			// 连接FTP服务器
			ftpClient = new FTPClient();
			if (ftpPort > 0) {
				ftpClient.connect(ftpIP, ftpPort);
			} else {
				ftpClient.connect(ftpIP);
			}
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				return false;
			}

			// 登陆FTP服务器
			if (!ftpClient.login(ftpUser, ftpPsw)) {
				throw new BusinessException("登陆FTP服务器的用户名或密码不正确！");
			}

			ftpClient.setControlEncoding("GBK");
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.setBufferSize(bufferSize);// 1024
			ftpClient.changeWorkingDirectory("/");
			ftpClient.enterLocalPassiveMode();

			// 设置默认超时
			// ftpClient.setDefaultTimeout(60000);
			// 设置数据超时
			// ftpClient.setDataTimeout(60000);
			// 设置连接超时
			// ftpClient.setConnectTimeout(60000);
			// socket超时
			// ftpClient.setSoTimeout(60000);
		} catch (IOException e) {
			throw new BusinessException("FTP连接异常！" + e.getMessage());
		}
		return true;
	}

	/**
	 * 断开FTP
	 * 查看org.apache.commons.net的API，可以看到，FTPClient的logout()也会抛出IOException的,
	 * 如果disconnect()直接写在logout后面，积少成多ftp不会关闭，从而连接异常
	 * 
	 * logout多次会无缘无故挂起，不知何故，直接disconnect，暂时没发现问题。
	 * 
	 * @throws Exception
	 */
	public void disconnect() throws Exception {
		if (ftpClient != null) {
			try {
				// ftpClient.logout();
				// ftpClient.disconnect();
				// } catch (IOException e) {
				// throw new BusinessException("FTP断开出错！" + e.getMessage());
			} finally {
				if (ftpClient.isConnected()) {
					try {
						ftpClient.disconnect();
					} catch (IOException e) {
						throw new BusinessException("FTP断开出错！" + e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * 验证当前目录下某一子目录是否存在
	 */
	public boolean existDirectory(String path) throws Exception {
		FTPFile[] ftpFileArr = ftpClient.listFiles();
		for (FTPFile ftpFile : ftpFileArr) {
			if (ftpFile.isDirectory() && ftpFile.getName().equalsIgnoreCase(path)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 变更当前工作目录,path必须以/开始的完整路径或者当前目录下的子目录
	 */
	public boolean changeDirectory(String path) throws Exception {
		return ftpClient.changeWorkingDirectory(path);
	}

	/**
	 * 切换到当前工作目录的上级目录,path必须以/开始的完整路径
	 */
	public boolean changeToParentDirectory() throws Exception {
		return ftpClient.changeToParentDirectory();
	}

	/**
	 * 在当前工作目录下创建新文件子目录
	 */
	public boolean createAndChangeDirectory(String pathName) throws Exception {
		if (!ftpClient.makeDirectory(pathName)) {
			return false;
		}
		return changeDirectory(pathName);
	}

	/**
	 * 在当前目录下创建多级文件目录
	 */
	public boolean createAndChangeDirectory(String fullPathName, boolean isCascade) throws Exception {
		if (!isCascade) {
			return createAndChangeDirectory(fullPathName);
		}
		String[] filePaths = fullPathName.split("/");
		for (String filePath : filePaths) {
			if (StringUtils.isBlank(filePath)) {
				continue;
			}
			if (!existDirectory(filePath)) {// 是否存在目录
				if (!createAndChangeDirectory(filePath)) {
					return false;
				}
			} else {
				if (!changeDirectory(filePath)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 删除目录及包含的所有文件,不级联删除子目录
	 */
	public boolean removeDirectory(String path) throws Exception {
		return ftpClient.removeDirectory(path);
	}

	/**
	 * 删除目录及包含的所有文件，isALL控制是否级联删除子目录
	 */
	public boolean removeDirectory(String path, boolean isAll) throws Exception {
		if (!isAll) {
			return removeDirectory(path);
		}

		FTPFile[] ftpFileArr = ftpClient.listFiles(path);
		if (ftpFileArr == null || ftpFileArr.length == 0) {
			return removeDirectory(path);
		}

		for (FTPFile ftpFile : ftpFileArr) {
			String name = ftpFile.getName();
			if (ftpFile.isDirectory()) {
				removeDirectory(path + "/" + name, true);
			} else if (ftpFile.isFile()) {
				deleteFile(path + "/" + name);
			}
		}
		return ftpClient.removeDirectory(path);
	}

	/**
	 * 获取FTP服务器下某路径中所有的文件名列表 List<String>
	 */
	public List<String> getFileNameList(String path) throws Exception {
		FTPFile[] ftpFiles = ftpClient.listFiles(path);
		List<String> retList = new ArrayList<String>();
		if (ftpFiles == null || ftpFiles.length == 0) {
			return retList;
		}
		for (FTPFile ftpFile : ftpFiles) {
			if (ftpFile.isFile()) {
				retList.add(ftpFile.getName());
			}
		}
		return retList;
	}

	/**
	 * 获取FTP服务器下某路径中所有的文件列表String[],包括子目录名
	 */
	public String[] getFileNameArray(String path) throws Exception {
		return ftpClient.listNames(path);
	}

	/**
	 * 删除指定文件
	 * 
	 * @param fileName
	 *            文件名
	 * @throws Exception
	 */
	public boolean deleteFile(String pathName) throws Exception {
		return ftpClient.deleteFile(pathName);
	}

	/**
	 * 上传文件到当前目录：本地文件名可以同上传文件名不一致
	 * @param fileName 文件名
	 */
	public boolean uploadFile(String fileName, String newName) throws Exception {
		boolean flag = false;
		InputStream iStream = null;
		try {
			iStream = new FileInputStream(new File(fileName));
			flag = ftpClient.storeFile(newName, iStream);
		} catch (IOException e) {
			return false;
		} finally {
			if (iStream != null) {
				iStream.close();
			}
		}
		return flag;
	}

	/**
	 * 上传文件到当前目录：文件名同本地文件名
	 * @param fileName 文件名
	 */
	public boolean uploadFile(String fileName) throws Exception {
		return uploadFile(fileName, fileName);
	}

	/**
	 * 上传文件到当前目录：按输入流上传
	 */
	public boolean uploadFile(InputStream iStream, String newName) throws Exception {
		try {
			return ftpClient.storeFile(newName, iStream);
		} catch (IOException e) {
			return false;
		} finally {
			if (iStream != null) {
				iStream.close();
			}
		}
	}

	/**
	 * 下载当前工作目录下的文件到本地指定文件
	 */
	public boolean downloadFile(String remoteFileName, String localFilePath,
			String localFileName) throws IOException {
		boolean flag = false;
		File outfile = new File(localFilePath + "/" + localFileName);
		OutputStream oStream = null;
		try {
			oStream = new FileOutputStream(outfile);
			flag = ftpClient.retrieveFile(remoteFileName, oStream);
		} catch (IOException e) {
			return false;
		} finally {
			oStream.close();
		}
		return flag;
	}

	/**
	 * 下载当前工作目录下的文件到输入流
	 */
	public InputStream downloadFile(String sourceFileName) throws Exception {
		return ftpClient.retrieveFileStream(sourceFileName);
	}

	/**
	 * 下载当前工作目录下的指定目录的所有文件到输入流 据说需要ftpClient每个文件都要断开重连才行？？？
	 */
	public HashMap<String, InputStream> downloadAllFile(String ftpFilePath) throws Exception {
		String[] fileNames = ftpClient.listNames(ftpFilePath);
		int length = fileNames.length;
		HashMap<String, InputStream> resultMap = new HashMap<String, InputStream>();
		for (int i = 0; i < length; i++) {
			String fileName = fileNames[i];
			resultMap.put(fileName, ftpClient.retrieveFileStream(fileName));
		}
		return resultMap;
	}

}