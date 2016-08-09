package net.spirit.batman.exception;

/**
 * BizException
 * @author SummerPotato
 * 
 */
public class BizException extends AppException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 无参构造函数
	 */
	public BizException() {
		super();
	}

	/**
	 * 构造函数
	 * @param message 异常信息
	 * @param cause 异常
	 */
	public BizException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造函数
	 * @param message 异常信息
	 */
	public BizException(String message) {
		super(message);
	}

	/**
	 * 构造函数
	 * @param cause 异常
	 */
	public BizException(Throwable cause) {
		super(cause);
	}
	
}
