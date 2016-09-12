package net.spirit.batman.util.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * protostuff序列化工具类
 * 
 * @author SummerPotato
 * @date 2016-8-16
 */
public class ProtostuffSerializerUtils {

	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();
	
	/**
	 * 如果一个类没有参数为空的构造方法，那么直接调用newInstance()方法来得到一个实例对象的时候会抛异常。
	 * 像私有构造方法，带参数的构造等不能通过class.newInstance()实例化。
	 * Objenesis不用构造方法来实例化对象， 优于 Java 反射。
	 */
	private static Objenesis objenesis = new ObjenesisStd(true);
	
	
	/**
	 * 构建schema，可以将使用过的类对应的schema能被缓存起来，降低耗时
	 * @param clazz
	 * @return
	 */
	private static <T> Schema<T> getSchema(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.getSchema(clazz);
            //schema = RuntimeSchema.createFrom(clazz);
            if (schema != null) {
                cachedSchema.put(clazz, schema);
            }
        }
        return schema;
    }

    /**
     * 序列化(对象 --> 字节数组)
     *
     * @param obj
     * @return
     */
    public static <T> byte[] serializer(T obj) {
    	if(obj == null){
    		return null;
    	}
    	
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(clazz);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化(字节数组 --> 对象)
     *
     * @param data
     * @param clazz
     * @return
     */
    public static <T> T deserializer(byte[] data, Class<T> clazz) {
    	if(data == null || data.length == 0){
    		return null;
    	}
    	
        try {
        	T obj = (T) objenesis.newInstance(clazz);	//clazz.newInstance();
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(data, obj, schema);
            return obj;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
    
    
    
    public static <T> byte[] listSerializer(List<T> objList) {
        if (objList == null || objList.isEmpty()) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(objList.get(0).getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(1024);
        byte[] protostuff = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ProtostuffIOUtil.writeListTo(bos, objList, schema, buffer);
            protostuff = bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            buffer.clear();
            try {
                if(bos!=null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return protostuff;
    }
    
    public static <T> List<T> listDeserializer(byte[] dataArrayOfByte, Class<T> targetClass) {
        if (dataArrayOfByte == null || dataArrayOfByte.length == 0) {
            return null;
        }
        
        Schema<T> schema = RuntimeSchema.getSchema(targetClass);
        List<T> result = null;
        try {
            result = ProtostuffIOUtil.parseListFrom(new ByteArrayInputStream(dataArrayOfByte), schema);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }
	
	
}
