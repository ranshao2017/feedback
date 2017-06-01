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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sense.app.dao.AppProblemDao;
import com.sense.app.service.AppProblemService;
import com.sense.feedback.entity.Problem;
import com.sense.feedback.entity.ProblemReply;
import com.sense.feedback.enumdic.EnumProblemSta;
import com.sense.frame.base.BaseService;
import com.sense.frame.pub.model.PageInfo;
import com.sense.sys.dic.DicLoader;
import com.sense.sys.entity.Usr;

@Service
public class AppProblemServiceImpl extends BaseService implements AppProblemService {
	
	@Value("#{propertiesReader[FILE_PATH]}")
	private String filePath;
	@Autowired
	private AppProblemDao problemDao;
	@Autowired
	private DicLoader loader;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void publishProblem(String userid, String descr, String protype, List<MultipartFile> imgList) throws Exception {
		Problem problem = new Problem();
		problem.setId(dBUtil.getCommonId());
		problem.setCreateUsrID(userid);
		Usr usr = commonDao.findEntityByID(Usr.class, userid);
		problem.setCreateUsrNam(usr.getUsrNam());
		problem.setDescr(descr);
		problem.setProType(protype);
		problem.setCreateDate(new Date());
		problem.setStatus(EnumProblemSta.init.getCode());
		problem.setReplyCount(0);
		
		//处理图片
		if(CollectionUtils.isNotEmpty(imgList)){
			StringBuffer imgPathBuffer = new StringBuffer();
			for(MultipartFile file : imgList){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				String origName = file.getOriginalFilename();
				String imgPath = "problem/quality/" + sdf.format(new Date()) + origName.substring(origName.lastIndexOf("."));
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
			problem.setImgPath(imgPathBuffer.toString().substring(1));
		}
		
		commonDao.saveEntity(problem);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryProblemPage(PageInfo pi) throws Exception {
		pi = problemDao.queryProblemPage(pi);
		List<Problem> list = (List<Problem>) pi.getRows();
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		for(Problem problem : list){
			Map<String, Object> map = new HashMap<String, Object>();
			/*
			   	id：问题ID
				descr：问题描述
				createdate：问题创建时间 yyyy-MM-dd HH:mm:ss
				createusrnam:问题创建人
				status：问题状态 0初始状态 1已回复 2人工关闭 3自动关闭
				protype：问题类型名称
				replycount：回复次数
				imgpath：图片路径，多张图片用逗号分隔，图片展示参考【查看单张图片接口】
			 */
			map.put("id", problem.getId());
			map.put("descr", problem.getDescr());
			map.put("createdate", sdf.format(problem.getCreateDate()));
			map.put("createusrnam", problem.getCreateUsrNam());
			map.put("status", EnumProblemSta.getLabelByCode(problem.getStatus()));
			map.put("protype", loader.getCacheDicName("PROTYPE", problem.getProType()));
			map.put("replycount", problem.getReplyCount());
			map.put("imgpath", problem.getImgPath());
			returnList.add(map);
		}
		return returnList;
	}

	@Override
	public Map<String, Object> queryProblem(String problemid) throws Exception {
		Problem problem = commonDao.findEntityByID(Problem.class, problemid);
		Map<String, Object> map = new HashMap<String, Object>();
		/*
		   	id：问题ID
			descr：问题描述
			createdate：问题创建时间 yyyy-MM-dd HH:mm:ss
			createusrnam:问题创建人
			status：问题状态 0初始状态 1已回复 2人工关闭 3自动关闭
			protype：问题类型名称
			replycount：回复次数
			imgpath：图片路径，多张图片用逗号分隔，图片展示参考【查看单张图片接口】
		 */
		map.put("id", problem.getId());
		map.put("descr", problem.getDescr());
		map.put("createdate", sdf.format(problem.getCreateDate()));
		map.put("createusrnam", problem.getCreateUsrNam());
		map.put("status", EnumProblemSta.getLabelByCode(problem.getStatus()));
		map.put("protype", loader.getCacheDicName("PROTYPE", problem.getProType()));
		map.put("replycount", problem.getReplyCount());
		map.put("imgpath", problem.getImgPath());
		return map;
	}

	@Override
	public List<Map<String, Object>> queryProblemReply(String problemid) throws Exception {
		List<ProblemReply> replyList = problemDao.queryProblemReply(problemid);
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		for(ProblemReply reply : replyList){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("descr", reply.getDescr());
			map.put("createdate", sdf.format(reply.getCreateDate()));
			map.put("createusrnam", reply.getCreateUsrNam());
			returnList.add(map);
		}
		return returnList;
	}

	@Override
	public void replyProblem(String userid, String descr, String problemid) throws Exception {
		ProblemReply reply = new ProblemReply();
		reply.setId(dBUtil.getCommonId());
		reply.setDescr(descr);
		reply.setCreateDate(new Date());
		reply.setCreateUsrID(userid);
		Usr usr = commonDao.findEntityByID(Usr.class, userid);
		reply.setCreateUsrNam(usr.getUsrNam());
		reply.setProblemID(problemid);
		commonDao.saveEntity(reply);
		
		Problem problem = commonDao.findEntityByID(Problem.class, problemid);
		problem.setStatus(EnumProblemSta.replyed.getCode());
		problem.setReplyCount(problem.getReplyCount() + 1);
		commonDao.updateEntity(problem);
	}

}