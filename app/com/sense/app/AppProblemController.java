package com.sense.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSONArray;
import com.sense.app.service.AppProblemService;
import com.sense.app.util.AppJsonUtil;
import com.sense.frame.base.BaseController;
import com.sense.frame.pub.model.PageInfo;
import com.sense.sys.dic.DicLoader;

@Controller
@RequestMapping("/appProblem")
public class AppProblemController extends BaseController {
	
	private static Logger log = Logger.getLogger("appFileLog");
	
	@Autowired
	private AppProblemService appProblemService;
	@Autowired
	private DicLoader dicLoader;
	@Value("#{propertiesReader[FILE_PATH]}")
	private String filePath;
	
	/**
	 * 问题类别
	 */
	@RequestMapping("/queryProType")
	@ResponseBody
	public Map<String, Object> queryProType() {
		JSONArray dicAarray = dicLoader.getCacheDicList("PROTYPE");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("returnlist", dicAarray);
		return AppJsonUtil.writeSucc("操作成功！", paraMap);
	}
	
	/**
	 * 发布问题
	 */
	@RequestMapping("/publishProblem")
	@ResponseBody
	public Map<String, Object> publishProblem(String userid, String descr, String protype, MultipartHttpServletRequest request) {
		List<MultipartFile> imgList = request.getFiles("proimgfile");
		try {
			appProblemService.publishProblem(userid, descr, protype, imgList);
		} catch (Exception e) {
			log.error("发布问题异常", e);
			return AppJsonUtil.writeErr("发布问题异常");
		}
		return AppJsonUtil.writeSucc("发布问题成功！");
	}
	
	/**
	 * 查看问题列表
	 * @param pagerow 每页显示多少行
	 * @param pagesize 当前第几页
	 */
	@RequestMapping("/queryProblemPage")
	@ResponseBody
	public Map<String, Object> queryProblemPage(int pagerow, int pagesize) {
		try{
			PageInfo pi = new PageInfo();
			pi.setPageSize(pagerow);
			pi.setPageNumber(pagesize);
			List<Map<String, Object>> list = appProblemService.queryProblemPage(pi);
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("returnlist", list);
			return AppJsonUtil.writeSucc("操作成功", paraMap);
		}catch(Exception e){
			log.error("查看问题列表异常", e);
			return AppJsonUtil.writeErr("查看问题列表异常");
		}
	}
	
	/**
	 * 查看问题详情
	 */
	@RequestMapping("/queryProblem")
	@ResponseBody
	public Map<String, Object> queryProblem(String problemid) {
		try{
			Map<String, Object> problemMap = appProblemService.queryProblem(problemid);
			List<Map<String, Object>> replyList = appProblemService.queryProblemReply(problemid);
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("problem", problemMap);
			paraMap.put("returnlist", replyList);
			return AppJsonUtil.writeSucc("操作成功", paraMap);
		}catch(Exception e){
			log.error("查看问题详情异常", e);
			return AppJsonUtil.writeErr("查看问题详情异常");
		}
	}
	
	/**
	 * 回复问题
	 */
	@RequestMapping("/replyProblem")
	@ResponseBody
	public Map<String, Object> replyProblem(String userid, String descr, String problemid) {
		try {
			appProblemService.replyProblem(userid, descr, problemid);
		} catch (Exception e) {
			log.error("回复问题异常", e);
			return AppJsonUtil.writeErr("回复问题异常");
		}
		return AppJsonUtil.writeSucc("回复问题成功！");
	}
	
	@RequestMapping("/showBase64Pic")
	@ResponseBody
	public Map<String, Object> showPic(String imgpath) {
		InputStream is = null;
		try {
			is = new FileInputStream(new File(filePath + imgpath));
			byte[] buffer = IOUtils.toByteArray(is);
			String base64Pic = new BASE64Encoder().encode(buffer);
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("base64pic", base64Pic);
			return AppJsonUtil.writeSucc("查询图片成功", paraMap);
		} catch (Exception e) {
			log.error("查看图片异常", e);
			return AppJsonUtil.writeErr("查询图片异常");
		}finally{
			if(null != is){
				IOUtils.closeQuietly(is);
			}
		}
	}
	
	@RequestMapping("/showPic")
	public void showPic(HttpServletResponse response, String imgpath) {
		InputStream is = null;
		OutputStream os = null;
		try{
			is = new FileInputStream(new File(filePath + imgpath));
			os = response.getOutputStream();
			IOUtils.copy(is, os);
		}catch(Exception e){
			log.error("查看图片异常", e);
		}finally{
			if(null != is){
				IOUtils.closeQuietly(is);
			}
			if(null != os){
				IOUtils.closeQuietly(os);
			}
		}
		
	}
	
	public static void main(String[] args) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(
					"http://111.14.45.104:8400/feedback/appProblem/publishProblem.do");
			FileBody bin1 = new FileBody(new File("E://image//angelababy.jpg"));
			FileBody bin2 = new FileBody(new File("E://image//xusong.jpg"));
			HttpEntity reqEntity = MultipartEntityBuilder
					.create()
					.addPart("proimgfile", bin1)// 文件
					.addPart("proimgfile", bin2)
					.addPart("userid", new StringBody("7876ddf7a3084c7b9ca1e6ae5bfd9830",ContentType.create("text/plain", "utf-8")))
					.addPart("descr", new StringBody("质量问题很严重", ContentType.create("text/plain", "utf-8")))
					.addPart("protype", new StringBody("A", ContentType.create("text/plain", "utf-8"))).build();
			httpPost.setEntity(reqEntity);
			response = httpClient.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == 200){
				String result = EntityUtils.toString(response.getEntity(), "utf-8");
				System.out.println(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(response != null){
					response.close();
				}
			} catch (IOException e) { }
			try {
				if(httpClient != null){
					httpClient.close();
				}
			} catch (IOException e) { }
		}

	}
	
}