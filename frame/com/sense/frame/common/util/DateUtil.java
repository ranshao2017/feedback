package com.sense.frame.common.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 日期操作工具类
 */
public class DateUtil {

	public static String dateToString(Date date, String format)
			throws Exception {
		if (date == null) {
			return null;
		}
		if (StringUtils.isBlank(format)) {
			throw new Exception("传入参数中的[时间格式]为空");
		}
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * 将字符串转化为日期. 要求传入6位(yyyyMM)或者8位(yyyyMMdd)的参数
	 * 
	 * @param dateString
	 * @return Date
	 */
	public static Date stringToDate(String dateString) throws Exception {
		String vformat = null;
		if (StringUtils.isBlank(dateString)) {
			return null;
		}
		if (dateString.length() != 4 && dateString.length() != 6
				&& dateString.length() != 7 && dateString.length() != 8
				&& dateString.length() != 10 && dateString.length() != 14
				&& dateString.length() != 19) {
			throw new Exception("[时间串" + dateString + "]输入格式错误,请输入合法的日期格式!");
		}
		if (dateString.length() == 4) {
			vformat = "yyyy";
		} else if (dateString.length() == 6) {
			vformat = "yyyyMM";
		} else if (dateString.length() == 7) {
			dateString = dateString.substring(0, 4)
					+ dateString.substring(5, 7);
			vformat = "yyyyMM";
		} else if (dateString.length() == 8) {
			vformat = "yyyyMMdd";
		} else if (dateString.length() == 10) {
			dateString = dateString.substring(0, 4)
					+ dateString.substring(5, 7) + dateString.substring(8, 10);
			vformat = "yyyyMMdd";
		} else if (dateString.length() == 14) {
			vformat = "yyyyMMddHHmmss";
		} else if (dateString.length() == 17) {
			vformat = "yyyyMMddHHmmssSSS";
		} else if (dateString.length() == 19) {
			dateString = dateString.substring(0, 4)
					+ dateString.substring(5, 7) + dateString.substring(8, 10)
					+ dateString.substring(11, 13)
					+ dateString.substring(14, 16)
					+ dateString.substring(17, 19);
			vformat = "yyyyMMddHHmmss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(vformat);
		return sdf.parse(dateString);
	}

	/**
	 * 将传入的Date型参数增加秒数
	 * 
	 * @param date
	 *            Date型
	 * @param monthNumber
	 *            int型 增加秒数，可以是负数
	 * @return Date
	 */
	public static Date addSecond(Date date, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, second);
		return calendar.getTime();
	}

	/**
	 * 将传入的Date型参数增加天数
	 * 
	 * @param date
	 *            Date型
	 * @param dayNumber
	 *            int型 增加月数，可以是负数
	 * @return Date
	 */
	public static Date addDay(Date date, int dayNumber) {
		if (date == null || dayNumber == 0)
			return date;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, dayNumber);
		date = cal.getTime();
		return date;
	}

	/**
	 * 将传入的Date型参数增加月数
	 * 
	 * @param date
	 *            Date型
	 * @param monthNumber
	 *            int型 增加月数，可以是负数
	 * @return Date
	 */
	public static Date addMonth(Date date, int monthNumber) {
		if (date == null || monthNumber == 0)
			return date;
		Calendar vcal = Calendar.getInstance();
		vcal.setTime(date);
		vcal.add(Calendar.MONTH, monthNumber);
		date = vcal.getTime();
		return date;
	}

	/**
	 * 将传入的Date型参数增加年数
	 * 
	 * @param date
	 *            Date型
	 * @param monthNumber
	 *            int型 增加年数，可以是负数
	 * @return Date
	 */
	public static Date addYear(Date date, int yearNumber) {
		if (date == null || yearNumber == 0)
			return date;
		Calendar vcal = Calendar.getInstance();
		vcal.setTime(date);
		vcal.add(Calendar.YEAR, yearNumber);
		date = vcal.getTime();
		return date;
	}

