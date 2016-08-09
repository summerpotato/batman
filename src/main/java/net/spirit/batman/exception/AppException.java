package net.spirit.batman.exception;

/**
 * 一切异常类的父类，提供一些统一的方法
 * @author SummerPotato
 * 
 */
public class AppException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 无参构造函数
	 */
	public AppException() {
		super();
	}

	/**
	 * 构造函数
	 * @param message 异常信息
	 * @param cause 异常
	 */
	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造函数
	 * @param message 异常信息
	 */
	public AppException(String message) {
		super(message);
	}

	/**
	 * 构造函数
	 * @param cause 异常
	 */
	public AppException(Throwable cause) {
		super(cause);
	}

}
