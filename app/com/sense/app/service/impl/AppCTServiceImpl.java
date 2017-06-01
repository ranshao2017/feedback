package com.sense.app.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sense.app.dao.AppCTDao;
import com.sense.app.dto.ProcInstDto;
import com.sense.app.dto.ProcInstNodeDto;
import com.sense.app.service.AppCTService;
import com.sense.feedback.entity.ProcInst;
import com.sense.feedback.entity.ProcInstNode;
import com.sense.feedback.entity.QueJian;
import com.sense.feedback.enumdic.EnumProcNode;
import com.sense.frame.base.BaseService;
import com.sense.sys.dic.DicLoader;
import com.sense.sys.entity.Org;
import com.sense.sys.entity.Usr;

@Service
public class AppCTServiceImpl extends BaseService implements AppCTService {
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private AppCTDao appCtDao;
	@Autowired
	private DicLoader dicLoader;

	@Override
	public ProcInstDto queryProcInst(String dph, String userid) throws Exception {
		Usr usr = commonDao.findEntityByID(Usr.class, userid);
		Org org = commonDao.findEntityByID(Org.class, usr.getOrgID());
		List<ProcInst> list = commonDao.findEntityList(ProcInst.class, "dph", dph);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		ProcInst procInst = list.get(0);
		if(!procInst.getStatus().equals(org.getProcs())){
			return null;
		}
		ProcInstDto dto = new  ProcInstDto();
		dto.setBz(procInst.getBz());
		dto.setCx(procInst.getCx());
		dto.setDdh(procInst.getDdh());
		dto.setDescr(procInst.getDescr());
		dto.setDph(procInst.getDph());
		dto.setFdj(procInst.getFdj());
		dto.setJcsj(sdf.format(procInst.getJcsj()));
		dto.setJcusrid(procInst.getJcUsrID());
		dto.setJcusrnam(procInst.getJcUsrNam());
		dto.setPz(procInst.getPz());
		dto.setQjflag(procInst.getQjFlag());
		dto.setScdh(procInst.getScdh());
		dto.setStatus(procInst.getStatus());
		dto.setXxsj(sdf.format(procInst.getXxsj()));
		if(StringUtils.isNotBlank(procInst.getXzOrgID())){
			Org xzorg = commonDao.findEntityByID(Org.class, procInst.getXzOrgID());
			dto.setXzOrg(xzorg.getOrgNam());
		}else{
			dto.setXzOrg("");
		}
		return dto;
	}

	@Override
	public List<ProcInstNodeDto> queryNodeList(String scdh) throws Exception {
		List<ProcInstNodeDto> dtoList = new ArrayList<ProcInstNodeDto>();
		List<ProcInstNode> nodeList = appCtDao.queryNodeList(scdh);
		if(CollectionUtils.isEmpty(nodeList)){
			return dtoList;
		}
		for(ProcInstNode node : nodeList){
			ProcInstNodeDto dto = new ProcInstNodeDto();
			dto.setCarset(dicLoader.getCacheDicName("CARSEAT", node.getCarSet()));
			dto.setDescr(node.getDescr());
			dto.setId(node.getId());
			dto.setPronode(EnumProcNode.getLabelByCode(node.getProNode()));
			dto.setScdh(node.getScdh());
			if(null != node.getSubmitTime()){
				dto.setSubmittime(sdf.format(node.getSubmitTime()));
			}
			if(null != node.getTs()){
				dto.setTs(sdf.format(node.getTs()));
			}
			dto.setUsrid(node.getUsrID());
			dto.setUsrnam(node.getUsrNam());
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Override
	public void saveCT(String scdh, String carseat, String descr, String userid) throws Exception {
		ProcInst proc = commonDao.findEntityByID(ProcInst.class, scdh);
		
		ProcInstNode inst = appCtDao.queryNode(scdh, proc.getStatus());
		if(null == inst){
			inst = new ProcInstNode();
			inst.setId(dBUtil.getCommonId());
		}
		inst.setScdh(scdh);
		inst.setCarSet(carseat);
		inst.setDescr(descr);
		inst.setProNode(proc.getStatus());
		inst.setTs(new Date());
		inst.setUsrID(userid);
		Usr usr = commonDao.findEntityByID(Usr.class, userid);
		inst.setUsrNam(usr.getUsrNam());
		commonDao.saveOrUpdateEntity(inst);
	}

	@Override
	public void submitCT(String scdh, String carseat, String descr, String userid) throws Exception {
		ProcInst proc = commonDao.findEntityByID(ProcInst.class, scdh);
		
		ProcInstNode inst = appCtDao.queryNode(scdh, proc.getStatus());
		if(null == inst){
			inst = new ProcInstNode();
			inst.setId(dBUtil.getCommonId());
		}
		inst.setScdh(scdh);
		inst.setCarSet(carseat);
		inst.setDescr(descr);
		Date nowDate = new Date();
		inst.setProNode(proc.getStatus());
		inst.setSubmitTime(nowDate);
		inst.setTs(nowDate);
		inst.setUsrID(userid);
		Usr usr = commonDao.findEntityByID(Usr.class, userid);
		inst.setUsrNam(usr.getUsrNam());
		commonDao.saveOrUpdateEntity(inst);
		
		proc.setStatus(String.valueOf(Integer.parseInt(proc.getStatus()) + 1));
		commonDao.updateEntity(proc);
	}

	@Override
	public List<Map<String, Object>> queryQJ(String scdh) throws Exception {
		List<QueJian> list = commonDao.findEntityList(QueJian.class, "scdh", scdh);
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		for(QueJian qj : list){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("scdh", qj.getScdh());
			map.put("wlh", qj.getWlh());
			map.put("qjs", qj.getQjs());
			returnList.add(map);
		}
		return returnList;
	}

}