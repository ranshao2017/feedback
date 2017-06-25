package com.sense.feedback.cartest;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import jxl.Workbook;
import jxl.write.DateFormats;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

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
import com.sense.frame.base.BusinessException;
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
	 * 批量接车页面
	 */
	@RequestMapping("/forwardPickCars")
	public String forwardPickCars(HttpServletRequest request, ModelMap map) throws Exception {
		String scdhs = request.getParameter("scdhs");
		map.put("scdhs", scdhs);
		return "cartest/pickcars";
	}
	
	/**
	 * 下线提交至调车
	 */
	@RequestMapping("/submitTC")
	@ResponseBody
	public Map<String, Object> submitTC(HttpServletRequest request, @Valid ProcInst inst) throws Exception {
		if(StringUtils.isNotBlank(inst.getXzOrgID())){
			String xzOrg = inst.getXzOrgID().replaceAll("-", ",");
			inst.setXzOrgID(xzOrg);
		}
		String qjData = request.getParameter("qjData");
		carTestService.submitTC(inst, getLoginInfo(request), qjData);
		return this.writeSuccMsg("已提交至调车");
	}
	
	/**
	 * 下线批量提交至调车
	 */
	@RequestMapping("/submitTCs")
	@ResponseBody
	public Map<String, Object> submitTCs(HttpServletRequest request, @Valid ProcInst inst) throws Exception {
		String scdhs = request.getParameter("scdhs");
		if(StringUtils.isNotBlank(inst.getXzOrgID())){
			String xzOrg = inst.getXzOrgID().replaceAll("-", ",");
			inst.setXzOrgID(xzOrg);
		}
		carTestService.submitTCs(inst, getLoginInfo(request), scdhs);
		return this.writeSuccMsg("已提交至调车");
	}
	
	/**
	 * 缺件不可调车
	 */
	@RequestMapping("/saveXx")
	@ResponseBody
	public Map<String, Object> saveXx(HttpServletRequest request, @Valid ProcInst inst) throws Exception {
		String qjData = request.getParameter("qjData");
		if(StringUtils.isNotBlank(inst.getXzOrgID())){
			String xzOrg = inst.getXzOrgID().replaceAll("-", ",");
			inst.setXzOrgID(xzOrg);
		}
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
	 * 各模块记录统计
	 */
	@RequestMapping("/queryCTCount")
	@ResponseBody
	public Map<String, Object> queryCTCount(String status) throws Exception {
		Map<String, Object> paras = carTestService.queryCTCount(status);
		return this.writeSuccMsg("", paras);
	}
	
	/**
	 * 调试、故障排除、送检页面
	 */
	@RequestMapping("/forwardCT")
	public String forwardCT(HttpServletRequest request, ModelMap map) throws Exception {
		String scdh = request.getParameter("scdh");
		ProcInst inst = carTestService.queryProcInst(scdh);
		if(EnumProcNode.sy.getCode().equals(inst.getStatus())){
			map.put("backBtn", "退回故障排除");
			map.put("unQuailyBtn", "复检不合格");
		}
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
	 * 批量调试、故障排除、送检页面
	 */
	@RequestMapping("/forwardCTs")
	public String forwardCTs(HttpServletRequest request, ModelMap map) throws Exception {
		String scdhs = request.getParameter("scdhs");
		String[] scdhArr = scdhs.split(",");
		ProcInst inst = carTestService.queryProcInst(scdhArr[0]);
		if(EnumProcNode.sy.getCode().equals(inst.getStatus())){
			map.put("backBtn", "退回故障排除");
			map.put("unQuailyBtn", "复检不合格");
		}
		map.put("scdhs", scdhs);
		return "cartest/ctcars";	
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
	 * 退回至前一环节
	 */
	@RequestMapping("/backCT")
	@ResponseBody
	public Map<String, Object> backCT(HttpServletRequest request, @Valid ProcInstNode inst) throws Exception {
		carTestService.backCT(inst, getLoginInfo(request));
		return this.writeSuccMsg("已退回");
	}
	
	/**
	 * 复检不合格
	 */
	@RequestMapping("/unQuailyCT")
	@ResponseBody
	public Map<String, Object> unQuailyCT(HttpServletRequest request, @Valid ProcInstNode inst) throws Exception {
		carTestService.unQuailyCT(inst, getLoginInfo(request));
		return this.writeSuccMsg("保存不合格信息");
	}
	
	/**
	 * 批量提交处理，至下一环节
	 */
	@RequestMapping("/submitCTs")
	@ResponseBody
	public Map<String, Object> submitCTs(HttpServletRequest request, @Valid ProcInstNode inst) throws Exception {
		String scdhs = request.getParameter("scdhs");
		if(StringUtils.isBlank(scdhs)){
			throw new BusinessException("参数不合法");
		}
		carTestService.submitCTs(inst, getLoginInfo(request), scdhs);
		return this.writeSuccMsg("已提交");
	}
	
	/**
	 * 批量退回处理，至前一环节
	 */
	@RequestMapping("/backCTs")
	@ResponseBody
	public Map<String, Object> backCTs(HttpServletRequest request, @Valid ProcInstNode inst) throws Exception {
		String scdhs = request.getParameter("scdhs");
		if(StringUtils.isBlank(scdhs)){
			throw new BusinessException("参数不合法");
		}
		carTestService.backCTs(inst, getLoginInfo(request), scdhs);
		return this.writeSuccMsg("已退回");
	}
	
	/**
	 * 批量复检不合格
	 */
	@RequestMapping("/unQuailyCTs")
	@ResponseBody
	public Map<String, Object> unQuailyCTs(HttpServletRequest request, @Valid ProcInstNode inst) throws Exception {
		String scdhs = request.getParameter("scdhs");
		if(StringUtils.isBlank(scdhs)){
			throw new BusinessException("参数不合法");
		}
		carTestService.unQuailyCTs(inst, getLoginInfo(request), scdhs);
		return this.writeSuccMsg("已保存复检不合格信息");
	}
	
	/**
	 * 入库信息
	 */
	@RequestMapping("/forwardCarRepo")
	public String forwardCarRepo(HttpServletRequest request, ModelMap map) throws Exception {
		return "cartest/cartrepotm";	
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
		return "cartest/unpickcartm";	
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
		return "cartest/unpickcar";
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
		if(StringUtils.isNotBlank(inst.getXzOrgID())){
			String xzOrg = inst.getXzOrgID().replaceAll("-", ",");
			inst.setXzOrgID(xzOrg);
		}
		carTestService.saveUXx(inst, getLoginInfo(request), qjData);
		return this.writeSuccMsg("已保存缺件不可调车");
	}
	
	/**
	 * 协助调车
	 */
	@RequestMapping("/forwardXzTC")
	public String forwardXzTC(HttpServletRequest request, ModelMap map) throws Exception {
		boolean tsOrg = carTestService.queryTsFlag(getLoginInfo(request).getOrgId());
		map.put("tsOrg", tsOrg);
		if(!tsOrg){
			map.put("xzOrgID", getLoginInfo(request).getOrgId());
		}
		return "cartest/xzcartm";	
	}
	
	/**
	 * 分页检索协助调车信息
	 */
	@RequestMapping("/queryXZCTPage")
	@ResponseBody     
	public PageInfo queryXZCTPage(HttpServletRequest request) throws Exception{	
		Map<String, String> paras = getRequestPara(request);
		return carTestService.queryXZCTPage(getPageInfo(request), paras);
	}
	
	/**
	 * 协助详情
	 */
	@RequestMapping("/forwardXzInfo")
	public String forwardXzInfo(HttpServletRequest request, ModelMap map) throws Exception {
		String scdh = request.getParameter("scdh");
		ProcInst inst = carTestService.queryProcInst(scdh);
		String instJson = JSON.toJSONStringWithDateFormat(inst, "yyyy-MM-dd HH:mm:ss");
		map.put("instJson", instJson);
		if(EnumYesNo.yes.getCode().equals(inst.getQjFlag())){
			String qjJson = carTestService.queryQJData(scdh);
			map.put("qjJson", qjJson);
		}
		String preNodeListJson = carTestService.queryPreInstNodeList(scdh, inst.getStatus());
		if(StringUtils.isNotBlank(preNodeListJson)){
			map.put("preNodeListJson", preNodeListJson);
		}
		map.put("closeReply", inst.getCloseFlag());
		String replyListJson = carTestService.queryReplyList(scdh);
		if(StringUtils.isNotBlank(replyListJson)){
			map.put("replyListJson", replyListJson);
		}
		return "cartest/xzcarinfo";
	}
	
	/**
	 * 提交回复
	 */
	@RequestMapping("/submitReply")
	@ResponseBody
	public Map<String, Object> submitReply(HttpServletRequest request) throws Exception {
		String scdh = request.getParameter("scdh");
		String descr = request.getParameter("descr");
		if(StringUtils.isBlank(scdh) || StringUtils.isBlank(descr)){
			throw new BusinessException("参数不合法");
		}
		carTestService.submitReply(scdh, descr, getLoginInfo(request));
		return this.writeSuccMsg("已提交回复");
	}
	
	/**
	 * 关闭回复
	 */
	@RequestMapping("/closeReply")
	@ResponseBody
	public Map<String, Object> closeReply(HttpServletRequest request) throws Exception {
		String scdh = request.getParameter("scdh");
		if(StringUtils.isBlank(scdh)){
			throw new BusinessException("参数不合法");
		}
		carTestService.closeReply(scdh);
		return this.writeSuccMsg("已关闭回复");
	}
	
	/**
	 * 导出
	 */
	@RequestMapping("/exportCT")
	public void exportCT(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> paraMap = getRequestPara(request);
		List<ProcInst> instList = carTestService.queryExportCT(paraMap);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    OutputStream os = response.getOutputStream();//取得输出流
	    response.reset();//清空输出流
	    response.setCharacterEncoding("UTF-8");//设置相应内容的编码格式
	    String fileName = java.net.URLEncoder.encode(EnumProcNode.getLabelByCode(paraMap.get("status")) + "记录" + df.format(new Date()), "UTF-8");
	    response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "GBK") + ".xls");
	    response.setContentType("application/msexcel");//定义输出类型
	    
        WritableWorkbook workbook = Workbook.createWorkbook(os);//创建工作薄
        WritableSheet sheet = workbook.createSheet(EnumProcNode.getLabelByCode(paraMap.get("status")) + "记录", 0);//创建新的一页
        //创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
        String[] titleArr = new String[] {"底盘号", "随车单号", "车型", "订单号", "下线时间", "发动机", "配置", "是否缺件", "备注"};
        for (int i = 0; i < titleArr.length; i++) {
        	Label title = new Label(i, 0, titleArr[i]);
            sheet.addCell(title);
		}
        
        for(int i = 0; i < instList.size(); i ++){
        	ProcInst inst = instList.get(i);
        	sheet.addCell(new Label(0, i + 1, inst.getDph()));
        	sheet.addCell(new Label(1, i + 1, inst.getScdh()));
        	sheet.addCell(new Label(2, i + 1, inst.getCx()));
        	sheet.addCell(new Label(3, i + 1, inst.getDdh()));
        	
        	WritableCellFormat wcf = new WritableCellFormat(DateFormats.FORMAT1);
            DateTime createDt = new DateTime(4, i + 1, inst.getJcsj(), wcf);
            sheet.addCell(createDt);//下线时间
            
            sheet.addCell(new Label(5, i + 1, inst.getFdj()));
            sheet.addCell(new Label(6, i + 1, inst.getPz()));
            sheet.addCell(new Label(7, i + 1, EnumYesNo.getLabelByCode(inst.getQjFlag())));
            sheet.addCell(new Label(8, i + 1, inst.getBz()));
        }
        
        workbook.write();
        workbook.close();
        os.close();
	}
	
}