package com.sense.frame.common.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.sense.frame.pub.model.TreeModel;

public class ListUtil {
	
	/**
	 * 获取第一个数据
	 */
	public static <T> T  getFirst(List<T> a) {
		if (a != null && a.size()>0) {
			return a.get(0);
		}
		return null;
	}

	/**
	 * 合并两个list
	 */
	public static <T> List<T> mergeList(List<T> a, List<T> b) {
		if (a == null) {
			return b;
		}
		if (b == null) {
			return a;
		}
		a.addAll(b);
		return a;
	}

	/**
	 * 将list的元素补全到指定的数量totalSize
	 */
	public static <T> List<T> fillList(List<T> list, Class<T> pclass,
			int totalSize) throws Exception {
		int size;
		if (totalSize < 1) {
			return list;
		}

		if (list == null) {
			list = new ArrayList<T>();
		}
		size = list.size();
		if (totalSize <= size) {
			return list;
		}

		for (int i = 0, count = totalSize - size; i < count; i++) {
			list.add(pclass.newInstance());
		}
		return list;
	}

	/**
	 * 获取list中某一行元素中的某一列的数据
	 */
	public static Object getObject(List<HashMap<String, Object>> list, int row,
			String columnname) throws Exception {
		HashMap<String, Object> map = getRow(list, row);
		// column考虑大小写的情况，大小写都认，规范上不允许出现大小写两个列同时存在
		String columnName_Upper, columnName_Lower, keyName;
		keyName = columnname;
		if (!map.containsKey(keyName)) {
			columnName_Upper = columnname.toUpperCase();
			columnName_Lower = columnname.toLowerCase();
			if (map.containsKey(columnName_Upper)
					&& map.containsKey(columnName_Lower)) {
				throw new Exception("获取list元素值时出错，第["
						+ String.valueOf(row) + "]行数据中存在大小写重复的列[" + columnname
						+ "]!");
			}
			if (map.containsKey(columnName_Lower)) {
				keyName = columnName_Lower;
			} else if (map.containsKey(columnName_Upper)) {
				keyName = columnName_Upper;
			} else {
				throw new Exception("获取list元素值时出错，第["
						+ String.valueOf(row) + "]行数据中没有[" + columnname + "]列!");
			}
		}

		return map.get(keyName);
	}

	/**
	 * 检查list中是否存在某一行元素
	 */
	private static void checkRow(List<HashMap<String, Object>> list, int row)
			throws Exception {
		if (row < 0 || row >= list.size()) {
			throw new Exception("传入的行号[" + String.valueOf(row)
					+ "]错误，当前list共有[" + String.valueOf(list.size()) + "]行!");
		}
	}

	/**
	 * 获取list中一行元素的值，若行号错误则报错
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> getRow(
			List<HashMap<String, Object>> list, int row)
			throws Exception {
		checkRow(list, row);
		Object o = list.get(row);
		if (o instanceof HashMap) {
			return (HashMap<String, Object>) o;
		}
		return null;
	}

	/**
	 * 从list中的起止行号内查找满足某个条件的元素，返回第一个符合条件的元素序号
	 * @author xfh 20130816
	 * @since 1.0
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int find(List<HashMap<String, Object>> list,
			String condition, int pBeginRow, int pEndRow) throws Exception{

		String objectStr;

		if (list == null) {
			return -1;
		}
		if (list.size() == 0) {
			return -1;
		}
		if (pEndRow > list.size()) {
			pEndRow = list.size();
		}
		Condition conditions[] = getConditions(condition);
		Comparator comparator = new HTComparator();

		for (int i = pBeginRow; i < pEndRow; i++) {
			for (int k = 0; k < conditions.length; k++) {
				objectStr = "";
				Object o = getObject(list, i, conditions[k].columnName);
				// 不同类型的都转换成String再比较

				if (o instanceof String) {
					// String类型比较
					objectStr = o.toString();
				} else if (o instanceof java.util.Date) {
					// java.util.Date类型比较
					objectStr = DateUtil.dateToString((java.util.Date) o, "yyyyMMdd");
				} else if (o instanceof BigDecimal) {
					// BigDecimal类型比较
					objectStr = String.valueOf(((BigDecimal) o).setScale(10,
							BigDecimal.ROUND_HALF_UP));
				} else if (o instanceof Integer) {
					// Integer类型比较
					objectStr = String.valueOf((Integer) o);
				} else if (o instanceof Double) {
					// Double类型比较
					objectStr = MathUtil.formateNumber((Double) o, 10);
				} else if (o instanceof Boolean) {
					// Boolean类型比较
					objectStr = String.valueOf((Boolean) o);
				} else if (o instanceof Long) {
					// Long类型比较
					objectStr = String.valueOf((Long) o);
				} else {
					objectStr = o.toString();
				}

				if ((!"==".equals(conditions[k].operator) || comparator
						.compare(objectStr, conditions[k].value) != 0)
						&& (!"!=".equals(conditions[k].operator) || comparator
								.compare(objectStr, conditions[k].value) == 0)
						&& (!">".equals(conditions[k].operator) || comparator
								.compare(objectStr, conditions[k].value) <= 0)
						&& (!"<".equals(conditions[k].operator) || comparator
								.compare(objectStr, conditions[k].value) >= 0)
						&& (!">=".equals(conditions[k].operator) || comparator
								.compare(objectStr, conditions[k].value) < 0)
						&& (!"<=".equals(conditions[k].operator) || comparator
								.compare(objectStr, conditions[k].value) > 0)) {
					break;
				}

				if (k == conditions.length - 1) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 从list中所有元素中查找满足某个条件的元素，返回第一个符合条件的元素序号
	 * 
	 * @author xfh 20130816
	 * @since 1.0
	 */
	public static int find(List<HashMap<String, Object>> list, String condition)
			throws Exception {
		return find(list, condition, 0, list.size());
	}

