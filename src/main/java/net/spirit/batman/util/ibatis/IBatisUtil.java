package net.spirit.batman.util.ibatis;

import java.util.List;

import net.spirit.batman.util.bean.PageBean;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * ibatis工具类
 * @author SummerPotato
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class IBatisUtil {
	
	/**
	 * 分页查询
	 * @param smct
	 * @param statementName
	 * @param start
	 * @param pageSize
	 * @return
	 * @throws DataAccessException
	 */
	public static List queryPageByStartNum(SqlMapClientTemplate smct,
			String statementName, int start, int pageSize) throws DataAccessException {
		return queryPage(smct, statementName, null, start/pageSize+1, pageSize, false).getData();
	}
	
	
	/**
	 * 分页查询
	 * @param smct
	 * @param statementName
	 * @param parameterObject
	 * @param start
	 * @param pageSize
	 * @return
	 * @throws DataAccessException
	 */
	public static List queryPageByStartNum(SqlMapClientTemplate smct,
			String statementName, Object parameterObject, int start, int pageSize) throws DataAccessException {
		return queryPage(smct, statementName, parameterObject, start/pageSize+1, pageSize, false).getData();
	}
	
	
	public static PageBean queryPageByStartNumWithCount(SqlMapClientTemplate smct,
			String statementName, int start, int pageSize) throws DataAccessException {
		return queryPage(smct, statementName, null, start/pageSize+1, pageSize, true);
	}
	
	
	public static PageBean queryPageByStartNumWithCount(SqlMapClientTemplate smct,
			String statementName, Object parameterObject, int start, int pageSize) throws DataAccessException {
		return queryPage(smct, statementName, parameterObject, start/pageSize+1, pageSize, true);
	}
	
	
	public static List queryPage(SqlMapClientTemplate smct,
			String statementName, int pageNum, int pageSize) throws DataAccessException {
		return queryPage(smct, statementName, null, pageNum, pageSize, false).getData();
	}
	
	
	public static List queryPage(SqlMapClientTemplate smct,
				String statementName, Object parameterObject, int pageNum, int pageSize)	throws DataAccessException {
		return queryPage(smct, statementName, parameterObject, pageNum, pageSize, false).getData();
	}
	
	
	public static PageBean queryPageWithCount(SqlMapClientTemplate smct,
			String statementName, int pageNum, int pageSize) throws DataAccessException {
		return queryPage(smct, statementName, null, pageNum, pageSize, true);
	}
	
	
	public static PageBean queryPageWithCount(SqlMapClientTemplate smct,
			String statementName, Object parameterObject, int pageNum, int pageSize) throws DataAccessException {
		return queryPage(smct, statementName, parameterObject, pageNum, pageSize, true);
	}
	
	
	private static PageBean queryPage(SqlMapClientTemplate smct,
				String statementName, Object parameterObject, int pageNum, int pageSize, boolean appTotalRows) throws DataAccessException {
		PageBean bean = new PageBean(pageNum, pageSize, null, appTotalRows?null:-1, null);
		PagingSqlHandler.setPageInfo(bean);
		
		/** 临时 **/
		int skipResults = (pageNum-1) * pageSize;
		int maxResults = pageSize;
		
		List data = smct.queryForList(statementName, parameterObject, skipResults, maxResults);
		bean.setData(data);
		return bean;
	}
	
	
	public static int queryCount(SqlMapClientTemplate smct, String statementName) throws DataAccessException {
		return queryCount(smct, statementName, null);
	}
	
	
	public static int queryCount(SqlMapClientTemplate smct, String statementName, Object parameterObject)	throws DataAccessException {
		PageBean bean = new PageBean(null, null, null, null, null);
		PagingSqlHandler.setPageInfo(bean);
		smct.queryForList(statementName, parameterObject);
		return bean.getTotalRows();
	}
	
}
