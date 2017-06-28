package com.sense.feedback.cartest.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Component;

import com.sense.feedback.entity.PaiChan;
import com.sense.feedback.entity.ProcInst;
import com.sense.feedback.entity.ProcInstNode;
import com.sense.feedback.entity.ProcInstReply;
import com.sense.feedback.enumdic.EnumYesNo;
import com.sense.frame.base.BaseDAO;
import com.sense.frame.common.util.BeanUtil;
import com.sense.frame.pub.model.PageInfo;

@Component
public class CarTestDao extends BaseDAO {

	public PageInfo queryPCPage(PageInfo pageInfo, Map<String, String> paras) throws Exception{
		String sql = "select pc.* from BIZ_PC pc where not exists (select 1 from BIZ_PROCINST p where pc.SCDH = p.SCDH)";
		String scdh = paras.get("scdh");
		String dph = paras.get("dph");
		String ddh = paras.get("ddh");
		if(StringUtils.isNotBlank(scdh)){
			sql += " and pc.SCDH = :scdh";
		}
		if(StringUtils.isNotBlank(dph)){
			sql += " and pc.DPH = :dph";
		}
		if(StringUtils.isNotBlank(ddh)){
			sql += " and pc.DDH = :ddh";
		}
		SQLEntity entity = new SQLEntity(sql);
		if(StringUtils.isNotBlank(scdh)){
			entity.setObject("scdh", scdh);
		}
		if(StringUtils.isNotBlank(dph)){
			entity.setObject("dph", dph);
		}
		if(StringUtils.isNotBlank(ddh)){
			entity.setObject("ddh", ddh);
		}
		return executePageQuery(pageInfo, entity, PaiChan.class);
	}

	public PageInfo queryCTPage(PageInfo pageInfo, Map<String, String> paras) throws Exception{
		StringBuffer sql = new StringBuffer("");
		sql.append("select BIZ_PROCINST.*,pnode.descr as nodedescr "
							+ "from BIZ_PROCINST left join (select MAX(scdh) scdh,MAX(PRONODE) pronode,MAX(DESCR) descr from BIZ_PROCNODE group by SCDH,PRONODE) pnode"
							+ "	   on BIZ_PROCINST.scdh = pnode.scdh and BIZ_PROCINST.status = pnode.proNode  where 1 = 1 ");
		String status = paras.get("status");
		if(StringUtils.isNotBlank(status)){
			sql.append(" and STATUS = :status");
		}
		String scdh = paras.get("scdh");
		if(StringUtils.isNotBlank(scdh)){
			sql.append(" and SCDH = :scdh");
		}
		String dph = paras.get("dph");
		if(StringUtils.isNotBlank(dph)){
			sql.append(" and DPH = :dph");
		}
		String xzOrgID = paras.get("xzOrgID");
		if(StringUtils.isNotBlank(xzOrgID)){
			sql.append(" and XZORGID like :xzOrgID");
		}
		String ddh = paras.get("ddh");
		if(StringUtils.isNotBlank(ddh)){
			sql.append(" and DDH = :ddh");
		}
		String nodeDescr = paras.get("nodeDescr");
		if(StringUtils.isNotBlank(nodeDescr)){
			sql.append(" and pnode.descr like :nodeDescr");
		}
		String jcsjStart = paras.get("jcsjStart");
		if(StringUtils.isNotBlank(jcsjStart)){
			sql.append(" and XXSJ >= CONVERT(datetime, :jcsjStart)");
		}
		String jcsjEnd = paras.get("jcsjEnd");
		if(StringUtils.isNotBlank(jcsjEnd)){
			sql.append(" and XXSJ < CONVERT(datetime, :jcsjEnd)");
		}
		String trq = paras.get("trq");
		if(StringUtils.isNotBlank(trq)){
			if(EnumYesNo.yes.getCode().equals(trq)){
				sql.append(" and (CX like '%L/%' or CX like '%C/%')");
			}else{
				sql.append(" and (CX not like '%L/%' and CX not like '%C/%')");
			}
		}
		sql.append(" order by xxsj ");
		SQLEntity entity = new SQLEntity(sql.toString());
		if(StringUtils.isNotBlank(status)){
			entity.setObject("status", status);
		}
		if(StringUtils.isNotBlank(scdh)){
			entity.setObject("scdh", scdh);
		}
		if(StringUtils.isNotBlank(dph)){
			entity.setObject("dph", dph);
		}
		if(StringUtils.isNotBlank(xzOrgID)){
			entity.setObject("xzOrgID", "%" + xzOrgID + "%");
		}
		if(StringUtils.isNotBlank(ddh)){
			entity.setObject("ddh", ddh);
		}
		if(StringUtils.isNotBlank(jcsjStart)){
			entity.setObject("jcsjStart", jcsjStart);
		}
		if(StringUtils.isNotBlank(jcsjEnd)){
			entity.setObject("jcsjEnd", jcsjEnd);
		}
		if(StringUtils.isNotBlank(nodeDescr)){
			entity.setObject("nodeDescr", "%" + nodeDescr + "%");
		}
		return executePageQuery(pageInfo, entity, ProcInst.class);
	}

