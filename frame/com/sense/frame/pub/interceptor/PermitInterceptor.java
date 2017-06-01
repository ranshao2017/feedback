package com.sense.frame.pub.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.sense.frame.pub.global.LoginInfo;

public class PermitInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		LoginInfo loginInfo = (LoginInfo) request.getSession(true).getAttribute(LoginInfo.LOGIN_USER);
		Set<String> urlList = loginInfo.getUrlList();
		String requestUrl = request.getRequestURI() + "?" + request.getQueryString();
		boolean hasPermit = false;
		for(String url : urlList){
			if(requestUrl.indexOf(url) != -1){
				hasPermit = true;
				break;
			}
		}
		if(!hasPermit){
			if (!(request.getHeader("accept").indexOf("application/json") > -1 || 
            		(request.getHeader("X-Requested-With")!= null && 
            			request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {// 普通跳转请求
				response.sendRedirect(request.getContextPath() + "/nopermit.jsp");
            } else {// ajax请求，返回JSON格式
				try {
					String returnStr = "{'errflag':'1', 'errtext':'您没有权限进行该操作，请联系管理员！', 'msgtext':''}";
					JSONObject jsonObject = JSONObject.parseObject(returnStr);
					response.setContentType("application/json; charset=utf-8"); 
					PrintWriter writer = response.getWriter();
					writer.print(jsonObject);
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
			return false;
		}
		return super.preHandle(request, response, handler);
	}

}