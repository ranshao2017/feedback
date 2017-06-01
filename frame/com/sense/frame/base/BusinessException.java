package com.sense.frame.base;

/**
 * 业务级异常类
 */
public class BusinessException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 构造详细消息为 null 的异常
	 */
	public BusinessException() {
		super();
	}

	/**
	 * 构造带指定详细消息的异常
	 */
	public BusinessException(String msg) {
		super(msg);
	}
	
	public BusinessException(String msg, String solution) {
		super(msg + solution);
	}

	/**
	 * 构造带指定详细消息和原因的异常
	 * @param msg  指定详细消息
	 * @param cause 指定的原因
	 */
	public BusinessException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}
}