package com.sense.frame.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 数值操作工具类
 */
public class MathUtil {

	/**
	 * 得到绝对值
	 * 
	 * @param num
	 *            Double型
	 * @return double
	 */
	public static double abs(double num) {
		if (num < 0) {
			return -1 * num;
		}
		return num;
	}

	/**
	 * 截取double指定位数函数
	 * 
	 * @param d
	 *            double 原double
	 * @param i
	 *            int 小数位
	 * @return double
	 */
	public static double truncate(double d, int i) {
		double tmp = Math.pow(10, i);
		return Math.floor(d * tmp) / tmp;
	}

	/**
	 * 四舍五入函数
	 * 
	 * @param v
	 *            double 原double
	 * @param scale
	 *            int 小数位
	 * @return double
	 * @throws BusinessException
	 */
	public static double round(double v, int scale) throws Exception {
		if (scale < 0) {
			throw new Exception("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		double i = b.divide(one, 10, BigDecimal.ROUND_HALF_UP).doubleValue();
		b = new BigDecimal(Double.toString(i));
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 判定一个字符串是不是数值，包括long ,double, 科学计数法
	 * 
	 * @param numberString
	 *            判定字符串
	 * @return boolean
	 */
	public static boolean isNumber(String numberString) {
		try {
			Double.parseDouble(numberString);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * 将月数转换成汉字描述<br>
	 * 
	 * @since 2012-8-15
	 * @param monthsValue
	 * @return String
	 * @throws BusinessException
	 */
	public static String formatMonths(Integer monthsValue) throws Exception {
		if (monthsValue == null || monthsValue == 0) {
			return "0个月";
		}
		String monStr = "";
		Integer months = monthsValue % 12;
		Integer years = monthsValue / 12;
		if (years != 0) {
			monStr = years + "年";
		}
		if (monStr != null && !"".equals(monStr)) {
			if (months > 0) {
				monStr = monStr + "零" + months + "个月";
			}
		} else {
			monStr = months + "个月";
		}
		return monStr;
	}

	/**
	 * 获取金额,通过千分位表示
	 * 
	 * @since 2012-8-15
	 * @param para
	 * @return String
	 * @throws BusinessException
	 */
	public static String getNumKb(BigDecimal para) throws Exception {
		if (para == null) {
			para = BigDecimal.ZERO;
		}
		NumberFormat formatter = new DecimalFormat("#,##0.00");
		return formatter.format(para) + "";
	}

	public static double getNumber(BigDecimal bigDecimal) {
		if (bigDecimal == null) {
			return 0;
		}
		return bigDecimal.doubleValue();
	}

	/**
	 * 将double类型的数字进行格式化处理，保留i位小数，返回的是String类型 注意:没有四舍五入
	 * 
	 * @param d
	 * @param i
	 * @return
	 */
	public static String formateNumber(double d, int i) {
		String formatStr = "#";
		if (i > 0)
			formatStr = "#.";
		for (int m = 0; m < i; m++) {
			formatStr += "0";
		}
		DecimalFormat df = new DecimalFormat(formatStr);
		return df.format(d);
	}

	public static int calPageCount(int totalNum, int pageSize) throws Exception {
		if (totalNum < 0) {
			throw new Exception("传入参数[totalNum]小于0，不合法！");
		}
		if (pageSize < 1) {
			throw new Exception("传入参数[pageSize]小于1，不合法！");
		}
		if (totalNum % pageSize == 0) {
			return totalNum / pageSize;
		}
		return totalNum / pageSize + 1;
	}

	public static int[] calPageRangeNum(int pageSize, int pageNum)
			throws Exception {
		int[] rangeArray = new int[2];
		if (pageSize < 1) {
			throw new Exception("传入参数[pageSize]小于1，不合法！");
		}
		if (pageNum < 1) {
			throw new Exception("传入参数[pageNum]小于1，不合法！");
		}

		rangeArray[0] = (pageNum - 1) * pageSize + 1;
		rangeArray[1] = pageNum * pageSize;

		return rangeArray;
	}

	/**
	 * 将数字换转成中文大写字符串，例如：123456789.10壹亿贰仟叁佰肆拾伍万陆仟柒佰捌拾玖圆壹角零分
	 * 
	 * @xfh 20151104
	 * @param value
	 * @return
	 */
	public static String numToCNBig(double value) {
		char[] hunit = { '拾', '佰', '仟' }; // 段内位置表示
		char[] vunit = { '万', '亿' }; // 段名表示
		char[] digit = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' }; // 数字表示
		// long midVal = (long)(value*100); ////存在精度问题,如0.9->0.8999...
		BigDecimal midVal = new BigDecimal(Math.round(value * 100)); // 转化成整形,替换上句
		String valStr = String.valueOf(midVal); // 转化成字符串
		String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
		String rail = valStr.substring(valStr.length() - 2); // 取小数部分

		String prefix = ""; // 整数部分转化的结果
		String suffix = ""; // 小数部分转化的结果
		// 处理小数点后面的数
		if (rail.equals("00")) { // 如果小数部分为0
			suffix = "整";
		} else {
			suffix = digit[rail.charAt(0) - '0'] + "角"
					+ digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
		}
		// 处理小数点前面的数
		char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
		boolean preZero = false; // 标志当前位的上一位是否为有效0位（如万位的0对千位无效）
		byte zeroSerNum = 0; // 连续出现0的次数
		for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
			int idx = (chDig.length - i - 1) % 4; // 取段内位置
			int vidx = (chDig.length - i - 1) / 4; // 取段位置
			if (chDig[i] == '0') { // 如果当前字符是0
				preZero = true;
				zeroSerNum++; // 连续0次数递增
				if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
					prefix += vunit[vidx - 1];
					preZero = false; // 不管上一位是否为0，置为无效0位
				}
			} else {
				zeroSerNum = 0; // 连续0次数清零
				if (preZero) { // 上一位为有效0位
					prefix += digit[0]; // 只有在这地方用到'零'
					preZero = false;
				}
				prefix += digit[chDig[i] - '0']; // 转化该数字表示
				if (idx > 0)
					prefix += hunit[idx - 1];
				if (idx == 0 && vidx > 0) {
					prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
				}
			}
		}

		if (prefix.length() > 0) {
			prefix += '圆'; // 如果整数部分存在,则有圆的字样
		}

		return prefix + suffix; // 返回正确表示
	}

	/**
	 * 将整数数字换转成中文小写字符串，例如：99转换为九十九
	 * 
	 * @xfh 20151104
	 * @param value
	 * @return
	 */
	public static String numToCNSmall(int value) {
		char[] hunit = { '十', '百', '千' }; // 段内位置表示
		char[] vunit = { '万', '亿' }; // 段名表示
		char[] digit = { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九' }; // 数字表示
		String head = String.valueOf(value); // 转化成字符串
		String resultStr = "";
		char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
		boolean preZero = false; // 标志当前位的上一位是否为有效0位（如万位的0对千位无效）
		byte zeroSerNum = 0; // 连续出现0的次数
		for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
			int idx = (chDig.length - i - 1) % 4; // 取段内位置
			int vidx = (chDig.length - i - 1) / 4; // 取段位置
			if (chDig[i] == '0') { // 如果当前字符是0
				preZero = true;
				zeroSerNum++; // 连续0次数递增
				if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
					resultStr += vunit[vidx - 1];
					preZero = false; // 不管上一位是否为0，置为无效0位
				}
			} else {
				zeroSerNum = 0; // 连续0次数清零
				if (preZero) { // 上一位为有效0位
					resultStr += digit[0]; // 只有在这地方用到'零'
					preZero = false;
				}
				resultStr += digit[chDig[i] - '0']; // 转化该数字表示
				if (idx > 0)
					resultStr += hunit[idx - 1];
				if (idx == 0 && vidx > 0) {
					resultStr += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
				}
			}
		}

		return resultStr;
	}

}