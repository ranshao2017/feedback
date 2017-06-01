package com.sense.frame.pub.global;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sense.frame.base.BaseController;
import com.sense.frame.common.util.UUID32Utils;
import com.sense.frame.common.util.ValidateCodeHelper;

/**
 * 通用的请求控制,包括 1.菜单对应页面调整 2.jasper打印对应的打印页面
 * 
 * @author qinchao
 */
@Controller
public class GlobalController extends BaseController {
	
	/**
	 * 打开指定页面，主要用于点击菜单
	 */
	@RequestMapping("/menuDpch")
	public String menuDispatch(ModelMap map, String page) throws Exception {
		return page+".jsp";
	}
	
	/**
	 * 有些参数不带get提交，先post到后台，缓存起来
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/cacheParam")
	@ResponseBody
	public Map<String, Object> cacheParam(HttpServletRequest request) throws Exception {
		String key="cacheparam"+UUID32Utils.getUUID();
		HttpSession session = request.getSession(true);
    	Map<String,Map<String, String>> cacheMap=null;
    	Object tmp =session.getAttribute(CacheParamHelper.SESSION_CACHE_PARAM_MAP);
    	if(tmp!=null){
    		cacheMap = (Map<String,Map<String, String>>)tmp;
    	}else{
    		cacheMap = new  HashMap<String,Map<String, String>>();
    	}
    	Map<String, String> dataMap = getRequestPara(request);
    	cacheMap.put(key, dataMap);
    	session.setAttribute(CacheParamHelper.SESSION_CACHE_PARAM_MAP, cacheMap);
		return writeSuccMsg("", "cacheparamkey",key);
	}
	
	/**
	 * 获取验证码
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/reloadValidateCode")
	@ResponseBody
	public Map<String, Object> reloadValidateCode(HttpServletRequest request) throws Exception {
		int codeCount = Integer.parseInt(request.getParameter("codeCount") == null ? "4"
						: request.getParameter("codeCount"));
		ValidateCodeHelper validCodeHelper = new ValidateCodeHelper(codeCount);
		String validateCode = validCodeHelper.generateValidateCode();

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("validateCode", validateCode);

		return writeSuccMsg("", paraMap);
	}

	/**
	 * 预览验证码
	 */
	@RequestMapping("/viewValidateCodeImg")
	public void viewValidateCodeImg(HttpServletRequest request,
			HttpServletResponse response, String validateCode) throws Exception {

		if (StringUtils.isNotBlank(validateCode)) {
			int width = Integer
					.parseInt(request.getParameter("width") == null ? "200"
							: request.getParameter("width"));
			int height = Integer
					.parseInt(request.getParameter("height") == null ? "50"
							: request.getParameter("height"));
			int codeCount = validateCode.length();
			ValidateCodeHelper validCodeHelper = new ValidateCodeHelper(width, height, codeCount);
			validCodeHelper.viewValidateCodeImg(request, response, validateCode);
		}
	}

	/**
	 * 通用转换器
	 * 
	 * @author qinchao
	 * @date 创建时间 2013-10-24
	 */
	@RequestMapping("/baseIDNameConverter")
	@ResponseBody
	public String baseIDNameConverter(HttpServletRequest request,
			String converterType, String converterID) throws Exception {
		if (converterID == null || converterID.equals("")) {
			Map<String, String> rtnMap = new HashMap<String, String>();
			rtnMap.put(converterID, converterID);
			return JSON.toJSONString(rtnMap);
		}
		String serviceId;

		converterType = converterType + "Converter"; // sysUser --
														// sysUserConverter
		String prefix = converterType.substring(0, 2);
		if (hasTwoUppser(prefix)) {
			serviceId = converterType;
		} else {
			serviceId = converterType.substring(0, 1).toLowerCase()
					+ converterType.substring(1);
		}
		IBaseDataConverter converter = (IBaseDataConverter) super
				.getWebApplicationContext().getBean(serviceId);
		return converter.convertIDToName(converterID);
	}

	/**
	 * 通用转换器
	 */
	@RequestMapping("/baseIDCodeConverter")
	@ResponseBody
	public String baseIDCodeConverter(HttpServletRequest request,
			String converterType, String converterID) throws Exception {
		if (converterID == null || converterID.equals("")) {
			Map<String, String> rtnMap = new HashMap<String, String>();
			rtnMap.put(converterID, converterID);
			return JSON.toJSONString(rtnMap);
		}
		String serviceId;

		converterType = converterType + "Converter"; // sysUser --
														// sysUserConverter
		String prefix = converterType.substring(0, 2);
		if (hasTwoUppser(prefix)) {
			serviceId = converterType;
		} else {
			serviceId = converterType.substring(0, 1).toLowerCase()
					+ converterType.substring(1);
		}
		IBaseDataConverter converter = (IBaseDataConverter) super
				.getWebApplicationContext().getBean(serviceId);
		return converter.convertIDToCode(converterID);
	}

	/**
	 * 判断参数传入的组件名称的前两位是不是都为大写<br>
	 * 如果是则不做处理，如果不是，需要将首字母变为小写（Spring3应用）
	 * 
	 * @param prefix 组件名称前缀
	 * @return Boolean类型：true Or False
	 */
	private boolean hasTwoUppser(String prefix) {
		return prefix.toUpperCase().equals(prefix);
	}

	/**
	 * 跳转到展示报表弹出窗口界面 这个方法所有的打印都会调用它，然后转发的打印界面
	 * 
	 * @author qinchao
	 * @date 创建时间 2013-10-24
	 */
	@RequestMapping("/jasperReport/forwardReportPrint")
	public String forwardReportPrint(ModelMap map, HttpServletRequest request) throws Exception {
		return "pub/winReportPrint";
	}

}