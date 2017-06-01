package com.sense.frame.common.util;

/**
 * 数组解析工具类
 */
public class ArrayUtil {

	/**
	 * 获取传入对象所对应的数组下标（泛型，通用）
	 * @param array
	 * @param obj
	 * @return int
	 * @throws BusinessException
	 */
	public static <T> int find(T[] array, T obj) throws Exception {
		int find = -1;
		T element;

		if (array == null) {
			throw new Exception("传入的参数[array]为空！");
		}

		for (int i = 0; i < array.length; i++) {
			element = array[i];

			if (obj == null) {
				if (element == obj) {
					return i;
				}
			} else {
				if (obj.equals(element)) {
					return i;
				}
			}
		}
		return find;
	}

	/**
	 * 获取传入String对象所对应的String数组下标
	 * @param strArray
	 * @param str
	 * @return int型
	 * @throws BusinessException
	 */
	public static int findStringIgnoreCase(String[] strArray, String str) throws Exception {

		int find = -1;
		String element;

		if (strArray == null) {
			throw new Exception("传入的参数[strArray]为空！");
		}

		for (int i = 0; i < strArray.length; i++) {
			element = strArray[i];

			if (str == null) {
				if (element == str) {
					return i;
				}
			} else {
				if (str.equalsIgnoreCase(element)) {
					return i;
				}
			}
		}
		return find;
	}
}