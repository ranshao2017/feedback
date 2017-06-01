package com.sense.frame.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sense.frame.common.spring.DateEditor;
import com.sense.frame.pub.global.LoginInfo;
import com.sense.frame.pub.model.PageInfo;

/**
 * Controller层基础类,继承MultiActionController类
 */
public abstract class BaseController extends MultiActionController {

	/**
	 * 日志处理类
	 */
	private Logger logger = Logger.getLogger(BaseController.class);

	/**
	 * BeanUtils的populate方法或者getProperty,setProperty方法其实都会调用convert进行转换
	 * 但Converter只支持一些基本的类型，对于其它类型，
	 * 比如对于java.sql.Date类型，需要实现了一个Converter,并注册。apache提供了这些converter
	 */
	static {
		ConvertRegisterHelper.registerConverters();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateEditor(false));
		binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(
				BigDecimal.class, true));
		binder.registerCustomEditor(Integer.class, null,
				new CustomNumberEditor(Integer.class, null, true));
		binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(
				Long.class, null, true));
		binder.registerCustomEditor(Float.class, new CustomNumberEditor(
				Float.class, true));
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(
				Double.class, true));
		binder.registerCustomEditor(BigInteger.class, new CustomNumberEditor(
				BigInteger.class, true));
	}

	/**
	 * BusinessException异常的捕捉和处理，只对ajax请求校验，
	 * 前台如果用到的话在方法中增加相应的异常对象绑定自行处理返回页面情况，如未处理则跳转到异常页面
	 * 
	 * @param ex
	 *            BusinessException异常对象
	 * @param request
	 *            request请求对象
	 * @return 返回错误页面
	 */
	@ExceptionHandler(BusinessException.class)
	public ModelAndView expBusiException(BusinessException ex,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("业务数据异常", ex);
		return handleException(request, response, ex.getMessage());
	}

	/**
	 * BindException异常的捕捉和处理，只对ajax请求校验，
	 * 前台如果用到的话在方法中增加相应的异常对象绑定自行处理返回页面情况，如未处理则跳转到异常页面
	 * 
	 * @param ex
	 *            expBindException异常对象
	 * @param request
	 * @return 返回错误页面
	 */
	@ExceptionHandler(BindException.class)
	public ModelAndView expBindException(BindException ex,
			HttpServletRequest request, HttpServletResponse response) {
		String errFiledMsg = "提交数据验证不通过：";
		List<FieldError> fieldErrList = ex.getFieldErrors();
		for (FieldError fieldError : fieldErrList) {
			errFiledMsg += "\n" + fieldError.getDefaultMessage();
		}
		logger.info("数据校验异常", ex);
		return handleException(request, response, errFiledMsg);
	}

	/**
	 * 其他异常的捕捉和处理
	 * 
	 * @param ex
	 *            Exception异常对象
	 * @param request
	 *            request请求对象
	 * @return 返回错误页面
	 */
	@ExceptionHandler
	public ModelAndView expException(Exception ex, HttpServletRequest request,
			HttpServletResponse response) {
		if (!(ex instanceof BindException)
				&& !(ex instanceof BusinessException)) {
			logger.warn("程序异常", ex);
			return handleException(request, response, "服务器出现异常");
		}
		return null;
	}

	private ModelAndView handleException(HttpServletRequest request,
			HttpServletResponse response, String exceptionMsg) {
		if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request
				.getHeader("X-Requested-With") != null && request.getHeader(
				"X-Requested-With").indexOf("XMLHttpRequest") > -1))) {// 普通跳转请求
			request.setAttribute("exceptionMsg", exceptionMsg);
			return new ModelAndView("exception");
		} else {// ajax请求，返回JSON格式
			try {
				String returnStr = "{'errflag':'1', 'errtext':'" + exceptionMsg
						+ "', 'msgtext':''}";
				JSONObject jsonObject = JSON.parseObject(returnStr);
				response.setContentType("application/json; charset=utf-8");
				PrintWriter writer = response.getWriter();
				writer.print(jsonObject);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * 返回错误信息
	 */
	@ResponseBody
	protected Map<String, Object> writeErrMsg(String errtext) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("errflag", "1");
		map.put("errtext", errtext);
		map.put("msgtext", "");
		return map;
	}
	
	/**
	 * 返回信息
	 */
	@ResponseBody
	public Map<String, Object> writeSuccMsg() {
		return writeSuccMsg("",null);
	}

	/**
	 * 返回信息
	 */
	@ResponseBody
	public Map<String, Object> writeSuccMsg(String msgtext) {
		return writeSuccMsg(msgtext,null);
	}
	
	/**
	 * 返回信息
	 */
	@ResponseBody
	public Map<String, Object> writeSuccMsg(String msgtext,String key ,String value) {
		HashMap<String, Object> frontParamMap = new HashMap<String, Object>();
		frontParamMap.put(key, value);
		return writeSuccMsg(msgtext,frontParamMap);
	}

	/**
	 * 返回信息，包含参数
	 */
	@ResponseBody
	public Map<String, Object> writeSuccMsg(String msgtext, Map<String, Object> frontParamMap) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("errflag", "0");
		map.put("errtext", "");
		map.put("msgtext", msgtext);
		if (frontParamMap != null) {
			map.put("paramMap", frontParamMap);
		}
		return map;
	}
	
	/**
	 * 从request获取所有参数，以key-value的方式存储到map中
	 */
	public Map<String, String> getRequestPara(HttpServletRequest request) {
		Map<String, String> dataMap = new HashMap<String, String>();
		Enumeration<String> paraNames = request.getParameterNames();
		while (paraNames.hasMoreElements()) {
			String paraName = paraNames.nextElement();
			dataMap.put(paraName, request.getParameter(paraName));
		}
		return dataMap;
	}

	/**
	 * 获取分页信息，主要用于dataGrid的分页查询
	 */
	public PageInfo getPageInfo(HttpServletRequest request) {
		PageInfo pi = new PageInfo();
		String sortOrder = request.getParameter("order");
		String pageNumber = request.getParameter("page");
		String pageSize = request.getParameter("rows");
		String sortName = request.getParameter("sort");
		pi.setPageNumber(pageNumber == null ? 1 : Integer.parseInt(pageNumber));
		pi.setPageSize(pageSize == null ? 15 : Integer.parseInt(pageSize));
		pi.setSortName(sortName);
		pi.setSortOrder(sortOrder);
		return pi;
	}

	/**
	 * 获取当前登录的用户
	 */
	public LoginInfo getLoginInfo(HttpServletRequest request) throws Exception {
		return (LoginInfo) request.getSession().getAttribute(LoginInfo.LOGIN_USER);
	}

}