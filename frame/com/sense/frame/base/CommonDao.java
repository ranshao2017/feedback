package com.sense.frame.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Component;

/**
 * 通用数据操作
 */
@Component
public class CommonDao extends BaseDAO{

	/**
	 * 保存
	 */
	public void saveEntity(Object entity) throws Exception {
		getCurrentSession().save(entity);
	}
	
	/**
	 * 保存或者修改
	 */
	public void saveOrUpdateEntity(Object entity) throws Exception {
		getCurrentSession().saveOrUpdate(entity);
	}

	/**
	 * 修改
	 */
	public void updateEntity(Object entity) throws Exception {
		getCurrentSession().update(entity);
	}
	
	/**
	 * 按主键查询
	 */
	@SuppressWarnings("unchecked")
	public <T> T findEntityByID(Class<T> entityClass, Serializable id) throws Exception {
		return (T) getCurrentSession().get(entityClass, id);
	}
	
	/**
	 * 查询全部实体
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findEntityList(Class<T> entityClass) throws Exception {
		return getCurrentSession().createCriteria(entityClass).list();
	}
	
	/**
	 * 根据类名、属性、运算符、值获取实体列表
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findEntityList(Class<T> entityClass, String fieldName, String operate, Object value) throws Exception {
		String hql = "from " + entityClass.getSimpleName() + " where " + fieldName + " " + operate + " :fieldName";
		return getCurrentSession().createQuery(hql).setParameter("fieldName", value).list();
	}
	
	/**
	 * 根据类名、属性、值获取实体列表，默认是等号
	 */
	public <T> List<T> findEntityList(Class<T> entityClass, String fieldName, Object value) throws Exception {
		return findEntityList(entityClass, fieldName, "=", value);
	}
	
	/**
	 * 根据类名、属性、值列表获取实体，in查询
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findEntityList(Class<T> entityClass, String fieldName, Collection<String> valueList) throws Exception{
		String hql = "from " + entityClass.getSimpleName() + " where " + fieldName + " in (:fieldName)";
		return getCurrentSession().createQuery(hql).setParameterList("fieldName", valueList).list();
	}
	
	/**
	 * 删除对象
	 */
	public void delEntity(Object entity) throws Exception {
		getCurrentSession().delete(entity);
	}
	
	/**
	 * 按主键删除
	 */
	public void delEntityById(Class<?> entityClass, Serializable id) throws Exception {
		Object entity = getCurrentSession().get(entityClass, id);
		if (entity != null) {
			delEntity(entity);
		}
	}
	
	/**
	 * 直接执行原生sql语句
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findEntityList(Class<T> className, String sql) throws Exception {
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		return query.addEntity(className).list();
	}
	
}