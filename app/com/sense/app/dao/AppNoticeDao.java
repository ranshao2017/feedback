package com.sense.app.dao;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Component;

import com.sense.feedback.entity.Notice;
import com.sense.frame.base.BaseDAO;
import com.sense.frame.pub.model.PageInfo;

@Component
public class AppNoticeDao extends BaseDAO {

	public PageInfo queryNoticePage(PageInfo pi, String userid) throws Exception{
		String sql = "select n.*,nr.ISREAD from BIZ_NOTICE n, BIZ_NOTICE_READ nr where n.ID = nr.NOTICEID and nr.USERID = :userid order by CREATEDATE desc";
		SQLEntity entity = new SQLEntity(sql);
		entity.setObject("userid", userid);
		return executePageQuery(pi, entity, Notice.class);
	}

	public void readNotice(String noticeid, String userid) throws Exception{
		String sql = "update BIZ_NOTICE_READ set ISREAD = '1' where NOTICEID = :noticeid and USERID = :userid";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("noticeid", noticeid);
		query.setString("userid", userid);
		query.executeUpdate();
	}

}