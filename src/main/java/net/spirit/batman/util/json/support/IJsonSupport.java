package net.spirit.batman.util.json.support;

import java.util.List;
import java.util.Map;

public interface IJsonSupport {
		/**
	     * 对象转json
	     * @param aInstance
	     * @param o
	     * @return
	     * @throws java.lang.Exception
	     * @roseuid 4CF4C7D3034B
	     */
	   public  String toJsonString(Object o) ;
	   
	   /**
	    * @param o
	    * @param properties
	    * @return String
	    * @throws java.lang.Exception
	    * @roseuid 4CF4D88D0233
	    */
	   public  String toJsonString(Object o, String[] properties) ;
	   
	   /**
	    * @param o
	    * @param dateFormat
	    * @return String
	    * @roseuid 4F6C245502B9
	    */
	   public String toJsonString(Object o, String dateFormat);
	   
	   /**
	    * @param o
	    * @param dateFormat
	    * @param properties
	    * @return String
	    * @roseuid 4F6C24FE0191
	    */
	   public String toJsonString(Object o, String dateFormat, String[] properties) ;
	   
	   /**
	    * @param o
	    * @param dateFormat
	    * @param timeFormat
	    * @param timestampFormat
	    * @return String
	    * @roseuid 4F6C2530036A
	    */
	   public String toJsonString(Object o, String dateFormat, String timeFormat, String timestampFormat);
	   
	   /**
	    * @param o
	    * @param dateFormat
	    * @param timeFormat
	    * @param timestampFormat
	    * @param properties
	    * @roseuid 4F6C256D029D
	    */
	   public String toJsonString(Object o, String dateFormat, String timeFormat, String timestampFormat, String[] properties);
	  	   
	   /**
	    * @param json
	    * @return Object
	    * @roseuid 4F6C273D00FA
	    */
	   public <T> T parse(String json,Class<T> clazz);
	  
	   
	   /**
	    * @param json
	    * @return Map<String,Object>
	    * @roseuid 4F6C27CC0093
	    */
	   public Map<String,Object> parseObject(String json);
	   
	   public List<Object> parseArray(String json);

}
