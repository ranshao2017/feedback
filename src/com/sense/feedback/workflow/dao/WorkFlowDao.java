package com.sense.feedback.workflow.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Component;

import com.sense.frame.base.BaseDAO;
import com.sense.sys.entity.ProcNode;

@Component
public class WorkFlowDao extends BaseDAO {

	@SuppressWarnings("unchecked")
	public List<String> queryUsrByProcNode(String procNode) throws Exception {
		String sql = "select USRID from SYS_USR_PROC where NODEID = :NODEID";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("NODEID", procNode);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> queryRoleByProcNode(String procNode) throws Exception {
		String sql = "select distinct sur.USRID from SYS_ROLE_PROC srp,SYS_USR_ROLE sur "
				+ "where srp.NODEID = :NODEID and srp.ROLEID = sur.ROLEID";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("NODEID", procNode);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<ProcNode> queryAllProcNode() throws Exception {
		String hql = "from ProcNode order by seqNO";
		Query query = getCurrentSession().createQuery(hql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> queryPstByProcNode(String procNode) throws Exception{
		String sql = "select distinct sup.USRID from SYS_PST_PROC spp, SYS_USR_PST sup "
				+ "where spp.pstid = sup.pstid and spp.nodeid = :nodeid";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("nodeid", procNode);
		return query.list();
	}
	
}