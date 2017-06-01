package com.sense.sys.pst.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Component;

import com.sense.frame.base.BaseDAO;
import com.sense.frame.pub.model.PageInfo;
import com.sense.sys.entity.Pst;

@Component
public class PstDao extends BaseDAO {

	@SuppressWarnings("unchecked")
	public boolean queryPstExistCod(String orgID, String pstCod) throws Exception{
		Query query = getCurrentSession().createQuery("from Pst where orgID = :orgID and pstCod = :pstCod");
		query.setString("orgID", orgID);
		query.setString("pstCod", pstCod);
		List<Pst> list = query.list();
		if(CollectionUtils.isNotEmpty(list)){
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean queryPstExistNam(String orgID, String pstNam) throws Exception {
		Query query = getCurrentSession().createQuery("from Pst where orgID = :orgID and pstNam = :pstNam");
		query.setString("orgID", orgID);
		query.setString("pstNam", pstNam);
		List<Pst> list = query.list();
		if(CollectionUtils.isNotEmpty(list)){
			return true;
		}
		return false;
	}

	public int queryPstSeqNO(String orgID) throws Exception {
		SQLQuery query = getCurrentSession().createSQLQuery("select count(*) num from SYS_PST where orgID = :orgID");
		query.setString("orgID", orgID);
		int count = (Integer) query.addScalar("num", IntegerType.INSTANCE).uniqueResult();
		return count + 1;
	}

	public PageInfo queryPstWithPage(PageInfo pi, String orgID) throws Exception{
		String sql = "select * from SYS_PST where ORGID = :orgID";
		SQLEntity entity = new SQLEntity(sql);
		entity.setObject("orgID", orgID);
		return executePageQuery(pi, entity, Pst.class);
	}
	
	@SuppressWarnings("unchecked")
	public boolean queryPstExistCod(String orgID, String pstCod, String pstID) throws Exception{
		Query query = getCurrentSession().createQuery("from Pst where orgID = :orgID and pstCod = :pstCod and pstID <> :pstID");
		query.setString("orgID", orgID);
		query.setString("pstCod", pstCod);
		query.setString("pstID", pstID);
		List<Pst> list = query.list();
		if(CollectionUtils.isNotEmpty(list)){
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean queryPstExistNam(String orgID, String pstNam, String pstID) throws Exception {
		Query query = getCurrentSession().createQuery("from Pst where orgID = :orgID and pstNam = :pstNam and pstID <> :pstID");
		query.setString("orgID", orgID);
		query.setString("pstNam", pstNam);
		query.setString("pstID", pstID);
		List<Pst> list = query.list();
		if(CollectionUtils.isNotEmpty(list)){
			return true;
		}
		return false;
	}

	public void delPst(List<String> pstCheckList) throws Exception{
		Query query = getCurrentSession().createQuery("delete from Pst where pstID in (:pstID)");
		query.setParameterList("pstID", pstCheckList);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Pst> queryPstOwne(String usrID, String orgID) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct pst.* ");
		sql.append("   from SYS_PST pst, SYS_USR_PST sup ");
		sql.append("  where pst.pstID = sup.pstID ");
		sql.append("    and pst.orgID = :orgID ");
		sql.append("    and sup.usrID = :usrID ");
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.setString("usrID", usrID);
		query.setString("orgID", orgID);
		return query.addEntity(Pst.class).list();
	}

	@SuppressWarnings("unchecked")
	public List<Pst> queryPstNotOwne(String usrID, String orgID) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct pst.* ");
		sql.append("   from SYS_PST pst ");
		sql.append("  where pst.orgID = :orgID ");
		sql.append("    and pst.pstID not in ( ");
		sql.append("        select sup.pstID from SYS_USR_PST sup ");
		sql.append("         where sup.usrID = :usrID) ");
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.setString("usrID", usrID);
		query.setString("orgID", orgID);
		return query.addEntity(Pst.class).list();
	}

	@SuppressWarnings("unchecked")
	public List<String> queryProcNodeIDList(String pstID) throws Exception{
		SQLQuery query = getCurrentSession().createSQLQuery("select NODEID from sys_pst_proc where PSTID = :pstID");
		query.setString("pstID", pstID);
		return query.list();
	}

	public void delPstProcNode(String pstID) throws Exception{
		SQLQuery query = getCurrentSession().createSQLQuery("delete from sys_pst_proc where PSTID = :pstID");
		query.setString("pstID", pstID);
		query.executeUpdate();
	}
	
}