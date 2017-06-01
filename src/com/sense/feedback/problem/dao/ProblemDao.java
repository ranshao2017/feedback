package com.sense.feedback.problem.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Component;

import com.sense.feedback.entity.Problem;
import com.sense.feedback.entity.ProblemReply;
import com.sense.frame.base.BaseDAO;
import com.sense.frame.pub.model.PageInfo;

@Component
public class ProblemDao extends BaseDAO {

	public PageInfo queryProblemWithPage(PageInfo pageInfo, String userId, Map<String, String> paras) throws Exception {
		String proType = paras.get("proType");
		StringBuffer sql = new StringBuffer();
		sql.append("select * from BIZ_PROBLEM where CREATEUSRID = :userId");
		if(StringUtils.isNotBlank(proType)){
			sql.append(" and PROTYPE = :proType");
		}
		sql.append(" order by CREATEDATE desc");
		SQLEntity entity = new SQLEntity(sql.toString());
		entity.setObject("userId", userId);
		if(StringUtils.isNotBlank(proType)){
			entity.setObject("proType", proType);
		}
		return executePageQuery(pageInfo, entity, Problem.class);
	}

	@SuppressWarnings("unchecked")
	public List<ProblemReply> queryReply(String problemID) throws Exception{
		String hql = "from ProblemReply where problemID = :problemID order by createDate";
		Query query = getCurrentSession().createQuery(hql);
		query.setString("problemID", problemID);
		return query.list();
	}

	public PageInfo queryAllProblemWithPage(PageInfo pageInfo, Map<String, String> paras) throws Exception{
		String proType = paras.get("proType");
		StringBuffer sql = new StringBuffer();
		sql.append("select * from BIZ_PROBLEM where 1 = 1");
		if(StringUtils.isNotBlank(proType)){
			sql.append(" and PROTYPE = :proType");
		}
		sql.append(" order by CREATEDATE desc");
		SQLEntity entity = new SQLEntity(sql.toString());
		if(StringUtils.isNotBlank(proType)){
			entity.setObject("proType", proType);
		}
		return executePageQuery(pageInfo, entity, Problem.class);
	}

	public void autoCloseProblem(int day) throws Exception{
		String sql = "update BIZ_PROBLEM set STATUS = '3' where GETDATE() > DATEADD(day,:dayNum,CREATEDATE) and (STATUS <> '3' and STATUS <> '2')";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setInteger("dayNum", day);
		query.executeUpdate();
	}

}