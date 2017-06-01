package com.sense.frame.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.sense.frame.common.util.StringUtil;

/**
 * 类描述:被校验对象必须是一个string。校验它的UTF8长度是否小于固定值
 */
public class MaxUtf8LengthValidator implements ConstraintValidator<MaxUtf8Length, String> {

	private int maxValue;  //最大长度
	
	@Override
	public void initialize(MaxUtf8Length maxLength) {
		maxValue = maxLength.value();
	}

	@Override
	public boolean isValid(String needChkValue, ConstraintValidatorContext arg1) {
		if(needChkValue==null||needChkValue.trim().equals("")){
			return true;
		}
		
		if(maxValue >= needChkValue.trim().length()*3){
			//如果输入的值*3之后的长度仍然小于最大长度，可以判定一定是满足条件的，就不需要下面的循环每个字符判定
			return true;
		}
		
		return maxValue >= StringUtil.chnStrLenUTF8(needChkValue.trim());
	}

}
