package com.sense.feedback.notice.service;

import java.util.List;

import com.sense.feedback.entity.Notice;
import com.sense.frame.pub.global.LoginInfo;
import com.sense.frame.pub.model.PageInfo;
import com.sense.frame.pub.model.TreeModel;

public interface NoticeService {

	public PageInfo queryNoticeWithPage(PageInfo pageInfo, LoginInfo loginInfo) throws Exception;

	public void addNotice(Notice notice, LoginInfo loginInfo) throws Exception;

	public void delNotice(String id) throws Exception;

	public Notice queryNoticeByID(String noticeID) throws Exception;

	/**
	 * 获取通知的接收用户树
	 */
	public List<TreeModel> queryNoticeUsrTree(String noticeID) throws Exception;

	/**
	 * 分页检索当前用户接收的通知
	 */
	public PageInfo queryRecNoticeWithPage(PageInfo pageInfo, LoginInfo loginInfo) throws Exception;

	/**
	 * 查看通知，修改通知状态为已读
	 */
	public Notice seeNoticeDtl(LoginInfo loginInfo, String noticeID) throws Exception;

}
