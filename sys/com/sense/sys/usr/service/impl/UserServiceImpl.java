package com.sense.sys.usr.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sense.frame.base.BaseService;
import com.sense.frame.base.BusinessException;
import com.sense.frame.common.spring.ServerInfoProperty;
import com.sense.frame.common.util.Md5Util;
import com.sense.frame.common.util.TreeUtil;
import com.sense.frame.enumdic.EnumFuncAutType;
import com.sense.frame.pub.global.LoginInfo;
import com.sense.frame.pub.model.PageInfo;
import com.sense.frame.pub.model.TreeModel;
import com.sense.sys.entity.Func;
import com.sense.sys.entity.Org;
import com.sense.sys.entity.ProcNode;
import com.sense.sys.entity.Usr;
import com.sense.sys.entity.UsrProcNode;
import com.sense.sys.entity.UsrPst;
import com.sense.sys.entity.UsrRole;
import com.sense.sys.enumdic.EnumUsrSta;
import com.sense.sys.role.dao.RoleDao;
import com.sense.sys.usr.dao.UserDao;
import com.sense.sys.usr.service.UserService;

@Service
public class UserServiceImpl extends BaseService implements UserService {
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private ServerInfoProperty serverInfoProperty;
	
	@Override
	public void changeRoleForUsr(String usrID,String roleID,boolean addFlag)throws Exception {
		if(addFlag){
			//增加角色
			if(userDao.queryUsrRoleEntity(usrID,roleID)==null){
				UsrRole entity = new UsrRole();
				entity.setPk(dBUtil.getCommonId());
				entity.setRoleID(roleID);
				entity.setUsrID(usrID);
				commonDao.saveEntity(entity);
			}
		}else{
			//减少角色
			userDao.delUsrRoleEntity(usrID, roleID);
		}
	}
	
	/**
	 * 配置岗位
	 */
	public void changePstForUsr(String usrID, String pstID, boolean addFlag)throws Exception{
		if(addFlag){//增加岗位
			if(userDao.queryUsrPst(usrID, pstID) == null){
				UsrPst entity = new UsrPst();
				entity.setPk(dBUtil.getCommonId());
				entity.setPstID(pstID);
				entity.setUsrID(usrID);
				commonDao.saveEntity(entity);
			}
		}else{
			//减少岗位
			userDao.delUsrPst(usrID, pstID);
		}
	}
	
	/**
	 * 获取用户的功能权限树
	 */
	@Override
	public List<TreeModel> queryUsrAuthorityTree(String usrID)throws Exception {
		List<Func> list = null;
		if(roleDao.ownSuperRole(usrID)){
			list = roleDao.findFuncs(null);
		}else{
			list = roleDao.findFuncs(null, usrID);
		}
		List<TreeModel> tree= TreeUtil.setTree(list);
		return TreeUtil.setTreeOpenLevel(tree, 2);
	}

	//注销用户
	@Override
	public void writeOffUsr(String writeOffUsrID) throws Exception{
		Usr dbUsr= commonDao.findEntityByID(Usr.class, writeOffUsrID);
		if(dbUsr!=null){
			dbUsr.setUsrSta(EnumUsrSta.illegal.getCode());
			commonDao.updateEntity(dbUsr);
		}
	}
	
	//恢复用户
	@Override
	public void recoverUsr(String recoverUsrID) throws Exception{
		Usr dbUsr= commonDao.findEntityByID(Usr.class, recoverUsrID);
		if(dbUsr!=null){
			dbUsr.setUsrSta(EnumUsrSta.normal.getCode());
			commonDao.updateEntity(dbUsr);
		}
	}

	//重置用户密码
	@Override
	public void resetUsrPwd(String resetUsrID) throws Exception{
		Usr dbUsr= commonDao.findEntityByID(Usr.class, resetUsrID);
		if(dbUsr!=null){
			String initPWD = serverInfoProperty.getInitPwd();//初始密码
			dbUsr.setUsrPwd(Md5Util.md5(initPWD));
			commonDao.updateEntity(dbUsr);
		}
	}

