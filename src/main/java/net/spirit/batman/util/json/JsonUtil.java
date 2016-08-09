package net.spirit.batman.util.json;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import net.spirit.batman.exception.AppException;
import net.spirit.batman.util.json.support.IJsonSupport;
import net.spirit.batman.util.json.support.JsonSupportJacksonImpl;

/**
 * Java对象与Json字符串互相转换的工具类，所有Json相关的工具api都应通过本api类进行调用，禁止直接调用Json库或其它Json相关的工具类。
 * @author kulj
 */
public class JsonUtil {

	private static IJsonSupport jsonUtil = new JsonSupportJacksonImpl();

	/**
	 * 对象转json，支持Map等集合类型，支持appframe生成的Bean对象。日期类型（Date、Time、Timestamp）输出格式为数字，可用new Date(数字)进行恢复。
	 *
	 * @param aInstance
	 * @param o
	 * @return
	 * @throws java.lang.Exception
	 * @roseuid 4CF4C7D3034B
	 */
	public static String toJsonString(Object o) {
		return jsonUtil.toJsonString(o);
	}

	/**
	 * 对象转json，支持Map等集合类型，支持appframe生成的Bean对象。日期类型（Date、Time、Timestamp）输出格式为数字，可用new Date(数字)进行恢复。
	 * @param o
	 * @param properties 输出json时仅包含参数指定的属性，其它属性将被忽略
	 * @return String
	 * @throws java.lang.Exception
	 * @roseuid 4CF4D88D0233
	 */
	public static String toJsonString(Object o, String[] properties) {
		if (properties == null) {
			return jsonUtil.toJsonString(o);
		}
		return jsonUtil.toJsonString(o, properties);
	}

	/**
	 * 对象转json，支持Map等集合类型，支持appframe生成的Bean对象
	 * @param o
	 * @param dateFormat 指定日期类型（Date、Time、Timestamp）的输出格式
	 * @return String
	 * @roseuid 4F6C245502B9
	 */
	public static String toJsonString(Object o, String dateFormat) {
		return jsonUtil.toJsonString(o, dateFormat);
	}

	/**
	 * @param o
	 * @param dateFormat 指定日期类型（Date、Time、Timestamp）的输出格式
	 * @param properties 输出json时仅包含参数指定的属性，其它属性将被忽略
	 * @return String
	 * @roseuid 4F6C24FE0191
	 */
	public static String toJsonString(Object o, String dateFormat,
			String[] properties) {
		return jsonUtil.toJsonString(o, dateFormat, properties);
	}

	/**
	 * @param o
	 * @param dateFormat 指定日期类型（Date）的输出格式
	 * @param timeFormat 指定日期类型（Time）的输出格式
	 * @param timestampFormat 指定日期类型（Timestamp）的输出格式
	 * @return String
	 * @throws Exception
	 * @roseuid 4F6C2530036A
	 */
	public static String toJsonString(Object o, String dateFormat,
			String timeFormat, String timestampFormat) {
		return jsonUtil
				.toJsonString(o, dateFormat, timeFormat, timestampFormat);
	}

	/**
	 * @param o
	 * @param dateFormat 指定日期类型（Date）的输出格式
	 * @param timeFormat 指定日期类型（Time）的输出格式
	 * @param timestampFormat 指定日期类型（Timestamp）的输出格式
	 * @param properties 输出json时仅包含参数指定的属性，其它属性将被忽略
	 * @roseuid 4F6C256D029D
	 */
	public static String toJsonString(Object o, String dateFormat,
			String timeFormat, String timestampFormat, String[] properties) {
		return jsonUtil.toJsonString(o, dateFormat, timeFormat,
				timestampFormat, properties);
	}

	/**
	 * @param json
	 * @return Object 简单数据返回类型为List<Object>，对象数组的返回类型为List<Map<String,
	 *         Object>>，对象的返回类型为Map<String,Object>
	 * @throws Exception
	 * @roseuid 4F6C273D00FA
	 */
	public static Object parse(String json) {
		return jsonUtil.parseObject(json);
	}

	/**
	 * @param json
	 * @return List<Map<String,Object>>
	 * @throws Exception
	 * @roseuid 4F6C27770214
	 */
	public static List<Object> parseArray(String json) {
		return jsonUtil.parseArray(json);
	}

	/**
	 * @param json
	 * @return Map<String,Object>
	 * @throws Exception
	 * @roseuid 4F6C27CC0093
	 */
	public static Map<String, Object> parseObject(String json) {
		return jsonUtil.parseObject(json);
	}

	public static <T> T parseObject(String json,Class<T> clazz) {
		return jsonUtil.parse(json, clazz);
	}

	/**
	 * JSON串转换为Java泛型对象(jackson专有方法)
	 *
	 * @author lansw
	 *
	 * @param json
	 *            JSON字符串
	 * @param tr
	 *            TypeReference,例如: new TypeReference< List<FamousUser> >(){}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseObject(String json, TypeReference<T> tr) {
		ObjectMapper objMapp = JsonSupportJacksonImpl.getObjectMapper();
		if (json == null || "".equals(json.trim())) {
			return null;
		} else {
			try {
				return (T) objMapp.readValue(json, tr);
			} catch (Exception e) {
				throw new AppException(e);
			}
		}
	}

}