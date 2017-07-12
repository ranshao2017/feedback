package com.sense.app.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Component;

import com.sense.feedback.entity.ProcInst;
import com.sense.feedback.entity.ProcInstNode;
import com.sense.frame.base.BaseDAO;

@Component
public class AppCTDao extends BaseDAO {

	@SuppressWarnings("unchecked")
	public List<ProcInstNode> queryNodeList(String scdh) throws Exception{
		String hql = "from ProcInstNode where scdh = :scdh order by createTime";
		Query query = getCurrentSession().createQuery(hql);
		query.setString("scdh", scdh);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public ProcInstNode queryNode(String scdh, String status) throws Exception{
		String hql = "from ProcInstNode where scdh = :scdh order by createTime desc";
		Query query = getCurrentSession().createQuery(hql);
		query.setString("scdh", scdh);
		List<ProcInstNode> list = query.list();
		if(CollectionUtils.isNotEmpty(list)){
			ProcInstNode node = list.get(0);
			if(node.getProNode().equals(status)){
				return node;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public ProcInst queryProcInst(String dph) throws Exception{
		if(dph.length() == 5){
			String hql = "from ProcInst where dph like :dph";
			Query query = getCurrentSession().createQuery(hql);
			query.setString("dph", "%" + dph);
			List<ProcInst> list = query.list();
			if(CollectionUtils.isNotEmpty(list)){
				return list.get(0);
			}
		}else{
			String hql = "from ProcInst where dph = :dph";
			Query query = getCurrentSession().createQuery(hql);
			query.setString("dph", dph);
			List<ProcInst> list = query.list();
			if(CollectionUtils.isNotEmpty(list)){
				return list.get(0);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<String> queryOwnProcNode(String userid) throws Exception{
		String sql = "select distinct nodeid from SYS_USR_ROLE sur,SYS_ROLE_PROC srp where sur.ROLEID = srp.ROLEID and sur.USRID = :userid";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("userid", userid);
		return query.list();
	}

	public Integer queryCTTotal(String dph, String ddh, String cx, List<String> nodeIDs) throws Exception{
		StringBuffer sql = new StringBuffer("select count(1) num from BIZ_PROCINST where STATUS not in ('0', '4') ");
		if(CollectionUtils.isNotEmpty(nodeIDs)){
			sql.append(" and STATUS in (");
			for(int i = 0; i < nodeIDs.size(); i ++){
				if(i > 0){
					sql.append(",");
				}
				sql.append("'").append(nodeIDs.get(i)).append("'");
			}
			sql.append(")");
		}
		if(StringUtils.isNotBlank(dph)){
			if(dph.length() == 5){
				sql.append(" and DPH like :dph");
			}else{
				sql.append(" and DPH = :dph");
			}
		}
		if(StringUtils.isNotBlank(ddh)){
			sql.append(" and DDH = :ddh");
		}
		if(StringUtils.isNotBlank(cx)){
			sql.append(" and CX = :cx");
		}
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		if(StringUtils.isNotBlank(dph)){
			if(dph.length() == 5){
				query.setString("dph", "%" + dph + "%");
			}else{
				query.setString("dph", dph);
			}
		}
		if(StringUtils.isNotBlank(ddh)){
			query.setString("ddh", ddh);
		}
		if(StringUtils.isNotBlank(cx)){
			query.setString("cx", cx);
		}
		int count = (Integer) query.addScalar("num", IntegerType.INSTANCE).uniqueResult();
		return count;
	}

}