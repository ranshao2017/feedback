package com.sense.feedback.problem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.sense.feedback.entity.Problem;
import com.sense.feedback.problem.service.ProblemService;
import com.sense.frame.base.BaseController;
import com.sense.frame.base.BusinessException;
import com.sense.frame.pub.model.PageInfo;

@Controller
@RequestMapping("/problem")
public class ProblemController extends BaseController {
	
	@Autowired
	private ProblemService problemService;
	@Value("#{propertiesReader[FILE_PATH]}")
	private String filePath;
	
	/**
	 * 发布问题页面
	 */
	@RequestMapping("/forwardAddProblem")
	public String forwardAddProblem(HttpServletRequest request, ModelMap map) throws Exception {
		return "problem/addproblem";	
	}
	
	/**
	 * 发布问题
	 */
	@RequestMapping("/addProblem")
	@ResponseBody
	public Map<String, Object> addProblem(HttpServletRequest request, @Valid Problem problem) throws Exception {
		problemService.addProblem(problem, getLoginInfo(request));
		return this.writeSuccMsg("发布问题成功");
	}
	
	/**
	 * 修改问题
	 */
	@RequestMapping("/modifyProblem")
	@ResponseBody
	public Map<String, Object> modifyProblem(@Valid Problem problem) throws Exception {
		problemService.modifyQuality(problem);
		return this.writeSuccMsg("修改问题成功");
	}
	
	/**
	 * 问题管理页面
	 */
	@RequestMapping("/forwardProManage")
	public String forwardProManage(HttpServletRequest request, ModelMap map) throws Exception {
		return "problem/managepro";	
	}
	
	/**
	 * 查看所有问题页面
	 */
	@RequestMapping("/forwardAllPro")
	public String forwardAllPro(HttpServletRequest request, ModelMap map) throws Exception {
		return "problem/allproblem";	
	}
	
	/**
	 * 回复问题
	 */
	@RequestMapping("/replyProblem")
	@ResponseBody
	public Map<String, Object> replyProblem(HttpServletRequest request, String problemID, String descr) throws Exception {
		if(StringUtils.isBlank(descr) || StringUtils.isBlank(problemID)){
			throw new BusinessException("传入参数不合法");
		}
		problemService.replyProblem(problemID, descr, getLoginInfo(request));
		return this.writeSuccMsg("回复问题成功");
	}
	
	/**
	 * 回复问题页面
	 */
	@RequestMapping("/forwardReplyProblem")
	public String forwardReplyProblem(HttpServletRequest request, ModelMap map) throws Exception {
		return "problem/replyproblem";	
	}
	
	/**
	 * 分页检索当前用户发布的问题
	 */
	@RequestMapping("/queryProblemWithPage")
	@ResponseBody     
	public PageInfo queryProblemWithPage(HttpServletRequest request)throws Exception{	
		Map<String, String> paras = getRequestPara(request);
		return problemService.queryProblemWithPage(getPageInfo(request), getLoginInfo(request), paras);
	}
	
	/**
	 * 分页检索问题
	 */
	@RequestMapping("/queryAllProblemWithPage")
	@ResponseBody     
	public PageInfo queryAllProblemWithPage(HttpServletRequest request)throws Exception{	
		Map<String, String> paras = getRequestPara(request);
		return problemService.queryAllProblemWithPage(getPageInfo(request), paras);
	}
	
	/**
	 * 删除问题
	 */
	@RequestMapping("/delProblem")
	@ResponseBody
	public Map<String, Object> delProblem(HttpServletRequest request, String id) throws Exception {
		problemService.delProblem(id);
		return this.writeSuccMsg("删除问题成功");
	}
	
	/**
	 * 上传问题描述图片
	 */
	@RequestMapping("/addProblemImg")
	public void addProblemImg(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8"); 
		MultipartFile mf = request.getFile("problemImg");
		if(null == mf){
			response.getWriter().write("FAI-上传文件不能为空");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String imgPath = "problem/" + sdf.format(new Date()) + mf.getOriginalFilename().substring(mf.getOriginalFilename().lastIndexOf("."));
		File aimFile = new File(filePath + imgPath);
		if(!aimFile.getParentFile().exists()){
			aimFile.getParentFile().mkdirs();
		}
		OutputStream os = new FileOutputStream(aimFile);
		InputStream is = mf.getInputStream();
		IOUtils.copy(is, os);
		IOUtils.closeQuietly(is);
		IOUtils.closeQuietly(os);
		response.getWriter().write("SUC-" + imgPath);
	}
	
	/**
	 * 显示图片
	 */
	@RequestMapping("/showPic")
	public void showPic(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("image/*");
		String imgPath = request.getParameter("imgPath");
		InputStream is = new FileInputStream(new File(filePath + imgPath));
		OutputStream os = response.getOutputStream();
		IOUtils.copy(is, os);
		IOUtils.closeQuietly(is);
		IOUtils.closeQuietly(os);
	}
	
	/**
	 * 显示图片
	 */
	@RequestMapping("/showPicA")
	public void showPicA(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String imgPath = request.getParameter("imgPath");
		InputStream inStream = new FileInputStream(new File(filePath + imgPath));
		OutputStream outStream = response.getOutputStream();
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
	
	/**
	 * 打开修改问题页面
	 */
	@RequestMapping("/forwardModifyProblem")
	public String forwardModifyProblem(String problemID, ModelMap map) throws Exception {
		Problem problem = problemService.queryProble(problemID);
		String problemJson = JSON.toJSONStringWithDateFormat(problem, "yyyy-MM-dd HH:mm:ss");
		map.put("problemJson", problemJson);
		return "problem/modifyproblem";	
	}
	
	/**
	 * 打开问题详情页面
	 */
	@RequestMapping("/forwardDetail")
	public String forwardDetail(String problemID, ModelMap map) throws Exception {
		Problem problem = problemService.queryProble(problemID);
		String problemJson = JSON.toJSONStringWithDateFormat(problem, "yyyy-MM-dd HH:mm:ss");
		map.put("problemJson", problemJson);
		return "problem/detailproblem";	
	}
	
	/**
	 * 检索反馈记录
	 */
	@RequestMapping("/queryProblemReply")
	@ResponseBody     
	public PageInfo queryProblemReply(HttpServletRequest request, String problemID)throws Exception{	
		return problemService.queryProblemReply(getPageInfo(request), problemID);
	}
	
	/**
	 * 关闭问题
	 */
	@RequestMapping("/closeProblem")
	@ResponseBody
	public Map<String, Object> closeProblem(HttpServletRequest request, String id) throws Exception {
		problemService.closeProblem(id);
		return this.writeSuccMsg("关闭问题成功");
	}
}