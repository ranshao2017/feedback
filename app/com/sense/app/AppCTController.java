package com.sense.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sense.app.dto.ProcInstDto;
import com.sense.app.dto.ProcInstNodeDto;
import com.sense.app.service.AppCTService;
import com.sense.app.util.AppJsonUtil;
import com.sense.frame.base.BaseController;
import com.sense.sys.dic.DicLoader;

@Controller
@RequestMapping("/appCT")
public class AppCTController extends BaseController {
	
	private static Logger log = Logger.getLogger("appFileLog");
	
	@Autowired
	private AppCTService appCTService;
	@Autowired
	private DicLoader dicLoader;
	
	/**
	 * 存放车位
	 */
	@RequestMapping("/queryCarSeat")
	@ResponseBody
	public Map<String, Object> queryCarSeat() {
		JSONArray dicAarray = dicLoader.getCacheDicList("CARSEAT");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("returnlist", dicAarray);
		return AppJsonUtil.writeSucc("操作成功！", paraMap);
	}
	
	/**
	 * 整车调试接口
	 */
	@RequestMapping("/queryCT")
	@ResponseBody
	public Map<String, Object> queryCT(String dph, String userid) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		ProcInstDto procInst = null;
		try {
			procInst = appCTService.queryProcInst(dph, userid);
			if(null == procInst){
				return AppJsonUtil.writeErr("未查询到信息");
			}
			paraMap.put("procinst", procInst);
		} catch (Exception e) {
			log.error("获取整车调试信息异常", e);
			return AppJsonUtil.writeErr("获取整车调试信息异常");
		}
		try {
			List<ProcInstNodeDto> nodeDtoList = appCTService.queryNodeList(procInst.getScdh());
			paraMap.put("returnlist", nodeDtoList);
		} catch (Exception e) {
			log.error("获取环节记录信息异常", e);
			return AppJsonUtil.writeErr("获取环节记录信息异常");
		}
		return AppJsonUtil.writeSucc("操作成功！", paraMap);
	}
	
	/**
	 * 整车调试保存接口
	 */
	@RequestMapping("/saveCT")
	@ResponseBody
	public Map<String, Object> saveCT(String scdh, String carseat, String descr, String userid) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		try {
			appCTService.saveCT(scdh, carseat, descr, userid);
		} catch (Exception e) {
			log.error("整车调试保存接口异常", e);
			return AppJsonUtil.writeErr("整车调试保存接口异常");
		}
		return AppJsonUtil.writeSucc("操作成功！", paraMap);
	}
	
	/**
	 * 整车调试提交接口
	 */
	@RequestMapping("/submitCT")
	@ResponseBody
	public Map<String, Object> submitCT(String scdh, String carseat, String descr, String userid) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		try {
			appCTService.submitCT(scdh, carseat, descr, userid);
		} catch (Exception e) {
			log.error("整车调试提交接口异常", e);
			return AppJsonUtil.writeErr("整车调试提交接口异常");
		}
		return AppJsonUtil.writeSucc("操作成功！", paraMap);
	}
	
	/**
	 * 缺件信息接口
	 */
	@RequestMapping("/queryQJ")
	@ResponseBody
	public Map<String, Object> queryQJ(String scdh) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> qjList = appCTService.queryQJ(scdh);
			paraMap.put("returnlist", qjList);
		} catch (Exception e) {
			log.error("缺件信息接口异常", e);
			return AppJsonUtil.writeErr("缺件信息接口异常");
		}
		return AppJsonUtil.writeSucc("操作成功！", paraMap);
	}
	
}