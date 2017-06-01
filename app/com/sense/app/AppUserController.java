package com.sense.app;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sense.app.dto.UserDto;
import com.sense.app.service.AppUserService;
import com.sense.app.util.AppJsonUtil;
import com.sense.frame.base.BaseController;

@Controller
@RequestMapping("/appUser")
public class AppUserController extends BaseController {
	
	private static Logger log = Logger.getLogger("appFileLog");
	
	@Autowired
	private AppUserService appUserService;
	
	/**
	 * 登录
	 */
	@RequestMapping("/login")
	@ResponseBody
	public Map<String, Object> login(String username, String pwd) {
		if(StringUtils.isBlank(username)){
			return AppJsonUtil.writeErr("用户名不能为空");
		}
		if(StringUtils.isBlank(pwd)){
			return AppJsonUtil.writeErr("密码不能为空");
		}
		try {
			UserDto dto = appUserService.queryUsr(username, pwd);
			if(null == dto){
				return AppJsonUtil.writeErr("登录失败，用户密码错误");
			}
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("userinfo", dto);
			return AppJsonUtil.writeSucc("登录成功！", paraMap);
		} catch (Exception e) {
			log.error("登录异常", e);
			return AppJsonUtil.writeErr("登录异常");
		}
		
	}
	
	/**
	 * 修改个人资料
	 */
	@RequestMapping("/modifyUserInfo")
	@ResponseBody
	public Map<String, Object> modifyUserInfo(String userid, String name, String xb, String telephone, String email, String address) {
		if(StringUtils.isBlank(userid)){
			return AppJsonUtil.writeErr("用户ID不能为空");
		}
		try {
			appUserService.modifyUserInfo(userid, name, xb, telephone, email, address);
			return AppJsonUtil.writeSucc("修改成功！");
		} catch (Exception e) {
			log.error("修改个人资料异常", e);
			return AppJsonUtil.writeErr("修改个人资料异常");
		}
	}
	
	/**
	 * 修改密码
	 */
	@RequestMapping("/modifyPwd")
	@ResponseBody
	public Map<String, Object> modifyPwd(String userid, String newpwd, String oldpwd) {
		if(StringUtils.isBlank(userid)){
			return AppJsonUtil.writeErr("用户ID不能为空");
		}
		if(StringUtils.isBlank(newpwd)){
			return AppJsonUtil.writeErr("新密码不能为空");
		}
		if(StringUtils.isBlank(oldpwd)){
			return AppJsonUtil.writeErr("旧密码不能为空");
		}
		try {
			appUserService.modifyPwd(userid, newpwd, oldpwd);
			return AppJsonUtil.writeSucc("修改成功！");
		} catch (Exception e) {
			log.error("修改密码异常", e);
			return AppJsonUtil.writeErr(e.getMessage());
		}
	}
	
	/**
	 * 上传客户端标识
	 */
	@RequestMapping("/uploadJG")
	@ResponseBody
	public Map<String, Object> uploadJG(String userid, String clientid, String clienttype) {
		if(StringUtils.isBlank(userid)){
			return AppJsonUtil.writeErr("用户ID不能为空");
		}
		if(StringUtils.isBlank(clientid)){
			return AppJsonUtil.writeErr("手机识别码不能为空");
		}
		if(StringUtils.isBlank(clienttype)){
			return AppJsonUtil.writeErr("客户端类型不能为空");
		}
		try {
			appUserService.uploadJG(userid, clientid, clienttype);
			return AppJsonUtil.writeSucc("上传成功！");
		} catch (Exception e) {
			log.error("上传异常", e);
			return AppJsonUtil.writeErr(e.getMessage());
		}
	}
}