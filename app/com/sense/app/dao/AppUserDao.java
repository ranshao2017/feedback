package com.sense.app.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Component;

import com.sense.frame.base.BaseDAO;
import com.sense.sys.entity.Pst;
import com.sense.sys.entity.Usr;

@Component
public class AppUserDao extends BaseDAO {

	@SuppressWarnings("unchecked")
	public Usr queryUser(String username, String pwd) throws Exception{
		String hql = "from Usr where usrCod = :usrCod and usrPwd = :usrPwd";
		Query query = getCurrentSession().createQuery(hql);
		query.setString("usrCod", username);
		query.setString("usrPwd", pwd);
		List<Usr> list = query.list();
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Pst> queryPst(String usrID) throws Exception{
		String sql = "select * from SYS_PST where PSTID in (select PSTID from SYS_USR_PST where USRID = :usrID)";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("usrID", usrID);
		return query.addEntity(Pst.class).list();
	}

}