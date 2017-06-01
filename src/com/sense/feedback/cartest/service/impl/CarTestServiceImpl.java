package com.sense.feedback.cartest.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sense.feedback.cartest.dao.CarTestDao;
import com.sense.feedback.cartest.service.CarTestService;
import com.sense.feedback.entity.PaiChan;
import com.sense.feedback.entity.ProcInst;
import com.sense.feedback.entity.ProcInstNode;
import com.sense.feedback.entity.QueJian;
import com.sense.feedback.enumdic.EnumProcNode;
import com.sense.feedback.enumdic.EnumYesNo;
import com.sense.frame.base.BaseService;
import com.sense.frame.pub.global.LoginInfo;
import com.sense.frame.pub.model.PageInfo;

@Service
public class CarTestServiceImpl extends BaseService implements CarTestService {
	
	@Autowired
	private CarTestDao carTestDao;

	@Override
	public PageInfo queryPCPage(PageInfo pageInfo, Map<String, String> paras) throws Exception {
		return carTestDao.queryPCPage(pageInfo, paras);
	}

	@Override
	public PaiChan queryPCDetail(String scdh) throws Exception {
		return carTestDao.queryPCDetail(scdh);
	}

	@Override
	public ProcInst queryProcInst(String scdh) throws Exception {
		return commonDao.findEntityByID(ProcInst.class, scdh);
	}

	@Override
	public void submitTC(ProcInst inst, LoginInfo loginInfo, String qjData) throws Exception {
		inst.setStatus(EnumProcNode.ts.getCode());
		inst.setJcsj(new Date());
		inst.setJcUsrID(loginInfo.getUserId());
		inst.setJcUsrNam(loginInfo.getUserNam());
		
		if(StringUtils.isNotBlank(qjData)){
			inst.setQjFlag(EnumYesNo.yes.getCode());
			JSONArray array = JSON.parseArray(qjData);
			for(int i = 0; i < array.size(); i ++){
				JSONObject obj = (JSONObject) array.get(i);
				QueJian qj = new QueJian();
				qj.setId(dBUtil.getCommonId());
				qj.setScdh(inst.getScdh());
				qj.setWlh(obj.getString("wlh"));
				qj.setQjs(obj.getInteger("qjs"));
				commonDao.saveEntity(qj);
			}
		}else{
			inst.setQjFlag(EnumYesNo.no.getCode());
		}
		commonDao.saveEntity(inst);
	}

	@Override
	public void saveXx(ProcInst inst, LoginInfo loginInfo, String qjData) throws Exception {
		inst.setStatus(EnumProcNode.jc.getCode());
		inst.setJcsj(new Date());
		inst.setJcUsrID(loginInfo.getUserId());
		inst.setJcUsrNam(loginInfo.getUserNam());
		
		if(StringUtils.isNotBlank(qjData)){
			inst.setQjFlag(EnumYesNo.yes.getCode());
			JSONArray array = JSON.parseArray(qjData);
			for(int i = 0; i < array.size(); i ++){
				JSONObject obj = (JSONObject) array.get(i);
				QueJian qj = new QueJian();
				qj.setId(dBUtil.getCommonId());
				qj.setScdh(inst.getScdh());
				qj.setWlh(obj.getString("wlh"));
				qj.setQjs(obj.getInteger("qjs"));
				commonDao.saveEntity(qj);
			}
		}else{
			inst.setQjFlag(EnumYesNo.no.getCode());
		}
		commonDao.saveEntity(inst);
	}

	@Override
	public PageInfo queryCTPage(PageInfo pageInfo, Map<String, String> paras) throws Exception {
		return carTestDao.queryCTPage(pageInfo, paras);
	}

	@Override
	public ProcInstNode queryProcInstNode(String scdh, String status) throws Exception {
		return carTestDao.queryProcInstNode(scdh, status);
	}

	@Override
	public String queryQJData(String scdh) throws Exception {
		List<QueJian> list = commonDao.findEntityList(QueJian.class, "scdh", scdh);
		PageInfo pi = new PageInfo();
		pi.setTotal("" + list.size());
		pi.setRows(list);
		return JSON.toJSONString(pi);
	}