	/**
	 * 日期转化为中文，例如2015-10-20转化为二零一五年十月二十日
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToChinese(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdf.format(date);
		StringBuffer yearSB = new StringBuffer();
		for (int i = 0; i < dateStr.substring(0, 4).length(); i++) {
			yearSB.append(formatDigit(dateStr.substring(0, 4).charAt(i)));
		}
		String monthStr = dateStr.substring(4, 6);
		StringBuffer monthSB = new StringBuffer();
		if (monthStr.charAt(0) == '1') {
			monthSB.append("十");
		} else if (monthStr.charAt(0) != '0') {
			monthSB.append(formatDigit(monthStr.charAt(0))).append("十");
		}
		if (monthStr.charAt(1) != '0') {
			monthSB.append(formatDigit(monthStr.charAt(1)));
		}
		String dayStr = dateStr.substring(6);
		StringBuffer daySB = new StringBuffer();
		if (dayStr.charAt(0) == '1') {
			daySB.append("十");
		} else if (dayStr.charAt(0) != '0') {
			daySB.append(formatDigit(dayStr.charAt(0))).append("十");
		}
		if (dayStr.charAt(1) != '0') {
			daySB.append(formatDigit(dayStr.charAt(1)));
		}
		return yearSB.toString() + "年" + monthSB.toString() + "月"
				+ daySB.toString() + "日";
	}

	public static String formatDigit(char sign) {
		String chinese = "";
		switch (sign) {
		case '0':
			chinese = "零";
			break;
		case '1':
			chinese = "一";
			break;
		case '2':
			chinese = "二";
			break;
		case '3':
			chinese = "三";
			break;
		case '4':
			chinese = "四";
			break;
		case '5':
			chinese = "五";
			break;
		case '6':
			chinese = "六";
			break;
		case '7':
			chinese = "七";
			break;
		case '8':
			chinese = "八";
			break;
		case '9':
			chinese = "九";
			break;
		}
		return chinese;
	}

	/**
	 * 说明：把时间减去一个月
	 * 
	 * @param dateString
	 *            String型
	 * @return String型
	 * @throws BusinessException
	 */
	public static String descreaseYearMonth(String dateString) throws Exception {
		if (dateString == null)
			return null;
		if (dateString.length() != 6)
			throw new Exception("[时间串]输入格式错误,请输入形如\"yyyyMM\"的日期格式!");
		int year = (new Integer(dateString.substring(0, 4))).intValue();
		int month = (new Integer(dateString.substring(4, 6))).intValue();
		if (--month >= 10)
			return dateString.substring(0, 4) + (new Integer(month)).toString();
		if (month > 0 && month < 10)
			return dateString.substring(0, 4) + "0"
					+ (new Integer(month)).toString();
		else
			return (new Integer(year - 1)).toString()
					+ (new Integer(month + 12)).toString();
	}

