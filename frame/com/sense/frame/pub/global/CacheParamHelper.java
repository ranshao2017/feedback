package com.sense.frame.pub.global;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CacheParamHelper {

	public final static String SESSION_CACHE_PARAM_MAP="cacheparam_map" ;
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getCacheParam(HttpServletRequest request,String key)
    {
		Map<String, String> rtn=null;
        HttpSession session = request.getSession(true);
        
		Map<String,Map<String, String>> cacheMap=(Map<String,Map<String, String>>)session.getAttribute(SESSION_CACHE_PARAM_MAP);
    	
    	if(cacheMap.keySet().contains(key)){
    		rtn= cacheMap.get(key);
    		cacheMap.remove(key); // remove the token so it won't be used again
   	    }
      // session.setAttribute(SESSION_CACHE_PARAM_MAP, cacheMap);
        return rtn;
    }
}
