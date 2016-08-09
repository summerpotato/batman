package net.spirit.batman.util.ibatis;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;

/**
 * <p>Title: 扩展的SqlMapClientTemplate</p>
 * <p>Description: 扩展SqlMapClientTemplate功能, 其它项目中如用ibatis应当都使用或继承该sqlMapClientTemplate</p>
 * 
 * @author SummerPotato
 */
public class ExtSqlMapClientTemplate extends SqlMapClientTemplate {

	/** 替换ibatis中原生的SqlExecutor **/
	private SqlExecutor sqlExecutor;

	public SqlExecutor getSqlExecutor() {
		return sqlExecutor;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setSqlExecutor(SqlExecutor sqlExecutor) throws Exception {
		SqlMapExecutorDelegate delegate = ((SqlMapClientImpl)getSqlMapClient()).getDelegate();
    	Class delegateclass = delegate.getClass();
    	try {
    		Method m = delegateclass.getDeclaredMethod("setSqlExecutor", SqlExecutor.class);
        	if(!Modifier.isPublic(m.getModifiers())) {
        		 m.setAccessible(true);
        	}
        	m.invoke(delegate, sqlExecutor);
    	}catch(Exception e) {
    		Field field = delegateclass.getDeclaredField("sqlExecutor");   
    		if (!Modifier.isPublic(field.getModifiers())) {   
    			field.setAccessible(true);   
    		}
    		field.set(delegate, sqlExecutor);   
    	}
		this.sqlExecutor = sqlExecutor;
	}
	
}
