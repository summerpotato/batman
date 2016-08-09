package net.spirit.batman.util.json.support;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.deser.std.StdScalarDeserializer;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

import net.spirit.batman.exception.AppException;

@SuppressWarnings({"unchecked", "rawtypes"})
public class JsonSupportJacksonImpl implements IJsonSupport{

	static class DyDateFormatSerializer extends JsonSerializer<Date>{
		
		//TODO 规范常量定义
		static  String ymdhmsF = "yyyy/MM/dd HH:mm:ss" ;  
		static  String ymdF = "yyyy-MM-dd" ; 
		static  String hmsF = "hh:mm:ss" ; 
	
		@Override
		public void serialize(Date value, JsonGenerator jGen,
				SerializerProvider paramSerializerProvider) throws IOException,
				JsonProcessingException {
			String dateStr = null;
			if (value instanceof Date){
				dateStr = DateFormatUtils.format(value,ymdF);			
			}
			if (value instanceof Timestamp){
				 dateStr = DateFormatUtils.format(value,ymdhmsF);
			}
			if (value instanceof Time){
				dateStr = DateFormatUtils.format(value,hmsF);
			}
			jGen.writeObject(dateStr);
		}
	
	}
	
	static class TimestampDeserializer extends StdScalarDeserializer<Timestamp>{

		public TimestampDeserializer() { super(Timestamp.class); }

	    @Override
	    public java.sql.Timestamp deserialize(JsonParser jp, DeserializationContext ctxt)
	        throws IOException, JsonProcessingException
	    {
	    	Date date = _parseDate(jp, ctxt);
	    	if(date == null){
	    		return null;
	    	}
	        return new Timestamp(date.getTime());
	    }
		
	}

	static ObjectMapper objMapp = new ObjectMapper();   
	static {
		CustomSerializerFactory serializerFactory1 = new CustomSerializerFactory();
		serializerFactory1.addGenericMapping(Date.class, new DyDateFormatSerializer());
		objMapp.setSerializerFactory(serializerFactory1);
		// added 2013-6-5
		SimpleModule simpleModule = new SimpleModule("fixedTimestampDeserializerModule", new Version(1, 0, 0, null))
			.addDeserializer(Timestamp.class,new TimestampDeserializer());
		objMapp.registerModule(simpleModule);
	}
	
	/**
	 * 获取mapper
	 * 
	 * @return
	 */
	public static ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}
	
	public <T> T parse(String json,Class<T> clazz) {
		try {
			return objMapp.readValue(json, clazz);
		} catch (JsonParseException e) {
			throw new AppException(e);
		} catch (JsonMappingException e) {
			throw new AppException(e);
		} catch (IOException e) {
			throw new AppException(e);
		}
	}
	
	public Map<String, Object> parseObject(String json) {
		try {
			return objMapp.readValue(json, HashMap.class);
		} catch (JsonParseException e) {
			throw new AppException(e);
		} catch (JsonMappingException e) {
			throw new AppException(e);
		} catch (IOException e) {
			throw new AppException(e);
		}
	}
	
	public List<Object> parseArray(String json) {
		try {
			return objMapp.readValue(json, new TypeReference<List<HashMap>>(){});
		} catch (JsonParseException e) {
			throw new AppException(e);
		} catch (JsonMappingException e) {
			throw new AppException(e);
		} catch (IOException e) {
			throw new AppException(e);
		}   
	}
	
	public String toJsonString(Object o) {
		ObjectMapper objMapp = new ObjectMapper();   
		Writer writer = new StringWriter();
		try {
			objMapp.writeValue(writer, o);
		} catch (JsonGenerationException e) {
			throw new AppException(e);
		} catch (JsonMappingException e) {
			throw new AppException(e);
		} catch (IOException e) {
			throw new AppException(e);
		}
		return writer.toString();
	}

	public String toJsonString(Object o, String[] properties) {
		ObjectMapper objMapp = new ObjectMapper();
		StringWriter sw = new StringWriter();
	    FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", SimpleBeanPropertyFilter.serializeAllExcept(properties));
		try {
			objMapp.writer(filters).writeValue(sw, o);
		} catch (JsonGenerationException e) {
			throw new AppException(e);
		} catch (JsonMappingException e) {
			throw new AppException(e);
		} catch (IOException e) {
			throw new AppException(e);
		}
		return sw.toString();
	}

	public String toJsonString(Object o, String dateFormat){
		ObjectMapper objMapp = new ObjectMapper();
		DateFormat df = new SimpleDateFormat(dateFormat);
		objMapp.setDateFormat(df); 
		String result = null;
		try {
			result = objMapp.writeValueAsString(o);
		} catch (JsonGenerationException e) {
			throw new AppException(e);
		} catch (JsonMappingException e) {
			throw new AppException(e);
		} catch (IOException e) {
			throw new AppException(e);
		}
		return result;
	}

	public String toJsonString(Object o, String dateFormat, String[] properties) {
		ObjectMapper objMapp = new ObjectMapper();
		DateFormat df = new SimpleDateFormat(dateFormat);
		objMapp.setDateFormat(df); 
		FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", SimpleBeanPropertyFilter.serializeAllExcept(properties));
		String result = null;
		try {
			result = objMapp.writer(filters).writeValueAsString(o);
		} catch (JsonGenerationException e) {
			throw new AppException(e);
		} catch (JsonMappingException e) {
			throw new AppException(e);
		} catch (IOException e) {
			throw new AppException(e);
		}
		return result;	
	}

	public String toJsonString(Object o, String dateFormat, String timeFormat,
			String timestampFormat) {
		DyDateFormatSerializer.ymdhmsF = timestampFormat;
		DyDateFormatSerializer.ymdF = dateFormat;
		DyDateFormatSerializer.hmsF = timeFormat;
		ObjectMapper objMapp = new ObjectMapper();
		CustomSerializerFactory serializerFactory = new CustomSerializerFactory();
		serializerFactory.addGenericMapping(Date.class, new DyDateFormatSerializer());
		objMapp.setSerializerFactory(serializerFactory);
		Writer writer = new StringWriter();
		try {
			objMapp.writeValue(writer, o);
		} catch (JsonGenerationException e) {
			throw new AppException(e);
		} catch (JsonMappingException e) {
			throw new AppException(e);
		} catch (IOException e) {
			throw new AppException(e);
		}
		return writer.toString();
	}

	public String toJsonString(Object o, String dateFormat, String timeFormat,
			String timestampFormat, String[] properties) {
		DyDateFormatSerializer.ymdhmsF = timestampFormat;
		DyDateFormatSerializer.ymdF = dateFormat;
		DyDateFormatSerializer.hmsF = timeFormat;
		ObjectMapper objMapp = new ObjectMapper();
		CustomSerializerFactory serializerFactory = new CustomSerializerFactory();
		serializerFactory.addGenericMapping(Date.class, new DyDateFormatSerializer());
		objMapp.setSerializerFactory(serializerFactory);
		StringWriter sw = new StringWriter();
	    FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", SimpleBeanPropertyFilter.serializeAllExcept(properties));
		try {
			objMapp.writer(filters).writeValue(sw, o);
		} catch (JsonGenerationException e) {
			throw new AppException(e);
		} catch (JsonMappingException e) {
			throw new AppException(e);
		} catch (IOException e) {
			throw new AppException(e);
		}
		return sw.toString();
	}
	
}
