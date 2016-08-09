package net.spirit.batman.util.ibatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 通用的DAO方法封装
 * @author SummerPotato
 *
 * @param <ID>
 * @param <T>
 */
public class GenericDAOImpl4Spring<ID, T> extends SqlMapClientDaoSupport implements GenericDAO<ID, T> {

	private static final Logger log = Logger.getLogger(GenericDAOImpl4Spring.class);

	private void writeLog(long ms) throws Exception{
		log.debug("执行SQL耗时："+ms+"(毫秒)");
	}
	
	public void delete(String statement, ID id) throws Exception {
		long begin = System.currentTimeMillis();
		getSqlMapClientTemplate().delete(statement, id);
		long end = System.currentTimeMillis();
		writeLog(end-begin);
	}

	public void deleteByParams(String statement, Map<String, Object> params)
			throws Exception {
		long begin = System.currentTimeMillis();
		getSqlMapClientTemplate().delete(statement, params);
		long end = System.currentTimeMillis();
		writeLog(end-begin);
	}

	public T find(String statement, ID id) throws Exception {
		long begin = System.currentTimeMillis();
		T t = (T) getSqlMapClientTemplate().queryForObject(statement, id);
		long end = System.currentTimeMillis();
		writeLog(end-begin);
		return t;
	}

	public List<T> findByObject(String statement, T t) throws Exception {
		long begin = System.currentTimeMillis();
		List<T> list = getSqlMapClientTemplate().queryForList(statement, t);
		long end = System.currentTimeMillis();
		writeLog(end-begin);
		return list;
	}

	public T find(String statement, Map<String, Object> params)
			throws Exception {
		List ls = findByParams(statement, params);
		T result = null;
		if (ls != null && ls.size() > 0)
			result = (T) ls.get(0);
		return result;
	}

	public List<T> findByParams(String statement, Map<String, Object> params)
			throws Exception {
		long begin = System.currentTimeMillis();
		List<T> list = getSqlMapClientTemplate().queryForList(statement, params);
		long end = System.currentTimeMillis();
		writeLog(end-begin);
		return list;
	}

	public ID save(String statement, T t) throws Exception {
		long begin = System.currentTimeMillis();
		ID id = (ID) getSqlMapClientTemplate().insert(statement, t);
		long end = System.currentTimeMillis();
		writeLog(end-begin);
		return id;
	}

	public void update(String statement, T t) throws Exception {
		long begin = System.currentTimeMillis();
		getSqlMapClientTemplate().update(statement, t);
		long end = System.currentTimeMillis();
		writeLog(end-begin);
	}

	public void execStatement(String statement, Map<String, Object> params)
			throws Exception {
		long begin = System.currentTimeMillis();
		getSqlMapClientTemplate().update(statement, params);
		long end = System.currentTimeMillis();
		writeLog(end-begin);
	}

	public long getSequenceNextVal(String sequenceName) throws Exception {
		long begin = System.currentTimeMillis();
		Object o = getSqlMapClientTemplate().queryForObject("FRAME_COMMON.getSequenceNextVal",
				sequenceName);
		long end = System.currentTimeMillis();
		writeLog(end-begin);
		if (o != null) {
			return Long.parseLong(o.toString());
		}
		return 1;
	}

	public int getTotalCount(String statement, Map<String, Object> params)
			throws Exception {
		long begin = System.currentTimeMillis();
		Object o = getSqlMapClientTemplate().queryForObject(statement,
				params);
		long end = System.currentTimeMillis();
		writeLog(end-begin);
		if(o != null){
			return Integer.parseInt(o.toString());
		}
		return 0;
	}

	public void deleteBatch(String statement, ID[] id) throws Exception {
		getSqlMapClient().startBatch();
		for (int i = 0; i < id.length; i++) {
			getSqlMapClient().delete(statement, id[i]);
		}
		long begin = System.currentTimeMillis();
		getSqlMapClient().executeBatch();
		long end = System.currentTimeMillis();
		writeLog(end-begin);
	}

	public ID[] saveBatch(String statement, T[] t) throws Exception {
		getSqlMapClient().startBatch();
		List<ID> keys = new ArrayList<ID>();
		for (int i = 0; i < t.length; i++) {
			ID id = (ID) getSqlMapClient().insert(statement, t[i]);
			if(id != null){
				keys.add(id);
			}
		}
		long begin = System.currentTimeMillis();
		getSqlMapClient().executeBatch();
		long end = System.currentTimeMillis();
		writeLog(end-begin);
		if(keys != null && keys.size() != 0){
			return (ID[])keys.toArray();
		} else {
			return null;
		}
	}

	public void updateBatch(String statement, T[] t) throws Exception {
		getSqlMapClient().startBatch();
		for (int i = 0; i < t.length; i++) {
			getSqlMapClientTemplate().update(statement, t[i]);
		}
		long begin = System.currentTimeMillis();
		getSqlMapClient().executeBatch();
		long end = System.currentTimeMillis();
		writeLog(end-begin);
	}

	public void executeBatch() throws Exception {
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}

	public void startBatch() throws Exception {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
	}
	
	public SqlMapClient getDBClient() throws Exception{
		return getSqlMapClientTemplate().getSqlMapClient();
	}
	
}
