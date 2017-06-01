package com.sense.sys.dic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sense.frame.common.util.ClassFilter;
import com.sense.frame.common.util.ClassUtils;
import com.sense.frame.common.util.EnumUtil;
import com.sense.sys.dic.service.DicService;

/**
 * 注入ServletContext，需要tomcat容器 为了不影响junit测试，把Component改为Controller，
 */
@Component
@Scope("singleton")
public class DicLoader {
	@Autowired
	private DicService dicService;

	@Autowired
	private ServletContext context;

	@Value("#{propertiesReader[ENUM_DIC_PKG]}")
	private String enumdicpkg;

	private static Log logger = LogFactory.getLog(DicLoader.class);

	/**
	 * 应用上下文——枚举类缓存JSON
	 */
	public static final String CONTEXT_ENUMCACHE = "cacheEnumData";
	
	/**
	 * 应用上下文——字典缓存MAP
	 */
	public static final String CONTEXT_DICCACHE = "cacheDicData";

	/**
	 * 启动加载实现方法
	 */
	@PostConstruct
	private void autoLoad() {
		try {
			loadDic();
		} catch (Exception e) {
			logger.error("系统启动加载字典出错，系统将立即关闭。", e);
			System.exit(0);
		}

	}

	/**
	 * 加载枚举类和字典
	 */
	public void loadDic() throws Exception {
		Map<String, Object> enumCacheData = loadEnumDic();
		context.setAttribute(CONTEXT_ENUMCACHE, JSON.toJSON(enumCacheData));
		
		Map<String, Object> dicCacheData = loadSysDic();
		context.setAttribute(CONTEXT_DICCACHE, dicCacheData);
	}

	private Map<String, Object> loadEnumDic() throws Exception {
		ClassFilter filter = new ClassFilter() {
			@Override
			public boolean accept(Class<?> clazz) {
				return true;
			}
		};

		Map<String, Object> cacheData = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(enumdicpkg)) {
			String[] ary_pkg = enumdicpkg.split(",");
			for (int i = 0; i < ary_pkg.length; i++) {
				if (StringUtils.isNotBlank(ary_pkg[i])) {
					
					// 扫描枚举类缓存到应用上下文:枚举类命名方式全部大写，并去掉前缀ENUM
					for (Class<?> clazz : ClassUtils.scanPackage(ary_pkg[i], filter)) {
						Class<?> c = Class.forName(clazz.getName());
						String dicCod = clazz.getSimpleName().toUpperCase();
						dicCod = dicCod.replaceFirst("ENUM", "");
						cacheData.put(dicCod, EnumUtil.getComboboxEnumValues(c));
					}
				}
			}
		}
		return cacheData;
	}

	private Map<String, Object> loadSysDic() throws Exception {
		Map<String, Object> cacheData = new HashMap<String, Object>();
		// 加载字典
		HashMap<String, List<HashMap<String, String>>> allDicMap = dicService.queryAllDicList();
		for (String dicCod : allDicMap.keySet()) {
			cacheData.put(dicCod.toUpperCase(), allDicMap.get(dicCod));
		}
		return cacheData;
	}
	
	/**
	 * 获取字典名称
	 */
	@SuppressWarnings("unchecked")
	public String getCacheDicName(String dicCod, String dicDtlCod) {
		if(StringUtils.isBlank(dicCod) || StringUtils.isBlank(dicDtlCod)){
			return "";
		}
		Map<String, Object> dicMap = (Map<String, Object>) context.getAttribute(CONTEXT_DICCACHE);
		if(!dicMap.containsKey(dicCod.toUpperCase())){
			return "";
		}
		List<Map<String, String>> dicList = (List<Map<String, String>>) dicMap.get(dicCod.toUpperCase());
		for(Map<String, String> map : dicList){
			if(dicDtlCod.equals(map.get("value"))){
				return map.get("text");
			}
		}
		return "";
	}
	
	/**
	 * 获取字典列表，JSONArray，[{'value':value,'text':text},{'value':value,'text':text}]
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getCacheDicList(String dicCod) {
		if(StringUtils.isBlank(dicCod)){
			return new JSONArray();
		}
		Map<String, Object> dicMap = (Map<String, Object>) context.getAttribute(CONTEXT_DICCACHE);
		if(!dicMap.containsKey(dicCod.toUpperCase())){
			return new JSONArray();
		}
		return JSONArray.parseArray(JSON.toJSONString(dicMap.get(dicCod.toUpperCase())));
	}

}