package com.sense.sys.download;

import java.io.BufferedOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sense.frame.base.BaseController;
import com.sense.frame.base.BusinessException;
import com.sense.sys.download.service.DownLoadService;
import com.sense.sys.entity.DownLoad;

@Controller
@RequestMapping("/sys/download")
public class DownLoadController extends BaseController {
	
	@Autowired
	private DownLoadService downLoadService;
	
	/**
	 * 常用下载
	 */
	@RequestMapping("/downLoadAssistFile")
	@ResponseBody
	public void downLoadAssistFile(HttpServletRequest request, HttpServletResponse response, String fileID) throws Exception {
		if(StringUtils.isBlank(fileID)){
			throw new BusinessException("文件ID为空！");
		}
		DownLoad downLoad = downLoadService.querydownLoadAssistFile(fileID);
		if(downLoad==null){
			throw new BusinessException("没有找到你要下载文件的信息！");
		}
		byte[] btyeCont = downLoad.getBtyeCont();
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition","attachment; filename=" + java.net.URLEncoder.encode(downLoad.getNam(), "UTF-8")+"."+downLoad.getTyp());
		response.addHeader("Content-Length", "" + btyeCont.length);
		BufferedOutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        toClient.write(btyeCont);
        toClient.flush();
        toClient.close();
	}
	
}