package com.sense.sys.usr.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.sense.frame.base.BaseDAO;
import com.sense.frame.enumdic.EnumRootNodeID;
import com.sense.frame.enumdic.EnumSuperAdminType;
import com.sense.frame.pub.model.PageInfo;
import com.sense.sys.entity.Func;
import com.sense.sys.entity.Usr;
import com.sense.sys.entity.UsrPst;
import com.sense.sys.entity.UsrRole;

@Component
public class UserDao extends BaseDAO {
	/**
	 * 某个用户与角色关系
	 */
	@SuppressWarnings("unchecked")
	public UsrRole queryUsrRoleEntity(String usrID ,String roleID) throws Exception {
		String sqlStr="select * from  sys_usr_role  where ROLEID=:roleID and USRID = :usrID ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		query.setString("usrID", usrID);
		query.addEntity(UsrRole.class);
		List<UsrRole> ls  = query.list();
		if(ls!=null&&ls.size()>0){
			return ls.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public UsrPst queryUsrPst(String usrID, String pstID) throws Exception {
		String hql = "from UsrPst where usrID = :usrID and pstID = :pstID";
		Query query = getCurrentSession().createQuery(hql);
		query.setString("usrID", usrID);
		query.setString("pstID", pstID);
		List<UsrPst> list = query.list();
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 某个用户与角色关系
	 */
	public void delUsrRoleEntity(String usrID ,String roleID) throws Exception {
		String sqlStr="delete from  sys_usr_role  where ROLEID=:roleID and USRID = :usrID ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		query.setString("usrID", usrID);
		query.executeUpdate();
	}
	
	/**
	 * 删除某个用户与岗位关系
	 */
	public void delUsrPst(String usrID ,String pstID) throws Exception {
		String sqlStr="delete from  sys_usr_pst  where pstID = :pstID and USRID = :usrID ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("pstID", pstID);
		query.setString("usrID", usrID);
		query.executeUpdate();
	}
	
	/*
	 * 查找用户编码
	 */
	@SuppressWarnings("unchecked")
	public List<Usr> queryUsrByCod(String usrCod, String usrID) {
		Query query = getCurrentSession().createQuery(" from Usr where usrCod = :usrCod and usrID <> :usrID");
		query.setString("usrCod", usrCod);
		query.setString("usrID", usrID);
		return query.list();
	}
	
	/*
	 * 查找用户名称
	 */
	@SuppressWarnings("unchecked")
	public List<Usr> queryUsrByNam(String usrNam, String usrID) {
		Query query = getCurrentSession().createQuery(" from Usr where usrNam = :usrNam and usrID <> :usrID");
		query.setString("usrNam", usrNam);
		query.setString("usrID", usrID);
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
	
	//保证超级用户superadmin不被查询出来,但是超级用户也可以是正常的机构用户
	//保证orgID = root的用户不能查询出来，都不是用户创建的。
	public PageInfo queryUsrWithPage(PageInfo pi ,String orgID,String usrCodOrNam) throws Exception{
		String sqlStr  = " select a.* ,b.orgnam as orgNam from sys_usr a " +
				" left join sys_org b on b.orgid = a.orgid " +
				" where  a.orgid <> :excludeRootOrg and a.usrcod <> :excludeRootUsr  " ;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("excludeRootOrg", EnumRootNodeID.ROOTNODE.getCode());
		parameters.put("excludeRootUsr", EnumSuperAdminType.SUPERADMIN.getCode());
		if(!EnumRootNodeID.ROOTNODE.getCode().equals(orgID)&&StringUtils.isNotBlank(orgID)){
			sqlStr += " and a.orgid = :orgID " ;//机构不是EAMSROOT，添加过滤条件，否则就查询全部机构
			parameters.put("orgID", orgID);
		}
		if(StringUtils.isNotBlank(usrCodOrNam)){
			//机构不是ROOT，添加过来条件，否则就查询全部机构
			sqlStr += " and (a.usrcod like :usrCod or a.usrnam like :usrNam ) " ;
			parameters.put("usrCod", "%"+usrCodOrNam+"%");
			parameters.put("usrNam", "%"+usrCodOrNam+"%");
		}
		SQLEntity sqlEntity = new SQLEntity(sqlStr, parameters);
		return executePageQuery(pi, sqlEntity, Usr.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Usr> queryUsrByOrg(String orgID, String usrCodOrNam) throws Exception {
		String sqlStr  = " select a.* ,b.orgnam as orgNam from sys_usr a " +
				" left join sys_org b on b.orgid = a.orgid " +
				" where  a.orgid <> :excludeRootOrg and a.usrcod <> :excludeRootUsr  ";
		if(!EnumRootNodeID.ROOTNODE.getCode().equals(orgID)&&StringUtils.isNotBlank(orgID)){
			sqlStr += " and a.orgid = :orgID " ;//机构不是EAMSROOT，添加过滤条件，否则就查询全部机构
		}
		if(StringUtils.isNotBlank(usrCodOrNam)){
			//机构不是ROOT，添加过来条件，否则就查询全部机构
			sqlStr += " and (a.usrcod like :usrCod or a.usrnam like :usrNam ) " ;
		}
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("excludeRootOrg", EnumRootNodeID.ROOTNODE.getCode());
		query.setString("excludeRootUsr", EnumSuperAdminType.SUPERADMIN.getCode());
		if(!EnumRootNodeID.ROOTNODE.getCode().equals(orgID)&&StringUtils.isNotBlank(orgID)){
			query.setString("orgID", orgID);
		}
		if(StringUtils.isNotBlank(usrCodOrNam)){
			query.setString("usrCod", "%"+usrCodOrNam+"%");
			query.setString("usrNam", "%"+usrCodOrNam+"%");
		}
		return query.addEntity(Usr.class).list();
	}
	
	/**
	 * 检测登入用户
	 */
	@SuppressWarnings("unchecked")
	public List<Usr> getUserByNoPwd(String usrCod, String pwd) throws Exception {
		String sqlStr = " select * from sys_usr where usrCod =:usrCod and usrPwd =:pwd ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("usrCod", usrCod);
		query.setString("pwd", pwd);
		return query.addEntity(Usr.class).list();
	}

	@SuppressWarnings("unchecked")
	public List<String> queryReferRoleIDs(String referUsrID) throws Exception {
		String sqlStr=" select distinct a.roleid from sys_usr_role a where a.usrid = :referUsrID ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("referUsrID", referUsrID);
		return query.list();
	}

	public void delUsrRole(String usrID) throws Exception {
		String sql = " delete from sys_usr_role where usrid = :usrID ";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("usrID", usrID);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<String> queryProcNodeIDList(String usrID) throws Exception{
		SQLQuery query = getCurrentSession().createSQLQuery("select NODEID from sys_usr_proc where USRID = :usrID");
		query.setString("usrID", usrID);
		return query.list();
	}

	public void delUsrProcNode(String usrID) throws Exception{
		SQLQuery query = getCurrentSession().createSQLQuery("delete from sys_usr_proc where USRID = :usrID");
		query.setString("usrID", usrID);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public String queryUsrPsts(String usrID) throws Exception {
		String sql = "select pstnam from SYS_PST where pstid in (select pstid from SYS_USR_PST where usrid = :usrID)";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("usrID", usrID);
		List<String> pstNamList = query.list();
		if(CollectionUtils.isNotEmpty(pstNamList)){
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < pstNamList.size(); i ++){
				if(i > 0){
					sb.append("，");
				}
				sb.append(pstNamList.get(i));
			}
			return sb.toString();
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryNoticeUsr(String[] usrIDArr) throws Exception{
		List<String> usrIDList = Arrays.asList(usrIDArr);
		String sql = "select CLIENTID,CLIENTTYPE from SYS_USR where USRID in (:usrIDArr)";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setParameterList("usrIDArr", usrIDList);
		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
}