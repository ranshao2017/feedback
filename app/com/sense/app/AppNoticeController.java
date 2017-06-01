package com.sense.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sense.app.dto.NoticeDto;
import com.sense.app.service.AppNoticeService;
import com.sense.app.util.AppJsonUtil;
import com.sense.frame.base.BaseController;
import com.sense.frame.pub.model.PageInfo;

@Controller
@RequestMapping("/appNotice")
public class AppNoticeController extends BaseController {
	
	private static Logger log = Logger.getLogger("appFileLog");
	
	@Autowired
	private AppNoticeService appNoticeService;

	/**
	 * 查看通知列表
	 * @param pagerow 每页显示多少行
	 * @param pagesize 当前第几页
	 * @param userid 用户id
	 */
	@RequestMapping("/queryNoticePage")
	@ResponseBody
	public Map<String, Object> queryNoticePage(int pagerow, int pagesize, String userid) {
		try{
			PageInfo pi = new PageInfo();
			pi.setPageSize(pagerow);
			pi.setPageNumber(pagesize);
			List<NoticeDto> list = appNoticeService.queryNoticePage(pi, userid);
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("returnlist", list);
			return AppJsonUtil.writeSucc("操作成功", paraMap);
		}catch(Exception e){
			log.error("查看通知列表异常", e);
			return AppJsonUtil.writeErr("查看通知列表异常");
		}
	}
	
	/**
	 * 标识已读
	 */
	@RequestMapping("/readNotice")
	@ResponseBody
	public Map<String, Object> readNotice(String noticeid, String userid) {
		try{
			appNoticeService.readNotice(noticeid, userid);
			return AppJsonUtil.writeSucc("操作成功");
		}catch(Exception e){
			log.error("标识已读异常", e);
			return AppJsonUtil.writeErr("标识已读异常");
		}
	}
	
}