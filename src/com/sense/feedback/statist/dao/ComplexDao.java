package com.sense.feedback.statist.dao;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Component;

import com.sense.feedback.entity.ProcInst;
import com.sense.feedback.enumdic.EnumProcNode;
import com.sense.frame.base.BaseDAO;
import com.sense.frame.pub.model.PageInfo;

@Component
public class ComplexDao extends BaseDAO {

	public PageInfo queryCarTPage(PageInfo pageInfo, Map<String, String> paras) throws Exception{
		StringBuffer sql = new StringBuffer("select * from BIZ_PROCINST where STATUS <> :status ");
		String dph = paras.get("dph");
		if(StringUtils.isNotBlank(dph)){
			sql.append(" and DPH = :dph");
		}
		String ddh = paras.get("ddh");
		if(StringUtils.isNotBlank(ddh)){
			sql.append(" and DDH = :ddh");
		}
		sql.append(" order by STATUS");
		SQLEntity entity = new SQLEntity(sql.toString());
		entity.setObject("status", EnumProcNode.rk.getCode());
		if(StringUtils.isNotBlank(dph)){
			entity.setObject("dph", dph);
		}
		if(StringUtils.isNotBlank(ddh)){
			entity.setObject("ddh", ddh);
		}
		return executePageQuery(pageInfo, entity, ProcInst.class);
	}

	public Integer queryTsCount() throws Exception{
		String sql = "select count(1) num from BIZ_PROCINST where STATUS = :status";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("status", EnumProcNode.ts.getCode());
		return (Integer) query.addScalar("num", IntegerType.INSTANCE).uniqueResult();
	}

	public Integer queryGzCount() throws Exception{
		String sql = "select count(1) num from BIZ_PROCINST where STATUS = :status";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("status", EnumProcNode.gz.getCode());
		return (Integer) query.addScalar("num", IntegerType.INSTANCE).uniqueResult();
	}

	public Integer querySyCount() throws Exception{
		String sql = "select count(1) num from BIZ_PROCINST where STATUS = :status";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("status", EnumProcNode.sy.getCode());
		return (Integer) query.addScalar("num", IntegerType.INSTANCE).uniqueResult();
	}

	public PageInfo queryCarTOverPage(PageInfo pageInfo) throws Exception{
		String sql = "select inst.*,v_node.JGDAY from biz_procinst inst,"
			+ "(select SCDH,datediff(day,MIN(SUBMITTIME),MAX(SUBMITTIME)) JGDAY from BIZ_PROCNODE group by SCDH) v_node "
				+ "where inst.SCDH = v_node.SCDH and v_node.JGDAY > 14 order by inst.STATUS";
		SQLEntity entity = new SQLEntity(sql);
		return executePageQuery(pageInfo, entity, ProcInst.class);
	}

}
