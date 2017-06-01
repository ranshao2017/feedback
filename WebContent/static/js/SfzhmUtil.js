/**
 * 前端身份证号码操作的工具类
 */
var SfzhmUtil = {
	/**
	 * 根据身份证号码得到性别
	 * 
	 * @param pSfzhm 身份证
	 * @return 1代表男,2代表女，仅对15、18位身份证号码有效，非法返回""
	 */
	getSexFromSfzhm : function(pSfzhm) {
		var sfzhm = pSfzhm.toString();
		var sex;
		if (sfzhm.length == 18) {
			sex = sfzhm.charAt(16);
			if (sex % 2 == 0) {
				return "2";
			} else if (sex % 2 == 1) {
				return "1";
			} else {
				return "";
			}
		} else if (sfzhm.length == 15) {
			sex = sfzhm.charAt(14);
			if (sex % 2 == 0) {
				return "2";
			} else if (sex % 2 == 1) {
				return "1";
			} else {
				return "";
			}
		} else {
			return "";
		}
	},
	/**
	 * 根据身份证号码得到出生日期
	 * 
	 * @param pSfzhm 身份证
	 * @return yyyy-mm-dd，仅对15、18位身份证号码有效，非法返回""
	 */
	getBirthDayFromSfzhm : function(pSfzhm) {
		var sfzhm = pSfzhm.toString();
		var year;
		var month;
		var day;
		if (sfzhm.length == 18) {
			year = sfzhm.substring(6, 10);

			var yearNumber = parseInt(year, 10);
			if (isNaN(yearNumber) || yearNumber < 1900 || yearNumber > 2100) {
				return "";
			}

			month = sfzhm.substring(10, 12);

			var monthNumber = parseInt(month, 10);
			if (isNaN(monthNumber) || monthNumber < 1 || monthNumber > 12) {
				return "";
			}

			day = sfzhm.substring(12, 14);
			var dayNumber = parseInt(day, 10);
			if (isNaN(dayNumber) || (dayNumber == 0) || (dayNumber > 31)) {
				return "";
			} else if (dayNumber > 28 && dayNumber < 31) {
				if (monthNumber == 2) {
					if (dayNumber != 29) {
						return "";
					} else {
						if ((yearNumber % 4) != 0) {
							return "";
						} else {
							if ((yearNumber % 100 == 0)
									&& (yearNumber % 400 != 0)) {
								return "";
							}
						}
					}
				}
			} else if (dayNumber == 31) {
				if ((monthNumber == 2) || (monthNumber == 4)
						|| (monthNumber == 6) || (monthNumber == 9)
						|| (monthNumber == 11)) {
					return "";
				}
			}
		} else if (sfzhm.length == 15) {
			year = sfzhm.substring(6, 8);
			year = "19" + year;
			month = sfzhm.substring(8, 10);
			day = sfzhm.substring(10, 12);
		} else {
			return "";
		}
		return year + "-" + month + "-" + day;
	},

	/**
	 * 检测身份证号码是否合法，不合法的返回错误信息
	 * 
	 * @param pSfzhm 身份证
	 * @return 错误信息，合法的身份证号码返回""
	 */
	checkSfzhm : function(pSfzhm) {
		var sfzhm = pSfzhm.toString();

		if (sfzhm == null || sfzhm == "") {
			return "";
		}
		var len = sfzhm.length;
		if (!SfzhmUtil.isContainIllegalChar(sfzhm)) {
			return "身份证号码包含非法字符！";
		}
		if (len == 15 || len == 18) {
			var errtext = SfzhmUtil.checkSfzhmDate(sfzhm, len);
			if (errtext != null && errtext != "") {
				return errtext;
			}
			if ((len == 18) && (!SfzhmUtil.validateCheckSum(sfzhm))) {
				return "身份证号码中的校验位错误！";
			}
		} else {
			return "身份证号码的位数不正确，应为15或18位！";
		}
		return "";
	},
	/**
	 * 检测身份证号码是否合法，不合法的返回false
	 * 
	 * @param pSfzhm 身份证
	 * @return boolean
	 */
	checkSfzhmBoolean : function(pSfzhm) {
		var sfzhm = pSfzhm.toString();

		if (sfzhm == null || sfzhm == "") {
			return false;
		}
		var len = sfzhm.length;
		if (!SfzhmUtil.isContainIllegalChar(sfzhm)) {
			return false;
		}
		if (len == 15 || len == 18) {
			var errtext = SfzhmUtil.checkSfzhmDate(sfzhm, len);
			if (errtext != null && errtext != "") {
				return false;
			}
			if ((len == 18) && (!SfzhmUtil.validateCheckSum(sfzhm))) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	},
	/**
	 * 验证身份证号码校验位是否正确
	 * @param psfzhm
	 * @returns {Boolean}
	 */
	checkSfzhmDate : function(pSfzhm, pLength) {
		var temp = "";
		var year = "", month, day;
		var sfzhm = pSfzhm.toString();

		if (sfzhm.length != pLength) {
			return "身份证号码位数不正确,应为" + pLength + "位。";
		}

		if (pLength == 18) {
			temp = sfzhm.substring(6, 10);
			year = parseInt(temp, 10);
			if (year < 1900 || year > 2100) {
				return "身份证中的出生年份应介于1900与2100之间，请重新输入！";
			}
		} else if (pLength == 15) {
			temp = sfzhm.substring(6, 8);
			year = parseInt(temp, 10);
			if (year < 00 || year > 99) {
				return "身份证中的出生年份应介于00与99之间，请重新输入！";
			}
		}

		if (pLength == 18) {
			temp = sfzhm.substring(10, 12);
		} else if (pLength == 15) {
			temp = sfzhm.substring(8, 10);
		}

		month = parseInt(temp, 10);
		if (month < 1 || month > 12) {
			return "身份证中出生月份必须介于1与12之间！";
		}

		if (pLength == 18) {
			temp = sfzhm.substring(12, 14);
		} else if (pLength == 15) {
			temp = sfzhm.substring(10, 12);
		}

		day = parseInt(temp, 10);
		if ((day == 0) || (day > 31)) {
			return "身份证中出生日必须介于0与31之间！";
		} else if (day > 28 && day < 31) {
			if (month == 2) {
				if (day != 29) {
					return "身份证中的出生日期不合法，" + year + "年" + month + "月无" + day
							+ "日!";
				} else {
					if ((year % 4) != 0) {
						return "身份证中的出生日期不合法，" + year + "年" + month + "月无"
								+ day + "日!";
					} else {
						if ((year % 100 == 0) && (year % 400 != 0)) {
							return "身份证中的出生日期不合法，" + year + "年" + month + "月无"
									+ day + "日!";
						}
					}
				}
			}
		} else if (day == 31) {
			if ((month == 2) || (month == 4) || (month == 6) || (month == 9)
					|| (month == 11)) {
				return "身份证中的出生日期不合法，" + month + "月无" + day + "日!";
			}
		}
		return "";
	},
	/**
	 * 判断身份证是否包含非法字符
	 * @param pSfzhm
	 * @returns {Boolean}
	 */
	isContainIllegalChar : function(pSfzhm) {		
		var sfzhm = pSfzhm.toString();
		var len = sfzhm.length;
		if (len == 18) {
			var lastChar = sfzhm.charAt(sfzhm.length - 1);
			if (lastChar == "X")
				sfzhm = sfzhm.substring(0, sfzhm.length - 1);
		}
		var newlength = sfzhm.length;
		for ( var i = 0; i < newlength; i++) {
			var oneChar = sfzhm.charAt(i);
			if (oneChar < "0" || oneChar > "9") {
				return false;
			}
		}
		return true;
	},
	/**
	 * 剔除身份证号码中的非法字符
	 * 
	 * @param pSfzhm 身份证
	 * @return String
	 */
	deleteInvalidChar : function(psfzhm) {
		var sfzhm = psfzhm.toString();
		if (sfzhm == null) {
			sfzhm = "";
			return sfzhm;
		}
		for ( var i = 0; i < sfzhm.length; i++) {
			if (sfzhm != "" && (sfzhm.charAt(i) < '0' || sfzhm.charAt(i) > '9')
					&& sfzhm.charAt(i) != 'X') {
				sfzhm = sfzhm.substring(0, i)
						+ sfzhm.substring(i + 1, sfzhm.length);
				i--;
			}
		}
		return sfzhm;
	},
	
	/**
	 * 计算身份证中的校验位
	 * 
	 * @param pSfzhm 身份证
	 * @return int
	 */
	calcCheckSum : function(psfzhm) {
		var sfzhm = psfzhm.toString();
		var i = (sfzhm.substr(0, 1) * 7 + sfzhm.substr(1, 1) * 9
				+ sfzhm.substr(2, 1) * 10 + sfzhm.substr(3, 1) * 5
				+ sfzhm.substr(4, 1) * 8 + sfzhm.substr(5, 1) * 4
				+ sfzhm.substr(6, 1) * 2 + sfzhm.substr(7, 1) * 1
				+ sfzhm.substr(8, 1) * 6 + sfzhm.substr(9, 1) * 3
				+ sfzhm.substr(10, 1) * 7 + sfzhm.substr(11, 1) * 9
				+ sfzhm.substr(12, 1) * 10 + sfzhm.substr(13, 1) * 5
				+ sfzhm.substr(14, 1) * 8 + sfzhm.substr(15, 1) * 4 
				+ sfzhm.substr(16, 1) * 2) % 11;
		var j;

		if (i > 2) {
			j = 12 - i;
		} else if (i == 2) {
			j = "X";
		} else {
			j = 1 - i;
		}
		return j;
	},
	/**
	 * 验证身份证号码校验位是否正确
	 * @param psfzhm
	 * @returns {Boolean}
	 */
	validateCheckSum : function(psfzhm) {
		var sfzhm = psfzhm.toString();
		var j = SfzhmUtil.calcCheckSum(sfzhm);

		if (sfzhm.substr(17, 1) == j) {
			return true;
		} else {
			return false;
		}
	}
};