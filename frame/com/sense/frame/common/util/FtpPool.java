package com.sense.frame.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;

/**
 * 通过common.pool实现ftp的连接池
 * @author zhangqh
 */
public class FtpPool {
	
	private static Log log = LogFactory.getLog(FtpPool.class);
	
	private static String ftpHost;//地址
    private static int ftpPort;//端口
    private static String ftpUser;//用户
    private static String ftpPassword;//密码
	
	private static final FtpPool instance = new FtpPool();
	
	private FtpPool(){}
	
	private static GenericObjectPool pool;
	
	public static FtpPool getInstance(String host, int port, String user, String password){
		if(null == pool){
			ftpHost = host;
			ftpPort = port;
			ftpUser = user;
			ftpPassword = password;
			Config config = new Config();
			config.testWhileIdle = true;
			config.minEvictableIdleTimeMillis = 60000L;
			config.timeBetweenEvictionRunsMillis = 30000L;
			config.maxActive = 100;
			pool = new GenericObjectPool(new FTPClientFactory(), config);
		}
		return instance;
	}
	
	/**
	 * 从连接池中获取资源
	 */
	public FTPClient getResource() {
		FTPClient client = null;
		try {
			client = (FTPClient) pool.borrowObject();
		} catch (Exception e) {
			log.error("无法从连接池中获取资源", e);
		}
		return client;
	}

	/**
	 * 将资源归还给连接池
	 */
	public boolean returnResource(FTPClient resource) {
		boolean result = false;
		try {
			pool.returnObject(resource);
			result = true;
		} catch (Exception e) {
			log.error("无法将资源归还给连接池", e);
		}
		return result;
	}

	/**
	 * 销毁资源
	 */
	public boolean invalidateBrokenResource(FTPClient resource) {
		boolean result = false;
		try {
			pool.invalidateObject(resource);
			result = true;
		} catch (Exception e) {
			log.error("无法将资源销毁", e);
		}
		return result;
	}

	/**
	 * 销毁连接池
	 */
	public boolean destroy() {
		boolean result = false;
		try {
			pool.close();
			result = true;
		} catch (Exception e) {
			log.error("无法销毁连接池", e);
		}
		return result;
	}
	
	private static class FTPClientFactory extends BasePoolableObjectFactory {
		
		/**
		 * 创建一个新对象;当对象池中的对象个数不足时,将会使用此方法来"输出"一个新的"对象",并交付给对象池管理.
		 */
		public Object makeObject() throws Exception {
			FTPClient ftpClient = new FTPClient();
			ftpClient.connect(ftpHost, ftpPort);
			ftpClient.login(ftpUser, ftpPassword); 
			
			ftpClient.setControlEncoding("GBK");
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.setBufferSize(524288);
			ftpClient.changeWorkingDirectory("/");
			ftpClient.enterLocalPassiveMode();
			
			return ftpClient;
		}

		/**
		 * 销毁对象,如果对象池中检测到某个"对象"idle的时间超时,
		 * 或者操作者向对象池"归还对象"时检测到"对象"已经无效,那么此时将会导致"对象销毁"
		 */
		public void destroyObject(Object obj) throws Exception {
			if ((obj == null) || (!(obj instanceof FTPClient)))
				return;
			FTPClient ftpClient = (FTPClient) obj;
			if(!ftpClient.isConnected()){
				return;
			}
			ftpClient.disconnect();
		}

		/**
		 * 检测对象是否"有效"
		 */
		public boolean validateObject(Object obj) {
			if(obj instanceof FTPClient){  
	            FTPClient ftpClient=(FTPClient) obj;  
	            try{  
	                return ftpClient.isConnected();  
	            }catch(Exception e){  
	                return false;  
	            }  
	        }
			return false;
		}
	}
}