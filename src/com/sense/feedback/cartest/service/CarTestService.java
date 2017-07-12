package com.sense.feedback.cartest.service;

import java.util.List;
import java.util.Map;

import com.sense.feedback.entity.PaiChan;
import com.sense.feedback.entity.ProcInst;
import com.sense.feedback.entity.ProcInstNode;
import com.sense.frame.pub.global.LoginInfo;
import com.sense.frame.pub.model.PageInfo;

public interface CarTestService {

	public PageInfo queryPCPage(PageInfo pageInfo, Map<String, String> paras) throws Exception;

	public PaiChan queryPCDetail(String scdh) throws Exception;

	public ProcInst queryProcInst(String scdh) throws Exception;

	public void submitTC(ProcInst inst, LoginInfo loginInfo, String qjData) throws Exception;
	
	public void saveTC(ProcInst inst, LoginInfo loginInfo, String qjData) throws Exception;

	public void saveXx(ProcInst inst, LoginInfo loginInfo, String qjData) throws Exception;

	public PageInfo queryCTPage(PageInfo pageInfo, Map<String, String> paras) throws Exception;

	public ProcInstNode queryProcInstNode(String scdh, String status) throws Exception;

	public String queryQJData(String scdh) throws Exception;

	public void saveCT(ProcInstNode inst, LoginInfo loginInfo) throws Exception;

	public void submitCT(ProcInstNode inst, LoginInfo loginInfo) throws Exception;

	public String queryPreInstNodeList(String scdh, String status) throws Exception;

	public void submitUTC(ProcInst inst, LoginInfo loginInfo, String qjData) throws Exception;

	public void saveUXx(ProcInst inst, LoginInfo loginInfo, String qjData) throws Exception;

	public PageInfo queryRepoPage(PageInfo pageInfo, Map<String, String> paras) throws Exception;

	public void submitTCs(ProcInst inst, LoginInfo loginInfo, String scdhs) throws Exception;

	public void submitCTs(ProcInstNode inst, LoginInfo loginInfo, String scdhs) throws Exception;

	public void backCT(ProcInstNode inst, LoginInfo loginInfo) throws Exception;

	public void backCTs(ProcInstNode inst, LoginInfo loginInfo, String scdhs) throws Exception;

	public void unQuailyCT(ProcInstNode inst, LoginInfo loginInfo) throws Exception;

	public void unQuailyCTs(ProcInstNode inst, LoginInfo loginInfo, String scdhs) throws Exception;

	public Map<String, Object> queryCTCount(String status) throws Exception;

	public PageInfo queryXZCTPage(PageInfo pageInfo, Map<String, String> paras) throws Exception;

	public String queryReplyList(String scdh) throws Exception;

	public void submitReply(String scdh, String descr, LoginInfo loginInfo) throws Exception;

	public boolean queryTsFlag(String orgId) throws Exception;

	public void closeReply(String scdh) throws Exception;

	public List<ProcInst> queryExportCT(Map<String, String> paraMap) throws Exception;

	public PaiChan queryPCProcinst(String scdh) throws Exception;

}