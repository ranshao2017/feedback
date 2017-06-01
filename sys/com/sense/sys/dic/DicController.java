package com.sense.sys.dic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sense.frame.base.BaseController;
import com.sense.frame.pub.model.PageInfo;
import com.sense.sys.dic.service.DicService;
import com.sense.sys.entity.Dic;
import com.sense.sys.entity.DicDtl;

@Controller
@RequestMapping("/sys/dic")
public class DicController extends BaseController {
	
	@Autowired
	private DicService dicService;
	
	@Autowired
	private DicLoader dicLoader;
	
	/**
	 * 分页获取字典
	 */
	@RequestMapping("/queryDicWithPage")
	@ResponseBody
	public PageInfo queryDicWithPage(HttpServletRequest request,String dicNamOrCod)throws Exception {
		PageInfo pi = super.getPageInfo(request);
		return dicService.queryDicWithPage(pi, dicNamOrCod);
	}
	
	/**
	 * 分页获取字典明细，按照seqno排序
	 */
	@RequestMapping("/queryDicDtlWithPage")
	@ResponseBody
	public PageInfo queryDicDtlWithPage(HttpServletRequest request, String dicCod)throws Exception {
		PageInfo pi = super.getPageInfo(request);
		return dicService.queryDicDtlWithPage(pi, dicCod);
	}
	
	@RequestMapping("/addDic")
	@ResponseBody
	public Map<String, Object> addDic(HttpServletRequest request,
			@Valid Dic dic) throws Exception {
		dicService.addDic(dic);
		return this.writeSuccMsg("");
	}
	
	@RequestMapping("/editDic")
	@ResponseBody
	public Map<String, Object> editDic( HttpServletRequest request,String oldDicCod,String oldDicNam,
			@Valid Dic dic) throws Exception {
		dicService.editDic(oldDicCod ,oldDicNam ,dic);
		return this.writeSuccMsg("");
	}
	
	/**
	 * 删除某个字典（非基础类型的字典才允许修改、删除）
	 */
	@RequestMapping("/delDic")
	@ResponseBody
	public Map<String, Object> delDic( HttpServletRequest request,String dicCod) 
			throws Exception {
		dicService.delDic(dicCod);
		return this.writeSuccMsg("刪除字典成功！");
	}
	
	@RequestMapping("/addDicDtl")
	@ResponseBody
	public Map<String, Object> addSubDic(HttpServletRequest request,@Valid DicDtl dicDtl) throws Exception {
		dicService.addDicDtl(dicDtl);
		return this.writeSuccMsg("");
	}
	
	@RequestMapping("/editDicDtl")
	@ResponseBody
	public Map<String, Object> editDicDtl(HttpServletRequest request,@Valid DicDtl dicDtl) throws Exception {
		dicService.editDicDtl( dicDtl);
		return this.writeSuccMsg("");
	}
	
	
	@RequestMapping("/delDicDtl")
	@ResponseBody
	public Map<String, Object> delDicDtl( HttpServletRequest request,String dicCod,String dicDtlCod  ) 
			throws Exception {
		dicService.delDicDtl(dicCod, dicDtlCod);
		return this.writeSuccMsg("");
	}
	
	/**
	 * 刷新字典缓存
	 */
	@RequestMapping("/getDicCache")
	@ResponseBody
	public Map<String, Object> getDicCache( HttpServletRequest request) throws Exception {
		dicLoader.loadDic();
		return this.writeSuccMsg("字典缓存刷新完成！");
	}
	
