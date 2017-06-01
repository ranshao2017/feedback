package com.sense.sys.role;

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
import com.sense.frame.pub.global.LoginInfo;
import com.sense.frame.pub.model.PageInfo;
import com.sense.frame.pub.model.TreeModel;
import com.sense.sys.entity.Role;
import com.sense.sys.role.service.RoleService;

/**
 * 角色
 */
@Controller
@RequestMapping("/sys/role")
public class RoleController extends BaseController {
	
	@Autowired
	private RoleService roleService;
	
	/**
	 * 查询用户拥有的角色列表
	 * 直接在sys_usr_role 角色表中管理
	 */
	@RequestMapping("/queryOwnedRoleDgByUser")
	@ResponseBody
	public PageInfo queryOwnedRoleDgByUser(HttpServletRequest request, String usrID) throws Exception{
		return roleService.queryOwnedRoleDgByUser(usrID);
	}
	
	/**
	 * 查询用户没有拥有的角色列表，但是可以作为可选
	 * 直接在sys_usr_role 角色表中管理
	 */
	@RequestMapping("/queryNotOwnedRoleDgByUser")
	@ResponseBody
	public PageInfo queryNotOwnedRoleDgByUser(HttpServletRequest request, String usrID) throws Exception{
		LoginInfo user = this.getLoginInfo(request);
		return roleService.queryNotOwnedRoleDgByUser(user, usrID);
	}
	
	/**
	 * 查询用户拥有的角色列表
	 * 直接在sys_usr_role 角色表中管理
	 */
	@RequestMapping("/queryOwnedRoleArvtypeDgByUser")
	@ResponseBody
	public PageInfo queryOwnedRoleArvtypeDgByUser(HttpServletRequest request,String usrID) throws Exception{
		return roleService.queryOwnedRoleDgByUser(usrID);
	}
	
	/**	
	 * 查询用户没有拥有的角色列表，但是可以作为可选
	 * 直接在sys_usr_role 角色表中管理
	 */
	@RequestMapping("/queryNotOwnedRoleArvtypeDgByUser")
	@ResponseBody
	public PageInfo queryNotOwnedRoleArvtypeDgByUser(HttpServletRequest request,String usrID) throws Exception{
		LoginInfo user = this.getLoginInfo(request);
		return roleService.queryNotOwnedRoleDgByUser(user ,usrID);
	}
	
	/**
	 * 分页角色列表，
	 * 其中： 当用户用户超级管理角色时，可以管理的角色为除了超级管理之外的所有角色；
	 * 当用户只是普通用户时，能管理的角色只是她自身拥有的角色，
	 *  总之，超级管理角色在角色管理页面是不可见的，即不可编辑。
	 */
	@RequestMapping("/queryRoleDgOwnByUserWithPage")
	@ResponseBody
	public PageInfo queryRoleDgOwnByUserWithPage(HttpServletRequest request) throws Exception{
		PageInfo pi = this.getPageInfo(request);
		LoginInfo user = this.getLoginInfo(request);
		return roleService.queryRoleDgOwnByUserWithPage(pi ,user);
	}
	
	/**
	 *  新增角色--功能权限类型
	 */ 
	@RequestMapping("/addRole")
	@ResponseBody
	public Map<String, Object> addRole(HttpServletRequest request,@Valid Role role) throws Exception{		
		roleService.addRole(role);				
		return this.writeSuccMsg("");		
	}
	
	/**
	 *  删除角色--功能权限类型
	 */ 
	@RequestMapping("/delRoleFnc")
	@ResponseBody
	public Map<String, Object> delRoleFnc(HttpServletRequest request,String roleID) throws Exception{
		roleService.delRoleFnc(roleID);
		return this.writeSuccMsg("删除角色成功！");
	}
	
	/**
	 *  修改角色--功能权限类型
	 */ 
	@RequestMapping("/editRole")
	@ResponseBody
	public Map<String, Object> editRole(HttpServletRequest request, @Valid Role role) throws Exception{
		roleService.editRole(role);				
		return this.writeSuccMsg("");		
	}
	
	/**
	 * 根据角色ID，获取该角色的功能权限树
	 */
	@RequestMapping("/queryRoleFuncTree")
	@ResponseBody
	public List<TreeModel> queryRoleFuncTree(HttpServletRequest request, String roleID, String frontPageCascade) throws Exception{
		boolean frontPageCascadeFlag=true;
		if("0".equals(frontPageCascade)){
			frontPageCascadeFlag=false;
		}
		LoginInfo user = this.getLoginInfo(request);
		List<TreeModel> tList = roleService.queryRoleFuncTree(roleID, user, frontPageCascadeFlag);
		return tList;		
	}
	
	/**
	 * 对角色进行赋权保存
	 */	
	@RequestMapping("/saveRoleFunc")
	@ResponseBody
	public Map<String, Object> saveRoleFunc(HttpServletRequest request, String roleID, String funcIDs) throws Exception{
		roleService.saveRoleFunc(roleID ,funcIDs);
		return this.writeSuccMsg("保存成功！");		
	}
	
	/**
	 * 主菜单：角色管理
	 */
	@RequestMapping("/forwardRoleindex")
	public String forwardRoleindex(HttpServletRequest request) throws Exception {
		return "sys/role/roleindex";	
	}
	
	/**
	 * 新增或者修改角色页面
	 */
	@RequestMapping("/forwardManagerole")
	public String forwardManagerole(HttpServletRequest request) throws Exception {
		return "sys/role/managerole";	
	}
	
	/**
	 * 配置反馈处理流程环节权限
	 */
	@RequestMapping("/forwardRoleWF")
	public String forwardRoleWF(HttpServletRequest request) throws Exception {
		return "sys/role/setrolewf";
	}
	
	/**
	 * 根据角色ID，获取该角色的反馈流程节点权限
	 */
	@RequestMapping("/queryRoleWFTree")
	@ResponseBody
	public List<TreeModel> queryRoleWFTree(HttpServletRequest request, String roleID) throws Exception{
		return roleService.queryRoleWFTree(roleID);
	}
	
	/**
	 * 对角色进行流程环节赋权
	 */	
	@RequestMapping("/saveRoleProc")
	@ResponseBody
	public Map<String, Object> saveRoleProc(String roleID, String nodeIDs) throws Exception{
		if(StringUtils.isBlank(roleID))
			throw new BusinessException("必须传入需要配置的角色ID");
		roleService.saveRoleProc(roleID ,nodeIDs);
		return this.writeSuccMsg("保存成功！");		
	}
	
}