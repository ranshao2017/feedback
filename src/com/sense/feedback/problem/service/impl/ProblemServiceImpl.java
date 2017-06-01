package com.sense.feedback.problem.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sense.feedback.entity.Problem;
import com.sense.feedback.entity.ProblemReply;
import com.sense.feedback.enumdic.EnumProblemSta;
import com.sense.feedback.problem.dao.ProblemDao;
import com.sense.feedback.problem.service.ProblemService;
import com.sense.frame.base.BaseService;
import com.sense.frame.pub.global.LoginInfo;
import com.sense.frame.pub.model.PageInfo;

@Service
public class ProblemServiceImpl extends BaseService implements ProblemService {
	
	@Autowired
	private ProblemDao problemDao;

	/**
	 * 发布质量问题
	 */
	@Override
	public void addProblem(Problem problem, LoginInfo loginInfo) throws Exception {
		//保存质量问题
		String promblemID = dBUtil.getCommonId();
		problem.setId(promblemID);
		problem.setCreateDate(new Date());
		problem.setCreateUsrID(loginInfo.getUserId());
		problem.setCreateUsrNam(loginInfo.getUserNam());
		problem.setStatus(EnumProblemSta.init.getCode());
		problem.setReplyCount(0);
		commonDao.saveEntity(problem);
	}

	@Override
	public Problem queryProble(String problemID) throws Exception {
		return commonDao.findEntityByID(Problem.class, problemID);
	}

	@Override
	public void modifyQuality(Problem problem) throws Exception {
		problem.setCreateDate(new Date());
		commonDao.updateEntity(problem);
	}
	
	@Override
	public PageInfo queryProblemWithPage(PageInfo pageInfo, LoginInfo loginInfo, Map<String, String> paras) throws Exception {
		return problemDao.queryProblemWithPage(pageInfo, loginInfo.getUserId(), paras);
	}

	@Override
	public void delProblem(String id) throws Exception {
		commonDao.delEntityById(Problem.class, id);
	}

	@Override
	public void replyProblem(String problemID, String descr, LoginInfo loginInfo) throws Exception {
		ProblemReply reply = new ProblemReply();
		reply.setCreateDate(new Date());
		reply.setCreateUsrID(loginInfo.getUserId());
		reply.setCreateUsrNam(loginInfo.getUserNam());
		reply.setDescr(descr);
		reply.setId(dBUtil.getCommonId());
		reply.setProblemID(problemID);
		commonDao.saveEntity(reply);
		
		Problem problem = commonDao.findEntityByID(Problem.class, problemID);
		problem.setStatus(EnumProblemSta.replyed.getCode());
		problem.setReplyCount(problem.getReplyCount() + 1);
		commonDao.updateEntity(problem);
	}

	@Override
	public PageInfo queryProblemReply(PageInfo pageInfo, String problemID) throws Exception {
		List<ProblemReply> replyList = problemDao.queryReply(problemID);
		pageInfo.setRows(replyList);
		pageInfo.setTotal("" + replyList.size());
		return pageInfo;
	}

	@Override
	public void closeProblem(String id) throws Exception {
		Problem problem = commonDao.findEntityByID(Problem.class, id);
		problem.setStatus(EnumProblemSta.manclose.getCode());
		commonDao.updateEntity(problem);
	}

	@Override
	public PageInfo queryAllProblemWithPage(PageInfo pageInfo, Map<String, String> paras) throws Exception {
		return problemDao.queryAllProblemWithPage(pageInfo, paras);
	}

	@Override
	public void autoCloseProblem(int day) throws Exception {
		problemDao.autoCloseProblem(day);
	}
	
}