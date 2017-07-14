package com.sense.app.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sense.app.dao.AppCTDao;
import com.sense.app.dto.ProcInstDto;
import com.sense.app.dto.ProcInstNodeDto;
import com.sense.app.service.AppCTService;
import com.sense.feedback.cartest.dao.CarTestDao;
import com.sense.feedback.entity.ProcInst;
import com.sense.feedback.entity.ProcInstNode;
import com.sense.feedback.entity.QueJian;
import com.sense.feedback.enumdic.EnumProcNode;
import com.sense.feedback.enumdic.EnumProcesSta;
import com.sense.feedback.statist.dao.ComplexDao;
import com.sense.frame.base.BaseService;
import com.sense.frame.pub.model.PageInfo;
import com.sense.sys.dic.DicLoader;
import com.sense.sys.entity.Org;
import com.sense.sys.entity.Usr;

@Service
public class AppCTServiceImpl extends BaseService implements AppCTService {
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Value("#{propertiesReader[FILE_PATH]}")
	private String filePath;
	@Autowired
	private AppCTDao appCtDao;
	@Autowired
	private CarTestDao carTestDao;
	@Autowired
	private DicLoader dicLoader;
	@Autowired
	private ComplexDao complexDao;

	@Override
	public ProcInstDto queryProcInst(String dph, String userid) throws Exception {
		if(StringUtils.isBlank(dph)){
			return null;
		}
		ProcInst procInst = appCtDao.queryProcInst(dph);
		if(null == procInst){
			return null;
		}
		
		if(StringUtils.isNotBlank(userid)){
			List<String> nodeList = appCtDao.queryOwnProcNode(userid);
			if(CollectionUtils.isEmpty(nodeList)){
				return null;
			}else{
				if(!nodeList.contains(procInst.getStatus())){
					return null;
				}
			}
		}
		
		ProcInstDto dto = new  ProcInstDto();
		dto.setBz(procInst.getBz());
		dto.setCx(procInst.getCx());
		dto.setDdh(procInst.getDdh());
		dto.setDescr(procInst.getDescr());
		dto.setDph(procInst.getDph());
		dto.setFdj(procInst.getFdj());
		if(null != procInst.getJcsj()){
			dto.setJcsj(sdf.format(procInst.getJcsj()));
		}
		dto.setJcusrid(procInst.getJcUsrID());
		dto.setJcusrnam(procInst.getJcUsrNam());
		dto.setPz(procInst.getPz());
		dto.setQjflag(procInst.getQjFlag());
		dto.setScdh(procInst.getScdh());
		dto.setStatus(procInst.getStatus());
		if(null != procInst.getXxsj()){
			dto.setXxsj(sdf.format(procInst.getXxsj()));
		}
		if(StringUtils.isNotBlank(procInst.getXzOrgID())){
			String[] orgArr = procInst.getXzOrgID().split(",");
			StringBuffer orgSB = new StringBuffer();
			for (int i = 0; i < orgArr.length; i++) {
				Org xzorg = commonDao.findEntityByID(Org.class, orgArr[i]);
				if(i > 0){
					orgSB.append(",");
				}
				orgSB.append(xzorg.getOrgNam());
			}
			dto.setXzOrg(orgSB.toString());
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
			if(null != node.getCreateTime()){
				dto.setCreatetime(sdf.format(node.getCreateTime()));
			}
			dto.setUsrid(node.getUsrID());
			dto.setUsrnam(node.getUsrNam());
			dto.setImgpath(node.getImgPath());
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Override
	public void saveCT(String scdh, String carseat, String descr, String userid, List<MultipartFile> imgList) throws Exception {
		ProcInst proc = commonDao.findEntityByID(ProcInst.class, scdh);
		ProcInstNode node = carTestDao.queryCurrenttNode(scdh, proc.getStatus());
		if(null == node){
			node = new ProcInstNode();
			node.setId(dBUtil.getCommonId());
			node.setScdh(scdh);
			node.setProNode(proc.getStatus());
			if(EnumProcNode.ts.getCode().equals(proc.getStatus())){//调试保存，将接车时间保存为该环节开始时间
				node.setCreateTime(proc.getJcsj());
			}else{
				ProcInstNode preNode = carTestDao.queryPreInstNode(scdh, proc.getStatus());
				node.setCreateTime(preNode.getSubmitTime());
			}
		}
		node.setCarSet(carseat);
		node.setDescr(descr);
		node.setTs(new Date());
		node.setUsrID(userid);
		Usr usr = commonDao.findEntityByID(Usr.class, userid);
		node.setUsrNam(usr.getUsrNam());
		
		buildImg(node, imgList);
		
		commonDao.saveOrUpdateEntity(node);//处理图片
		
		proc.setProcesSta(EnumProcesSta.clbc.getCode());
		commonDao.updateEntity(proc);
	}

	@Override
	public void submitCT(String scdh, String carseat, String descr, String userid, List<MultipartFile> imgList) throws Exception {
		ProcInst proc = commonDao.findEntityByID(ProcInst.class, scdh);
		ProcInstNode node = carTestDao.queryCurrenttNode(scdh, proc.getStatus());
		if(null == node){
			node = new ProcInstNode();
			node.setId(dBUtil.getCommonId());
			node.setScdh(scdh);
			node.setProNode(proc.getStatus());
			if(EnumProcNode.ts.getCode().equals(proc.getStatus())){//调试保存，将接车时间保存为该环节开始时间
				node.setCreateTime(proc.getJcsj());
			}else{
				ProcInstNode preNode = carTestDao.queryPreInstNode(scdh, proc.getStatus());
				node.setCreateTime(preNode.getSubmitTime());
			}
		}
		node.setCarSet(carseat);
		node.setDescr(descr);
		Date nowDate = new Date();
		node.setSubmitTime(nowDate);
		node.setTs(nowDate);
		node.setUsrID(userid);
		Usr usr = commonDao.findEntityByID(Usr.class, userid);
		node.setUsrNam(usr.getUsrNam());
		
		buildImg(node, imgList);//处理图片
				
		commonDao.saveOrUpdateEntity(node);
		
		if(EnumProcNode.sy.getCode().equals(proc.getStatus())){//记录入库时间
			proc.setRksj(nowDate);
		}
		
		proc.setStatus(String.valueOf(Integer.parseInt(proc.getStatus()) + 1));
		proc.setProcesSta(EnumProcesSta.xtj.getCode());
		commonDao.updateEntity(proc);
	}

	@Override
	public void unQualiyCT(String scdh, String carseat, String descr,
			String userid, String processta, List<MultipartFile> imgList) throws Exception {
		ProcInst proc = commonDao.findEntityByID(ProcInst.class, scdh);
		ProcInstNode node = carTestDao.queryCurrenttNode(scdh, proc.getStatus());
		if(null == node){
			node = new ProcInstNode();
			node.setId(dBUtil.getCommonId());
			node.setScdh(scdh);
			node.setProNode(proc.getStatus());
			ProcInstNode preNode = carTestDao.queryPreInstNode(scdh, proc.getStatus());
			node.setCreateTime(preNode.getSubmitTime());
		}
		node.setCarSet(carseat);
		node.setDescr(descr);
		Date nowDate = new Date();
		if(EnumProcesSta.fjbhg.getCode().equals(processta)){
			proc.setProcesSta(processta);
		}else if(EnumProcesSta.th.getCode().equals(processta)){
			node.setSubmitTime(nowDate);
			proc.setStatus(String.valueOf(Integer.parseInt(proc.getStatus()) - 1));
			proc.setProcesSta(processta);
		}
		node.setTs(nowDate);
		node.setUsrID(userid);
		Usr usr = commonDao.findEntityByID(Usr.class, userid);
		node.setUsrNam(usr.getUsrNam());
		buildImg(node, imgList);//处理图片
		commonDao.saveOrUpdateEntity(node);
		
		commonDao.updateEntity(proc);
	}

	private void buildImg(ProcInstNode node, List<MultipartFile> imgList) throws Exception {
		//处理图片
		if(CollectionUtils.isNotEmpty(imgList)){
			StringBuffer imgPathBuffer = new StringBuffer();
			for(MultipartFile file : imgList){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				String origName = file.getOriginalFilename();
				String imgPath = "appct/" + sdf.format(new Date()) + origName.substring(origName.lastIndexOf("."));
				File aimFile = new File(filePath + imgPath);
				if(!aimFile.getParentFile().exists()){
					aimFile.getParentFile().mkdirs();
				}
				OutputStream os = new FileOutputStream(aimFile);
				InputStream is = file.getInputStream();
				IOUtils.copy(is, os);
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(os);
				imgPathBuffer.append(",").append(imgPath);
			}
			node.setImgPath(imgPathBuffer.toString().substring(1));
		}
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

	@SuppressWarnings("unchecked")
	@Override
	public List<ProcInstDto> queryCTPage(PageInfo pi, String dph, String ddh, String cx, String userid) throws Exception {
		Map<String, String> paras = new HashMap<String, String>();
		paras.put("dph", dph);
		paras.put("ddh", ddh);
		paras.put("cx", cx);
		List<String> nodeIDs = null;
		if(StringUtils.isNotBlank(userid)){
			nodeIDs = appCtDao.queryOwnProcNode(userid);
		}
		pi = complexDao.queryCarTPage(pi, paras, nodeIDs);
		List<ProcInst> instList = (List<ProcInst>) pi.getRows();
		List<ProcInstDto> returnList = new ArrayList<ProcInstDto>();
		for(ProcInst procInst : instList){
			ProcInstDto dto = new  ProcInstDto();
			dto.setBz(procInst.getBz());
			dto.setCx(procInst.getCx());
			dto.setDdh(procInst.getDdh());
			dto.setDescr(procInst.getDescr());
			dto.setDph(procInst.getDph());
			dto.setFdj(procInst.getFdj());
			if(null != procInst.getJcsj()){
				dto.setJcsj(sdf.format(procInst.getJcsj()));
			}
			dto.setJcusrid(procInst.getJcUsrID());
			dto.setJcusrnam(procInst.getJcUsrNam());
			dto.setPz(procInst.getPz());
			dto.setQjflag(procInst.getQjFlag());
			dto.setScdh(procInst.getScdh());
			dto.setStatus(procInst.getStatus());
			if(null != procInst.getXxsj()){
				dto.setXxsj(sdf.format(procInst.getXxsj()));
			}
			returnList.add(dto);
		}
		return returnList;
	}

	@Override
	public Integer queryCTTotal(String dph, String ddh, String cx, String userid) throws Exception {
		List<String> nodeIDs = null;
		if(StringUtils.isNotBlank(userid)){
			nodeIDs = appCtDao.queryOwnProcNode(userid);
		}
		return appCtDao.queryCTTotal(dph, ddh, cx, nodeIDs);
	}
}