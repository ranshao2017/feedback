package com.sense.feedback.statist.dao;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Component;

import com.sense.feedback.entity.ProcInst;
import com.sense.feedback.enumdic.EnumProcNode;
import com.sense.feedback.enumdic.EnumYesNo;
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
		String sql = "select *, datediff(day, JCSJ, RKSJ) JGDAY from biz_procinst "
				+ " where datediff(day, JCSJ, (CASE WHEN RKSJ IS NULL THEN getdate() ELSE RKSJ END)) > 14 order by STATUS";
		SQLEntity entity = new SQLEntity(sql);
		return executePageQuery(pageInfo, entity, ProcInst.class);
	}

	public PageInfo queryStaistPage(PageInfo pageInfo, Map<String, String> paras) throws Exception{
		StringBuffer sql = new StringBuffer("select * from BIZ_PROCINST where 1 = 1 ");
		String ddh = paras.get("ddh");
		if(StringUtils.isNotBlank(ddh)){
			sql.append(" and DDH = :ddh");
		}
		String dph = paras.get("dph");
		if(StringUtils.isNotBlank(dph)){
			sql.append(" and DPH = :dph");
		}
		String qj = paras.get("qj");
		if(StringUtils.isNotBlank(qj)){
			sql.append(" and QJFLAG = :qj");
		}
		String jcsjStart = paras.get("jcsjStart");
		if(StringUtils.isNotBlank(jcsjStart)){
			sql.append(" and JCSJ >= CONVERT(datetime, :jcsjStart)");
		}
		String jcsjEnd = paras.get("jcsjEnd");
		if(StringUtils.isNotBlank(jcsjEnd)){
			sql.append(" and JCSJ < CONVERT(datetime, :jcsjEnd)");
		}
		String status = paras.get("status");
		if(StringUtils.isNotBlank(status)){
			sql.append(" and STATUS = :status");
		}
		String rkStart = paras.get("rkStart");
		if(StringUtils.isNotBlank(rkStart)){
			sql.append(" and RKSJ >= CONVERT(datetime, :rkStart)");
		}
		String rkEnd = paras.get("rkEnd");
		if(StringUtils.isNotBlank(rkEnd)){
			sql.append(" and RKSJ < CONVERT(datetime, :rkEnd)");
		}
		String trq = paras.get("trq");
		if(StringUtils.isNotBlank(trq)){
			if(EnumYesNo.yes.getCode().equals(trq)){
				sql.append(" and (CX like '%L/%' or CX like '%C/%')");
			}else{
				sql.append(" and (CX not like '%L/%' and CX not like '%C/%')");
			}
		}
		String cq = paras.get("cq");
		if(StringUtils.isNotBlank(cq)){
			if(EnumYesNo.yes.getCode().equals(cq)){
				sql.append(" and datediff(day, JCSJ, (CASE WHEN RKSJ IS NULL THEN getdate() ELSE RKSJ END)) > 14");
			}else{
				sql.append(" and datediff(day, JCSJ, (CASE WHEN RKSJ IS NULL THEN getdate() ELSE RKSJ END)) <= 14");
			}
		}
		sql.append(" order by STATUS");
		SQLEntity entity = new SQLEntity(sql.toString());
		if(StringUtils.isNotBlank(ddh)){
			entity.setObject("ddh", ddh);
		}
		if(StringUtils.isNotBlank(dph)){
			entity.setObject("dph", dph);
		}
		if(StringUtils.isNotBlank(qj)){
			entity.setObject("qj", qj);
		}
		if(StringUtils.isNotBlank(jcsjStart)){
			entity.setObject("jcsjStart", jcsjStart);
		}
		if(StringUtils.isNotBlank(jcsjEnd)){
			entity.setObject("jcsjEnd", jcsjEnd);
		}
		if(StringUtils.isNotBlank(status)){
			entity.setObject("status", status);
		}
		if(StringUtils.isNotBlank(rkStart)){
			entity.setObject("rkStart", rkStart);
		}
		if(StringUtils.isNotBlank(rkEnd)){
			entity.setObject("rkEnd", rkEnd);
		}
		return executePageQuery(pageInfo, entity, ProcInst.class);
	}

}
