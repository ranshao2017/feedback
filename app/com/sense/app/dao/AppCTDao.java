package com.sense.app.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
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

}