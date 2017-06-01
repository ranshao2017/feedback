package com.sense.frame.common.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletRequest;

/**
 * ServletRequest工具类
 */
public class ServletRequestUtil {

	/**
	 * 获取ServletRequest中参数的String值，如不含此参数则抛出Exception异常
	 * 
	 * @param request
	 * @param paraName
	 * @return
	 * @throws Exception
	 */
	public static String getString(ServletRequest request, String paraName)
			throws Exception {
		if (request == null) {
			throw new Exception("参数[request]为空！");
		}
		if ((paraName == null) || "".equals(paraName)) {
			throw new Exception("参数[paraName]为空！");
		}
		if (!request.getParameterMap().containsKey(paraName)) {
			throw new Exception("请求中未找到参数[" + paraName + "']！");
		}

		return request.getParameter(paraName);
	}

	/**
	 * 获取ServletRequest中参数的String值，如不含此参数则返回默认值
	 * 
	 * @param request
	 * @param paraName
	 * @return
	 * @throws Exception
	 */
	public static String getString(ServletRequest request, String paraName,
			String defaultValue) throws Exception {
		if (request == null) {
			throw new Exception("参数[request]为空！");
		}
		if ((paraName == null) || "".equals(paraName)) {
			throw new Exception("参数[paraName]为空！");
		}

		if (!request.getParameterMap().containsKey(paraName)) {
			return defaultValue;
		}

		return request.getParameter(paraName);
	}

	/**
	 * 获取ServletRequest中参数的int值，如不含此参数则抛出Exception异常
	 * 
	 * @param request
	 * @param paraName
	 * @return
	 * @throws Exception
	 */
	public static int getInt(ServletRequest request, String paraName)
			throws Exception {
		String paraValue;

		if (request == null) {
			throw new Exception("参数[request]为空！");
		}
		if ((paraName == null) || "".equals(paraName)) {
			throw new Exception("参数[paraName]为空！");
		}
		if (!request.getParameterMap().containsKey(paraName)) {
			throw new Exception("请求中未找到参数[" + paraName + "']！");
		}

		paraValue = request.getParameter(paraName);
		if ((paraValue == null) || "".equals(paraValue)) {
			return 0;
		}
		return Integer.parseInt(paraValue);
	}

	/**
	 * 获取ServletRequest中参数的int值，如不含此参数则返回默认值
	 * 
	 * @param request
	 * @param paraName
	 * @return
	 * @throws Exception
	 */
	public static int getInt(ServletRequest request, String paraName,
			int defaultValue) throws Exception {
		String paraValue;

		if (request == null) {
			throw new Exception("参数[request]为空！");
		}
		if ((paraName == null) || "".equals(paraName)) {
			throw new Exception("参数[paraName]为空！");
		}
		if (!request.getParameterMap().containsKey(paraName)) {
			return defaultValue;
		}

		paraValue = request.getParameter(paraName);
		if ((paraValue == null) || "".equals(paraValue)) {
			return 0;
		}
		return Integer.parseInt(paraValue);
	}

	/**
	 * 获取ServletRequest中参数的BigDecimal值，如不含此参数则抛出Exception异常
	 * 
	 * @param request
	 * @param paraName
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal getBigDecimal(ServletRequest request,
			String paraName) throws Exception {
		String paraValue;

		if (request == null) {
			throw new Exception("参数[request]为空！");
		}
		if ((paraName == null) || "".equals(paraName)) {
			throw new Exception("参数[paraName]为空！");
		}
		if (!request.getParameterMap().containsKey(paraName)) {
			throw new Exception("请求中未找到参数[" + paraName + "']！");
		}

		paraValue = request.getParameter(paraName);
		if ((paraValue == null) || "".equals(paraValue)) {
			return BigDecimal.ZERO;
		}
		return new BigDecimal(paraValue);
	}

	/**
	 * 获取ServletRequest中参数的BigDecimal值，如不含此参数则返回默认值
	 * 
	 * @param request
	 * @param paraName
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal getBigDecimal(ServletRequest request,
			String paraName, BigDecimal defaultValue) throws Exception {
		String paraValue;

		if (request == null) {
			throw new Exception("参数[request]为空！");
		}
		if ((paraName == null) || "".equals(paraName)) {
			throw new Exception("参数[paraName]为空！");
		}
		if (!request.getParameterMap().containsKey(paraName)) {
			return defaultValue;
		}

		paraValue = request.getParameter(paraName);
		if ((paraValue == null) || "".equals(paraValue)) {
			return BigDecimal.ZERO;
		}
		return new BigDecimal(paraValue);
	}

	/**
	 * 获取ServletRequest中参数的Date值，如不含此参数则抛出Exception异常
	 * 
	 * @param request
	 * @param paraName
	 * @return
	 * @throws Exception
	 */
	public static Date getDate(ServletRequest request, String paraName)
			throws Exception {
		String paraValue;

		if (request == null) {
			throw new Exception("参数[request]为空！");
		}
		if ((paraName == null) || "".equals(paraName)) {
			throw new Exception("参数[paraName]为空！");
		}
		if (!request.getParameterMap().containsKey(paraName)) {
			throw new Exception("请求中未找到参数[" + paraName + "']！");
		}

		paraValue = request.getParameter(paraName);
		if ((paraValue == null) || "".equals(paraValue)) {
			return null;
		}
		return DateUtil.stringToDate(paraValue);
	}