	/**
	 * 从list中的起始行后查找满足某个条件的元素，返回第一个符合条件的元素序号
	 */
	public static int find(List<HashMap<String, Object>> list,
			String condition, int pBeginRow) throws Exception {
		return find(list, condition, pBeginRow, list.size());
	}

	/**
	 * 从list中查找满足某个条件的子集
	 * 
	 * @author xfh 20130816
	 * @since 1.0
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<HashMap<String, Object>> findAll(
			List<HashMap<String, Object>> list, String condition)
			throws Exception {
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		String objectStr;

		if (list == null) {
			return result;
		}
		if (list.size() == 0) {
			return result;
		}
		Condition conditions[] = getConditions(condition);
		Comparator comparator = new HTComparator();

		for (int i = 0; i < list.size(); i++) {
			for (int k = 0; k < conditions.length; k++) {
				objectStr = "";
				Object o = getObject(list, i, conditions[k].columnName);
				// 不同类型的都转换成String再比较

				if (o instanceof String) {
					// String类型比较
					objectStr = o.toString();
				} else if (o instanceof java.util.Date) {
					// java.util.Date类型比较
					objectStr = DateUtil.dateToString((java.util.Date) o,
							"yyyyMMdd");
				} else if (o instanceof BigDecimal) {
					// BigDecimal类型比较
					objectStr = String.valueOf(((BigDecimal) o).setScale(10,
							BigDecimal.ROUND_HALF_UP));
				} else if (o instanceof Integer) {
					// Integer类型比较
					objectStr = String.valueOf((Integer) o);
				} else if (o instanceof Double) {
					// Double类型比较
					objectStr = MathUtil.formateNumber((Double) o, 10);
				} else if (o instanceof Boolean) {
					// Boolean类型比较
					objectStr = String.valueOf((Boolean) o);
				} else if (o instanceof Long) {
					// Long类型比较
					objectStr = String.valueOf((Long) o);
				} else {
					objectStr = o.toString();
				}

				if ((!"==".equals(conditions[k].operator) || comparator
						.compare(objectStr, conditions[k].value) != 0)
						&& (!"!=".equals(conditions[k].operator) || comparator
								.compare(objectStr, conditions[k].value) == 0)
						&& (!">".equals(conditions[k].operator) || comparator
								.compare(objectStr, conditions[k].value) <= 0)
						&& (!"<".equals(conditions[k].operator) || comparator
								.compare(objectStr, conditions[k].value) >= 0)
						&& (!">=".equals(conditions[k].operator) || comparator
								.compare(objectStr, conditions[k].value) < 0)
						&& (!"<=".equals(conditions[k].operator) || comparator
								.compare(objectStr, conditions[k].value) > 0)) {
					break;
				}

				if (k == conditions.length - 1) {
					result.add(getRow(list, i));
				}
			}
		}
		return result;
	}

	/**
	 * list检索条件表达式
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Condition[] getConditions(String conditions) throws Exception {
		conditions = (new StringBuilder()).append(conditions).append(" and ").toString();
		ArrayList resultList = new ArrayList();
		do {
			int pos = conditions.indexOf("and");
			if (pos >= 0) {
				String subCondition = conditions.substring(0, pos).trim();
				int posBlank = subCondition.indexOf(" ");
				if (posBlank < 0) {
					throw new Exception("List查找时检索条件表达式不合法!");
				}
				String columnName = subCondition.substring(0, posBlank).trim();
				subCondition = subCondition.substring(posBlank + 1);
				posBlank = subCondition.indexOf(" ");
				if (posBlank < 0) {
					throw new Exception("List查找时检索条件表达式不合法!");
				}

				String operator = subCondition.substring(0, posBlank).trim();
				String value = subCondition.substring(posBlank + 1).trim();
				Condition condition = new Condition();
				condition.setColumnName(columnName);
				condition.setOperator(operator);
				condition.setValue(value);
				resultList.add(condition);
				conditions = conditions.substring(pos + 3);
			} else {
				Condition[] result = new Condition[resultList.size()];
				resultList.toArray(result);
				return result;
			}
		} while (true);
	}

	/**
	 * 
	 * 方法简介.该方法主要是根据page、rows对list进行截取
	 * <p>
	 * 方法详述
	 * </p>
	 * 
	 * @param 关键字
	 *            说明
	 * @return 关键字 说明
	 * @throws 异常说明
	 *             发生条件
	 * @author rbn
	 * @date 创建时间 2013-8-26
	 * @since V1.0
	 */
	public static <T> List<T> getListByNumber(List<T> sList, int page, int rows)
			throws Exception {
		List<T> targetList = new ArrayList<T>();
		int targetRow = page * rows;
		int listSize = sList.size();
		if (targetRow > listSize) {
			targetRow = listSize;
		}
		for (int i = (page - 1) * rows; i < targetRow; i++) {
			targetList.add(sList.get(i));
		}
		return targetList;
	}

