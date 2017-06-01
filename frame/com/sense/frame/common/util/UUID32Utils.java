package com.sense.frame.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * GUID是一个128位长的数字，一般用16进制表示。 算法的核心思想是结合机器的网卡、当地时间、一个随即数来生成GUID。
 * 从理论上讲，如果一台机器每秒产生10000000个GUID， 则可以保证（概率意义上）3240年不重复。
 * @author qinchao 创建时间 2014-10-14
 */
public class UUID32Utils {

	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		return s.replace("-", ""); // 去掉“-”符号
	}

	/**
	 * 获得指定数目的UUID
	 * @param  int 需要获得的UUID数量
	 * @return String[] UUID数组
	 */
	public static List<String> getUUIDList(int number) {
		if (number < 1) {
			return null;
		}
		List<String> ls = new ArrayList<String>(number);
		for (int i = 0; i < number; i++) {
			ls.add(getUUID());
		}
		return ls;
	}

}