package com.sense.app.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Component;

import com.sense.feedback.entity.ProcInstNode;
import com.sense.frame.base.BaseDAO;

@Component
public class AppCTDao extends BaseDAO {

	@SuppressWarnings("unchecked")
	public List<ProcInstNode> queryNodeList(String scdh) throws Exception{
		String hql = "from ProcInstNode where scdh = :scdh order by proNode";
		Query query = getCurrentSession().createQuery(hql);
		query.setString("scdh", scdh);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public ProcInstNode queryNode(String scdh, String status) throws Exception{
		String hql = "from ProcInstNode where scdh = :scdh and proNode = :proNode";
		Query query = getCurrentSession().createQuery(hql);
		query.setString("scdh", scdh);
		query.setString("proNode", status);
		List<ProcInstNode> nodeList = query.list();
		if(CollectionUtils.isNotEmpty(nodeList)){
			return nodeList.get(0);
		}
		return null;
	}

}