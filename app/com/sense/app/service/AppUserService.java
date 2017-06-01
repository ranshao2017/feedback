package com.sense.app.service;

import com.sense.app.dto.UserDto;

public interface AppUserService {

	public UserDto queryUsr(String username, String pwd) throws Exception;

	public void modifyUserInfo(String userid, String name, String xb,
			String telephone, String email, String address) throws Exception;

	public void modifyPwd(String userid, String newpwd, String oldpwd) throws Exception;

	public void uploadJG(String userid, String clientid, String clienttype) throws Exception;

}