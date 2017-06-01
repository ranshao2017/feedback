package com.sense.frame.common.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 字符串操作工具类
 */
public class StringUtil {
	
	/**
	 * 判定如果字符串a is null，返回b,否则是a
	 * @param a String
	 * @param b String
	 * @return String
	 */
	public static String nvlStr(String a, String b) {
		if (a == null) {
			return b;
		}
		return a;
	}

	/**
	 * Description:String的length()方法计算字符串长度，英文一个字母算一个，汉语一个字也算一个。
	 * <p>
	 * 在填充表格时不能精确确定显示内容的长度。编写chnSubstring和chnStrLen方法，计算字符
	 * <p>
	 * 串长度和截取子串时，一个汉字长度算两个。
	 * <p>
	 * @param chinaString
	 * @return int
	 */
	public static int chnStrLen(String chinaString) {
		int len = 0;
		for (int i = 0; i < chinaString.length(); i++) {
			if (((int) chinaString.charAt(i)) > 255)
				len += 2;
			else
				len++;
		}
		return len;
	}
	
	/**
	 * orcal中UTF-8中一个汉字为三个字节
	 * <p>方法详述</p>
	 */
	public static int chnStrLenUTF8(String chinaString) {
		int len = 0;
		for (int i = 0; i < chinaString.length(); i++) {
			if (((int) chinaString.charAt(i)) > 255)
				len += 3;
			else
				len ++;
		}
		return len;
	}
	
	/**
	 * orcal中UTF-8中一个汉字为三个字节
	 * <p>获取不超过长度为maxLength的汉字字符串</p>
	 */
	public static String getStrLenUTF8(int maxLength,String chinaString) {
		if(chinaString==null){
			return chinaString;
		}else{
			int substrLen=chinaString.length();
			int len = 0;
			for (int i = 0; i < chinaString.length(); i++) {
				if (((int) chinaString.charAt(i)) > 255)
					len += 3;
				else
					len ++;
				if(maxLength<len){
					substrLen=(i-1);
					break;
				}
			}
			
			return chinaString.substring(0,substrLen);
		}
	}

	/**
	 * 获取字符串中汉字的个数
	 * @param chinaString
	 * @return int 
	 */
	public static int getChnNumber(String chinaString) {
		int len = 0;
		for (int i = 0; i < chinaString.length(); i++) {
			if (((int) chinaString.charAt(i)) > 255)
				len++;
		}
		return len;
	}

	/**
	 * 获取字符串中字母和数字的个数
	 * @param chinaString
	 * @return int
	 */
	public static int getchrNumber(String chinaString) {
		int len = 0;
		for (int i = 0; i < chinaString.length(); i++) {
			if (((int) chinaString.charAt(i)) > 255)
				continue;
			else
				len++;
		}
		return len;
	}

	/**
	 * 在str中查找findstr出现的所有位置
	 * @param str
	 * @param findstr
	 * @return int[]
	 */
	public static int[] findAllPos(String str, String findstr) {
		ArrayList<Integer> posArray = new ArrayList<Integer>();
		int find;
		int[] intArray;

		if ((str == null) || (findstr == null) || "".equals(findstr)) {
			return new int[0];
		}

		find = str.indexOf(findstr);
		while (find > -1) {
			posArray.add(find);
			find = str.indexOf(findstr, find + 1);
		}

		intArray = new int[posArray.size()];
		for (int i = 0, len = intArray.length; i < len; i++) {
			intArray[i] = posArray.get(i);
		}
		return intArray;
	}
	
	/** 
	 * 取字符串的汉语拼音全拼
	 * @param pStr
	 * @return String
	 * @throws BadHanyuPinyinOutputFormatCombination
	 * @author xfh 
	 * @date 创建时间 2013-8-30
	 * @since V1.0
	 */	
	public static String getPinYin(String pStr) throws Exception {
		char[] charArray = null;
		String[] stringArray = null;
		String pinYin = "";

		if (pStr == null || "".equals(pStr)) {
			return pStr;
		}

		charArray = pStr.toCharArray();
		stringArray = new String[charArray.length];

		// 设置汉字拼音输出的格式
		HanyuPinyinOutputFormat pinYinFormat = new HanyuPinyinOutputFormat();
		pinYinFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		pinYinFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		pinYinFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

		int t0 = charArray.length;
		for (int i = 0; i < t0; i++) {
			// 判断是否为汉字字符
			if (Character.toString(charArray[i]).matches("[\\u4E00-\\u9FA5]+")) {
				// 将汉字的几种全拼都存到stringArray数组中
				stringArray = PinyinHelper.toHanyuPinyinStringArray(charArray[i], pinYinFormat);
				pinYin += stringArray[0];// 取出该汉字全拼的第一种读音并连接到字符串pinYin后
			} else {
				// 如果不是汉字字符，直接取出字符并连接到字符串pinYin后
				pinYin += Character.toString(charArray[i]);
			}
		}
		return pinYin;
	}
		