	/**
	 * 枚举类型的字典缓存到客户端，cacheEnumData <br>
	 * 字典表中的内容缓存在服务器端，cacheDicData <br>
	 * 页面组装form中的下拉列表时，先再cacheEnumData中找，找到了就返回，找不到再从cacheDicData中查找找 <br>
	 * code都是大写的，前台传递过来的可能是大写或者小写，需要转为大写 <br>
	 * 以下为从服务器缓存中获取数据
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDicItemByCode")
	@ResponseBody
	public JSONArray queryDicItemByCode(HttpServletRequest request, String rootflag) throws Exception {
		String syscode = request.getParameter("syscode");
		String code = syscode == null ? "" : syscode;
		ServletContext servletContext = request.getSession().getServletContext();
		Map<String, Object> dicMap = (Map<String, Object>) servletContext.getAttribute(DicLoader.CONTEXT_DICCACHE);//dicList的code全部是大写
		
		List<Map<String, Object>> itemList = null;
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		//获取字典一起先判断是否存在，存在再取，否则返回空
		if(dicMap != null && dicMap.containsKey(code.toUpperCase())){
			itemList = (List<Map<String, Object>>) dicMap.get(code.toUpperCase());
		}else{
			//找不到再从数据库sys_dic中查找
			itemList = dicService.queryDicDtlList(code.toUpperCase());
		}
		if(CollectionUtils.isEmpty(itemList)){
			return new JSONArray();
		}
		if("root".equals(rootflag) && CollectionUtils.isNotEmpty(itemList)){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("value", "");
			map.put("text", "全部");
			returnList.add(0, map);
		}else if("null".equals(rootflag) && CollectionUtils.isNotEmpty(itemList)){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("value", "");
			map.put("text", "");
			returnList.add(0, map);
		}
		returnList.addAll(itemList);
		return (JSONArray) JSON.toJSON(returnList);
	}
	
	/**
	 * 枚举类型的字典缓存到客户端，cacheEnumData <br>
	 * 字典表中的内容缓存在服务器端，cacheDicData <br>
	 * 页面组装datagrid中的中文意思时，先再cacheEnumData中找，找到了就返回，找不到再从cacheDicData中查找找 <br>
	 * code都是大写的，前台传递过来的可能是大写或者小写，需要转为大写 <br>
	 * 以下为从服务器缓存中获取数据
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getDicDtlNamConverter")
	@ResponseBody
	public Map<String, Object> getDicDtlNamConverter(HttpServletRequest request, String dicCod, String dicDtlCod) throws Exception {
		String rtnDicDtlNam = dicDtlCod;
		ServletContext servletContext = request.getSession().getServletContext();
		Map<String, Object> dicMap = (Map<String, Object>) servletContext.getAttribute(DicLoader.CONTEXT_DICCACHE);
		List<Map<String, Object>> itemList = null;
		//获取字典一起先判断是否存在，存在再取，否则返回空
		if(dicMap != null && dicMap.containsKey(dicCod.toUpperCase())){
			itemList = (List<Map<String, Object>>) dicMap.get(dicCod.toUpperCase());
		}else{
			//找不到再从数据库sys_dic中查找
			itemList = dicService.queryDicDtlList(dicCod.toUpperCase());
		}
		if(CollectionUtils.isNotEmpty(itemList)){
			for(Map<String, Object> map : itemList){
				if(map.get("value") != null && map.get("value").equals(dicDtlCod)){
					rtnDicDtlNam = (String) map.get("text");
					break;
				}
			}
		}
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("dicDtlNam", rtnDicDtlNam);
		return this.writeSuccMsg("", rtnMap);
	}
	
	/**
	 * 主菜单：字典管理
	 */
	@RequestMapping("/forwardDicindex")
	public String forwardDicindex(HttpServletRequest request) throws Exception {
		return "sys/dic/dicindex";	
	}
	
	/**
	 * 新增、修改字典页面
	 */
	@RequestMapping("/forwardManagedic")
	public String forwardManagedic(HttpServletRequest request) throws Exception {
		return  "sys/dic/managedic";	
	}
	
	/**
	 * 新增、修改字典明细页面
	 */
	@RequestMapping("/forwardManagedtl")
	public String forwardManagedtl(HttpServletRequest request) throws Exception {
		return "sys/dic/managedtl";	
	}
}