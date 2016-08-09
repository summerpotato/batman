package net.spirit.batman.util.ibatis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.spirit.batman.util.bean.PageBean;

import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultMapping;
import com.ibatis.sqlmap.engine.mapping.statement.RowHandlerCallback;
import com.ibatis.sqlmap.engine.scope.StatementScope;
import com.ibatis.sqlmap.engine.type.IntegerTypeHandler;

/**
 * <p>Title: 对SQL分页处理</p>
 * <p>Description: 拦截ibatis最终执行的SQL做分页处理</p>
 * 
 * @author SummerPotato
 */
public class PagingSqlHandler implements SqlHandler {

	/**
	 * PageBean中属性定义：
	 * 		pageNum或pageSize为null表示不执行分页查询
	 * 		totalRows = -1表示不查询总行数
	 */
	private static final ThreadLocal<PageBean<?>> pageinfo = new ThreadLocal<PageBean<?>>();

	private static class NestedObject {
		private Object value;
		public Object getValue() {
			return this.value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
	}

	private static final List<ResultMapping> countResultMapping = new ArrayList<ResultMapping>();

	static {
		ResultMapping mapping = new ResultMapping();
		mapping.setColumnIndex(1);
		mapping.setTypeHandler(new IntegerTypeHandler());
		countResultMapping.add(mapping);
	}


	public static void setPageInfo(PageBean<?> bean) {
		pageinfo.set(bean);
	}

	public static PageBean<?> getPageInfo() {
		return pageinfo.get();
	}

	
	public String parseQuerySql(StatementScope statementScope, Connection conn,
			String sql, Object[] parameters, int skipResults, int maxResults,
			RowHandlerCallback callback, ExtSqlExecutor sqlExecutor) throws SQLException {
		PageBean<?> bean = getPageInfo();
		if(bean != null) {
			setPageInfo(null);

			Integer totalRows = bean.getTotalRows();
			Integer pageNum = bean.getPageNum();
			Integer pageSize = bean.getPageSize();

			if(totalRows==null || totalRows.intValue()!=-1) {
				totalRows = queryCount(statementScope, conn, sql, parameters, SqlExecutor.NO_SKIPPED_RESULTS, SqlExecutor.NO_MAXIMUM_RESULTS, callback, sqlExecutor);
				bean.setTotalRows(totalRows);
				if(pageNum==null || pageSize==null) return null;
			}

			if(pageNum!=null && pageSize!=null) {
				int totalPages = totalRows%pageSize==0 ? totalRows/pageSize:totalRows/pageSize+1;
				if(pageNum <= 0) {
					pageNum = 1;
				}else if(pageNum > totalPages) {
					pageNum = totalPages;
				}
				if(pageNum == 0) pageNum = 1;

				bean.setPageNum(pageNum);
				bean.setPageSize(pageSize);
				bean.setTotalPages(totalPages);
				bean.setTotalRows(totalRows);

				/** 拼分页SQL **/

			}
		}
		return sql;
	}


	private int queryCount(StatementScope statementScope, Connection conn,
			String sql, Object[] params, int skipResults, int maxResults,
			RowHandlerCallback callback, ExtSqlExecutor sqlExecutor) throws SQLException {
		Iterator<SqlHandler> iterator = sqlExecutor.getSqlHandlerIterator();
		while(iterator.hasNext()) {
			SqlHandler handler = iterator.next();
			sql = handler.parseQuerySql(statementScope, conn, sql, params, skipResults, maxResults, callback, sqlExecutor);
		}

		if(sql == null) throw new SQLException(" is NULL sql by query-count! ");

		sql = " select count(1) from (" + sql + ") a";
		final NestedObject obj = new NestedObject();
		ResultMap resultMap = statementScope.getResultMap();

		ResultMap countResultMap = new ResultMap(resultMap.getDelegate());
		countResultMap.setResultMappingList(countResultMapping);
		statementScope.setResultMap(countResultMap);

		sqlExecutor.executeQuery(statementScope, conn, sql, params, SqlExecutor.NO_SKIPPED_RESULTS, SqlExecutor.NO_MAXIMUM_RESULTS, new RowHandlerCallback(null, null, null) {
			public void handleResultObject(StatementScope statementScope, Object[] results, ResultSet rs) throws SQLException {
				obj.setValue(results[0]);
			}
		});
		Integer count = (Integer)obj.getValue();
		statementScope.setResultMap(resultMap);

		return count;
	}

	public String parseUpdateSql(StatementScope statementScope,
			Connection conn, String sql, Object[] parameters,
			ExtSqlExecutor sqlExecutor) throws SQLException {
		return sql;
	}

}
