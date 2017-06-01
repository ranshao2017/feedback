package com.sense.sys.org;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sense.frame.base.BaseController;
import com.sense.frame.base.BusinessException;
import com.sense.frame.pub.model.TreeModel;
import com.sense.sys.entity.Org;
import com.sense.sys.org.service.OrgService;

/**
 * 组织机构管理Controller控制器
 * @author zhangqinghe
 * @version 2.0
 */
@Controller
@RequestMapping("/sys/org")
public class OrgController extends BaseController {
	
	@Autowired
	private OrgService orgService;
	
	/**
	 * 获取机构树，不带根节点
	 */
	@RequestMapping("/queryAllOrgTree")
	@ResponseBody
	public List<TreeModel> queryAllOrgTree(HttpServletRequest request) throws Exception {
		return orgService.getAllOrgTree();
	}
	
	/**
	 * 获取机构树，带根节点
	 */
	@RequestMapping("/queryOrgTreeWithRoot")
	@ResponseBody
	public List<TreeModel> queryOrgTreeWithRoot(HttpServletRequest request) throws Exception {
		return orgService.getOrgTreeWithRoot();
	}
	
	/**
	 * 根据ID获取机构信息
	 */
	@RequestMapping("/queryOrgByID")
	@ResponseBody
	public Map<String, Object> queryOrgByID(HttpServletRequest request,String orgID ) throws Exception {
		if(StringUtils.isBlank(orgID)){
			throw new BusinessException("机构ID不能为空！");
		}
		//把查询到的机构放到map中，返回
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("Org", orgService.queryOrgByID(orgID));
		
		return this.writeSuccMsg("", paramMap);
	}
	
	/**
	 * 新增机构
	 */
	@RequestMapping("/addOrg")
	@ResponseBody
	public Map<String, Object> addOrg(HttpServletRequest request, @Valid Org org) throws Exception {
		String orgID = orgService.addOrg(org);
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID",orgID);
		
		return this.writeSuccMsg("", paramMap);
	}
	
	/**
	 * 删除机构
	 */
	@RequestMapping("/delOrg")
	@ResponseBody
	public Map<String, Object> delOrg(HttpServletRequest request,String delID ) throws Exception {
		if(StringUtils.isBlank(delID)){
			throw new BusinessException("要删除的机构ID不能为空！");
		}
		orgService.delOrg(delID);
		return this.writeSuccMsg("删除机构成功！");
	}
	
	/**
	 * 修改机构
	 */
	@RequestMapping("/modifyOrg")
	@ResponseBody
	public Map<String, Object> modifyOrg(HttpServletRequest request, @Valid Org org) throws Exception {
		orgService.modifyOrg(org);
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID", org.getOrgID());
		
		return this.writeSuccMsg("", paramMap); 
	}
	
	//----------------------跳转页面
	/**
	 * 主菜单：机构管理
	 */
	@RequestMapping("/forwardOrg")
	public String forwardOrg(HttpServletRequest request) throws Exception {
		return "sys/org/orgindex";	
	}
	
	/**
	 * 跳转到新增机构页面
	 */
	@RequestMapping("/forwardAddOrg")
	public String forwardAddOrg(HttpServletRequest request) throws Exception {
		return "sys/org/addorg";	
	}
	
	/**
	 * 跳转到修改机构页面
	 */
	@RequestMapping("/forwardModifyorg")
	public String forwardModifyorg(HttpServletRequest request) throws Exception {
		return "sys/org/modifyorg";	
	}
	//----------------------end of 跳转页面	
}