	/**
	 * 
	 * 方法简介. 查找treeModels是否存在findID，如果存在返回一个包含findID的TreeModel集合
	 * <p>
	 * 方法详述
	 * </p>
	 * 
	 * @param 关键字
	 *            说明
	 * @return 关键字 说明
	 * @throws 异常说明
	 *             发生条件
	 * @author rbn
	 * @date 创建时间 2013-9-4
	 * @since V1.0
	 */
	public static List<TreeModel> findTreeModel(List<TreeModel> treeModels,
			String findID) throws Exception {
		List<TreeModel> treeModeList = new ArrayList<TreeModel>();
		for (TreeModel treeModel : treeModels) {
			if (treeModel.getAttributes().containsValue(findID)) {
				treeModeList.add(treeModel);
			}
			;
		}
		return treeModeList;
	}

	/**
	 * 
	 * 移除某一列相等的重复记录
	 * <p>
	 * 根据指定的key，将list中key列数值一致的记录去重
	 * </p>
	 * 
	 * @param List
	 *            <HashMap<String,Object>>，String
	 * @return List<HashMap<String,Object>>
	 * @throws Exception
	 * @author xfh
	 * @date 创建时间 2013-9-5
	 * @since V1.0
	 */
	public static List<HashMap<String, Object>> removeDuplicateRow(
			List<HashMap<String, Object>> pList, String key) throws Exception {
		String[] keyArray = { key };
		return removeDuplicateRow(pList, keyArray);
	}

	/**
	 * 
	 * 移除指定列值相等的重复记录
	 * <p>
	 * 根据指定的keyArray，将list中keyarray中指定列数值一致的记录去重
	 * </p>
	 * 
	 * @param List
	 *            <HashMap<String,Object>>，String[]
	 * @return List<HashMap<String,Object>>
	 * @throws Exception
	 * @author xfh
	 * @date 创建时间 2013-9-5
	 * @since V1.0
	 */
	public static List<HashMap<String, Object>> removeDuplicateRow(
			List<HashMap<String, Object>> pList, String[] keyArray)
			throws Exception {
		String findCondition, columnName, columnValue;
		HashMap<String, Object> oneRowMap;
		Object columnObject;
		int find, arraylength;

		arraylength = keyArray.length;
		for (int i = 0; i < pList.size(); i++) {
			oneRowMap = pList.get(i);
			// 组织检索条件
			findCondition = "";
			for (int j = 0; j < arraylength; j++) {
				columnName = keyArray[j];

				if (!oneRowMap.containsKey(columnName)) {
					throw new Exception(
							"RemoveDuplicateRow获取list元素值时出错，第["
									+ String.valueOf(i) + "]行数据不存在列["
									+ columnName + "]!");
				}
				columnObject = oneRowMap.get(columnName);
				if (columnObject == null) {
					continue;
				}
				columnValue = columnObject.toString();
				if ("".equals(findCondition)) {
					findCondition += " " + columnName + " == " + columnValue;
				} else {
					findCondition += " and " + columnName + " == "
							+ columnValue;
				}
			}
			if (findCondition == null || "".equals(findCondition)) {
				continue;
			}

			// 往后逐行扫描
			find = find(pList, findCondition, i + 1);
			if (find >= 0) {
				pList.remove(find);
			}
		}
		return pList;
	}
}
