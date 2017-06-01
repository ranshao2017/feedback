package com.sense.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sense.app.dao.AppUserDao;
import com.sense.app.dto.UserDto;
import com.sense.app.service.AppUserService;
import com.sense.frame.base.BaseService;
import com.sense.frame.base.BusinessException;
import com.sense.frame.common.util.Md5Util;
import com.sense.sys.entity.Org;
import com.sense.sys.entity.Pst;
import com.sense.sys.entity.Usr;
import com.sense.sys.enumdic.EnumSex;

@Service
public class AppUserServiceImpl extends BaseService implements AppUserService {

	@Autowired
	private AppUserDao userDao;
	
	@Override
	public UserDto queryUsr(String username, String pwd) throws Exception {
		Usr usr = userDao.queryUser(username, Md5Util.md5(pwd));
		if(null == usr){
			return null;
		}
		
		UserDto dto = new UserDto();
		dto.setAddress(usr.getAddr());
		dto.setEmail(usr.getEmail());
		dto.setName(usr.getUsrNam());
		dto.setOrgid(usr.getOrgID());
		Org org = commonDao.findEntityByID(Org.class, usr.getOrgID());
		dto.setOrgname(org.getOrgNam());
		List<Pst> pstList = userDao.queryPst(usr.getUsrID());
		StringBuffer pstIDBuffer = new StringBuffer();
		StringBuffer pstNameBuffer = new StringBuffer();
		for(Pst pst : pstList){
			pstIDBuffer.append(",").append(pst.getPstID());
			pstNameBuffer.append(",").append(pst.getPstNam());
		}
		if(pstIDBuffer.length() > 0){
			dto.setPstid(pstIDBuffer.toString().substring(1));
			dto.setPstname(pstNameBuffer.toString().substring(1));
		}
		dto.setSex(EnumSex.getLabelByCode(usr.getSex()));
		dto.setTelephone(usr.getTelNO());
		dto.setUserid(usr.getUsrID());
		dto.setUsrename(usr.getUsrCod());
		return dto;
	}

	@Override
	public void modifyUserInfo(String userid, String name, String xb,
			String telephone, String email, String address) throws Exception {
		Usr usr = commonDao.findEntityByID(Usr.class, userid);
		usr.setUsrNam(name);
		usr.setSex(xb);
		usr.setTelNO(telephone);
		usr.setEmail(email);
		usr.setAddr(address);
		commonDao.updateEntity(usr);
	}

	@Override
	public void modifyPwd(String userid, String newpwd, String oldpwd) throws Exception {
		Usr usr = commonDao.findEntityByID(Usr.class, userid);
		if(!Md5Util.md5(oldpwd).equals(usr.getUsrPwd())){
			throw new BusinessException("旧密码不对");
		}
		usr.setUsrPwd(Md5Util.md5(newpwd));
		commonDao.updateEntity(usr);
	}

	@Override
	public void uploadJG(String userid, String clientid, String clienttype) throws Exception {
		Usr usr = commonDao.findEntityByID(Usr.class, userid);
		usr.setClientID(clientid);
		usr.setClientType(clienttype);
		commonDao.updateEntity(usr);
	}

}