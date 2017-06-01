package com.sense.app;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sense.app.util.AppJsonUtil;
import com.sense.frame.base.BaseController;

@Controller
@RequestMapping("/appConfig")
public class AppConfigController extends BaseController {

	/**
	 * 升级接口
	 */
	@RequestMapping("/queryVersion")
	@ResponseBody
	public Map<String, Object> queryVersion() {
		ResourceBundle resource = ResourceBundle.getBundle("app-version");
		String num = resource.getString("num");
		String descr = "";
		try {
			descr = new String(resource.getString("descr").getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		String insideurl = resource.getString("insideurl");
		String outsideurl = resource.getString("outsideurl");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("num", num);
		map.put("descr", descr);
		map.put("insideurl", insideurl);
		map.put("outsideurl", outsideurl);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("version", map);
		return AppJsonUtil.writeSucc("操作成功！", paraMap);
	}
	
}