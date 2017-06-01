package com.sense.frame.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * enum类型字典
 */
public class EnumUtil {

	/**
	 * 遍历某个enum类型，并装入hashmap
	 * 
	 * @date 创建时间 2014-9-29
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap<String, Object> getEnumValues(Class c)
			throws Exception {

		HashMap<String, Object> rtnMap = null;
		if (c.isEnum()) {
			rtnMap = new HashMap<String, Object>();
			Object[] objs = c.getEnumConstants();

			for (Object obj : objs) {

				Method m = obj.getClass().getDeclaredMethod("values", null);

				Object[] result = (Object[]) m.invoke(obj, null);


				List list = Arrays.asList(result);

				Iterator it = list.iterator();

				while (it.hasNext()) {

					Object objOne = it.next();

					Field code = objOne.getClass().getDeclaredField("code");

					Field codeDesc = objOne.getClass()
							.getDeclaredField("label");

					Field priority = null;

					try {

						priority = objOne.getClass().getDeclaredField(
								"priority");

					} catch (Exception e) {

						priority = null;

					}

					code.setAccessible(true);

					codeDesc.setAccessible(true);

					if (priority != null) {

						priority.setAccessible(true);

					}

					rtnMap.put((String) code.get(objOne), codeDesc.get(objOne));

				}

				break;

			}

		}

		return rtnMap;
	}

	/**
	 * 查询enum字典，用于combobox，不包括空白ID
	 */
	public static List<HashMap<String, String>> getComboboxEnumValues(Class c) throws Exception {
		return getComboboxEnumValues(c, false, "");
	}

	/**
	 * 查询enum字典，用于combobox，包括“全部”
	 */
	private static List<HashMap<String, String>> getComboboxEnumValues(Class c, boolean withRoot, String rootName) throws Exception {
		List<HashMap<String, String>> rtnList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> valueTextMap = null;
		if (withRoot) {
			valueTextMap = new HashMap<String, String>();
			valueTextMap.put("value", "");
			valueTextMap.put("text", rootName);
			rtnList.add(valueTextMap);
		}

		if (c.isEnum()) {
			Object[] objs = c.getEnumConstants();
			int first = 0;
			for (Object obj : objs) {
				Method m = obj.getClass().getDeclaredMethod("values", null);
				Object[] result = (Object[]) m.invoke(obj, null);
				List list = Arrays.asList(result);
				Iterator it = list.iterator();
				while (it.hasNext()) {
					Object objOne = it.next();
					Field code = objOne.getClass().getDeclaredField("code");
					Field codeDesc = objOne.getClass().getDeclaredField("label");
					Field priority = null;
					try {
						priority = objOne.getClass().getDeclaredField("priority");
					} catch (Exception e) {
						priority = null;
					}
					code.setAccessible(true);
					codeDesc.setAccessible(true);
					if (priority != null) {
						priority.setAccessible(true);
					}
					valueTextMap = new HashMap<String, String>();
					valueTextMap.put("value", String.valueOf(code.get(objOne)));
					valueTextMap.put("text", (String) codeDesc.get(objOne));
					rtnList.add(valueTextMap);
				}
				break;
			}
		}
		return rtnList;
	}

}