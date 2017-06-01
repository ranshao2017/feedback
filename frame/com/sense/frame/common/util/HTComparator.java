package com.sense.frame.common.util;

import java.util.Comparator;

/**
 * 比较器 */
class HTComparator<T> implements Comparator<T>{
	
	HTComparator() { }

	/**
	 * 实现两个泛型对象的比较	 * 支持的对象类型：String，Date，BigDecimal，Double，Integer，Boolean，Long
	 */
	public int compare(Object t1, Object t2) {
		int compareResult;
		try {
			compareResult = -1;
			
			if (t1 instanceof String && t2 instanceof String) {
				// String类型比较
				String t1tmp, t2tmp;
				t1tmp = t1.toString();
				t2tmp = t2.toString();

				compareResult = t1tmp.compareToIgnoreCase(t2tmp);
			} else if (t1 instanceof java.util.Date
					&& t2 instanceof java.util.Date) {
				// java.util.Date类型比较
				java.util.Date t1tmp, t2tmp;
				t1tmp = (java.util.Date) t1;
				t2tmp = (java.util.Date) t2;

				compareResult = t1tmp.compareTo(t2tmp);
			} else if (t1 instanceof java.math.BigDecimal
					&& t2 instanceof java.math.BigDecimal) {
				// java.math.BigDecimal类型比较
				java.math.BigDecimal t1tmp, t2tmp;
				t1tmp = (java.math.BigDecimal) t1;
				t2tmp = (java.math.BigDecimal) t2;

				compareResult = t1tmp.compareTo(t2tmp);
			} else if (t1 instanceof Integer && t2 instanceof Integer) {
				// Integer类型比较
				Integer t1tmp, t2tmp;
				t1tmp = (Integer) t1;
				t2tmp = (Integer) t2;
				compareResult = t1tmp.compareTo(t2tmp);
			} else if (t1 instanceof Double && t2 instanceof Double) {
				// Double类型比较
				Double t1tmp, t2tmp;
				t1tmp = (Double) t1;
				t2tmp = (Double) t2;
				compareResult = t1tmp.compareTo(t2tmp);
			} else if (t1 instanceof Boolean && t2 instanceof Boolean) {
				// Boolean类型比较
				Boolean t1tmp, t2tmp;
				t1tmp = (Boolean) t1;
				t2tmp = (Boolean) t2;
				compareResult = t1tmp.compareTo(t2tmp);
			} else if (t1 instanceof Long && t2 instanceof Long) {
				// Long类型比较
				Long t1tmp, t2tmp;
				t1tmp = (Long) t1;
				t2tmp = (Long) t2;
				compareResult = t1tmp.compareTo(t2tmp);
			} else {
				throw new Exception("HTComparator不支持的比较类型：["
						+ t1.getClass().getSimpleName() + "]["
						+ t2.getClass().getSimpleName() + "]");
			}

			return compareResult;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}