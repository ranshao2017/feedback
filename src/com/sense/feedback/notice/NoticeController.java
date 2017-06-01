package com.sense.feedback.notice;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sense.feedback.entity.Notice;
import com.sense.feedback.notice.service.NoticeService;
import com.sense.frame.base.BaseController;
import com.sense.frame.pub.model.PageInfo;
import com.sense.frame.pub.model.TreeModel;

@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {
	
	@Autowired
	private NoticeService noticeService;
	
	/**
	 * 通知管理页面
	 */
	@RequestMapping("/forwardNoticeManage")
	public String forwardNoticeManage(HttpServletRequest request, ModelMap map) throws Exception {
		return "notice/managenotice";	
	}
	
	/**
	 * 分页检索当前用户发布的通知
	 */
	@RequestMapping("/queryNoticeWithPage")
	@ResponseBody     
	public PageInfo queryNoticeWithPage(HttpServletRequest request)throws Exception{	
		return noticeService.queryNoticeWithPage(getPageInfo(request), getLoginInfo(request));
	}
	
	/**
	 * 发布通知页面
	 */
	@RequestMapping("/forwardPubNotice")
	public String forwardPubNotice(HttpServletRequest request, ModelMap map) throws Exception {
		return "notice/addnotice";	
	}
	
	/**
	 * 发布通知
	 */
	@RequestMapping("/addNotice")
	@ResponseBody
	public Map<String, Object> addNotice(HttpServletRequest request, @Valid Notice notice) throws Exception {
		noticeService.addNotice(notice, getLoginInfo(request));
		return this.writeSuccMsg("发布通知成功");
	}
	
	/**
	 * 删除通知
	 */
	@RequestMapping("/delNotice")
	@ResponseBody
	public Map<String, Object> delNotice(String id) throws Exception {
		noticeService.delNotice(id);
		return this.writeSuccMsg("删除通知成功");
	}
	
	/**
	 * 查看通知页面
	 */
	@RequestMapping("/forwardNoticeDtl")
	public String forwardNoticeDtl(HttpServletRequest request, ModelMap map, String noticeID) throws Exception {
		Notice notice = noticeService.queryNoticeByID(noticeID);
		String noticeJson = JSON.toJSONStringWithDateFormat(notice, "yyyy-MM-dd HH:mm:ss");
		map.put("noticeJson", noticeJson);
		return "notice/noticedtl";
	}
	
	/**
	 * 获取通知的接收用户树
	 */
	@RequestMapping("/queryNoticeUsrTree")
	@ResponseBody
	public List<TreeModel> queryNoticeUsrTree(String noticeID) throws Exception {
		return noticeService.queryNoticeUsrTree(noticeID);
	}
	
	/**
	 * 接收通知页面
	 */
	@RequestMapping("/forwardNoticeRec")
	public String forwardNoticeRec(HttpServletRequest request, ModelMap map) throws Exception {
		return "notice/noticerec";	
	}
	
	/**
	 * 分页检索当前用户接收的通知
	 */
	@RequestMapping("/queryRecNoticeWithPage")
	@ResponseBody     
	public PageInfo queryRecNoticeWithPage(HttpServletRequest request)throws Exception{	
		return noticeService.queryRecNoticeWithPage(getPageInfo(request), getLoginInfo(request));
	}
	
	/**
	 * 查看通知，修改通知状态为已读
	 */
	@RequestMapping("/seeNoticeDtl")
	public String seeNoticeDtl(HttpServletRequest request, ModelMap map, String noticeID) throws Exception {
		Notice notice = noticeService.seeNoticeDtl(getLoginInfo(request), noticeID);
		String noticeJson = JSON.toJSONStringWithDateFormat(notice, "yyyy-MM-dd HH:mm:ss");
		map.put("noticeJson", noticeJson);
		return "notice/seenoticedtl";	
	}
}