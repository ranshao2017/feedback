package com.sense.frame.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.sense.frame.common.util.BeanUtil;
import com.sense.frame.pub.model.PageInfo;


/**
 * Dao层基础类
 */
public abstract class BaseDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Value("#{propertiesReader[DATABASE]}")
	private String dataBase;//数据库 SQLSERVER ORACLE

	public Session getCurrentSession() {
		Session session= sessionFactory.getCurrentSession();
		session.setFlushMode(FlushMode.ALWAYS); //默认值为FlushMode.MANUAL
		return session;
	}
	
	/**
	 * 分页返回map列表，传入预处理sql，有效防止sql注入
	 */
	@SuppressWarnings("unchecked")
	public PageInfo executePageQuery(PageInfo pageInfo, SQLEntity sqlEntity) throws Exception {
		
		//1、查询指定的行数
		SQLQuery query = prepareSQLQuery(sqlEntity);
		int from = (pageInfo.getPageNumber() - 1) * pageInfo.getPageSize();
		query.setFirstResult(from);
		query.setMaxResults(pageInfo.getPageSize());
		List<Map<String, Object>> datalist = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		pageInfo.setRows(datalist);
		
		//2、查询总数
		String countSql = null;
		if("SQLSERVER".equals(dataBase)){
			int orderLast = sqlEntity.getSql().toLowerCase().lastIndexOf("order by");
			if(orderLast > 0){
				String tempCountSql = sqlEntity.getSql().substring(0, orderLast);
				countSql = "select count(1) num from (" + tempCountSql + ") sqlExecutorTable";
			}else{
				countSql = "select count(1) num from (" + sqlEntity.getSql() + ") sqlExecutorTable";
			}
		}else{
			countSql = "select count(1) num from (" + sqlEntity.getSql() + ") sqlExecutorTable";
		}
		sqlEntity.setSql(countSql);
		SQLQuery countQuery = prepareSQLQuery(sqlEntity);
		int count = (Integer) countQuery.addScalar("num", IntegerType.INSTANCE).uniqueResult();
		pageInfo.setTotal(String.valueOf(count));
				
		return pageInfo;
	}
	
	/**
	 * 分页返回对象列表，传入预处理sql，有效防止sql注入
	 */
	@SuppressWarnings("unchecked")
	public <T> PageInfo executePageQuery(PageInfo pageInfo, SQLEntity sqlEntity, Class<T> className) throws Exception {
		
		//1、查询指定的行数
		SQLQuery query = prepareSQLQuery(sqlEntity);
		int from = (pageInfo.getPageNumber() - 1) * pageInfo.getPageSize();
		query.setFirstResult(from);
		query.setMaxResults(pageInfo.getPageSize());
		List<Map<String, Object>> datalist = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		pageInfo.setRows(BeanUtil.getBeanListFromMap(datalist, className));
		
		//2、查询总数
		String countSql = null;
		if("SQLSERVER".equals(dataBase)){
			int orderLast = sqlEntity.getSql().toLowerCase().lastIndexOf("order by");
			if(orderLast > 0){
				String tempCountSql = sqlEntity.getSql().substring(0, orderLast);
				countSql = "select count(1) num from (" + tempCountSql + ") sqlExecutorTable";
			}else{
				countSql = "select count(1) num from (" + sqlEntity.getSql() + ") sqlExecutorTable";
			}
		}else{
			countSql = "select count(1) num from (" + sqlEntity.getSql() + ") sqlExecutorTable";
		}
		sqlEntity.setSql(countSql);
		SQLQuery countQuery = prepareSQLQuery(sqlEntity);
		int count = (Integer) countQuery.addScalar("num", IntegerType.INSTANCE).uniqueResult();
		pageInfo.setTotal(String.valueOf(count));
				
		return pageInfo;
	}
	
	//-------------------分页使用---------------------
	private SQLQuery prepareSQLQuery(SQLEntity sqlEntity){
		SQLQuery query = getCurrentSession().createSQLQuery(sqlEntity.getSql());
		
		if(null != sqlEntity.getParameters()){
			for (String paraName : sqlEntity.getParameters().keySet()) {
				Object paraValue = sqlEntity.getParameters().get(paraName);
				query.setParameter(paraName, paraValue);
			}
		}
		if(null != sqlEntity.getParameterList()){
			for (String paraName : sqlEntity.getParameterList().keySet()) {
				Collection<?> collectionValue = sqlEntity.getParameterList().get(paraName);
				query.setParameterList(paraName, collectionValue);
			}
		}
		
		return query;
	}
	
	public class SQLEntity {
		public String sql;//原生sql
		public Map<String, Object> parameters;//简单查询条件值
		public Map<String, Collection<?>> parameterList;//复杂查询条件值 in(?,?,?)
		
		public SQLEntity(){}

		public SQLEntity(String sql){
			this.sql = sql;
		}
		
		public SQLEntity(String sql, Map<String, Object> parameters){
			this.sql = sql;
			this.parameters = parameters;
		}
		
		public SQLEntity(String sql, Map<String, Object> parameters, Map<String, Collection<?>> parameterList){
			this.sql = sql;
			this.parameters = parameters;
			this.parameterList = parameterList;
		}
		
		public void setObject(String cod, Object value){
			if(getParameters()==null){
				this.parameters = new HashMap<String, Object>();
			}
			getParameters().put(cod, value);
		}
		
		public String getSql() {
			return sql;
		}
		public void setSql(String sql) {
			this.sql = sql;
		}
		public Map<String, Object> getParameters() {
			return parameters;
		}
		public Map<String, Collection<?>> getParameterList() {
			return parameterList;
		}
		public void setParameterList(Map<String, Collection<?>> parameterList) {
			this.parameterList = parameterList;
		}
	}
	
}