	/**
	 * 说明：得到汉字的日期
	 * 
	 * @param date
	 * @return String
	 */
	public static String getChineseDate(Date date) {
		if (date == null)
			return null;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd",
				new DateFormatSymbols());
		String dtrDate = df.format(date);
		return dtrDate.substring(0, 4) + "年"
				+ Integer.parseInt(dtrDate.substring(4, 6)) + "月"
				+ Integer.parseInt(dtrDate.substring(6, 8)) + "日";
	}

	/**
	 * 说明：得到当前的时间
	 * 
	 * @return Date
	 */
	public static Date getLocalDate() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	/**
	 * 计算记录date有i天是那一天
	 * 
	 * @param date
	 *            Date型
	 * @param i
	 *            第i天
	 * @return Date型
	 */
	public static Date getDateBetween(Date date, int i) {
		if (date == null || i == 0)
			return date;
		Calendar calo = Calendar.getInstance();
		calo.setTime(date);
		calo.add(5, i);
		return calo.getTime();
	}

	/**
	 * 求两个日期之间相差的天数
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return long
	 * @throws BusinessException
	 */
	public static long getDayDifferenceBetweenTwoDate(Date beginDate,
			Date endDate) throws Exception {
		if (beginDate == null)
			throw new Exception("传入参数[开始时间]为空");
		if (endDate == null)
			throw new Exception("传入参数[结束时间]为空");
		long ld1 = beginDate.getTime();
		long ld2 = endDate.getTime();
		long days = (long) ((ld2 - ld1) / 86400000);
		return days;
	}

	/**
	 * 取某年月最后一天
	 * 
	 * @param dateString
	 * @return String型
	 * @throws BusinessException
	 */
	public static String getLastDayOfMonth(String dateString) throws Exception {
		if (dateString == null)
			return null;
		if (dateString.length() != 6)
			throw new Exception("[时间串]输入格式错误,请输入形如\"yyyyMM\"的日期格式!");
		int vnf = Integer.parseInt(dateString.substring(0, 4));
		int vyf = Integer.parseInt(dateString.substring(4, 6));
		if (vyf == 2) {
			if ((vnf % 4 == 0 && vnf % 100 != 0) || vnf % 400 == 0) {
				return "29";
			} else {
				return "28";
			}
		}
		switch (vyf) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return "31";
		case 4:
		case 6:
		case 9:
		case 11:
			return "30";
		default:
			return null;
		}
	}

	/**
	 * 得到本月的第一天
	 * 
	 * @return int型
	 */
	public static int getMonthFirstDay(Date pdate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pdate);
		return calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
	}

	public static int getMonthLastDay(Date pdate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pdate);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 计算某个时间后的几个月
	 * 
	 * @param date
	 * @param i
	 * @return Date型
	 */
	public static Date increaseMonth(Date date, int i) {
		if (date == null || i == 0)
			return date;
		Calendar calo = Calendar.getInstance();
		calo.setTime(date);
		calo.add(2, i);
		return calo.getTime();
	}

	/**
	 * 说明：计算某个时间后的几年
	 * 
	 * @param date
	 *            Date型
	 * @param i
	 *            int型
	 * @return Date型
	 */
	public static Date increaseYear(Date date, int i) {
		if (date == null || i == 0)
			return date;
		Calendar calo = Calendar.getInstance();
		calo.setTime(date);
		calo.add(1, i);
		return calo.getTime();
	}

	/**
	 * 说明：获取参数时间yearMonth的下一个月
	 * 
	 * @param dateString
	 * @return String型
	 * @throws BusinessException
	 */
	public static String increaseYearMonth(String dateString) throws Exception {
		if (dateString == null)
			return null;
		if (dateString.length() != 6)
			throw new Exception("[时间串]输入格式错误,请输入形如\"yyyymm\"的日期格式!");
		int year = (new Integer(dateString.substring(0, 4))).intValue();
		int month = (new Integer(dateString.substring(4, 6))).intValue();
		if (++month <= 12 && month >= 10)
			return dateString.substring(0, 4) + (new Integer(month)).toString();
		if (month < 10)
			return dateString.substring(0, 4) + "0"
					+ (new Integer(month)).toString();
		else
			return (new Integer(year + 1)).toString() + "0"
					+ (new Integer(month - 12)).toString();
	}

	/**
	 * 说明：将month数转成几年几个月，如果13个月表示一年零一个月
	 * 
	 * @param month
	 * @return String
	 * @throws BusinessException
	 */
	public static String monthToYearMonth(String month) throws Exception {
		if (StringUtils.isBlank(month))
			throw new Exception("传入参数中的[月数]为空");
		String yearMonth = "";
		int smonth = 0;
		int year = 0;
		int rmonth = 0;
		if ("0".equals(month))
			return "0月";
		smonth = Integer.parseInt(month);
		year = smonth / 12;
		rmonth = smonth % 12;
		if (year > 0)
			yearMonth = year + "年";
		if (rmonth > 0)
			yearMonth = yearMonth + rmonth + "个月";
		return yearMonth;
	}

	/**
	 * 获取星期数
	 * 
	 * @param date
	 * @return int
	 */
	public static int getWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取星期数
	 * 
	 * @param date
	 * @return String
	 */
	public static String getChineseWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int i = cal.get(Calendar.DAY_OF_WEEK);
		if (i == 1) {
			return "星期天";
		} else if (i == 2) {
			return "星期一";
		} else if (i == 3) {
			return "星期二";
		} else if (i == 4) {
			return "星期三";
		} else if (i == 5) {
			return "星期四";
		} else if (i == 6) {
			return "星期五";
		} else if (i == 7) {
			return "星期六";
		} else {
			return "";
		}
	}
	
	public static String dateTimeToDateTimeBox(Date date) throws Exception{
		return dateToString(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String dateTimeToDateBox(Date date) throws Exception{
		return dateToString(date, "yyyy-MM-dd");
	}

}