	@Override
	public List<TreeModel> getMenuAut(LoginInfo loginInfo) throws Exception {
		List<Func> list = null;
		if(loginInfo.isSuperAdmin()){
			list = roleDao.findFuncs(EnumFuncAutType.MENUAUT.getCode());
		}else{
			list = roleDao.findFuncs(EnumFuncAutType.MENUAUT.getCode(), loginInfo.getUserId());
		}
		return TreeUtil.setTree(list);
	}

	@Override
	public Map<String, Set<String>> getBtnAut(LoginInfo loginInfo) throws Exception {
		List<Func> list = null;
		if(loginInfo.isSuperAdmin()){
			list = roleDao.findFuncs(null);
		}else{
			list = roleDao.findFuncs(null, loginInfo.getUserId());
		}
		Map<String, Set<String>> btnMap = new HashMap<String, Set<String>>();
		for(Func func : list) {
			if(EnumFuncAutType.BTNAUT.getCode().equals(func.getFuncTyp())){
				if(null == btnMap.get(func.getParentID())){
					btnMap.put(func.getParentID(), new HashSet<String>());
				}
				btnMap.get(func.getParentID()).add(func.getFuncID());
			}
		}
		return btnMap;
	}

	@Override
	public Set<String> getUsrFuncUrls(LoginInfo loginInfo) throws Exception {
		List<Func> list = null;
		if(loginInfo.isSuperAdmin()){
			list = roleDao.findFuncs(null);
		}else{
			list = roleDao.findFuncs(null, loginInfo.getUserId());
		}
		Set<String> urls = new HashSet<String>();
		for(Func func : list){
			if(StringUtils.isNotBlank(func.getFuncUrl())){
				String[] urlArr = func.getFuncUrl().split(",");
				for (int i = 0; i < urlArr.length; i++) {
					urls.add(urlArr[i]);
				}
			}
		}
		return urls;
	}
	
	/**
	 * 根据前台传过来的机构ID，查询获取该机构下的所有用户列表,根据orgID、usrCod、usrNam检索
	 * 以hibernate分页的方式去查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PageInfo queryUsrWithPage(PageInfo pi, String orgID, String usrCodOrNam) throws Exception{
		pi = userDao.queryUsrWithPage(pi ,orgID,usrCodOrNam);
		List<Usr> usrList = (List<Usr>) pi.getRows();
		for(Usr usr : usrList){
			String psts = userDao.queryUsrPsts(usr.getUsrID());
			usr.setPstNam(psts);
		}
		return pi;
	}
	
	/*
	 * 根据ID查询用户信息
	 */
	@Override
	public Usr queryUsrByID(String usrID) throws Exception {
		return commonDao.findEntityByID(Usr.class, usrID);
	}
	
	/**
	 * 修改密码
	 */
	@Override
	public void modifyPwd(String usrCod, String oldPwd, String newPwd, String newPwdSure) throws Exception {
		List<Usr> usrList = userDao.getUserByNoPwd(usrCod, Md5Util.md5(oldPwd));
		if(usrList==null || usrList.size()==0){
			throw new BusinessException("您输入的旧密码和原密码不一致！");
		}
		if (usrList.size() > 1) {
			throw new BusinessException("查询用户信息出错:存在重复的用户编号[" + usrCod + "]！");
		}
		if(!newPwd.equals(newPwdSure)){
			throw new BusinessException("两次输入的密码不一致！");
		}
		Usr usr = usrList.get(0);
		usr.setUsrPwd(Md5Util.md5(newPwd));
		commonDao.updateEntity(usr);
	}
	
	/**
	 * 新增用户
	 */
	@Override
	public String addUsr(Usr usr) throws Exception {
		//判断用户编码是否重复
		if(commonDao.findEntityList(Usr.class, "usrCod", usr.getUsrCod()).size()!=0){
			throw new BusinessException("用户编码不能重复！");
		}
		//判断用户名称是否重复
		if(commonDao.findEntityList(Usr.class, "usrNam", usr.getUsrNam()).size()!=0){
			throw new BusinessException("用户名称不能重复！");
		}
		String initPWD = serverInfoProperty.getInitPwd();//初始密码
		usr.setUsrID(dBUtil.getCommonId());
		usr.setUsrPwd(Md5Util.md5(initPWD));
		usr.setUsrSta(EnumUsrSta.normal.getCode());// 新增加的用户的状态，默认为1（正常）
		commonDao.saveEntity(usr);
		return usr.getUsrID();
	}
	
