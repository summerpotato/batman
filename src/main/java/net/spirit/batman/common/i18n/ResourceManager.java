package net.spirit.batman.common.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * <P>国际化资源信息管理</p>
 * 
 * @author SummerPotato
 * @version 1.0, 2016-8-10
 */
public class ResourceManager {

	private static final Logger logger = Logger.getLogger(ResourceManager.class);

	/**
	 * 国际化资源信息对象
	 */
	private ResourceBundle resource = null;

	public ResourceManager(String resouceFullPath) {
		try {
			resource = ResourceBundle.getBundle(resouceFullPath);
		} catch (Exception e) {
			logger.error("Load i18n resource " + resouceFullPath + " error", e);
		}
		logger.info("dazzle i18n resources init finish");
	}

	/**
	 * 获取一条国际化资源信息
	 * 
	 * @param messageKey
	 *            信息唯一标示
	 * @param values
	 *            信息参数
	 * @return 国际化资源信息
	 */
	public String get(String messageKey, Object... values) {
		String message = resource.getString(messageKey);
		if (message != null) {
			return MessageFormat.format(message, values);
		}
		return null;
	}
	
}
