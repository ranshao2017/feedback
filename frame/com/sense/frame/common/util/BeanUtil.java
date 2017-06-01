package com.sense.frame.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean类操作工具类
 */
public class BeanUtil {

	/**
	 * 实现Bean的list整体克隆复制
	 * 
	 * @param list
	 * @return List<T>类型
	 * @throws FrameException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Cloneable> List<T> cloneList(List<T> list)
			throws Exception {
		if (list == null) {
			return null;
		}
		Class<List<T>> listClass = (Class<List<T>>) list.getClass();
		List<T> copyList = listClass.newInstance();
		for (T obj : list) {
			copyList.add(cloneBean(obj));
		}
		return copyList;
	}

	/**
	 * 实现javabean的克隆复制
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Cloneable> T cloneBean(T obj) throws Exception {
		if (obj == null) {
			return null;
		}
		Method cloneMethod = obj.getClass().getMethod("clone");
		return (T) cloneMethod.invoke(obj);
	}

	/**
	 * 从hashmap中获得javabean
	 */
	public static <T> T getBeanFromMap(Map<String, Object> map, Class<T> pclass) throws Exception {
		T bean = pclass.newInstance();
		BeanInfo beanInfo = Introspector.getBeanInfo(pclass);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			if (key.equals("class")) {
				continue;
			}
			// 支持大小写 ，因此不用containsKey,牺牲效率
			for (String name : map.keySet()) {
				if (name == null) {
					continue;
				}
				if (key.equalsIgnoreCase(name)) {
					Object value = map.get(name);
					// 对于value的class同实体的class不一致时，在这里进行Class转换
					// 解决date转成了Timestamp，使用时出错的BUG
					Class<?> beanClass = property.getPropertyType();
					if (value != null) {
						Class<?> mapClass = value.getClass();
						if ((beanClass == java.util.Date.class || beanClass == java.sql.Date.class)
								&& mapClass == java.sql.Timestamp.class) {
							// 时间类型的转换
							value = new java.util.Date(Timestamp.valueOf(
									value.toString()).getTime());
						} else if (beanClass == java.sql.Timestamp.class
								&& mapClass != beanClass) {
							// Timestamp转换
							value = Timestamp.valueOf(value.toString());
						} else if (beanClass == java.lang.Long.class
								&& mapClass != beanClass) {
							// Long
							value = Long.valueOf(value.toString());
						} else if ((beanClass == int.class || beanClass == java.lang.Integer.class)
								&& mapClass != beanClass) {
							// int
							value = Integer.parseInt(value.toString());
						} else if (beanClass == byte[].class
								&& mapClass != beanClass) {
							// blob
							byte[] tmpByteArray = null;
							try {
								tmpByteArray = blobToBytes((java.sql.Blob) value);
							} catch (Exception e) {

							}
							value = tmpByteArray;
						} else if (beanClass == java.lang.String.class
								&& mapClass != beanClass) {
							// clob
							value = clobToString((java.sql.Clob) value);
						}
					}

					// 得到property对应的setter方法
					Method setter = property.getWriteMethod();
					setter.invoke(bean, value);
				}
			}
		}
		return bean;
	}

	/**
	 * 将某个Bean对象转换成HashMap
	 * 
	 * @author xfh
	 * @date 创建时间 2013-8-27
	 * @since V1.0
	 */
	public static Map<String, Object> getMapFromBean(Object bean) throws Exception {
		if (bean == null) {
			return null;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			// 过滤class属性
			if (!key.equals("class")) {
				// 得到property对应的getter方法
				Method getter = property.getReadMethod();
				Object value = getter.invoke(bean);
				resultMap.put(key, value);
			}
		}
		return resultMap;
	}

	/**
	 * 将List<T> 转换成List<HashMap<String, Object>>
	 * 
	 * @author xfh
	 * @date 创建时间 2013-8-22
	 * @since V1.0
	 */
	public static <T> List<Map<String, Object>> getListMapFromListBean(
			List<T> listBean) throws Exception {
		if (listBean == null) {
			return null;
		}
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>(listBean.size());
		for (T bean : listBean) {
			listMap.add(getMapFromBean(bean));
		}
		return listMap;
	}

	/**
	 * 从hashmap中获得javabean的list
	 */
	public static <T> List<T> getBeanListFromMap(List<Map<String, Object>> mapList, Class<T> pclass)
			throws Exception {
		if (pclass == null) {
			throw new Exception("传入参数[pclass]为空！");
		}
		if (mapList == null) {
			return null;
		}
		List<T> list = new ArrayList<T>(mapList.size());
		for (Map<String, Object> map : mapList) {
			list.add(getBeanFromMap(map, pclass));
		}
		return list;
	}

	/**
	 * 将blob转化为byte[],可以转化二进制流的
	 * 
	 * @param blob
	 * @return
	 */
	private static byte[] blobToBytes(java.sql.Blob blob) {
		InputStream is = null;
		byte[] b = null;
		try {
			is = blob.getBinaryStream();
			b = new byte[(int) blob.length()];
			is.read(b);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return b;
	}

	/**
	 * 将blob转化为byte[],可以转化二进制流的
	 * 
	 * @param blob
	 * @return
	 */
	public static String clobToString(java.sql.Clob clob) {
		String value = null;
		Reader inStream = null;
		try {
			inStream = clob.getCharacterStream();
			char[] c = new char[(int) clob.length()];
			inStream.read(c);
			value = new String(c);
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
				inStream = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
}