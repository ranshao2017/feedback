package com.sense.frame.common.validator;

import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
   
import javax.validation.Constraint;  
import javax.validation.Payload;  

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=MaxUtf8LengthValidator.class)
/**
 * 
 * 类描述:被校验对象必须是一个string。校验它的UTF8长度是否小于固定值
 * @author qinchao
 * 创建时间 2013-9-26
 */
public @interface MaxUtf8Length {
 
    int value() default 0;
   
    String message() default "";
   
    Class<?>[] groups() default {};
   
    Class<? extends Payload>[] payload() default {};
}
