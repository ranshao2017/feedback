package com.sense.sys.usr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sense.frame.base.BaseController;
import com.sense.frame.base.BusinessException;
import com.sense.frame.pub.global.LoginInfo;
import com.sense.frame.pub.model.PageInfo;
import com.sense.frame.pub.model.TreeModel;
import com.sense.sys.entity.Usr;
import com.sense.sys.usr.service.UserService;

@Controller
@RequestMapping("/sys/usr")
public class UserController extends BaseController {

	@Autowired
	private UserService userService ;
	
	/**
	 * 获取用户的功能权限树
	 */
	@RequestMapping("/queryUsrAuthorityTree")
	@ResponseBody
	public List<TreeModel> queryUsrAuthorityTree(HttpServletRequest request, String usrID) throws Exception {
		return userService.queryUsrAuthorityTree(usrID);
	}
	
	/**
	 * 变更用户拥有的角色
	 */
	@RequestMapping("/changeRoleForUsr")
	@ResponseBody
	public Map<String, Object> changeRoleForUsr( HttpServletRequest request,String usrID,String roleID,String oper) throws Exception {
		boolean addFlag = false;
		if("add".equals(oper)){
			addFlag = true;
		}
		userService.changeRoleForUsr(usrID,roleID,addFlag);
		return this.writeSuccMsg("");
	}
	
	/**
	 * 变更用户拥有的岗位
	 */
	@RequestMapping("/changePstForUsr")
	@ResponseBody
	public Map<String, Object> changePstForUsr(HttpServletRequest request, String usrID, String pstID, String oper) throws Exception {
		boolean addFlag = false;
		if("add".equals(oper)){
			addFlag = true;
		}
		userService.changePstForUsr(usrID, pstID, addFlag);
		return this.writeSuccMsg("");
	}
	
	/**
	 * 注销用户：删除用户相关的权限，并更改用户状态为”2“
	 */
	@RequestMapping("/writeOffUsr")
	@ResponseBody
	public Map<String, Object> writeOffUsr( HttpServletRequest request,String usrID) throws Exception {
		userService.writeOffUsr(usrID);
		return this.writeSuccMsg("注销用户成功！");
	}
	
	
	/**
	 * 恢复用户：删除用户相关的权限，并更改用户状态为”2“
	 */
	@RequestMapping("/recoverUsr")
	@ResponseBody
	public Map<String, Object> recoverUsr(HttpServletRequest request,String usrID) throws Exception {
		userService.recoverUsr(usrID);
		return this.writeSuccMsg("恢复用户成功！");
	}
	
	/**
	 * 重置用户密码：返回重置后的密码给前台管理员
	 */
	@RequestMapping("/resetUsrPwd")
	@ResponseBody
	public Map<String, Object> resetUsrPwd(HttpServletRequest request,String usrID) throws Exception {
		userService.resetUsrPwd(usrID);
		return this.writeSuccMsg("重置用户密码成功！");
	}
	
	/**
	 * 根据前台传过来的机构ID，查询获取该机构下的所有用户列表,根据orgID、usrCod、usrNam检索
	 * 以hibernate分页的方式去查询
	 */
	@RequestMapping("/queryUsrWithPage")
	@ResponseBody     
	public PageInfo queryUsrWithPage(ModelMap map, HttpServletRequest request, String orgID, String usrCodOrNam)throws Exception{	
		PageInfo pi = super.getPageInfo(request);
		return userService.queryUsrWithPage(pi,orgID,usrCodOrNam);
	}
	
	/**
	 * 根据前台传过来的机构ID，查询获取该机构下的所有用户列表,根据orgID、usrCod、usrNam检索
	 */
	@RequestMapping("/queryUsrByOrg")
	@ResponseBody     
	public PageInfo queryUsrByOrg(String orgID, String usrCodOrNam)throws Exception{	
		return userService.queryUsrByOrg(orgID,usrCodOrNam);
	}
	
	/**
	 * 根据ID获取用户信息
	 */
	@RequestMapping("/queryUsrByID")
	@ResponseBody
	public Map<String, Object> queryUsrByID(HttpServletRequest request,String usrID ) throws Exception {
		if(StringUtils.isBlank(usrID)){
			throw new BusinessException("人员ID不能为空！");
		}
		//把查询到的人员信息放到map中，返回
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("Usr", userService.queryUsrByID(usrID));
		
		return this.writeSuccMsg("", paramMap);
	}
	
