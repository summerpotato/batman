package net.spirit.batman.common;

import net.spirit.batman.util.bean.ErrorCode;

public enum ErrorType implements ErrorCode {

	NO_ERROR, PARAM_VERIFY, TOKEN_NEEDED, OPERATE_DENIED, OBJECT_EXISTS, OBJECT_CANNOT_DELETE, USER_NOT_EXIST, USER_PASSWORD_ERROR, USER_LOCKED, USER_DISABLED, USER_NONACTIVE, USER_DELETED;
	
	private int code;
	private String msg;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
