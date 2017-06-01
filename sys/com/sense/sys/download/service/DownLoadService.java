package com.sense.sys.download.service;

import com.sense.sys.entity.DownLoad;

public interface DownLoadService {
	
	/**
	 * 常用下载
	 */
	public DownLoad querydownLoadAssistFile(String fileID) throws Exception;

}