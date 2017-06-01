package com.sense.sys.role.service;

import java.util.List;

import com.sense.frame.pub.global.LoginInfo;
import com.sense.frame.pub.model.PageInfo;
import com.sense.frame.pub.model.TreeModel;
import com.sense.sys.entity.Role;

public interface RoleService {
	
	/**
	 * 判断某个用户是否拥有超级管理权限
	 */
	public boolean ownSuperRole(String usrID)throws Exception;
	
	/**
	 * 查询用户拥有的角色列表
	 * 直接在sys_usr_role 角色表中管理
	 */
	public PageInfo queryOwnedRoleDgByUser(String usrID) throws Exception;
	
	/**
	 * 通过sys_usr_role关联关系获取用户拥有的role
	 */
	public List<Role> queryAllRoleOwnByUsr(String usrID) throws Exception;
	
	/**
	 * 查询用户没有拥有的角色列表，但是可以作为可选
	 */
	public PageInfo queryNotOwnedRoleDgByUser(LoginInfo user, String usrID) throws Exception;
	
	/**
	 * 分页角色列表
	 */
	public PageInfo queryRoleDgOwnByUserWithPage(PageInfo pi ,LoginInfo user) throws Exception ;
	
	/**
	 * 功能权限类型角色-增删改
	 */
	public void addRole(Role role) throws Exception ;
	public void delRoleFnc(String roleID) throws Exception ;
	public void editRole(Role role) throws Exception ;
	
	/**
	 * 获取角色具有的功能权限树：已有的打勾
	 */
	public List<TreeModel> queryRoleFuncTree(String roleID,LoginInfo user,boolean frontPageCascadeFlag) throws Exception ;
	
	/**
	 * 为角色赋权保存
	 */
	public void  saveRoleFunc(String roleID ,String funcIDs) throws Exception;

	/**
	 * 根据角色ID，获取该角色的反馈流程节点权限
	 */
	public List<TreeModel> queryRoleWFTree(String roleID) throws Exception;

	/**
	 * 对角色进行流程环节赋权
	 */	
	public void saveRoleProc(String roleID, String nodeIDs) throws Exception;
}