	/**
	 * 修改密码
	 */
	@RequestMapping("/modifyPwd")
	@ResponseBody
	public Map<String, Object> modifyPwd(HttpServletRequest request, String oldPwd, String newPwd, String newPwdSure) throws Exception {
		if(StringUtils.isBlank(oldPwd)){
			throw new BusinessException("旧密码不能为空！");
		}
		if(StringUtils.isBlank(newPwd)){
			throw new BusinessException("新密码不能为空！");
		}
		if(StringUtils.isBlank(newPwdSure)){
			throw new BusinessException("密码不能为空！");
		}
		//获取当前登入用户
		LoginInfo loginInfo = getLoginInfo(request);
		String usrCod = loginInfo.getUserCod();
		userService.modifyPwd(usrCod, oldPwd, newPwd, newPwdSure);
		return this.writeSuccMsg("修改成功！");
	}
	
	/**
	 * 新增用户
	 */
	@RequestMapping("/addUsr")
	@ResponseBody
	public Map<String, Object> addUsr(HttpServletRequest request, @Valid Usr usr) throws Exception {
		String usrID = userService.addUsr(usr);
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID",usrID);
		
		return this.writeSuccMsg("", paramMap);
	}
	
	/**
	 * 修改用户
	 */
	@RequestMapping("/modifyUsr")
	@ResponseBody
	public Map<String, Object> modifyUsr(HttpServletRequest request, @Valid Usr usr) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID", userService.modifyUsr(usr));
		return this.writeSuccMsg("修改个人信息成功", paramMap); 
	}
	
	/**
	 * 保存用户参照授权
	 */
	@RequestMapping("/saveRefer")
	@ResponseBody
	public Map<String, Object> saveRefer(HttpServletRequest request, String referUsrID, String usrIDs) throws Exception {
		userService.saveRefer(referUsrID, usrIDs );
		return this.writeSuccMsg("授权成功"); 
	}
	
	/**
	 * 根据用户ID，获取该用户的反馈流程节点权限
	 */
	@RequestMapping("/queryUsrWFTree")
	@ResponseBody
	public List<TreeModel> queryUsrWFTree(HttpServletRequest request, String usrID) throws Exception{
		return userService.queryUsrWFTree(usrID);
	}
	
	/**
	 * 对用户进行流程环节赋权
	 */	
	@RequestMapping("/saveUsrProc")
	@ResponseBody
	public Map<String, Object> saveUsrProc(String usrID, String nodeIDs) throws Exception{
		if(StringUtils.isBlank(usrID))
			throw new BusinessException("必须传入需要配置的用户ID");
		userService.saveUsrProc(usrID ,nodeIDs);
		return this.writeSuccMsg("保存成功！");		
	}
	
	//----------------------跳转页面
	/**
	 * 主菜单：用户管理
	 */
	@RequestMapping("/forwardUsrindex")
	public String forwardUsrindex(HttpServletRequest request,String referUsrNam) throws Exception {
		if(StringUtils.isNotBlank(referUsrNam)){
			referUsrNam=new String(referUsrNam.getBytes("iso8859-1"),"UTF-8");
		}
		request.setAttribute("referUsrNam", referUsrNam);
		return "sys/usr/usrindex";	
	}
	
	/**
	 * 新增用户页面
	 */
	@RequestMapping("/forwardAddusr")
	public String forwardAddusr(HttpServletRequest request) throws Exception {
		return "sys/usr/addusr";	
	}

	/**
	 * 修改用户页面
	 */
	@RequestMapping("/forwardModifyusr")
	public String forwardModifyusr(HttpServletRequest request) throws Exception {
		return "sys/usr/modifyusr";	
	}
	
	/**
	 * 修改密码页面
	 */
	@RequestMapping("/forwardModifypsw")
	public String forwardModifypsw(HttpServletRequest request) throws Exception {
		return "sys/usr/modifypass";	
	}
	
	/**
	 * 修改用户基本信息
	 */
	@RequestMapping("/forwardModifyInfo")
	public String forwardModifyInfo(HttpServletRequest request, ModelMap map) throws Exception {
		//获取当前用户对象
		LoginInfo loginInfo = getLoginInfo(request);
		map.put("usrID", loginInfo.getUserId());
		return "sys/usr/modifyInfo";	
	}
	
	/**
	 * 维护用户与角色关系页面
	 */
	@RequestMapping("/forwardCfgrolefunc")
	public String forwardCfgrolefunc(HttpServletRequest request) throws Exception {
		return "sys/usr/cfgrolefunc";	
	}
	
	/**
	 * 配置用户岗位页面
	 */
	@RequestMapping("/forwardCfgPst")
	public String forwardCfgPst(HttpServletRequest request) throws Exception {
		return "sys/usr/cfgpst";	
	}
	
	/**
	 * 配置反馈处理流程环节权限
	 */
	@RequestMapping("/forwardUsrWF")
	public String forwardUsrWF(HttpServletRequest request) throws Exception {
		return "sys/usr/setusrwf";	
	}
	
}