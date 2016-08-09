package net.spirit.batman.common;

import net.spirit.batman.util.bean.ResultBean;

/**
 * 封装返回结果的‘集合类型’定义
 * @param <T>
 * 
 * @author kulj
 */
public class BizResult<T> extends ResultBean<ErrorType, T> {

	private static final long serialVersionUID = 1L;
	
	public BizResult(){
	}
	
	public BizResult(ErrorType errType, String errMsg){
		super(errType, errMsg);
	}
	
	public BizResult(ErrorType errType) {
			super(errType);
	}

	public BizResult(T data) {
		super(data);
	}

	public ErrorType getErrType() {
		return ((ErrorType)getErrCode());
	}

	public void setErrType(ErrorType errType) {
		setErrCode(errType);
	}

	public BizResult(ErrorType errType, String errMsg, T data) {
		super(errType, errMsg, data);
	}

	public String getErrMsg(){
		ErrorType errType = getErrType();
		String msg = super.getErrMsg();
		if ((msg == null) && (errType != null)) {
			msg = errType.getMsg();
		}
		return msg;
	}

	public void setErrMsg(String errMsg){
		super.setErrMsg(errMsg);
	}
}
