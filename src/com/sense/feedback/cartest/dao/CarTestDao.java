package com.sense.feedback.cartest.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.sense.feedback.entity.PaiChan;
import com.sense.feedback.entity.ProcInst;
import com.sense.feedback.entity.ProcInstNode;
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
		String xzOrgID = paras.get("xzOrgID");
		if(StringUtils.isNotBlank(xzOrgID)){
			sql.append(" and XZORGID like :xzOrgID");
		}
		String ddh = paras.get("ddh");
		if(StringUtils.isNotBlank(ddh)){
			sql.append(" and DDH = :ddh");
		}
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
		return executePageQuery(pageInfo, entity, ProcInst.class);
	}

	@SuppressWarnings("unchecked")
	public ProcInstNode queryProcInstNode(String scdh, String status) throws Exception{
		String hql = "from ProcInstNode where scdh = :scdh and proNode = :status";
		Query query = getCurrentSession().createQuery(hql);
		query.setString("scdh", scdh);
		query.setString("status", status);
		List<ProcInstNode> list = query.list();
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ProcInstNode> queryPreInstNodeList(String scdh, String status) throws Exception{
		String hql = "from ProcInstNode where scdh = :scdh and proNode < :status";
		Query query = getCurrentSession().createQuery(hql);
		query.setString("scdh", scdh);
		query.setString("status", status);
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
		sql.append("select pro.*,case when rk.rksj is null then '未入库' when rk.rksj is not null then '已入库' else '未知' end as repoinfo from BIZ_PROCINST pro, biz_rkinfo rk where pro.scdh = rk.scdh ");
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
			sql.append(" and proc.DPH = :dph");
		}
		SQLEntity entity = new SQLEntity(sql.toString());
		if(StringUtils.isNotBlank(dph)){
			entity.setObject("dph", dph);
		}
		return executePageQuery(pageInfo, entity, ProcInst.class);
	}

}