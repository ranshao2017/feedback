package com.sense.frame.common.util;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 排序操作工具类
 */
public class SortUtil {
	public static final int ASC = 1;
	public static final int DESC = 2;

	/**
	 * List排序
	 * 
	 * @param list
	 *            排序list
	 * @param fieldName
	 *            排序字段
	 * @param sortType
	 *            排序类型
	 * @return List<T>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> sortList(List<T> list, String fieldName,
			int sortType) {
		String[] fieldNames = { fieldName };
		int[] sortTypes = { sortType };

		Collections.sort(list, new SortComparator(fieldNames, sortTypes));
		return list;
	}

	/**
	 * List排序，按照多字段多类型
	 * 
	 * @param list
	 * @param fieldNameArray
	 * @param sortTypeArray
	 * @return List<T>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> sortList(List<T> list, String[] fieldNameArray,
			int[] sortTypeArray) {

		Collections.sort(list,
				new SortComparator(fieldNameArray, sortTypeArray));
		return list;
	}
}

/**
 * 内部类,比较器
 */
class SortComparator<T> implements Comparator<T> {
	String[] sortFieldNames;
	int[] sortTypes;

	SortComparator(String[] sortFieldNames, int[] sortTypes) {
		this.sortFieldNames = sortFieldNames;
		this.sortTypes = sortTypes;
	}

	/**
	 * 实现两个泛型对象的比较
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int compare(T t1, T t2) {
		Object value1, value2;
		Comparable comp1, comp2;
		Field field;
		int sortType, compareResult;
		boolean accessible;
		String sortFieldName;
		try {
			for (int i = 0, len = sortFieldNames.length; i < len; i++) {
				sortFieldName = sortFieldNames[i];

				field = t1.getClass().getDeclaredField(sortFieldName);

				sortType = sortTypes[i];

				accessible = field.isAccessible();
				field.setAccessible(true);
				value1 = field.get(t1);
				value2 = field.get(t2);
				field.setAccessible(accessible);

				if ((value1 != null) && (!(value1 instanceof Comparable))) {
					throw new Exception("比较的属性需实现Comparable接口！");
				}
				if ((value2 != null) && (!(value2 instanceof Comparable))) {
					throw new Exception("比较的属性需实现Comparable接口！");
				}

				comp1 = (Comparable) value1;
				comp2 = (Comparable) value2;

				if (sortType == SortUtil.ASC) {
					if (value1 == null) {
						if (value2 != null) {
							return -1;
						}
					} else {
						if (value2 == null) {
							return 1;
						}

						compareResult = comp1.compareTo(comp2);
						if (compareResult != 0) {
							return compareResult;
						}
					}
				} else {// 降序排列
					if (value1 == null) {
						if (value2 != null) {
							return 1;
						}
					} else {
						if (value2 == null) {
							return -1;
						}

						compareResult = comp2.compareTo(comp1);
						if (compareResult != 0) {
							return compareResult;
						}
					}
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return 0;
	}
}