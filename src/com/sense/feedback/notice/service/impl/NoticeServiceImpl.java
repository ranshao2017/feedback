package com.sense.feedback.notice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sense.feedback.entity.Notice;
import com.sense.feedback.entity.NoticeRead;
import com.sense.feedback.enumdic.EnumYesNo;
import com.sense.feedback.jdpush.JdPush;
import com.sense.feedback.notice.dao.NoticeDao;
import com.sense.feedback.notice.service.NoticeService;
import com.sense.frame.base.BaseService;
import com.sense.frame.common.util.TreeUtil;
import com.sense.frame.pub.global.LoginInfo;
import com.sense.frame.pub.model.PageInfo;
import com.sense.frame.pub.model.TreeModel;
import com.sense.sys.entity.Org;
import com.sense.sys.entity.Usr;
import com.sense.sys.usr.dao.UserDao;

@Service
public class NoticeServiceImpl extends BaseService implements NoticeService {
	
	@Autowired
	private NoticeDao noticeDao;
	@Autowired
	private UserDao userDao;

	@Override
	public PageInfo queryNoticeWithPage(PageInfo pageInfo, LoginInfo loginInfo, Map<String,String> paras) throws Exception {
		return noticeDao.queryNoticeWithPage(pageInfo, loginInfo.getUserId(), paras);
	}

	@Override
	public void addNotice(Notice notice, LoginInfo loginInfo) throws Exception {
		//保存通知
		String noticeId = dBUtil.getCommonId();
		notice.setId(noticeId);
		notice.setCreateDate(new Date());
		notice.setCreateUsrID(loginInfo.getUserId());
		notice.setCreateUsrName(loginInfo.getUserNam());
		commonDao.saveEntity(notice);
		
		//保存通知接收人记录
		for(String reveciveUsrID : notice.getReveciveUsrIDs().split(",")){
			NoticeRead nr = new NoticeRead();
			nr.setId(dBUtil.getCommonId());
			nr.setIsRead(EnumYesNo.no.getCode());
			nr.setNoticeID(noticeId);
			nr.setUserID(reveciveUsrID);
			commonDao.saveEntity(nr);
		}
		
		//推送消息
		List<Map<String, String>> usrMapList = userDao.queryNoticeUsr(notice.getReveciveUsrIDs().split(","));
		Map<String, List<String>> clientMap = new HashMap<String, List<String>>();
		for(Map<String, String> map : usrMapList){
			String clientID = map.get("CLIENTID");
			String clientType = map.get("CLIENTTYPE");
			if(StringUtils.isBlank(clientID) || StringUtils.isBlank(clientType)){
				continue;
			}
			if(!clientMap.containsKey(clientType)){
				clientMap.put(clientType, new ArrayList<String>());
			}
			clientMap.get(clientType).add(clientID);
		}
		if(clientMap.size() > 0){
			new Thread(new PushMsgThread(notice, clientMap)).start();
		}
	}
	
	class PushMsgThread extends Thread {
		private Notice notice;
		private Map<String, List<String>> clientMap;
		
		public PushMsgThread(Notice notice, Map<String, List<String>> clientMap) {
			this.notice = notice;
			this.clientMap = clientMap;
		}

		@Override
		public void run() {
			JdPush.sendPushMessage(notice.getBody(), notice.getTopic(), clientMap);
		}
		
	}
	
	/**
	 * 删除通知
	 */
	@Override
	public void delNotice(String id) throws Exception {
		commonDao.delEntityById(Notice.class, id);
		noticeDao.delNoticeRead(id);
	}

	@Override
	public Notice queryNoticeByID(String noticeID) throws Exception {
		return commonDao.findEntityByID(Notice.class, noticeID);
	}

	/**
	 * 获取通知的接收用户树
	 */
	@Override
	public List<TreeModel> queryNoticeUsrTree(String noticeID) throws Exception {
		List<Org> orgList = commonDao.findEntityList(Org.class);
		List<TreeModel> orgTree = TreeUtil.setTree(orgList);
		List<Usr> usrList = noticeDao.queryNoticeReceiver(noticeID);
		List<TreeModel> usrTree = TreeUtil.setTree(usrList);
		List<TreeModel> mergTree = TreeUtil.mergeTree(orgTree, usrTree);
		TreeUtil.setTreeOpenLevel(mergTree, 2);
		return mergTree;
	}

	/**
	 * 分页检索当前用户接收的通知
	 */
	@Override
	public PageInfo queryRecNoticeWithPage(PageInfo pageInfo, LoginInfo loginInfo) throws Exception {
		return noticeDao.queryRecNoticeWithPage(pageInfo, loginInfo.getUserId());
	}

	/**
	 * 查看通知，修改通知状态为已读
	 */
	@Override
	public Notice seeNoticeDtl(LoginInfo loginInfo, String noticeID) throws Exception {
		Notice notice = commonDao.findEntityByID(Notice.class, noticeID);
		noticeDao.updateRead(noticeID, loginInfo.getUserId());
		return notice;
	}

}