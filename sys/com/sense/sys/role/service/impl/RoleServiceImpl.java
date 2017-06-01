package com.sense.sys.role.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sense.frame.base.BaseService;
import com.sense.frame.base.BusinessException;
import com.sense.frame.common.util.SortUtil;
import com.sense.frame.common.util.TreeUtil;
import com.sense.frame.pub.global.LoginInfo;
import com.sense.frame.pub.model.PageInfo;
import com.sense.frame.pub.model.TreeModel;
import com.sense.sys.entity.Func;
import com.sense.sys.entity.ProcNode;
import com.sense.sys.entity.Role;
import com.sense.sys.entity.RoleFunc;
import com.sense.sys.entity.RoleProcNode;
import com.sense.sys.role.dao.RoleDao;
import com.sense.sys.role.service.RoleService;

@Service
public class RoleServiceImpl extends BaseService implements RoleService {

	@Autowired
	private RoleDao roleDao;
	
	/**
	 * 判断某个用户是否拥有超级管理权限
	 */
	@Override
	public boolean ownSuperRole(String usrID)throws Exception{
		return roleDao.ownSuperRole(usrID);
	}
	
	/**
	 * 查询用户拥有的角色列表
	 * 直接在sys_usr_role 角色表中管理
	 */
	@Override
	public PageInfo queryOwnedRoleDgByUser(String usrID) throws Exception {
		List<Role> roles = roleDao.queryAllRoleOwnByUsr(usrID);
		PageInfo pi = new PageInfo();
		pi.setTotal(""+roles.size());
		pi.setRows(roles);
		return pi;
	}
	
	/**
	 * 通过sys_usr_role关联关系获取用户拥有的role
	 */
	@Override
	public List<Role> queryAllRoleOwnByUsr(String usrID) throws Exception{
		return roleDao.queryAllRoleOwnByUsr(usrID);
	}
	
	/**
	 * 查询用户没有拥有的角色列表，但是可以作为可选
	 */
	@Override
	public PageInfo queryNotOwnedRoleDgByUser(LoginInfo user, String usrID) throws Exception {
		List<Role> allRoles = null ;
		if(user.isSuperAdmin()){
			allRoles = roleDao.queryAllRoleExcludeSuperRole();
		}else{
			allRoles = roleDao.queryAllRoleOwnByUsr(user.getUserId());
		}
		List<Role> ownedRoles = roleDao.queryAllRoleOwnByUsr(usrID);
		Map<String ,Role> allRolesMap = new HashMap<String ,Role>();
		for(Role role:allRoles){
			allRolesMap.put(role.getRoleID(), role);
		}
		for(Role role:ownedRoles){
			if(allRolesMap.containsKey(role.getRoleID())){
				allRolesMap.remove(role.getRoleID());
			}
		}
		List<Role> notOwnedRoles = new ArrayList<Role>();
		for(String key:allRolesMap.keySet()){
			notOwnedRoles.add(allRolesMap.get(key));
		}
		SortUtil.sortList(notOwnedRoles, "roleNam", 1);// 对一组list进行排序
		
		PageInfo otherPi = new PageInfo();
		otherPi.setTotal(""+notOwnedRoles.size());
		otherPi.setRows(notOwnedRoles);
		return otherPi;
	}
	
	/**
	 * 分页角色列表
	 * 查看用户是否拥有超级角色，如果拥有超级角色则返回，除了超级角色之外的所有角色，
	 * 如果没拥有超级角色，返回该用户拥有的角色
	 */
	@Override
	public PageInfo queryRoleDgOwnByUserWithPage(PageInfo pi, LoginInfo user) throws Exception {
		if(user.isSuperAdmin()){
			return roleDao.queryAllRoleExcludeSuperRoleWithPage(pi);
		}else{
			return roleDao.queryAllRoleOwnByCommonUsrWithPage(pi, user.getUserId());
		}
	}
	
	/**
	 * 功能权限类型角色-增删改
	 */
	@Override
	public void addRole(Role role) throws Exception {
		if(null!=roleDao.queryRoleByNam(role.getRoleNam())){
			throw new BusinessException("角色名字已被占用！");
		}
		role.setRoleID(dBUtil.getCommonId());
		commonDao.saveEntity(role);
	}
	
