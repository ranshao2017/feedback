package com.sense.sys.org.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import com.sense.frame.base.BaseDAO;
import com.sense.sys.entity.Org;

@Component
public class OrgDao extends BaseDAO {
	
	/*
	 * 查找机构编码
	 */
	@SuppressWarnings("unchecked")
	public List<Org> queryOrgByCod(String orgCod, String orgID) {
		Query query = getCurrentSession().createQuery(" from Org where orgCod = :orgCod and orgID <> :orgID");
		query.setString("orgCod", orgCod);
		query.setString("orgID", orgID);
		return query.list();
	}
	
	/*
	 * 查找机构名称
	 */
	@SuppressWarnings("unchecked")
	public List<Org> queryOrgByNam(String orgNam, String orgID) {
		Query query = getCurrentSession().createQuery(" from Org where orgNam = :orgNam and orgID <> :orgID");
		query.setString("orgNam", orgNam);
		query.setString("orgID", orgID);
		return query.list();
	}
	
	/**
	 * 根据机构ID，获取直接子机构
	 */
	@SuppressWarnings("unchecked")
	public List<Org> querySunOrgByPID(String parentID) throws Exception {
		Query query = getCurrentSession().createQuery(" from Org where parentID= :parentID");
		query.setString("parentID", parentID);
		return query.list();
	}
	
}
