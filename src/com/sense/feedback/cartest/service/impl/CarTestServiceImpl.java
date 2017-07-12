package com.sense.feedback.cartest.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
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
import com.sense.feedback.entity.ProcInstReply;
import com.sense.feedback.entity.QueJian;
import com.sense.feedback.enumdic.EnumProcNode;
import com.sense.feedback.enumdic.EnumProcesSta;
import com.sense.feedback.enumdic.EnumYesNo;
import com.sense.frame.base.BaseService;
import com.sense.frame.base.BusinessException;
import com.sense.frame.pub.global.LoginInfo;
import com.sense.frame.pub.model.PageInfo;
import com.sense.sys.entity.Org;

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
		inst.setTsStatus("1");//可调车
		
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
		
		if(StringUtils.isNotBlank(inst.getXzOrgID())){
			inst.setCloseFlag(EnumYesNo.no.getCode());//协助部门讨论开关
			inst.setReplyCount(0);
		}
		commonDao.saveOrUpdateEntity(inst);
	}
	
	@Override
	public void saveTC(ProcInst inst, LoginInfo loginInfo, String qjData) throws Exception {
		inst.setStatus(EnumProcNode.ts.getCode());
		inst.setJcsj(new Date());
		inst.setJcUsrID(loginInfo.getUserId());
		inst.setJcUsrNam(loginInfo.getUserNam());
		inst.setTsStatus("2");//不可调车
		
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
		
		if(StringUtils.isNotBlank(inst.getXzOrgID())){
			inst.setCloseFlag(EnumYesNo.no.getCode());//协助部门讨论开关
			inst.setReplyCount(0);
		}
		commonDao.saveOrUpdateEntity(inst);
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
		
		if(StringUtils.isNotBlank(inst.getXzOrgID())){
			inst.setCloseFlag(EnumYesNo.no.getCode());//协助部门讨论开关
			inst.setReplyCount(0);
		}
		
		commonDao.saveOrUpdateEntity(inst);
	}

	@Override
	public PageInfo queryCTPage(PageInfo pageInfo, Map<String, String> paras) throws Exception {
		return carTestDao.queryCTPage(pageInfo, paras);
	}

	@Override
	public ProcInstNode queryProcInstNode(String scdh, String status) throws Exception {
		return carTestDao.queryCurrenttNode(scdh, status);
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
	public void saveCT(ProcInstNode instNode, LoginInfo loginInfo) throws Exception {
		ProcInst proc = commonDao.findEntityByID(ProcInst.class, instNode.getScdh());
				
		if(StringUtils.isBlank(instNode.getId())){
			instNode.setId(dBUtil.getCommonId());
		}
		instNode.setProNode(proc.getStatus());
		if(EnumProcNode.ts.getCode().equals(proc.getStatus())){//调试保存，将接车时间保存为该环节开始时间
			instNode.setCreateTime(proc.getJcsj());
		}else{
			ProcInstNode preNode = carTestDao.queryPreInstNode(instNode.getScdh(), proc.getStatus());
			instNode.setCreateTime(preNode.getSubmitTime());
		}
		instNode.setTs(new Date());
		instNode.setUsrID(loginInfo.getUserId());
		instNode.setUsrNam(loginInfo.getUserNam());
		commonDao.saveOrUpdateEntity(instNode);
		
		proc.setProcesSta(EnumProcesSta.clbc.getCode());
		commonDao.updateEntity(proc);
	}

	/**
	 * 保存当前环节处理信息，跳转至下一环节
	 */
	@Override
	public void submitCT(ProcInstNode node, LoginInfo loginInfo) throws Exception {
		ProcInst proc = commonDao.findEntityByID(ProcInst.class, node.getScdh());
		Date nowDate = new Date();
		if(StringUtils.isBlank(node.getId())){
			node.setId(dBUtil.getCommonId());
		}
		node.setProNode(proc.getStatus());
		if(EnumProcNode.ts.getCode().equals(proc.getStatus())){//调试保存，将接车时间保存为该环节开始时间
			node.setCreateTime(proc.getJcsj());
		}else{
			ProcInstNode preNode = carTestDao.queryPreInstNode(node.getScdh(), proc.getStatus());
			node.setCreateTime(preNode.getSubmitTime());
		}
		node.setSubmitTime(nowDate);
		node.setTs(nowDate);
		node.setUsrID(loginInfo.getUserId());
		node.setUsrNam(loginInfo.getUserNam());
		commonDao.saveOrUpdateEntity(node);
		
		if(EnumProcNode.sy.getCode().equals(proc.getStatus())){//记录入库时间
			proc.setRksj(nowDate);
		}
		proc.setStatus(String.valueOf(Integer.parseInt(proc.getStatus()) + 1));
		proc.setProcesSta(EnumProcesSta.xtj.getCode());
		commonDao.updateEntity(proc);
	}
	
	@Override
	public String queryPreInstNodeList(String scdh, String status) throws Exception {
		List<ProcInstNode> nodeList = carTestDao.queryPreInstNodeList(scdh);
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

	@Override
	public void submitTCs(ProcInst inst, LoginInfo loginInfo, String scdhs) throws Exception {
		String[] scdhArr = scdhs.split(",");
		for(int i = 0; i < scdhArr.length; i ++){
			ProcInst dbInst = new ProcInst();
			PaiChan pc = carTestDao.queryPCDetail(scdhArr[i]);
			BeanUtils.copyProperties(dbInst, pc);
			dbInst.setXxsj(pc.getXxsj());
			dbInst.setDescr(inst.getDescr());
			dbInst.setBz(inst.getBz());
			dbInst.setXzOrgID(inst.getXzOrgID());
			
			dbInst.setStatus(EnumProcNode.ts.getCode());
			dbInst.setJcsj(new Date());
			dbInst.setJcUsrID(loginInfo.getUserId());
			dbInst.setJcUsrNam(loginInfo.getUserNam());
			dbInst.setQjFlag(EnumYesNo.no.getCode());
			
			if(StringUtils.isNotBlank(inst.getXzOrgID())){
				dbInst.setCloseFlag(EnumYesNo.no.getCode());
				inst.setReplyCount(0);
			}
			
			commonDao.saveEntity(dbInst);
		}
		
	}

	@Override
	public void submitCTs(ProcInstNode node, LoginInfo loginInfo, String scdhs) throws Exception {
		String[] scdhArr = scdhs.split(",");
		for(int i = 0; i < scdhArr.length; i ++){
			ProcInst proc = commonDao.findEntityByID(ProcInst.class, scdhArr[i]);
			Date nowDate = new Date();
			ProcInstNode dbNode = carTestDao.queryCurrenttNode(scdhArr[i], proc.getStatus());
			if(null == dbNode){
				dbNode = new ProcInstNode();
				dbNode.setId(dBUtil.getCommonId());
				dbNode.setScdh(scdhArr[i]);
				dbNode.setProNode(proc.getStatus());
				if(EnumProcNode.ts.getCode().equals(proc.getStatus())){//调试保存，将接车时间保存为该环节开始时间
					dbNode.setCreateTime(proc.getJcsj());
				}else{
					ProcInstNode preNode = carTestDao.queryPreInstNode(scdhArr[i], proc.getStatus());
					dbNode.setCreateTime(preNode.getSubmitTime());
				}
			}
			
			dbNode.setSubmitTime(nowDate);
			dbNode.setTs(nowDate);
			dbNode.setUsrID(loginInfo.getUserId());
			dbNode.setUsrNam(loginInfo.getUserNam());
			dbNode.setCarSet(node.getCarSet());
			dbNode.setDescr(node.getDescr());
			commonDao.saveOrUpdateEntity(dbNode);
			
			if(EnumProcNode.sy.getCode().equals(proc.getStatus())){//记录入库时间
				proc.setRksj(nowDate);
			}
			proc.setStatus(String.valueOf(Integer.parseInt(proc.getStatus()) + 1));
			proc.setProcesSta(EnumProcesSta.xtj.getCode());
			commonDao.updateEntity(proc);
		}
	}

	@Override
	public void backCT(ProcInstNode node, LoginInfo loginInfo) throws Exception {
		ProcInst proc = commonDao.findEntityByID(ProcInst.class, node.getScdh());
		Date nowDate = new Date();
		if(StringUtils.isBlank(node.getId())){
			node.setId(dBUtil.getCommonId());
		}
		node.setProNode(proc.getStatus());
		if(EnumProcNode.ts.getCode().equals(proc.getStatus())){//调试保存，将接车时间保存为该环节开始时间
			node.setCreateTime(proc.getJcsj());
		}else{
			ProcInstNode preNode = carTestDao.queryPreInstNode(node.getScdh(), proc.getStatus());
			node.setCreateTime(preNode.getSubmitTime());
		}
		node.setSubmitTime(nowDate);
		node.setTs(nowDate);
		node.setUsrID(loginInfo.getUserId());
		node.setUsrNam(loginInfo.getUserNam());
		commonDao.saveOrUpdateEntity(node);
		
		proc.setStatus(String.valueOf(Integer.parseInt(proc.getStatus()) - 1));
		proc.setProcesSta(EnumProcesSta.th.getCode());
		commonDao.updateEntity(proc);
	}

	@Override
	public void backCTs(ProcInstNode node, LoginInfo loginInfo, String scdhs) throws Exception {
		String[] scdhArr = scdhs.split(",");
		for(int i = 0; i < scdhArr.length; i ++){
			ProcInst proc = commonDao.findEntityByID(ProcInst.class, scdhArr[i]);
			Date nowDate = new Date();
			ProcInstNode dbNode = carTestDao.queryCurrenttNode(scdhArr[i], proc.getStatus());
			if(null == dbNode){
				dbNode = new ProcInstNode();
				dbNode.setScdh(scdhArr[i]);
				dbNode.setId(dBUtil.getCommonId());
				dbNode.setProNode(proc.getStatus());
				if(EnumProcNode.ts.getCode().equals(proc.getStatus())){//调试保存，将接车时间保存为该环节开始时间
					dbNode.setCreateTime(proc.getJcsj());
				}else{
					ProcInstNode preNode = carTestDao.queryPreInstNode(scdhArr[i], proc.getStatus());
					dbNode.setCreateTime(preNode.getSubmitTime());
				}
			}
			dbNode.setSubmitTime(nowDate);
			dbNode.setTs(nowDate);
			dbNode.setUsrID(loginInfo.getUserId());
			dbNode.setUsrNam(loginInfo.getUserNam());
			dbNode.setCarSet(node.getCarSet());
			dbNode.setDescr(node.getDescr());
			commonDao.saveOrUpdateEntity(dbNode);
			
			proc.setStatus(String.valueOf(Integer.parseInt(proc.getStatus()) - 1));
			proc.setProcesSta(EnumProcesSta.th.getCode());
			commonDao.updateEntity(proc);
		}
	}

	@Override
	public void unQuailyCT(ProcInstNode node, LoginInfo loginInfo) throws Exception {
		ProcInst proc = commonDao.findEntityByID(ProcInst.class, node.getScdh());
		Date nowDate = new Date();
		if(StringUtils.isBlank(node.getId())){
			node.setId(dBUtil.getCommonId());
		}
		node.setProNode(proc.getStatus());
		if(EnumProcNode.ts.getCode().equals(proc.getStatus())){//调试保存，将接车时间保存为该环节开始时间
			node.setCreateTime(proc.getJcsj());
		}else{
			ProcInstNode preNode = carTestDao.queryPreInstNode(node.getScdh(), proc.getStatus());
			node.setCreateTime(preNode.getSubmitTime());
		}
		node.setSubmitTime(nowDate);
		node.setTs(nowDate);
		node.setUsrID(loginInfo.getUserId());
		node.setUsrNam(loginInfo.getUserNam());
		commonDao.saveOrUpdateEntity(node);
		
		proc.setProcesSta(EnumProcesSta.fjbhg.getCode());
		commonDao.updateEntity(proc);
	}

	@Override
	public void unQuailyCTs(ProcInstNode node, LoginInfo loginInfo, String scdhs) throws Exception {
		String[] scdhArr = scdhs.split(",");
		for(int i = 0; i < scdhArr.length; i ++){
			ProcInst proc = commonDao.findEntityByID(ProcInst.class, scdhArr[i]);
			Date nowDate = new Date();
			ProcInstNode dbNode = carTestDao.queryCurrenttNode(scdhArr[i], proc.getStatus());
			if(null == dbNode){
				dbNode = new ProcInstNode();
				dbNode.setScdh(scdhArr[i]);
				dbNode.setId(dBUtil.getCommonId());
				dbNode.setProNode(proc.getStatus());
				if(EnumProcNode.ts.getCode().equals(proc.getStatus())){//调试保存，将接车时间保存为该环节开始时间
					dbNode.setCreateTime(proc.getJcsj());
				}else{
					ProcInstNode preNode = carTestDao.queryPreInstNode(scdhArr[i], proc.getStatus());
					dbNode.setCreateTime(preNode.getSubmitTime());
				}
			}
			dbNode.setSubmitTime(nowDate);
			dbNode.setTs(nowDate);
			dbNode.setUsrID(loginInfo.getUserId());
			dbNode.setUsrNam(loginInfo.getUserNam());
			dbNode.setCarSet(node.getCarSet());
			dbNode.setDescr(node.getDescr());
			commonDao.saveOrUpdateEntity(dbNode);
			
			proc.setProcesSta(EnumProcesSta.fjbhg.getCode());
			commonDao.updateEntity(proc);
		}
	}

	@Override
	public Map<String, Object> queryCTCount(String status) throws Exception {
		if(StringUtils.isBlank(status)){
			throw new BusinessException("参数不合法");
		}
		Integer ctCount = carTestDao.queryCtCount(status);
		Integer lngCount = carTestDao.queryLngCount(status);
		Integer cngCount = carTestDao.queryCngCount(status);
		Map<String, Object> paras = new HashMap<String, Object>();
		if(EnumProcNode.ts.getCode().equals(status)){//调试
			Integer ktCount = carTestDao.queryKtCount(status);
			Integer bktCount = carTestDao.queryBktCount(status);
			paras.put("ktCount", ktCount);
			paras.put("bktCount", bktCount);
		}
		
		paras.put("ctCount", ctCount);
		paras.put("lngCount", lngCount);
		paras.put("cngCount", cngCount);
		return paras;
	}

	@Override
	public PageInfo queryXZCTPage(PageInfo pageInfo, Map<String, String> paras) throws Exception {
		return carTestDao.queryXZCTPage(pageInfo, paras);
	}

	@Override
	public String queryReplyList(String scdh) throws Exception {
		List<ProcInstReply> list = carTestDao.queryReplyList(scdh);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		PageInfo pi = new PageInfo();
		pi.setTotal("" + list.size());
		pi.setRows(list);
		return JSON.toJSONString(pi);
	}

	@Override
	public void submitReply(String scdh, String descr, LoginInfo loginInfo) throws Exception {
		ProcInstReply reply = new ProcInstReply();
		reply.setCreateTime(new Date());
		reply.setDescr(descr);
		reply.setScdh(scdh);
		reply.setXzID(dBUtil.getCommonId());
		reply.setXzOrgID(loginInfo.getOrgId());
		reply.setXzOrgNam(loginInfo.getOrgNam());
		reply.setXzUsrID(loginInfo.getUserId());
		reply.setXzUsrNam(loginInfo.getUserNam());
		commonDao.saveEntity(reply);
		
		ProcInst inst = commonDao.findEntityByID(ProcInst.class, scdh);
		int oldCount = inst.getReplyCount() == null ? 0 : inst.getReplyCount();
		inst.setReplyCount(oldCount + 1);
		commonDao.updateEntity(inst);
	}

	@Override
	public boolean queryTsFlag(String orgId) throws Exception {
		Org org = commonDao.findEntityByID(Org.class, orgId);
		if(StringUtils.isNotBlank(org.getProcs()) && org.getProcs().indexOf(EnumProcNode.ts.getCode()) >= 0){
			return true;
		}
		return false;
	}

	@Override
	public void closeReply(String scdh) throws Exception {
		ProcInst inst = commonDao.findEntityByID(ProcInst.class, scdh);
		inst.setCloseFlag(EnumYesNo.yes.getCode());
		commonDao.updateEntity(inst);
	}

	@Override
	public List<ProcInst> queryExportCT(Map<String, String> paraMap) throws Exception {
		return carTestDao.queryExportCT(paraMap);
	}

	@Override
	public PaiChan queryPCProcinst(String scdh) throws Exception {
		ProcInst inst = commonDao.findEntityByID(ProcInst.class, scdh);
		if(null != inst){
			PaiChan pc = new PaiChan();
			pc.setCx(inst.getCx());
			pc.setDdh(inst.getDdh());
			pc.setDph(inst.getDph());
			pc.setFdj(inst.getFdj());
			pc.setPz(inst.getPz());
			pc.setScdh(inst.getScdh());
			pc.setXxsj(inst.getXxsj());
			pc.setXzOrgID(inst.getXzOrgID());
			pc.setBz(inst.getBz());
			pc.setDescr(inst.getDescr());
			return pc;
		}
		return null;
	}

}