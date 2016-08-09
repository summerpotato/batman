package net.spirit.batman.util.ibatis;

import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 通用的DAO方法封装
 * @author SummerPotato
 *
 * @param <K>
 * @param <T>
 */
public interface GenericDAO<K, T> {

	T find(String statement, K id) throws Exception;
	 
	T find(String statement, Map<String, Object> params) throws Exception;
 
	List<T> findByParams(String statement, Map<String, Object> params)
			throws Exception;
 
	List<T> findByObject(String statement, T t) throws Exception;

	K save(String statement, T t) throws Exception;

	void update(String statement, T t) throws Exception;

	void delete(String statement, K id) throws Exception;

	void deleteByParams(String statement, Map<String, Object> params)
			throws Exception;
	
	long getSequenceNextVal(String sequenceName) throws Exception;

	void execStatement(String statement, Map<String, Object> params) throws Exception;

	void deleteBatch(String statement, K[] id) throws Exception;

	void updateBatch(String statement, T[] t) throws Exception;

	K[] saveBatch(String statement, T[] t) throws Exception;
	
	void startBatch() throws Exception;
	
	void executeBatch() throws Exception;
	
	SqlMapClient getDBClient() throws Exception;
}
