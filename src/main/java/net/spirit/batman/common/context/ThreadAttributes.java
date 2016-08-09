package net.spirit.batman.common.context;

import java.util.Map;

public interface ThreadAttributes {

	/**
	 * 自定义属性.
	 *
	 * @param key
	 * @param value
	 * @return string
	 */
	String setAttribute(String key, String value);

	/**
	 * Gets the 会话ID.
	 *
	 * @return the sessionId
	 */
	String getSessionId();

	/**
	 * Sets the 会话ID.
	 *
	 * @param sessionId
	 *            the sessionId to set
	 */
	void setSessionId(String sessionId);

	/**
	 * Gets the 线程ID.
	 *
	 * @return the threadId
	 */
	String getThreadId();

	/**
	 * Sets the 线程ID.
	 *
	 * @param threadId
	 *            the threadId to set
	 */
	void setThreadId(String threadId);

	/**
	 * Gets the 操作员ID.
	 *
	 * @return the operatorId
	 */
	Long getOperatorId();

	/**
	 * Sets the 操作员ID.
	 *
	 * @param operatorId
	 *            the operatorId to set
	 */
	void setOperatorId(Long operatorId);

	/**
	 * Gets the 自定义属性.
	 *
	 * @return the attributes
	 */
	Map<String, String> getAttributes();

	/**
	 * Sets the 自定义属性.
	 *
	 * @param attributes
	 *            the attributes to set
	 */
	void setAttributes(Map<String, String> attributes);
}
