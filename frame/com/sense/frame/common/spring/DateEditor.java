package com.sense.frame.common.spring;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

public class DateEditor extends PropertyEditorSupport {
	
	 private DateFormat yearFormat = new SimpleDateFormat("yyyy");
	 
	 private DateFormat yearMonthFormat;
	
	 private DateFormat dateFormat;
     
     private DateFormat dateTimeShortFormat;
     
     private DateFormat dateTimeFormat;

     private final boolean allowEmpty;


     public DateEditor(boolean allowEmpty) {
             this.allowEmpty = allowEmpty;
     }

     /**
      * 根据格式绑定数据
      */
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			setValue(null);
		} else if("".equals(text)){
			setValue(null);
		}else {
			try {
				text = text.trim();
				if (text.length() == 4) {
					this.yearFormat.setLenient(false);
					setValue(this.yearFormat.parse(text));
				} else if (text.length() == 7) {					
					if (text.indexOf("-") != -1) {						
						yearMonthFormat = new SimpleDateFormat("yyyy-MM");
					} else if (text.indexOf("/") != -1) {
						yearMonthFormat = new SimpleDateFormat("yyyy/MM");
					} else if (text.indexOf(" ") != -1) {
						yearMonthFormat = new SimpleDateFormat("yyyy MM");
					} else if (text.indexOf(".") != -1) {
						yearMonthFormat = new SimpleDateFormat("yyyy.MM");
					}
					this.yearMonthFormat.setLenient(false);
					setValue(this.yearMonthFormat.parse(text));
				} else if (text.length() == 10) {
					if (text.indexOf("-") != -1) {
						dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					} else if (text.indexOf("/") != -1) {
						dateFormat = new SimpleDateFormat("yyyy/MM/dd");
					} else if (text.indexOf(" ") != -1) {
						dateFormat = new SimpleDateFormat("yyyy MM dd");
					} else if (text.indexOf(".") != -1) {
						dateFormat = new SimpleDateFormat("yyyy.MM.dd");
					}
					this.dateFormat.setLenient(false);
					setValue(this.dateFormat.parse(text));
				} else if (text.length() == 16) {
					
					if (text.indexOf("-") != -1 && text.indexOf(" ") != -1) {
						dateTimeShortFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					} else if (text.indexOf(".") != -1 && text.indexOf(" ") != -1) {
						dateTimeShortFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
					}else if (text.indexOf("/") != -1 && text.indexOf(" ") != -1) {
						dateTimeShortFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
					}else if (text.indexOf(" ") != -1) {
						dateTimeShortFormat = new SimpleDateFormat("yyyy MM dd HH:mm");
					}
					this.dateTimeShortFormat.setLenient(false);
					setValue(this.dateTimeShortFormat.parse(text));
				} else if (text.length() == 19) {
					if (text.indexOf("-") != -1 && text.indexOf(" ") != -1) {
						dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					} else if (text.indexOf(".") != -1 && text.indexOf(" ") != -1) {
						dateTimeFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
					} else if (text.indexOf("/") != -1 && text.indexOf(" ") != -1) {
						dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					}else if (text.indexOf(" ") != -1) {
						dateTimeFormat = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
					}
					setValue(this.dateTimeFormat.parse(text));
				} else {
					throw new RuntimeException("日期格式不对");

				}
			} catch (ParseException ex) {
				IllegalArgumentException iae = new IllegalArgumentException(
						"日期格式不对，必须为：yyyy-MM-dd或者yyyy-MM-dd HH:mm:ss");
				iae.initCause(ex);
				throw iae;
			}
		}
	}

     /**
      * Format the Date as String, using the specified DateFormat.
      */
	public String getAsText() {
		Date value = (Date) getValue();
		return (value != null ? this.dateFormat.format(value) : "");
	}

}
