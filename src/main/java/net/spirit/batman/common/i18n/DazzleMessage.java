package net.spirit.batman.common.i18n;

/**
 * <P>国际化信息读取类</p>
 * 
 * @author SummerPotato
 * @version 1.0, 2016-8-10
 */
public class DazzleMessage {

	/**
	 * 国际化资源信息对象
	 */
	private static ResourceManager rm = new ResourceManager("resources.DazzleMessageResource");
 
	/**
	 * 获取一条国际化资源信息
	 * @param messageKey
	 * 			信息唯一标示
	 * @param values
	 * 			信息参数
	 * @return 国际化资源信息
	 */
	public static String get(String messageKey, Object... values) {
		return rm.get(messageKey, values);
	}
	
}
