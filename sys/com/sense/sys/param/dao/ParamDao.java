package com.sense.sys.param.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Component;

import com.sense.frame.base.BaseDAO;

@Component
public class ParamDao extends BaseDAO{

	@SuppressWarnings("unchecked")
	public List<String> queryValByCod(String code) throws Exception {
		String sqlStr=" select val from SYS_PARAM where COD = :code ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("code", code);
		return query.list();
	}
}