	/**
	 * 获取ServletRequest中参数的Date值，如不含此参数则返回默认值
	 * 
	 * @param request
	 * @param paraName
	 * @return
	 * @throws Exception
	 */
	public static Date getDate(ServletRequest request, String paraName,
			Date defaultValue) throws Exception {
		String paraValue;

		if (request == null) {
			throw new Exception("参数[request]为空！");
		}
		if ((paraName == null) || "".equals(paraName)) {
			throw new Exception("参数[paraName]为空！");
		}
		if (!request.getParameterMap().containsKey(paraName)) {
			return defaultValue;
		}

		paraValue = request.getParameter(paraName);
		if ((paraValue == null) || "".equals(paraValue)) {
			return null;
		}
		return DateUtil.stringToDate(paraValue);
	}

	/**
	 * 获取ServletRequest中参数的boolean值，如不含此参数则抛出Exception异常
	 * 
	 * @param request
	 * @param paraName
	 * @return
	 * @throws Exception
	 */
	public static boolean getBoolean(ServletRequest request, String paraName)
			throws Exception {
		String paraValue;

		if (request == null) {
			throw new Exception("参数[request]为空！");
		}
		if ((paraName == null) || "".equals(paraName)) {
			throw new Exception("参数[paraName]为空！");
		}
		if (!request.getParameterMap().containsKey(paraName)) {
			throw new Exception("请求中未找到参数[" + paraName + "']！");
		}

		paraValue = request.getParameter(paraName);
		if ((paraValue == null) || "".equals(paraValue)) {
			return false;
		}
		return Boolean.valueOf(paraValue);
	}

	/**
	 * 获取ServletRequest中参数的boolean值，如不含此参数则返回默认值
	 * 
	 * @param request
	 * @param paraName
	 * @return
	 * @throws Exception
	 */
	public static boolean getBoolean(ServletRequest request, String paraName,
			boolean defaultValue) throws Exception {
		String paraValue;

		if (request == null) {
			throw new Exception("参数[request]为空！");
		}
		if ((paraName == null) || "".equals(paraName)) {
			throw new Exception("参数[paraName]为空！");
		}
		if (!request.getParameterMap().containsKey(paraName)) {
			return defaultValue;
		}

		paraValue = request.getParameter(paraName);
		if ((paraValue == null) || "".equals(paraValue)) {
			return false;
		}
		return Boolean.valueOf(paraValue);
	}

	/**
	 * 获取ServletRequest中指定属性的值，如不含此属性则抛出Exception异常
	 * 
	 * @param request
	 * @param attrName
	 * @return
	 * @throws Exception
	 */
	public static <T> T getBeanAttribute(ServletRequest request,
			String attrName, Class<T> pclass) throws Exception {
		T bean;
		boolean exists = false;

		if (request == null) {
			throw new Exception("参数[request]为空！");
		}
		if ((attrName == null) || "".equals(attrName)) {
			throw new Exception("参数[attrName]为空！");
		}

		for (Enumeration<?> e = request.getAttributeNames(); e
				.hasMoreElements();) {
			if (attrName.equals(e.nextElement())) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			throw new Exception("请求中未找到属性[" + attrName + "']！");
		}

		bean = pclass.cast(request.getAttribute(attrName));
		return bean;
	}

	/**
	 * 获取ServletRequest中指定属性的值，如不含此参数则返回默认值
	 * 
	 * @param request
	 * @param attrName
	 * @param defaultBean
	 * @return
	 * @throws Exception
	 */
	public static <T> T getBeanAttribute(ServletRequest request,
			String attrName, Class<T> pclass, T defaultBean) throws Exception {
		T bean;
		boolean exists = false;

		if (request == null) {
			throw new Exception("参数[request]为空！");
		}
		if ((attrName == null) || "".equals(attrName)) {
			throw new Exception("参数[attrName]为空！");
		}
		for (Enumeration<?> e = request.getAttributeNames(); e
				.hasMoreElements();) {
			if (attrName.equals(e.nextElement())) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			return defaultBean;
		}

		bean = pclass.cast(request.getAttribute(attrName));
		return bean;
	}

	/**
	 * 获取ServletRequest中指定属性的list值，如不含此参数则抛出Exception异常
	 * 
	 * @param request
	 * @param attrName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getBeanListAttribute(ServletRequest request,
			String attrName, Class<T> pclass) throws Exception {
		List<T> tList;
		boolean exists = false;

		if (request == null) {
			throw new Exception("参数[request]为空！");
		}
		if ((attrName == null) || "".equals(attrName)) {
			throw new Exception("参数[attrName]为空！");
		}
		for (Enumeration<?> e = request.getAttributeNames(); e
				.hasMoreElements();) {
			if (attrName.equals(e.nextElement())) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			throw new Exception("请求中未找到属性[" + attrName + "']！");
		}

		tList = (List<T>) request.getAttribute(attrName);
		return tList;
	}

	/**
	 * 获取ServletRequest中指定属性的list值，如不含此参数则返回默认值
	 * 
	 * @param request
	 * @param attrName
	 * @param defaultBeanList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getBeanListAttribute(ServletRequest request,
			String attrName, List<T> defaultBeanList) throws Exception {
		List<T> tList;
		boolean exists = false;

		if (request == null) {
			throw new Exception("参数[request]为空！");
		}
		if ((attrName == null) || "".equals(attrName)) {
			throw new Exception("参数[paraName]为空！");
		}

		for (Enumeration<?> e = request.getAttributeNames(); e
				.hasMoreElements();) {
			if (attrName.equals(e.nextElement())) {
				exists = true;
				break;
			}
		}

		if (!exists) {
			return defaultBeanList;
		}

		tList = (List<T>) request.getAttribute(attrName);
		return tList;
	}

}
