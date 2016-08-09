package net.spirit.batman.util.misc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.spirit.batman.util.bean.PageBean;

import org.apache.commons.lang.StringUtils;

public class MapOTransformer {

	/**
	 * 根据map提供数据，生成目标class对象
	 * @param dataMap
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static Object toObject(Map<String,Object> dataMap, Class<?> c)
			throws Exception {
		
		if (dataMap == null || dataMap.size() == 0) {
			return null;
		}
		
		Object o = Class.forName(c.getName()).newInstance();
		
		List<Method> setMethodList = retrieveSetMethods(c.getMethods(), "set");
		
		for (Iterator<Entry<String,Object>> i = dataMap.entrySet().iterator(); i.hasNext(); ) {
			
			Entry<String, Object> e = i.next();
			
			String key = e.getKey();
			
			Object value = e.getValue();
			
			for (Iterator<Method> j = setMethodList.iterator(); j.hasNext(); ) {
				Method method = j.next();
				if (method.getName().equalsIgnoreCase("set" + StringUtils.capitalize(key))) {
					
					method.invoke(o, new Object[]{value});
				}
			}
		}
		return o;
	}

	/**
	 * 将标准bean通过get方法转化为map对象，key为属性名，value为属性值
	 * @param object
	 * @return
	 */
	public static Map<String, Object> toMap(Object object) throws Exception{
		
		List<Method> list = retrieveSetMethods(object.getClass().getMethods(), "get");
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		for (Iterator<Method> i = list.iterator(); i.hasNext();) {
			
			Method method = i.next();
			
			if (method.getParameterTypes().length == 0) {
				
				String methodName = method.getName();
				
				dataMap.put(StringUtils.uncapitalize(methodName.substring(3)), method.invoke(object, new Object[]{}));
			}
		}
		return dataMap;
	}
	
	/**
	 * 从方法数组中获取所有以prefix开头的方法
	 * @param methods
	 * @return
	 */
	private static List<Method> retrieveSetMethods(Method[] methods, String prefix) {
		
		List<Method> methodList = new LinkedList<Method>();
		
		for (int i=0; i<methods.length; i++) {
			if (methods[i].getName().indexOf(prefix) == 0) {
				methodList.add(methods[i]);
			}
		}
		return methodList;
	}
	
	//Test
	public static void main(String[] args) {
		PageBean pb = new PageBean();
		pb.setPageNum(1);
		pb.setPageSize(10);
		pb.setTotalPages(5);
		pb.setTotalRows(50);
		
		try {
			Map dataMap = toMap(pb);
			System.out.println(dataMap);
			
			Object o = toObject(dataMap, PageBean.class);
			System.out.println(o);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
