package com.sense.feedback.cartest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sense.feedback.cartest.service.CarTestService;
import com.sense.feedback.entity.PaiChan;
import com.sense.feedback.entity.ProcInst;
import com.sense.feedback.entity.ProcInstNode;
import com.sense.feedback.enumdic.EnumProcNode;
import com.sense.feedback.enumdic.EnumYesNo;
import com.sense.frame.base.BaseController;
import com.sense.frame.pub.model.PageInfo;

@Controller
@RequestMapping("/cartest")
public class CarTestController extends BaseController {
	
	@Autowired
	private CarTestService carTestService;
	
	/**
	 * 接车下线页面
	 */
	@RequestMapping("/forwardPickCarM")
	public String forwardPickCarM(HttpServletRequest request, ModelMap map) throws Exception {
		return "cartest/pickcarmanage";	
	}
	
	/**
	 * 分页检索排产信息
	 */
	@RequestMapping("/queryPCPage")
	@ResponseBody     
	public PageInfo queryPCPage(HttpServletRequest request)throws Exception{	
		Map<String, String> paras = getRequestPara(request);
		return carTestService.queryPCPage(getPageInfo(request), paras);
	}
	
	/**
	 * 接车页面
	 */
	@RequestMapping("/forwardPickCar")
	public String forwardPickCar(HttpServletRequest request, ModelMap map) throws Exception {
		String scdh = request.getParameter("scdh");
		PaiChan pc = carTestService.queryPCDetail(scdh);
		String pcJson = JSON.toJSONStringWithDateFormat(pc, "yyyy-MM-dd HH:mm:ss");
		map.put("pcJson", pcJson);
		return "cartest/pickcar";	
	}
	
	/**
	 * 下线提交至调车
	 */
	@RequestMapping("/submitTC")
	@ResponseBody
	public Map<String, Object> submitTC(HttpServletRequest request, @Valid ProcInst inst) throws Exception {
		String qjData = request.getParameter("qjData");
		carTestService.submitTC(inst, getLoginInfo(request), qjData);
		return this.writeSuccMsg("已提交至调车");
	}
	
	/**
	 * 缺件不可调车
	 */
	@RequestMapping("/saveXx")
	@ResponseBody
	public Map<String, Object> saveXx(HttpServletRequest request, @Valid ProcInst inst) throws Exception {
		String qjData = request.getParameter("qjData");
		carTestService.saveXx(inst, getLoginInfo(request), qjData);
		return this.writeSuccMsg("已保存缺件不可调车");
	}
	
	/**
	 * 调试页面
	 */
	@RequestMapping("/forwardCarTSM")
	public String forwardCarTSM(HttpServletRequest request, ModelMap map) throws Exception {
		map.put("status", EnumProcNode.ts.getCode());
		return "cartest/cartmange";	
	}
	
	/**
	 * 故障排除
	 */
	@RequestMapping("/forwardCarGZM")
	public String forwardCarGZM(HttpServletRequest request, ModelMap map) throws Exception {
		map.put("status", EnumProcNode.gz.getCode());
		return "cartest/cartmange";	
	}
	
	/**
	 * 送验
	 */
	@RequestMapping("/forwardCarSYM")
	public String forwardCarSYM(HttpServletRequest request, ModelMap map) throws Exception {
		map.put("status", EnumProcNode.sy.getCode());
		return "cartest/cartmange";	
	}
	
	/**
	 * 分页检索整车调试流程信息
	 */
	@RequestMapping("/queryCTPage")
	@ResponseBody     
	public PageInfo queryCTPage(HttpServletRequest request) throws Exception{	
		Map<String, String> paras = getRequestPara(request);
		return carTestService.queryCTPage(getPageInfo(request), paras);
	}
	
	/**
	 * 调试、故障排除、送检页面
	 */
	@RequestMapping("/forwardCT")
	public String forwardCT(HttpServletRequest request, ModelMap map) throws Exception {
		String scdh = request.getParameter("scdh");
		ProcInst inst = carTestService.queryProcInst(scdh);
		String instJson = JSON.toJSONStringWithDateFormat(inst, "yyyy-MM-dd HH:mm:ss");
		map.put("instJson", instJson);
		if(EnumYesNo.yes.getCode().equals(inst.getQjFlag())){
			String qjJson = carTestService.queryQJData(scdh);
			map.put("qjJson", qjJson);
		}
		ProcInstNode node = carTestService.queryProcInstNode(scdh, inst.getStatus());
		if(null != node){
			String nodeJson = JSON.toJSONStringWithDateFormat(node, "yyyy-MM-dd HH:mm:ss");
			map.put("nodeJson", nodeJson);
		}
		String preNodeListJson = carTestService.queryPreInstNodeList(scdh, inst.getStatus());
		if(StringUtils.isNotBlank(preNodeListJson)){
			map.put("preNodeListJson", preNodeListJson);
		}
		return "cartest/ctcar";	
	}
	
