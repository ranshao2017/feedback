package com.sense.app.service;

import java.util.List;

import com.sense.app.dto.NoticeDto;
import com.sense.frame.pub.model.PageInfo;

public interface AppNoticeService {

	List<NoticeDto> queryNoticePage(PageInfo pi, String userid) throws Exception;

	void readNotice(String noticeid, String userid) throws Exception;

}