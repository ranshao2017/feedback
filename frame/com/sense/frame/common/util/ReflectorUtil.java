package com.sense.frame.common.util;

/**
 * 反射工具类
 * @author qinchao
 */
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectorUtil {
	private Map attrNamesMap = new HashMap();

	/**
	 * 默认构造函数
	 * 
	 * @since V1.0
	 */
	public ReflectorUtil() {

	}

	/**
	 * 构造函数
	 * 
	 * @param invokeObj
	 *            需要反射的对象
	 */
	public ReflectorUtil(Object invokeObj) {
		if (attrNamesMap == null || attrNamesMap.size() == 0) {
			attrNamesMap = getFieldNames(invokeObj);
		}
	}

	/**
	 * 获取类方法返回值
	 * 
	 * @param invokeObj
	 * @param attr
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object getInvoke(Object invokeObj, String attr) throws Exception {
		int indexOfDot = attr.indexOf(".");
		if (indexOfDot >= 0) {
			try {
				String attrTmp = attr.substring(0, indexOfDot);
				Object tmpRe = getInvoke(invokeObj, attrTmp);
				String attrTmpNext = attr.substring(indexOfDot + 1);
				return getInvoke(tmpRe, attrTmpNext);
			} catch (Exception e) {
				throw new Exception("获取属性：" + attr + "失败!" + e.getMessage());
			}
		}

		String methodName = getBeanMethodName("get", attr);
		try {
			Method method = invokeObj.getClass().getMethod(methodName,
					new Class[0]);
			method.setAccessible(true);
			return method.invoke(invokeObj, new Object[0]);
		} catch (Exception ex) {
			try {
				if (attrNamesMap == null || attrNamesMap.size() == 0) {
					attrNamesMap = getFieldNames(invokeObj);
				}

				if (attrNamesMap.containsKey(attr.toUpperCase())) {
					Field f = (Field) attrNamesMap.get(attr.toUpperCase());

					f.setAccessible(true);
					return f.get(invokeObj);
				}
				if (((invokeObj instanceof Map))
						&& (((Map) invokeObj).containsKey(attr))) {
					return ((Map) invokeObj).get(attr);
				}
				throw new Exception("未找到对应名字的属性：" + attr);
			} catch (Exception exx) {
				throw new Exception("获取属性：" + attr + "失败!" + exx.getMessage());
			}
		}

	}

	/**
	 * 获得字段的名称
	 * 
	 * @param invokeObj
	 * @return
	 */
	private Map<String, Field> getFieldNames(Object invokeObj) {

		Class classOfObj = invokeObj.getClass();
		Field[] fields = (Field[]) null;
		while (classOfObj != null) {
			fields = classOfObj.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (!fields[i].getName().toUpperCase().equals("THIS$0"))
					attrNamesMap.put(fields[i].getName().toUpperCase(),
							fields[i]);
			}
			classOfObj = classOfObj.getSuperclass();
		}
		return attrNamesMap;
	}

	/**
	 * 获取字段的名称
	 * 
	 * @param classOfObj
	 * @return
	 */
	private Map<String, Field> getFieldNames(Class classOfObj) {
		Map fieldNamesMap = new HashMap();
		Field[] fields = (Field[]) null;
		while (classOfObj != null) {
			fields = classOfObj.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (!fields[i].getName().toUpperCase().equals("THIS$0"))
					fieldNamesMap.put(fields[i].getName().toUpperCase(),
							fields[i]);
			}
			classOfObj = classOfObj.getSuperclass();
		}
		return fieldNamesMap;
	}

	/**
	 * 设置对象属性值
	 * 
	 * @param invokeObj
	 * @param attr
	 * @param setValue
	 * @param ignoreNoneField
	 *            是否在找不到对应的属性时 忽略
	 * @throws Exception
	 */
	public void setInvoke(Object invokeObj, String attr, Object setValue,
			boolean ignoreNoneField) throws Exception {

		try {
			if (attrNamesMap == null || attrNamesMap.size() == 0) {
				attrNamesMap = getFieldNames(invokeObj);
			}
			if (attrNamesMap.containsKey(attr.toUpperCase())) {
				Field f = (Field) attrNamesMap.get(attr.toUpperCase());

				f.setAccessible(true);
				try {
					f.set(invokeObj, setValue);
				} catch (Exception e) {
					throw new Exception("设置属性：" + attr + "失败!" + e.getMessage());
				}
			} else {
				if (!ignoreNoneField) {
					throw new Exception("未找到对应名字的属性：" + attr);
				}

			}
		} catch (Exception e3) {
			throw new Exception("设置属性：" + attr + "失败!" + e3.getMessage());
		}

	}

	/**
	 * 批量设置对象属性值
	 * 
	 * @param invokeObj
	 * @param attr
	 * @param setValue
	 * @param ignoreNoneField
	 *            是否在找不到对应的属性时 忽略
	 * @throws Exception
	 */
	public void setInvokeBatch(Object invokeObj, List<String> attrList,
			List<Object> valueList, boolean ignoreNoneField) throws Exception {
		if (attrList == null || attrList.size() <= 0) {
			// 没有需要设置的属性，返回
			return;
		}

		if (valueList == null || attrList.size() != valueList.size()) {
			throw new Exception("参数错误：属性数量与属性值的数量不相等！");
		}

		try {
			String attr;
			Object setValue;
			for (int i = 0; i < attrList.size(); i++) {
				attr = attrList.get(i);
				setValue = valueList.get(i);

				if (attrNamesMap == null || attrNamesMap.size() == 0) {
					attrNamesMap = getFieldNames(invokeObj);
				}
				if (attrNamesMap.containsKey(attr.toUpperCase())) {
					Field f = (Field) attrNamesMap.get(attr.toUpperCase());

					f.setAccessible(true);
					try {
						f.set(invokeObj, setValue);
					} catch (Exception e) {
						throw new Exception("设置属性：" + attr + "失败!"
								+ e.getMessage());
					}
				} else {
					if (!ignoreNoneField) {
						throw new Exception("未找到对应名字的属性：" + attr);
					}

				}
			}

		} catch (Exception e3) {
			throw new Exception("批量设置属性失败：" + e3.getMessage());
		}

	}

	/**
	 * 获得类字段的类型
	 * 
	 * @param invokeObj
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public Class getFieldType(Object invokeObj, String fieldName)
			throws Exception {
		return getFieldType(invokeObj.getClass(), fieldName, invokeObj);
	}

	/**
	 * 获得类字段的类型
	 * 
	 * @param clax
	 * @param fieldName
	 * @param invokeObj
	 * @return
	 * @throws Exception
	 */
	public Class getFieldType(Class clax, String fieldName, Object invokeObj)
			throws Exception {
		int indexOfDot = fieldName.indexOf(".");
		if (indexOfDot >= 0) {
			String attrTmp = fieldName.substring(0, indexOfDot);
			Object objNext = null;
			try {
				if (invokeObj != null)
					objNext = getInvoke(invokeObj, attrTmp);
			} catch (Exception e) {
				objNext = null;
			}
			Class tmpRe = getFieldType(clax, attrTmp, invokeObj);
			String attrTmpNext = fieldName.substring(indexOfDot + 1);
			return getFieldType(tmpRe, attrTmpNext, objNext);
		}

		Map attrNamesMap = getFieldNames(clax);
		if (attrNamesMap.containsKey(fieldName.toUpperCase())) {
			Field f = (Field) attrNamesMap.get(fieldName.toUpperCase());
			return f.getType();
		}

		if (invokeObj == null) {
			throw new Exception("获取" + fieldName + "类型失败!");
		}
		if (((invokeObj instanceof Map))
				&& (((Map) invokeObj).containsKey(fieldName))) {
			return ((Map) invokeObj).get(fieldName).getClass();
		}
		throw new Exception("获取" + fieldName + "类型失败!");
	}

	/**
	 * 格式化类字段值
	 * 
	 * @param value
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Object formatValue(Object value, Class type) throws Exception {
		boolean candoType = false;
		boolean candoValue = false;

		if (type.isPrimitive())
			candoType = true;
		else if (type.equals(String.class)) {
			candoType = true;
		} else if ((type.equals(Integer.class)) || (type.equals(Short.class))
				|| (type.equals(Long.class)) || (type.equals(Double.class))
				|| (type.equals(Float.class)) || (type.equals(Boolean.class))
				|| (type.equals(Byte.class)) || (type.equals(Character.class))) {
			candoType = true;
		}

		if (value.getClass().isPrimitive())
			candoValue = true;
		else if (value.getClass().equals(String.class)) {
			candoValue = true;
		} else if (getPrimitiveClass(value) != null) {
			candoValue = true;
		}

		if ((candoValue) && (candoType)) {
			Class tmpType = type;
			if (type.isPrimitive()) {
				tmpType = getWrapperClass(type);
			}

			if ((getPrimitiveClass(type) != null)
					&& (value.getClass().equals(String.class))
					&& (value.toString().trim().length() == 0)) {
				return null;
			}
			if (value.getClass().equals(tmpType)) {
				return value;
			}
			if (type.equals(String.class)) {
				return value.toString();
			}
			if ((value.getClass().equals(String.class))
					&& ((type.equals(Character.TYPE)) || (type
							.equals(Character.class)))) {
				if (value.toString().length() == 1) {
					return Character.valueOf(value.toString().charAt(0));
				}
				throw new Exception("不支持此类型的转换：" + value.getClass().getName()
						+ "->" + type.getName());
			}

			Method valueOfMethod = tmpType.getDeclaredMethod("valueOf",
					new Class[] { value.getClass() });
			return valueOfMethod.invoke(tmpType,
					new Object[] { value.toString() });
		}

		throw new Exception("不支持此类型的转换：" + value.getClass().getName() + "->"
				+ type.getName());
	}

	/**
	 * 根据属性名获得类方法名
	 * 
	 * @param prefix
	 * @param attr
	 * @return
	 */
	private String getBeanMethodName(String prefix, String attr) {
		String first = attr.substring(0, 1).toUpperCase();
		String rest = attr.substring(1);
		return prefix + first + rest;
	}

	/**
	 * 获得类类型
	 * 
	 * @param obj
	 * @return
	 */
	private Class getPrimitiveClass(Object obj) {
		if ((obj instanceof Integer))
			return Integer.TYPE;
		if ((obj instanceof Short))
			return Short.TYPE;
		if ((obj instanceof Long))
			return Long.TYPE;
		if ((obj instanceof Double))
			return Double.TYPE;
		if ((obj instanceof Float))
			return Float.class;
		if ((obj instanceof Boolean))
			return Boolean.TYPE;
		if ((obj instanceof Byte))
			return Byte.TYPE;
		if ((obj instanceof Character)) {
			return Character.TYPE;
		}
		return null;
	}

	private Class getPrimitiveClass(Class clax) {
		if (clax.equals(Integer.class))
			return Integer.TYPE;
		if (clax.equals(Short.class))
			return Short.TYPE;
		if (clax.equals(Long.class))
			return Long.TYPE;
		if (clax.equals(Double.class))
			return Double.TYPE;
		if (clax.equals(Float.class))
			return Float.class;
		if (clax.equals(Boolean.class))
			return Boolean.TYPE;
		if (clax.equals(Byte.class))
			return Byte.TYPE;
		if (clax.equals(Character.class)) {
			return Character.TYPE;
		}
		return null;
	}

	private Class getWrapperClass(Class clax) {
		if (clax.equals(Integer.TYPE))
			return Integer.class;
		if (clax.equals(Short.TYPE))
			return Short.class;
		if (clax.equals(Long.TYPE))
			return Long.class;
		if (clax.equals(Double.TYPE))
			return Double.class;
		if (clax.equals(Float.TYPE))
			return Float.class;
		if (clax.equals(Byte.TYPE))
			return Byte.class;
		if (clax.equals(Character.TYPE))
			return Character.class;
		if (clax.equals(Boolean.TYPE)) {
			return Boolean.class;
		}
		return null;
	}
}
