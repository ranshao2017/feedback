package com.sense.sys.pst;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sense.frame.base.BaseController;
import com.sense.frame.base.BusinessException;
import com.sense.frame.pub.model.PageInfo;
import com.sense.frame.pub.model.TreeModel;
import com.sense.sys.entity.Pst;
import com.sense.sys.pst.service.PstService;

/**
 * 组织机构管理Controller控制器
 * @author zhangqinghe
 * @version 2.0
 */
@Controller
@RequestMapping("/sys/pst")
public class PstController extends BaseController {
	
	@Autowired
	private PstService pstService;
	
	/**
	 * 打开岗位管理
	 */
	@RequestMapping("/forwardPst")
	public String forwardPst(HttpServletRequest request) throws Exception {
		return "sys/pst/pstindex";	
	}
	
	/**
	 * 新增岗位页面
	 */
	@RequestMapping("/forwardAddPst")
	public String forwardAddPst(HttpServletRequest request) throws Exception {
		return "sys/pst/addpst";	
	}
	
	/**
	 * 获取指定部门的岗位树，不带根节点
	 */
	@RequestMapping("/queryPstTreeWithRoot")
	@ResponseBody
	public List<TreeModel> queryPstTreeWithRoot(String orgID) throws Exception {
		return pstService.queryPstTreeWithRoot(orgID);
	}
	
	/**
	 * 新增岗位
	 */
	@RequestMapping("/addPst")
	@ResponseBody
	public Map<String, Object> addPst(HttpServletRequest request, @Valid Pst pst) throws Exception {
		String pstID = pstService.addPst(pst);
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID", pstID);
		return this.writeSuccMsg("新增成功", paramMap);
	}
	
	/**
	 * 根据部门分页检索岗位
	 */
	@RequestMapping("/queryPstWithPage")
	@ResponseBody     
	public PageInfo queryPstWithPage(HttpServletRequest request, String orgID)throws Exception{	
		if (StringUtils.isBlank(orgID)) {
			throw new BusinessException("请传入部门ID！");
		}
		PageInfo pi = getPageInfo(request);
		return pstService.queryPstWithPage(pi, orgID);
	}
	
	/**
	 * 修改岗位页面
	 */
	@RequestMapping("/forwardModifyPst")
	public String forwardModifyPst() throws Exception {
		return "sys/pst/modifypst";	
	}
	
	/**
	 * 根据ID获取机构信息
	 */
	@RequestMapping("/queryPstByID")
	@ResponseBody
	public Map<String, Object> queryPstByID(String pstID) throws Exception {
		if(StringUtils.isBlank(pstID)){
			throw new BusinessException("岗位ID不能为空！");
		}
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pst", pstService.queryPstByID(pstID));
		return this.writeSuccMsg("", paramMap);
	}
	
	/**
	 * 修改岗位
	 */
	@RequestMapping("/modifyPst")
	@ResponseBody
	public Map<String, Object> modifyPst(@Valid Pst pst) throws Exception {
		String pstID = pstService.modifyPst(pst);
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID", pstID);
		return this.writeSuccMsg("修改成功", paramMap);
	}
	
	/**
	 * 删除选定岗位
	 */
	@RequestMapping("/delPst")
	@ResponseBody
	public Map<String, Object> delPst(@RequestBody List<String> pstCheckList) throws Exception {
		if(CollectionUtils.isEmpty(pstCheckList)){
			throw new BusinessException("请先选定岗位！");
		}
		pstService.delPst(pstCheckList);
		return this.writeSuccMsg("删除岗位成功");
	}
	
	/**
	 * 查询用户拥有的岗位列表
	 */
	@RequestMapping("/queryOwnedPstDgByUser")
	@ResponseBody
	public PageInfo queryOwnedPstDgByUser(HttpServletRequest request, String usrID, String orgID) throws Exception{
		return pstService.queryOwnedPstDgByUser(usrID, orgID);
	}
	
	/**
	 * 查询用户没有拥有的岗位列表，但是可以作为可选
	 */
	@RequestMapping("/queryNotOwnedPstDgByUser")
	@ResponseBody
	public PageInfo queryNotOwnedPstDgByUser(HttpServletRequest request, String usrID, String orgID) throws Exception{
		return pstService.queryNotOwnedPstDgByUser(usrID, orgID);
	}
	
	/**
	 * 配置反馈处理流程环节权限
	 */
	@RequestMapping("/forwardPstWF")
	public String forwardPstWF(HttpServletRequest request) throws Exception {
		return "sys/pst/setpstwf";	
	}
	
	/**
	 * 根据岗位ID，获取该岗位的反馈流程节点权限
	 */
	@RequestMapping("/queryPstWFTree")
	@ResponseBody
	public List<TreeModel> queryPstWFTree(String pstID) throws Exception{
		return pstService.queryPstWFTree(pstID);
	}
	
	/**
	 * 对岗位进行流程环节赋权
	 */	
	@RequestMapping("/savePstProc")
	@ResponseBody
	public Map<String, Object> savePstProc(String pstID, String nodeIDs) throws Exception{
		pstService.saveUsrProc(pstID ,nodeIDs);
		return this.writeSuccMsg("保存成功！");		
	}
}