	/*
	 * 修改用户
	 */
	@Override
	public String modifyUsr(Usr usr) throws Exception {
		String usrID = usr.getUsrID();
		String usrCod = usr.getUsrCod();
		String usrNam = usr.getUsrNam();
		
		//判断用户编码是否重复
		if(userDao.queryUsrByCod(usrCod, usrID).size()!=0){
			throw new BusinessException("用户编码不能重复！");
		}
		//判断用户名称是否重复
		if(userDao.queryUsrByNam(usrNam, usrID).size()!=0){
			throw new BusinessException("用户名称不能重复！");
		}
		commonDao.updateEntity(usr);
		return usrID;
	}
	
	/**
	 * 检测登入用户
	 */
	@Override
	public LoginInfo getUserByNoPwd(String usrCod, String pwd) throws Exception {
		List<Usr> userList = userDao.getUserByNoPwd(usrCod, Md5Util.md5(pwd));
		if (userList == null || userList.size() == 0) {
			throw new BusinessException("查询用户信息出错：用户编号[" + usrCod + "]或者密码有错！");
		}
		if (userList.size() > 1) {
			throw new BusinessException("查询用户信息出错:存在重复的用户编号[" + usrCod + "]！");
		}
		
		Usr user = userList.get(0);
		
		//只有正常状态的用户才允许登录
		if(!"1".equals(user.getUsrSta())){
			throw new BusinessException("查询用户信息出错:用户[" + usrCod + "]非正常状态，请联系管理员！");
		}
		LoginInfo logonUser = new LoginInfo();
		logonUser.setUserId(user.getUsrID());
		logonUser.setUserCod(user.getUsrCod());
		logonUser.setUserNam(user.getUsrNam());
		logonUser.setOrgId(user.getOrgID());
		
		Org org = commonDao.findEntityByID(Org.class, user.getOrgID());
		if(null != org){
			logonUser.setOrgNam(org.getOrgNam());
		}
		
		return logonUser;
	}
	
	/**
	 * 保存参照授权
	 * 把原用户的角色都删除
	 */
	@Override
	public void saveRefer(String referUsrID, String usrIDs) throws Exception{
		List<String> roleList = userDao.queryReferRoleIDs(referUsrID);
		for(String usrID : usrIDs.split(",")){
			if(StringUtils.isNotBlank(usrID)){
				userDao.delUsrRole(usrID);
				for(String roleID : roleList){
					UsrRole rel=new UsrRole();
					rel.setPk(dBUtil.getCommonId());
					rel.setUsrID(usrID);
					rel.setRoleID(roleID);
					commonDao.saveEntity(rel);
				}
			}
		}
	}
	
	/**
	 * 根据用户ID，获取该用户的反馈流程节点权限
	 */
	public List<TreeModel> queryUsrWFTree(String usrID) throws Exception{
		List<ProcNode> nodeList = commonDao.findEntityList(ProcNode.class);
		List<String> nodeIDList = userDao.queryProcNodeIDList(usrID);
		return TreeUtil.setTree(nodeList, nodeIDList);
	}

	/**
	 * 对用户进行流程环节赋权
	 */	
	@Override
	public void saveUsrProc(String usrID, String nodeIDs) throws Exception {
		userDao.delUsrProcNode(usrID);
		if(StringUtils.isNotBlank(nodeIDs)){
			String[] nodeIDArr = nodeIDs.split(",");
			for (int i = 0; i < nodeIDArr.length; i++) {
				UsrProcNode rpn = new UsrProcNode();
				rpn.setPk(dBUtil.getCommonId());
				rpn.setNodeID(nodeIDArr[i]);
				rpn.setUsrID(usrID);
				commonDao.saveEntity(rpn);
			}
		}
	}

	@Override
	public PageInfo queryUsrByOrg(String orgID, String usrCodOrNam) throws Exception {
		List<Usr> list = userDao.queryUsrByOrg(orgID, usrCodOrNam);
		for(Usr usr : list){
			String psts = userDao.queryUsrPsts(usr.getUsrID());
			usr.setPstNam(psts);
		}
		PageInfo pi = new PageInfo();
		pi.setRows(list);
		pi.setTotal("" + list.size());
		return pi;
	}

}