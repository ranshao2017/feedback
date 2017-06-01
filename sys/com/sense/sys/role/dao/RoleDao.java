package com.sense.sys.role.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Component;

import com.sense.frame.base.BaseDAO;
import com.sense.frame.enumdic.EnumSuperAdminType;
import com.sense.frame.pub.model.PageInfo;
import com.sense.sys.entity.Func;
import com.sense.sys.entity.Role;

@Component
public class RoleDao  extends BaseDAO{
	@SuppressWarnings("unchecked")
	public List<Func> findFuncs(String funcType) throws Exception {
		StringBuffer hql = new StringBuffer();
		hql.append("from Func");
		if(StringUtils.isNotBlank(funcType)){
			hql.append(" where funcTyp = :funcType");
		}
		Query query = getCurrentSession().createQuery(hql.toString());
		if(StringUtils.isNotBlank(funcType)){
			query.setString("funcType", funcType);
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Func> findFuncs(String funcType, String userId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct f.* ");
		sql.append("   from sys_func f, sys_role_func rf, sys_usr_role ur ");
		sql.append("  where ur.usrid = :userId ");
		sql.append("    and ur.roleid = rf.roleid ");
		sql.append("    and rf.funcid = f.funcid ");
		if(StringUtils.isNotBlank(funcType)){
			sql.append(" and f.functyp = :funcType ");
		}
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.setString("userId", userId);
		if(StringUtils.isNotBlank(funcType)){
			query.setString("funcType", funcType);
		}
		query.addEntity("f", Func.class);
		return query.list();
	}

	
	/**
	 * 判断某个用户是否拥有超级管理权限
	 */
	public boolean ownSuperRole(String usrID)throws Exception{
		String sqlStr = " select count(*) num from sys_usr_role where usrid = :usrID and roleid = :superRole ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("usrID", usrID);
		query.setString("superRole", EnumSuperAdminType.SUPERROLE.getCode());
		int count = (Integer) query.addScalar("num", IntegerType.INSTANCE).uniqueResult();
		if(count > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据名字查询角色
	 */
	@SuppressWarnings("unchecked")
	public Role queryRoleByNam(String roleNam)throws Exception{
		String sqlStr = " select * from sys_role where rolenam = :roleNam ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleNam", roleNam);
		query.addEntity(Role.class);
		List<Role> ls =query.list();
		if(ls!=null && ls.size() >0){
			return ls.get(0);
		}
		return null ;
	}
	
	/**
	 * 获取超级类型的用户拥有的角色列表，根据角色类型
	 */
	@SuppressWarnings("unchecked")
	public List<Role> queryAllRoleExcludeSuperRole() throws Exception{
		Query query = getCurrentSession().createQuery("from Role where roleID != :superRole order by roleNam");
		query.setString("superRole", EnumSuperAdminType.SUPERROLE.getCode());
		return query.list();
	}
	
	/**
	 * 获取超级类型的用户拥有的角色列表，根据角色类型
	 */
	public PageInfo queryAllRoleExcludeSuperRoleWithPage(PageInfo pi) throws Exception{
		String sqlStr = " select * from sys_role where roleid != :superRole ";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("superRole", EnumSuperAdminType.SUPERROLE.getCode());
		SQLEntity sqlEntity = new SQLEntity(sqlStr, parameters);
		return executePageQuery(pi, sqlEntity, Role.class);
	}
	
	/**
	 * 普通用户拥有的角色列表，根据角色类型
	 */
	@SuppressWarnings("unchecked")
	public List<Role> queryAllRoleOwnByUsr(String usrID) throws Exception{
		String sqlStr = " select distinct r.* from sys_role r ,sys_usr_role ur"
				+ " where r.roleid = ur.roleid  "
				+ " and ur.usrid = :usrID "
				+ " order by rolenam";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("usrID", usrID);
		return query.addEntity(Role.class).list();
	}
	
	/**
	 * 分页普通用户拥有的角色列表，根据角色类型
	 */
	public PageInfo queryAllRoleOwnByCommonUsrWithPage(PageInfo pi, String usrID) throws Exception{
		String sqlStr = " select distinct r.* from sys_role r ,sys_usr_role ur"
				+ " where r.roleid = ur.roleid  "
				+ " and r.roleid != :superRole "
				+ " and ur.usrid = :usrID "
				+ " order by rolenam" ;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("usrID", usrID);
		parameters.put("superRole", EnumSuperAdminType.SUPERROLE.getCode());
		SQLEntity sqlEntity = new SQLEntity(sqlStr, parameters);
	    return executePageQuery(pi, sqlEntity, Role.class);
	}
	
	/**
	 * 删除功能权限类型的角色及角色相关的表：角色与用户 ，角色与功能权限
	 */
	public void delRoleFncAndRelation(String roleID)throws Exception{
		String sqlStr = " delete from sys_role where roleid = :roleID " ;
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		query.executeUpdate();
		
		sqlStr = " delete from sys_usr_role where roleid = :roleID " ;
		query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		query.executeUpdate();
	}
	
	/**
	 * 某个角色用户的功能权限ID列表
	 */
	@SuppressWarnings("unchecked")
	public List<String> queryFuncListByRoleID(String roleID) throws Exception {
		String sqlStr="select srf.FUNCID from  sys_role_func srf where srf.ROLEID=:roleID";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		return query.list();
	}
	
	/**
	 * 删除角色与功能权限关系
	 */
	public void deleteRoleFuncEntity(String roleID,String funcID) throws Exception {
		String sqlStr="delete from  sys_role_func  where ROLEID=:roleID and FUNCID = :funcID ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		query.setString("funcID", funcID);
		query.executeUpdate();
	}

	public void delUsrRole(String usrID) throws Exception{
		SQLQuery query = getCurrentSession().createSQLQuery(" delete from sys_usr_role where usrid = :usrID ");
		query.setString("usrID", usrID);
		query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> queryProcNodeIDList(String roleID) throws Exception {
		SQLQuery query = getCurrentSession().createSQLQuery("select NODEID from sys_role_proc where ROLEID = :roleID");
		query.setString("roleID", roleID);
		return query.list();
	}

	public void delRoleProcNode(String roleID) throws Exception {
		SQLQuery query = getCurrentSession().createSQLQuery("delete from sys_role_proc where ROLEID = :roleID");
		query.setString("roleID", roleID);
		query.executeUpdate();
	}
}