	/** 
	 * 取字符串的汉语拼音首字母缩写
	 * @param pStr
	 * @return String
	 * @author xfh 
	 * @date 创建时间 2013-8-30
	 * @since V1.0
	 */	
	public static String getPinYinHead(String pStr) {
		String pinyinHead = "";

		if (pStr == null || "".equals(pStr)) {
			return pStr;
		}

		for (int i = 0, len = pStr.length(); i < len; i++) {
			char word = pStr.charAt(i);
			// 提取汉字的首字母
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				pinyinHead += pinyinArray[0].charAt(0);
			} else {
				pinyinHead += word;
			}
		}
		return pinyinHead;
	}  		 
	
	/**
	 * 将逗号分隔的字符串的每个元素都追加上单引号
	 * <p> 例如：传入【1,2,3,4】或者【1,2,3,4,】，返回【'1','2','3','4'】 </p>
	 * @param pStr
	 * @return String
	 * @author xfh
	 * @date 创建时间 2013-9-2
	 * @since V1.0
	 */
	public static String addSingleQuotesToCommaString(String pStr) {
		return addSingleQuotesToCommaString(pStr , ",");
	}
	
	/**
	 * 将指定分隔的字符串的每个元素都追加上单引号
	 * <p> 例如：传入【1,2,3,4】或者【1,2,3,4,】，返回【'1','2','3','4'】 </p>
	 * <p> 例如：传入【1-2-3-4】或者【1-2-3-4-】，返回【'1','2','3','4'】 </p>
	 * @param pStr
	 * @return String
	 * @author xfh
	 * @date 创建时间 2013-9-2
	 * @since V1.0
	 */
	public static String addSingleQuotesToCommaString(String pStr,String separator) {
		StringBuffer sbf = new StringBuffer();
		if (pStr == null || "".equals(pStr)) {
			return "";
		}
		sbf.append("'");
		sbf.append(pStr);

		if (separator.equalsIgnoreCase(sbf.substring(sbf.length() - 1))) {
			sbf = sbf.deleteCharAt(sbf.length() - 1);
		}

		return sbf.toString().replace(separator, "','") + "'";
	}
		
	/**
	 * 将List<String>的字符串的每个元素都追加上单引号
	 * <p> 例如：传入【1,2,3,4】，返回【'1','2','3','4'】 </p>
	 * @param pStr
	 * @return String
	 * @author xfh
	 * @date 创建时间 2013-9-2
	 * @since V1.0
	 */
	public static String addSingleQuotesToCommaString(List<String> strList) {
		StringBuffer sbf = new StringBuffer();

		if (strList == null || strList.size()<=0) {
			return "";//如果传递的是个空串，则返回
		}
		
		for(String str:strList){
			if(sbf.length()>0){
				sbf.append(",");
			}
			sbf.append("'");
			sbf.append(str);
			sbf.append("'");
		}

		return sbf.toString();
	}
	
	/**
	 * 检查字符串是否符合命名规则：
	 * 方法简介:字符串校验，以字母开头，并且只允许字母和数字
	 * ^[a-zA-Z][a-zA-Z0-9]+$,要求至少一个。
	 * ^[a-zA-Z][a-zA-Z0-9]*$ ,要求0个或多个
	 * @author qinchao
	 * @date 创建时间 2013-12-14
	 */
	public static boolean checkCorrectJavaName(String  str){
		boolean result = false ;
		if(str != null &&  !str.equals("")){
			Matcher matcher  = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*$").matcher(str);
			result = matcher.find() ;
		}
		return result ;
	}

	/**
	 * 将object 转换成String 
	 * @author xfh 
	 * @date 创建时间 20140123
	 */
	public static String objectToStirng(Object obj) throws Exception {
		String objString = null;

		if (obj != null) {
			if (obj instanceof java.util.Date) {
				// java.util.Date类型
				objString = DateUtil.dateToString((java.util.Date) obj, "yyyyMMdd");
			} else if (obj instanceof BigDecimal) {
				// BigDecimal类型
				objString = String.valueOf(((BigDecimal) obj).setScale(10, BigDecimal.ROUND_HALF_UP));
			} else if (obj instanceof Integer) {
				// Integer类型
				objString = String.valueOf((Integer) obj);
			} else if (obj instanceof Double) {
				// Double类型
				objString = MathUtil.formateNumber((Double) obj, 10);
			} else if (obj instanceof Boolean) {
				// Boolean类型
				objString = String.valueOf((Boolean) obj);
			} else if (obj instanceof Long) {
				// Long类型
				objString = String.valueOf((Long) obj);
			} else {
				objString = obj.toString();
			}
		}
		return objString;
	}
	
	/**
	 * 生成随机字符串，随机字符串长度为参数给定的length
	 * @param length
	 * @return
	 */
	public static synchronized String randomString(int length) {
		Random random = new Random(System.currentTimeMillis());
		String chars = "G6bKAUcS7FdPDeXkf4gJh1Rk3mInHpqrCstu2wxzBLoMjNOSvQViWaZy05T8E9Yl";
		StringBuffer buf = new StringBuffer();		
		buf.setLength(0);
		for (int i = 0; i < length; i++) {
			buf.append(chars.charAt(random.nextInt(64)));
		}
		return buf.toString();
	}
	
}