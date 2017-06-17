package com.sense.feedback.statist;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sense.feedback.statist.service.ComplexService;
import com.sense.frame.base.BaseController;
import com.sense.frame.pub.model.PageInfo;

@Controller
@RequestMapping("/complex")
public class ComplexController extends BaseController {
	
	@Autowired
	private ComplexService complexService;
	
	/**
	 * 不可调车页面
	 */
	@RequestMapping("/forwardTCPage")
	public String forwardTCPage(HttpServletRequest request, ModelMap map) throws Exception {
		return "statist/cartestquery";
	}
	
	/**
	 * 分页检索整车调试流程信息
	 */
	@RequestMapping("/queryCarTPage")
	@ResponseBody     
	public PageInfo queryCarTPage(HttpServletRequest request) throws Exception{	
		Map<String, String> paras = getRequestPara(request);
		return complexService.queryCarTPage(getPageInfo(request), paras);
	}
	
	/**
	 * 查询各环节总数
	 */
	@RequestMapping("/queryStaCount")
	@ResponseBody
	public Map<String, Object> queryStaCount() throws Exception {
		Map<String, Object> paras = complexService.queryStaCount();
		return this.writeSuccMsg("", paras);
	}
	
	/**
	 * 超期分析页面
	 */
	@RequestMapping("/forwardOverPage")
	public String forwardOverPage(HttpServletRequest request, ModelMap map) throws Exception {
		return "statist/cartoverquery";
	}
	
	/**
	 * 超期分析查询
	 */
	@RequestMapping("/queryCarTOverPage")
	@ResponseBody     
	public PageInfo queryCarTOverPage(HttpServletRequest request) throws Exception{	
		return complexService.queryCarTOverPage(getPageInfo(request));
	}
	
}