	@Override
	public void saveCT(ProcInstNode inst, LoginInfo loginInfo) throws Exception {
		ProcInst proc = commonDao.findEntityByID(ProcInst.class, inst.getScdh());
				
		if(StringUtils.isBlank(inst.getId())){
			inst.setId(dBUtil.getCommonId());
		}
		inst.setProNode(proc.getStatus());
		inst.setTs(new Date());
		inst.setUsrID(loginInfo.getUserId());
		inst.setUsrNam(loginInfo.getUserNam());
		commonDao.saveOrUpdateEntity(inst);
	}

	/**
	 * 保存当前环节处理信息，跳转至下一环节
	 */
	@Override
	public void submitCT(ProcInstNode inst, LoginInfo loginInfo) throws Exception {
		ProcInst proc = commonDao.findEntityByID(ProcInst.class, inst.getScdh());
		Date nowDate = new Date();
		if(StringUtils.isBlank(inst.getId())){
			inst.setId(dBUtil.getCommonId());
		}
		inst.setProNode(proc.getStatus());
		inst.setSubmitTime(nowDate);
		inst.setTs(nowDate);
		inst.setUsrID(loginInfo.getUserId());
		inst.setUsrNam(loginInfo.getUserNam());
		commonDao.saveOrUpdateEntity(inst);
		
		proc.setStatus(String.valueOf(Integer.parseInt(proc.getStatus()) + 1));
		commonDao.updateEntity(proc);
	}
	
	@Override
	public String queryPreInstNodeList(String scdh, String status) throws Exception {
		List<ProcInstNode> nodeList = carTestDao.queryPreInstNodeList(scdh, status);
		if(CollectionUtils.isEmpty(nodeList)){
			return null;
		}
		PageInfo pi = new PageInfo();
		pi.setTotal("" + nodeList.size());
		pi.setRows(nodeList);
		return JSON.toJSONString(pi);
	}

	@Override
	public void submitUTC(ProcInst inst, LoginInfo loginInfo, String qjData) throws Exception {
		inst.setStatus(EnumProcNode.ts.getCode());
		inst.setJcsj(new Date());
		inst.setJcUsrID(loginInfo.getUserId());
		inst.setJcUsrNam(loginInfo.getUserNam());
		
		//先将缺件信息删除
		carTestDao.deleteQJ(inst.getScdh());
		if(StringUtils.isNotBlank(qjData)){
			inst.setQjFlag(EnumYesNo.yes.getCode());
			JSONArray array = JSON.parseArray(qjData);
			for(int i = 0; i < array.size(); i ++){
				JSONObject obj = (JSONObject) array.get(i);
				QueJian qj = new QueJian();
				qj.setId(dBUtil.getCommonId());
				qj.setScdh(inst.getScdh());
				qj.setWlh(obj.getString("wlh"));
				qj.setQjs(obj.getInteger("qjs"));
				commonDao.saveEntity(qj);
			}
		}else{
			inst.setQjFlag(EnumYesNo.no.getCode());
		}
		commonDao.updateEntity(inst);
	}

	@Override
	public void saveUXx(ProcInst inst, LoginInfo loginInfo, String qjData) throws Exception {
		inst.setStatus(EnumProcNode.jc.getCode());
		inst.setJcsj(new Date());
		inst.setJcUsrID(loginInfo.getUserId());
		inst.setJcUsrNam(loginInfo.getUserNam());
		
		//先将缺件信息删除
		carTestDao.deleteQJ(inst.getScdh());
		if(StringUtils.isNotBlank(qjData)){
			inst.setQjFlag(EnumYesNo.yes.getCode());
			JSONArray array = JSON.parseArray(qjData);
			for(int i = 0; i < array.size(); i ++){
				JSONObject obj = (JSONObject) array.get(i);
				QueJian qj = new QueJian();
				qj.setId(dBUtil.getCommonId());
				qj.setScdh(inst.getScdh());
				qj.setWlh(obj.getString("wlh"));
				qj.setQjs(obj.getInteger("qjs"));
				commonDao.saveEntity(qj);
			}
		}else{
			inst.setQjFlag(EnumYesNo.no.getCode());
		}
		commonDao.updateEntity(inst);
	}

	@Override
	public PageInfo queryRepoPage(PageInfo pageInfo, Map<String, String> paras) throws Exception {
		return carTestDao.queryRepoPage(pageInfo, paras);
	}

}