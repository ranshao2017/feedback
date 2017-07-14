package com.sense.feedback.notice.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Component;

import com.sense.feedback.entity.Notice;
import com.sense.feedback.enumdic.EnumYesNo;
import com.sense.frame.base.BaseDAO;
import com.sense.frame.pub.model.PageInfo;
import com.sense.sys.entity.Usr;

@Component
public class NoticeDao extends BaseDAO {

	public PageInfo queryNoticeWithPage(PageInfo pageInfo, String userId, Map<String,String> paras) throws Exception{
		String noticeType = paras.get("noticeType");
		String topic = paras.get("topic");
		StringBuffer sql = new StringBuffer("select * from BIZ_NOTICE where CREATEUSRID = :userId ");
		if(StringUtils.isNotBlank(noticeType)){
			sql.append(" and NOTICETYPE = :noticeType");
		}
		if(StringUtils.isNotBlank(topic)){
			sql.append(" and TOPIC like :topic");
		}
		sql.append(" order by CREATEDATE desc");
		SQLEntity entity = new SQLEntity(sql.toString());
		entity.setObject("userId", userId);
		if(StringUtils.isNotBlank(noticeType)){
			entity.setObject("noticeType", noticeType);
		}
		if(StringUtils.isNotBlank(topic)){
			entity.setObject("topic", "%" + topic + "%");
		}
		return executePageQuery(pageInfo, entity, Notice.class);
	}

	public void delNoticeRead(String noticeID) {
		String sql = "delete from BIZ_NOTICE_READ where noticeid = :noticeID";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("noticeID", noticeID);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Usr> queryNoticeReceiver(String noticeID) throws Exception{
		String sql = "select * from sys_usr where usrid in (select userid from biz_notice_read where noticeid = :noticeID)";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("noticeID", noticeID);
		return query.addEntity(Usr.class).list();
	}

	public PageInfo queryRecNoticeWithPage(PageInfo pageInfo, String userId) throws Exception{
		String sql = "select n.*,nr.isRead from biz_notice n, BIZ_NOTICE_READ nr where n.id = nr.noticeid and nr.userID = :userID order by nr.isRead, n.createDate desc";
		SQLEntity entity = new SQLEntity(sql);
		entity.setObject("userID", userId);
		return executePageQuery(pageInfo, entity, Notice.class);
	}

	public void updateRead(String noticeID, String userId) throws Exception{
		String sql = "update BIZ_NOTICE_READ set isRead = '" + EnumYesNo.yes.getCode() + "' where noticeid = :noticeID and userid = :userId";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("noticeID", noticeID);
		query.setString("userId", userId);
		query.executeUpdate();
	}

}