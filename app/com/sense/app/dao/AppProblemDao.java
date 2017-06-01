package com.sense.app.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import com.sense.feedback.entity.Problem;
import com.sense.feedback.entity.ProblemReply;
import com.sense.frame.base.BaseDAO;
import com.sense.frame.pub.model.PageInfo;

@Component
public class AppProblemDao extends BaseDAO {

	public PageInfo queryProblemPage(PageInfo pi) throws Exception{
		String sql = "select * from BIZ_PROBLEM where STATUS = '0' or STATUS = '1' order by CREATEDATE desc";
		SQLEntity entity = new SQLEntity(sql);
		return executePageQuery(pi, entity, Problem.class);
	}

	@SuppressWarnings("unchecked")
	public List<ProblemReply> queryProblemReply(String problemid) throws Exception{
		String hql = "from ProblemReply where problemID = :problemID order by createDate desc";
		Query query = getCurrentSession().createQuery(hql);
		query.setString("problemID", problemid);
		return query.list();
	}
	
}