	/**
	 * 暂存处理
	 */
	@RequestMapping("/saveCT")
	@ResponseBody
	public Map<String, Object> saveCT(HttpServletRequest request, @Valid ProcInstNode inst) throws Exception {
		carTestService.saveCT(inst, getLoginInfo(request));
		return this.writeSuccMsg("已暂存");
	}
	
	/**
	 * 提交处理，至下一环节
	 */
	@RequestMapping("/submitCT")
	@ResponseBody
	public Map<String, Object> submitCT(HttpServletRequest request, @Valid ProcInstNode inst) throws Exception {
		carTestService.submitCT(inst, getLoginInfo(request));
		return this.writeSuccMsg("已提交");
	}
	
	/**
	 * 入库信息
	 */
	@RequestMapping("/forwardCarRepo")
	public String forwardCarRepo(HttpServletRequest request, ModelMap map) throws Exception {
		return "cartest/cartrepo";	
	}
	
	/**
	 * 分页检索入库信息
	 */
	@RequestMapping("/queryRepoPage")
	@ResponseBody     
	public PageInfo queryRepoPage(HttpServletRequest request) throws Exception{	
		Map<String, String> paras = getRequestPara(request);
		return carTestService.queryRepoPage(getPageInfo(request), paras);
	}
	
	/**
	 * 入库详情
	 */
	@RequestMapping("/forwardRepoInfo")
	public String forwardRepoInfo(HttpServletRequest request, ModelMap map) throws Exception {
		String scdh = request.getParameter("scdh");
		ProcInst inst = carTestService.queryProcInst(scdh);
		String instJson = JSON.toJSONStringWithDateFormat(inst, "yyyy-MM-dd HH:mm:ss");
		map.put("instJson", instJson);
		if(EnumYesNo.yes.getCode().equals(inst.getQjFlag())){
			String qjJson = carTestService.queryQJData(scdh);
			map.put("qjJson", qjJson);
		}
		ProcInstNode node = carTestService.queryProcInstNode(scdh, inst.getStatus());
		if(null != node){
			String nodeJson = JSON.toJSONStringWithDateFormat(node, "yyyy-MM-dd HH:mm:ss");
			map.put("nodeJson", nodeJson);
		}
		String preNodeListJson = carTestService.queryPreInstNodeList(scdh, inst.getStatus());
		if(StringUtils.isNotBlank(preNodeListJson)){
			map.put("preNodeListJson", preNodeListJson);
		}
		return "cartest/ctcarinfo";
	}
	
	/**
	 * 不可调车页面
	 */
	@RequestMapping("/forwardUnTC")
	public String forwardUnTC(HttpServletRequest request, ModelMap map) throws Exception {
		map.put("status", EnumProcNode.jc.getCode());
		return "cartest/cartmange";	
	}
	
	/**
	 * 调车处理
	 */
	@RequestMapping("/forwardPCTCar")
	public String forwardPCTCar(HttpServletRequest request, ModelMap map) throws Exception {
		String scdh = request.getParameter("scdh");
		ProcInst inst = carTestService.queryProcInst(scdh);
		String instJson = JSON.toJSONStringWithDateFormat(inst, "yyyy-MM-dd HH:mm:ss");
		map.put("instJson", instJson);
		if(EnumYesNo.yes.getCode().equals(inst.getQjFlag())){
			String qjJson = carTestService.queryQJData(scdh);
			map.put("qjJson", qjJson);
		}
		return "cartest/unpickcarm";
	}
	
	/**
	 * 下线提交至调车(不可调车处理)
	 */
	@RequestMapping("/submitUTC")
	@ResponseBody
	public Map<String, Object> submitUTC(HttpServletRequest request, @Valid ProcInst inst) throws Exception {
		String qjData = request.getParameter("qjData");
		carTestService.submitUTC(inst, getLoginInfo(request), qjData);
		return this.writeSuccMsg("已提交至调车");
	}
	
	/**
	 * 缺件不可调车(不可调车处理)
	 */
	@RequestMapping("/saveUXx")
	@ResponseBody
	public Map<String, Object> saveUXx(HttpServletRequest request, @Valid ProcInst inst) throws Exception {
		String qjData = request.getParameter("qjData");
		carTestService.saveUXx(inst, getLoginInfo(request), qjData);
		return this.writeSuccMsg("已保存缺件不可调车");
	}
	
	/**
	 * 协助调车
	 */
	@RequestMapping("/forwardXzTC")
	public String forwardXzTC(HttpServletRequest request, ModelMap map) throws Exception {
		map.put("xzOrgID", getLoginInfo(request).getOrgId());
		return "cartest/xzcart";	
	}
}