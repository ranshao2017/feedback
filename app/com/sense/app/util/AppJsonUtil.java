package com.sense.app.util;

import java.util.HashMap;
import java.util.Map;

public class AppJsonUtil {
	
	public static Map<String, Object> writeSucc(String succMsg){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("status", 0);
		returnMap.put("msg", succMsg);
		return returnMap;
	}
	
	public static Map<String, Object> writeSucc(String succMsg, Map<String, Object> paraMap){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("status", 0);
		returnMap.put("msg", succMsg);
		returnMap.putAll(paraMap);
		return returnMap;
	}
	
	public static Map<String, Object> writeErr(String errMsg){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("status", 1);
		returnMap.put("msg", errMsg);
		return returnMap;
	}
	
}