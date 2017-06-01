package com.sense.frame.base;

import java.util.Date;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sense.frame.common.util.DateUtil;
import com.sense.frame.common.util.UUID32Utils;

@Component
public class DBUtil {

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * 获取ID---使用uuid32
	 */
	public String getCommonId() throws Exception {
		return UUID32Utils.getUUID();
	}

	/**
	 * 获得系统日期
	 */
	public Date getSysDate() {
		return new Date();
	}

	/**
	 * 获取sequeneID---使用oracle sequene 生成单据号用 单据号为纯数字便于用户录入，如果用uuid，用户录入检索时会比较麻烦
	 */
	public String getSequenceIDWithDate() throws Exception {
		Date sysdate = this.getSysDate();
		String dateStr = DateUtil.dateToString(sysdate, "yyyyMMdd");
		return dateStr + generateSysId("SEQ_SYS");
	}

	/**
	 * 获得一个序列值
	 */
	private String generateSysId(String seqName) {
		String hql = "select " + seqName + ".nextval nextval from Dual";
		Session session = sessionFactory.getCurrentSession();
		session.setFlushMode(FlushMode.ALWAYS); // 默认值为FlushMode.MANUAL
		return String.valueOf((session.createSQLQuery(hql).addScalar("nextval",
				LongType.INSTANCE)).uniqueResult());
	}

}