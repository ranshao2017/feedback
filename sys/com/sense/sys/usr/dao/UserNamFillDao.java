package com.sense.sys.usr.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.sense.frame.base.BaseDAO;
import com.sense.frame.common.util.BeanUtil;

@Component
public class UserNamFillDao extends BaseDAO {
	/**
	 * 填充用户姓名
	 */
	public <T> T fillNam4Bean(T sourceBean ,String attrUsrID,String attrUsrNam, Class<T> pclass) throws Exception {
		if(sourceBean!=null){
			List<T> sourceList=new ArrayList<T>(1);
			sourceList.add(sourceBean);
			sourceBean = fillNam4BeanList(sourceList, attrUsrID, attrUsrNam, pclass).get(0);
		}
		return sourceBean;
	}
	
	/**
	 * 填充用户姓名
	 */
	public void fillNam4Map(Map<String, Object> sourceMap ,String attrUsrID,String attrUsrNam) throws Exception {
		if(sourceMap!=null){
			List<Map<String, Object>> sourceList=new ArrayList<Map<String, Object>>();
			sourceList.add(sourceMap);
			fillNam4MapList(sourceList, attrUsrID, attrUsrNam);
		}
	}
	
	/**
	 * 填充用户姓名
	 */
	public <T> List<T> fillNam4BeanList(List<T> sourceList ,String attrUsrID,String attrUsrNam, Class<T> pclass) throws Exception {
		if(sourceList!=null&&sourceList.size()>0){
			List<Map<String, Object>> transmap_sourceList =new ArrayList<Map<String, Object>>();
			for(int i=0;i<sourceList.size();i++){
				transmap_sourceList.add(BeanUtil.getMapFromBean(sourceList.get(i)));
			}
			fillNam4MapList(transmap_sourceList ,attrUsrID ,attrUsrNam);
			sourceList = BeanUtil.getBeanListFromMap(transmap_sourceList, pclass);
		}
		return sourceList;
	}
	
	/**
	 * 填充用户姓名
	 */
	@SuppressWarnings("unchecked")
	public void fillNam4MapList(List<Map<String, Object>> sourceList ,String attrUsrID,String attrUsrNam) throws Exception {
		if(sourceList!=null&&sourceList.size()>0){
			List<String> IDs=new ArrayList<String>();
			for(Map<String, Object> smap:sourceList){
				if(StringUtils.isNotBlank((String)smap.get(attrUsrID))){
					IDs.add((String)smap.get(attrUsrID));
				}
			}
			String sqlStr=" SELECT distinct USRID,USRNAM FROM SYS_USR where USRID in (:usrIDs)  ";
			SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
			query.setParameterList("usrIDs", IDs);
			List<Map<String, Object>> resultList = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			Map<String,String> usrIDNamMap=new HashMap<String,String>();
			for(Map<String, Object> tmp:resultList){
				usrIDNamMap.put((String)tmp.get("USRID"), (String)tmp.get("USRNAM"));
			}
			for(Map<String, Object> smap:sourceList){
				if(StringUtils.isNotBlank((String)smap.get(attrUsrID))){
					smap.put(attrUsrNam, usrIDNamMap.get((String)smap.get(attrUsrID)));
				}
			}
		}
	}
	
}