package net.spirit.batman.common.context;

import java.util.HashMap;
import java.util.Map;

public class DefaultThreadAttributes implements ThreadAttributes {

	String sessionId;	//会话ID.
	
	String threadId;	//线程ID.
	
	Long operatorId;	//操作员ID.
	
	//自定义属性.
	Map<String, String> attributes = new HashMap<String, String>();

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * 自定义属性.
	 *
	 * @param key
	 * @return the attribute
	 */
	String getAttribute(String key) {
		return attributes.get(key);
	}

	public String setAttribute(String key, String value) {
		return attributes.put(key, value);
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes.clear();
		this.attributes.putAll(attributes);
	}
	
}