	@SuppressWarnings("unchecked")
	public ProcInstNode queryCurrenttNode(String scdh, String status) throws Exception{
		String hql = "from ProcInstNode where scdh = :scdh order by createTime desc";
		Query query = getCurrentSession().createQuery(hql);
		query.setString("scdh", scdh);
		List<ProcInstNode> list = query.list();
		if(CollectionUtils.isNotEmpty(list)){
			ProcInstNode node = list.get(0);
			if(node.getProNode().equals(status)){
				return node;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ProcInstNode> queryPreInstNodeList(String scdh) throws Exception{
		String hql = "from ProcInstNode where scdh = :scdh order by createTime";
		Query query = getCurrentSession().createQuery(hql);
		query.setString("scdh", scdh);
		return query.list();
	}

	public void deleteQJ(String scdh) throws Exception{
		String sql = "delete from BIZ_QJ where SCDH = :scdh";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("scdh", scdh);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public PaiChan queryPCDetail(String scdh) throws Exception{
		String sql = "select * from BIZ_PC where SCDH = :scdh";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("scdh", scdh);
		Map<String, Object> paichan = (Map<String, Object>) query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
		return BeanUtil.getBeanFromMap(paichan, PaiChan.class);
	}

	public PageInfo queryRepoPage(PageInfo pageInfo, Map<String, String> paras) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select pro.*,case when rk.rksj is null then '未入库' when rk.rksj is not null then '已入库' else '未知' end as repoinfo,"
				+ "case when pro.status <> '4' and rk.rksj is not null then 1 "
				+ "     when pro.status = '4' and rk.rksj is null then 2 "
				+ "	    else 3 end as repoorder from BIZ_PROCINST pro, biz_rkinfo rk where pro.scdh = rk.scdh ");
		String repoinfo = paras.get("repoinfo");
		if(StringUtils.isNotBlank(repoinfo)){
			if(EnumYesNo.yes.getCode().equals(repoinfo)){
				sql.append(" and rk.rksj is not null ");
			}else{
				sql.append(" and rk.rksj is null ");
			}
		}
		String dph = paras.get("dph");
		if(StringUtils.isNotBlank(dph)){
			sql.append(" and pro.DPH = :dph");
		}
		sql.append(" order by repoorder,pro.STATUS");
		SQLEntity entity = new SQLEntity(sql.toString());
		if(StringUtils.isNotBlank(dph)){
			entity.setObject("dph", dph);
		}
		return executePageQuery(pageInfo, entity, ProcInst.class);
	}

	@SuppressWarnings("unchecked")
	public ProcInstNode queryPreInstNode(String scdh, String status) throws Exception{
		String hql = "from ProcInstNode where scdh = :scdh and proNode <> :status order by createTime desc";
		Query query = getCurrentSession().createQuery(hql);
		query.setString("scdh", scdh);
		query.setString("status", status);
		List<ProcInstNode> list = query.list();
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	public Integer queryCtCount(String status) throws Exception{
		String sql = "select count(1) num from BIZ_PROCINST where STATUS = :status";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("status", status);
		return (Integer) query.addScalar("num", IntegerType.INSTANCE).uniqueResult();
	}

	public Integer queryLngCount(String status) throws Exception{
		String sql = "select count(1) num from BIZ_PROCINST where STATUS = :status and CX like '%L/%'";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("status", status);
		return (Integer) query.addScalar("num", IntegerType.INSTANCE).uniqueResult();
	}

	public Integer queryCngCount(String status) throws Exception{
		String sql = "select count(1) num from BIZ_PROCINST where STATUS = :status and CX like '%C/%'";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("status", status);
		return (Integer) query.addScalar("num", IntegerType.INSTANCE).uniqueResult();
	}

	public PageInfo queryXZCTPage(PageInfo pageInfo, Map<String, String> paras) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select * from BIZ_PROCINST where 1 = 1 ");
		String scdh = paras.get("scdh");
		if(StringUtils.isNotBlank(scdh)){
			sql.append(" and SCDH = :scdh");
		}
		String dph = paras.get("dph");
		if(StringUtils.isNotBlank(dph)){
			sql.append(" and DPH = :dph");
		}
		String xzOrgID = paras.get("xzOrgID");
		if(StringUtils.isNotBlank(xzOrgID)){
			sql.append(" and XZORGID like :xzOrgID");
		}
		String ddh = paras.get("ddh");
		if(StringUtils.isNotBlank(ddh)){
			sql.append(" and DDH = :ddh");
		}
		SQLEntity entity = new SQLEntity(sql.toString());
		if(StringUtils.isNotBlank(scdh)){
			entity.setObject("scdh", scdh);
		}
		if(StringUtils.isNotBlank(dph)){
			entity.setObject("dph", dph);
		}
		if(StringUtils.isNotBlank(xzOrgID)){
			entity.setObject("xzOrgID", "%" + xzOrgID + "%");
		}
		if(StringUtils.isNotBlank(ddh)){
			entity.setObject("ddh", ddh);
		}
		return executePageQuery(pageInfo, entity, ProcInst.class);
	}

	@SuppressWarnings("unchecked")
	public List<ProcInstReply> queryReplyList(String scdh) throws Exception{
		String hql = "from ProcInstReply where scdh = :scdh order by createTime";
		Query query = getCurrentSession().createQuery(hql);
		query.setString("scdh", scdh);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<ProcInst> queryExportCT(Map<String, String> paras) throws Exception{
		StringBuffer sql = new StringBuffer("select * from BIZ_PROCINST where 1 = 1");
		String status = paras.get("status");
		if(StringUtils.isNotBlank(status)){
			sql.append(" and STATUS = :status");
		}
		String scdh = paras.get("scdh");
		if(StringUtils.isNotBlank(scdh)){
			sql.append(" and SCDH = :scdh");
		}
		String dph = paras.get("dph");
		if(StringUtils.isNotBlank(dph)){
			sql.append(" and DPH = :dph");
		}
		String ddh = paras.get("ddh");
		if(StringUtils.isNotBlank(ddh)){
			sql.append(" and DDH = :ddh");
		}
		String trq = paras.get("trq");
		if(StringUtils.isNotBlank(trq)){
			if(EnumYesNo.yes.getCode().equals(trq)){
				sql.append(" and (CX like '%L/%' or CX like '%C/%')");
			}else{
				sql.append(" and (CX not like '%L/%' and CX not like '%C/%')");
			}
		}
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		if(StringUtils.isNotBlank(status)){
			query.setString("status", status);
		}
		if(StringUtils.isNotBlank(scdh)){
			query.setString("scdh", scdh);
		}
		if(StringUtils.isNotBlank(dph)){
			query.setString("dph", dph);
		}
		if(StringUtils.isNotBlank(ddh)){
			query.setString("ddh", ddh);
		}
		return query.addEntity(ProcInst.class).list();
	}

}