	@Override
	public void delRoleFnc(String roleID) throws Exception {
		//删除角色需要删除角色本身 和角色与用户的管理，角色与功能权限的管理
		roleDao.delRoleFncAndRelation(roleID);
	}

	@Override
	public void editRole(Role role) throws Exception {
		Role dBRole = commonDao.findEntityByID(Role.class, role.getRoleID());
		Role otherRole = roleDao.queryRoleByNam(role.getRoleNam()) ;
		if(otherRole!=null && !otherRole.getRoleID().equals(dBRole.getRoleID())){
			throw new BusinessException("角色名字已被占用！");
		}
		dBRole.setRoleNam(role.getRoleNam());
		dBRole.setDescr(role.getDescr());
		commonDao.updateEntity(dBRole);
	}
	
	/**
	 * 获取角色具有的功能权限树：已有的打勾
	 * 根据赋权用户的ID，查询该用户是否拥有超级角色，
	 * 如果用户超级角色，返回所有的功能权限组成的树
	 * 否则，返回该用户用户的功能权限组成的树。
	 */
	@Override
	public List<TreeModel> queryRoleFuncTree(String roleID, LoginInfo user, boolean frontPageCascadeFlag) throws Exception {
		//先获取全部权限树
		List<Func> funcList = null ;
		if(user.isSuperAdmin()){
			funcList = roleDao.findFuncs("");
		}else{
			funcList = roleDao.findFuncs("", user.getUserId());
		}
		
		//再决定哪些需要打勾
		List<String> checkList = roleDao.queryFuncListByRoleID(roleID);
		List<TreeModel> tList = TreeUtil.setTree(funcList, checkList,frontPageCascadeFlag);
		return TreeUtil.setTreeOpenLevel(tList, 2);
	}
	
	/**
	 * 为角色赋权保存,前台传过来的，把由funcID组成的串分割成数组
	 */
	@Override
	public void  saveRoleFunc(String roleID, String funcIDs) throws Exception{
		String[] frontFuncIDArray = funcIDs.split(",");
		List<String> frontFuncIDList = Arrays.asList(frontFuncIDArray);
		List<String> oldFuncIDList = roleDao.queryFuncListByRoleID(roleID);
		for (String funcID : frontFuncIDList) {
			if(StringUtils.isNotBlank(funcID)){
				if(!oldFuncIDList.contains(funcID)){
					RoleFunc rolefunc = new RoleFunc();
					rolefunc.setPk(dBUtil.getCommonId());
					rolefunc.setRoleID(roleID);
					rolefunc.setFuncID(funcID);
					commonDao.saveEntity(rolefunc);
				}
			}
		}
		
		//找出要删除的
		for (String funcID : oldFuncIDList) {
			if(!frontFuncIDList.contains(funcID)){
				roleDao.deleteRoleFuncEntity(roleID, funcID);
			}
		}
		
	}

	/**
	 * 根据角色ID，获取该角色的反馈流程节点权限
	 */
	@Override
	public List<TreeModel> queryRoleWFTree(String roleID) throws Exception {
		List<ProcNode> nodeList = commonDao.findEntityList(ProcNode.class);
		List<String> nodeIDList = roleDao.queryProcNodeIDList(roleID);
		return TreeUtil.setTree(nodeList, nodeIDList);
	}

	/**
	 * 对角色进行流程环节赋权
	 */	
	@Override
	public void saveRoleProc(String roleID, String nodeIDs) throws Exception {
		roleDao.delRoleProcNode(roleID);
		if(StringUtils.isNotBlank(nodeIDs)){
			String[] nodeIDArr = nodeIDs.split(",");
			for (int i = 0; i < nodeIDArr.length; i++) {
				RoleProcNode rpn = new RoleProcNode();
				rpn.setPk(dBUtil.getCommonId());
				rpn.setNodeID(nodeIDArr[i]);
				rpn.setRoleID(roleID);
				commonDao.saveEntity(rpn);
			}
		}
	}
}