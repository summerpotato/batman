package net.spirit.batman.util.json;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.util.JSONPObject;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

/**
 * 简单封装Jackson(v-1.9.13), 实现JSON String<->Java Object的Mapper.
 *
 * 封装不同的输出风格, 使用不同的builder函数创建实例.
 *
 */
public class JsonMapper {

    private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);

    private static final JsonMapper NON_EMPTY_MAPPER =  new JsonMapper(JsonSerialize.Inclusion.NON_EMPTY);

    private static final JsonMapper NON_DEFAULT_MAPPER = new JsonMapper(JsonSerialize.Inclusion.NON_DEFAULT);
    
    private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static JsonMapper ACCEPT_SINGLE_AS_ARRAY_MAPPER = null;

    //对时间使用字符串格式化
    private static JsonMapper DATE_MAPPER = null;

    private ObjectMapper mapper;

    public JsonMapper() {
        this(null);
    }

    public JsonMapper(JsonSerialize.Inclusion include) {
        mapper = new ObjectMapper();
        // 设置输出时包含属性的风格
        if (include != null) {
            mapper.setSerializationInclusion(include);
        }
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用.
     * 建议本实例在项目中共享使用，不能修改其配置
     */
    public static JsonMapper nonEmptyMapper() {
        return JsonMapper.NON_EMPTY_MAPPER;
    }

    /**
     * 会自动将"{}"转换成数组中的一个元素
     * 注意：PHP在需要返回数组时有时会返回“{}”，可使用本Mapper反序列化，并对序列化后的Array或数组进行处理
     */
    public static JsonMapper acceptSingleAsArrayMapper() {
        if (JsonMapper.ACCEPT_SINGLE_AS_ARRAY_MAPPER == null) {
            synchronized (JsonMapper.class) {
                JsonMapper jsonMapper = new JsonMapper(JsonSerialize.Inclusion.NON_EMPTY);
                jsonMapper.getMapper().configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

                JsonMapper.ACCEPT_SINGLE_AS_ARRAY_MAPPER = jsonMapper;
            }
        }
        return JsonMapper.ACCEPT_SINGLE_AS_ARRAY_MAPPER;
    }

    /**
     * 创建只输出初始值被改变的属性到Json字符串的Mapper, 最节约的存储方式，建议在内部接口中使用。
     * 建议本实例在项目中共享使用，不能修改其配置
     */
    public static JsonMapper nonDefaultMapper() {
        return JsonMapper.NON_DEFAULT_MAPPER;
    }

    /**
     * 使用字符串格式(yyyy-MM-dd HH:mm:ss)保存日期属性
     */
    public static JsonMapper dateMapper() {
        if (JsonMapper.DATE_MAPPER == null) {
            synchronized (JsonMapper.class) {
                JsonMapper jsonMapper = new JsonMapper(JsonSerialize.Inclusion.NON_EMPTY);
                jsonMapper.getMapper().setDateFormat((DateFormat)DATE_TIME_FORMAT.clone());
                JsonMapper.DATE_MAPPER = jsonMapper;
            }
        }
        return JsonMapper.DATE_MAPPER;
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]".
     */
    public String toJson(Object object) {

        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.error("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     *
     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     *
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String, JavaType)
     */
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.error("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>, contructCollectionType()或contructMapType()构造类型, 然后调用本函数.
     *
     */
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.error("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 构造Collection类型.
     */
    public JavaType contructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    /**
     * 构造Map类型.
     */
    public JavaType contructMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        return mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
    }

    /**
     * 当JSON里只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.
     */
    public void update(String jsonString, Object object) {
        try {
            mapper.readerForUpdating(object).readValue(jsonString);
        } catch (JsonProcessingException e) {
            logger.error("update json string:" + jsonString + " to object:" + object + " error.", e);
        } catch (IOException e) {
            logger.error("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
    }

    /**
     * 輸出JSONP格式數據.
     */
    public String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * 設定是否使用Enum的toString函數來讀寫Enum, 為False時時使用Enum的name()函數來讀寫Enum, 默認為False. 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.
     */
    public void enableEnumUseToString() {
        mapper.enable(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING);
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return mapper;
    }
    
    
    public static DateFormat dateFormat() {
        // clone because DateFormat is not thread-safe
        return (DateFormat) DATE_FORMAT.clone();
    }

    public static DateFormat dateTimeFormat() {
        // clone because DateFormat is not thread-safe
        return (DateFormat) DATE_TIME_FORMAT.clone();
    }
    
}
