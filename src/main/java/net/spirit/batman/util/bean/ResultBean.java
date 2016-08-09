package net.spirit.batman.util.bean;

import java.io.Serializable;

/**
 * @author SummerPotato
 *
 * @param <CODE> 错误编码的类型
 * @param <DATA> 数据对象的类型
 */
public class ResultBean<CODE extends ErrorCode,DATA> implements Serializable {

	private static final long serialVersionUID = 1L;

	boolean success;
	String errMsg;
	DATA data;
	CODE errCode;
	String helpMsg;

	public ResultBean() {
		success = true;
	}
	public ResultBean(DATA data) {
		this.data = data;
		success = true;
	}
	public ResultBean(boolean success, DATA data) {
		this.data = data;
		this.success = success;
	}
	public ResultBean(CODE errCode) {
		this.errCode = errCode;
		this.success = false;
	}
	public ResultBean(CODE errCode,String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
		this.success = false;
	}

	public ResultBean(CODE errCode,String errMsg, DATA data) {
		this.errCode = errCode;
		this.errMsg = errMsg;
		this.data = data;
		this.success = false;
	}

	public ResultBean(boolean success, CODE errCode,String errMsg, DATA data) {
		this.errCode = errCode;
		this.errMsg = errMsg;
		this.data = data;
		this.success = success;
	}

	/**
	 * 重新初始化bean的所有属性
	 */
	public void initSuccess(DATA data) {
		init(true, null, null, data);
	}
	/**
	 * 重新初始化bean的所有属性
	 */
	public void initError(CODE errCode) {
		init(false, errCode, null, null);
	}
	/**
	 * 重新初始化bean的所有属性
	 */
	public void initError(CODE errCode,String errMsg) {
		init(false, errCode, errMsg, null);
	}

	/**
	 * 重新初始化bean的所有属性
	 */
	public void initError(String errMsg) {
		init(false, null, errMsg, null);
	}

	/**
	 * 重新初始化bean的所有属性
	 */
	public void initError(CODE errCode,String errMsg, DATA data) {
		init(false, errCode, errMsg, data);
	}

	/**
	 * 重新初始化bean的所有属性
	 */
	public void init(boolean success, CODE errCode,String errMsg, DATA data) {
		this.errCode = errCode;
		this.errMsg = errMsg;
		this.data = data;
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public DATA getData() {
		return data;
	}

	public void setData(DATA user) {
		this.data = user;
	}
	
	public CODE getErrCode() {
		return errCode;
	}
	
	public void setErrCode(CODE errCode) {
		this.errCode = errCode;
	}
	
	@Override
	public String toString() {
		return "ResultBean [success=" + success + ", errCode=" + errCode
				+ ", errMsg=" + errMsg + ", data=" + data + "]";
	}
	
	/**
	 * @return the helpMsg
	 */
	public String getHelpMsg() {
		return helpMsg;
	}
	
	/**
	 * @param helpMsg the helpMsg to set
	 */
	public void setHelpMsg(String helpMsg) {
		this.helpMsg = helpMsg;
	}
}
