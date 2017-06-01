package com.sense.app.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sense.app.dao.AppNoticeDao;
import com.sense.app.dto.NoticeDto;
import com.sense.app.service.AppNoticeService;
import com.sense.feedback.entity.Notice;
import com.sense.frame.base.BaseService;
import com.sense.frame.pub.model.PageInfo;

@Service
public class AppNoticeServiceImpl extends BaseService implements AppNoticeService {

	@Autowired
	private AppNoticeDao appNoticeDao;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NoticeDto> queryNoticePage(PageInfo pi, String userid) throws Exception {
		List<NoticeDto> dtoList = new ArrayList<NoticeDto>();
		pi = appNoticeDao.queryNoticePage(pi, userid);
		List<Notice> noticeList = (List<Notice>) pi.getRows();
		if(CollectionUtils.isNotEmpty(noticeList)){
			for(Notice notice : noticeList){
				NoticeDto dto = new NoticeDto();
				dto.setId(notice.getId());
				dto.setBody(notice.getBody());
				dto.setCreatedate(sdf.format(notice.getCreateDate()));
				dto.setCreateusrid(notice.getCreateUsrID());
				dto.setCreateusrname(notice.getCreateUsrName());
				dto.setIsread(notice.getIsRead());
				dto.setTopic(notice.getTopic());
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

	@Override
	public void readNotice(String noticeid, String userid) throws Exception {
		appNoticeDao.readNotice(noticeid, userid);
	}

}