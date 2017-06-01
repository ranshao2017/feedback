package com.sense.frame.pub.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.sense.frame.pub.global.LoginInfo;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		LoginInfo loginInfo = (LoginInfo) request.getSession(true)
				.getAttribute(LoginInfo.LOGIN_USER);
		if (null == loginInfo) {
			/*if (!(request.getHeader("accept").indexOf("application/json") > -1 || 
    		    (request.getHeader("X-Requested-With")!= null && 
    			request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {// 普通跳转请求
				System.out.println(request.getRequestURI());
				System.out.println(request.getRequestURL());
				response.sendRedirect(request.getContextPath() + "/nologin.jsp");
			    } else {// ajax请求，返回JSON格式
					try {
						String returnStr = "{'errflag':'1', 'errtext':'您还未登录或者登录超时，请重新登录！', 'msgtext':''}";
						JSONObject jsonObject = JSONObject.parseObject(returnStr);
						response.setContentType("application/json; charset=utf-8"); 
						PrintWriter writer = response.getWriter();
						writer.print(jsonObject);
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			    }*/

			if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request
					.getHeader("X-Requested-With") != null && request
					.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {// 普通跳转请求
				PrintWriter writer = response.getWriter();
				String returnStr = " <script type='text/javascript'>"
						+ " var topWin = (function (p, c) {while (p != c) {c = p;p = p.parent}return c;})(window.parent, window); "
						+ " try{ topWin.hitooctrl.openLoginWin();}catch(e){window.location='"
						+ request.getContextPath() + "/nologin.jsp'}"
						+ " </script>";
				writer.println(returnStr);
				writer.close();
			} else {// ajax请求，返回JSON格式
				try {
					String returnStr = "{'errflag':'logonOverTimeError', 'errtext':'您还未登录或者登录超时，请重新登录！', 'msgtext':''}";
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