package net.spirit.batman.util.ibatis;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.mapping.statement.RowHandlerCallback;
import com.ibatis.sqlmap.engine.scope.StatementScope;

/**
 * <p>Title: 扩展的SqlExecutor</p>
 * <p>Description: 由spring注入给ExtSqlMapClientTemplate, 它将替换ibatis中原生的SqlExecutor</p>
 * 
 * @author SummerPotato
 */
public class ExtSqlExecutor extends SqlExecutor {
	
	SqlExecutor sqlExecutor;
	
	/** SqlHandler容器 **/
	private List<SqlHandler> sqlHandlers = new ArrayList<SqlHandler>();
	
	
	/**
	 * 添加SqlHandler
	 * @param handler
	 */
	public void addSqlHandler(SqlHandler handler) {
		sqlHandlers.add(handler);
	}
	
	
	/**
	 * 删除指定SqlHandler
	 * @param handler
	 */
	public void removeSqlHandler(SqlHandler handler) {
		synchronized(sqlHandlers) {
			sqlHandlers.remove(handler);
		}
	}
	
	
	/**
	 * 清除所有SqlHandler
	 */
	public void clearSqlHandler() {
		synchronized(sqlHandlers) {
			sqlHandlers.clear();
		}
	}
	
	
	/**
	 * 重新设置SqlHandler
	 * @param handlers
	 */
	public void setSqlHandlers(List<SqlHandler> handlers) {
		clearSqlHandler();
		this.sqlHandlers.addAll(handlers);
	}
	
	
	/**
	 * 获取SqlHandler容器中所有SqlHandler迭代器
	 * @return
	 */
	public Iterator<SqlHandler> getSqlHandlerIterator() {
		return sqlHandlers.iterator();
	}
	
	
	public void executeQuery(StatementScope statementScope, Connection conn, String sql, Object[] parameters, int skipResults, int maxResults, RowHandlerCallback callback) throws SQLException {
		if(sqlHandlers.size() > 0) {
			Iterator<SqlHandler> iteraotr = getSqlHandlerIterator();
			while(iteraotr.hasNext()) {
				SqlHandler handler = iteraotr.next();
				sql = handler.parseQuerySql(statementScope, conn, sql, parameters, skipResults, maxResults, callback, this);
				if(sql == null) break;
			}
		}
		
		/** 如果SQL为null终于执行 **/
		if(sql != null) {
			if (sqlExecutor==null) {
				super.executeQuery(statementScope, conn, sql, parameters, skipResults, maxResults, callback);
			} else {
				sqlExecutor.executeQuery(statementScope, conn, sql, parameters, skipResults, maxResults, callback);
			}
		}
	}


	public SqlExecutor getSqlExecutor() {
		return sqlExecutor;
	}


	public void setSqlExecutor(SqlExecutor sqlExecutor) {
		this.sqlExecutor = sqlExecutor;
	}


	@Override
	public int executeUpdate(StatementScope statementScope, Connection conn,
			String sql, Object[] parameters) throws SQLException {
		if(sqlHandlers.size() > 0) {
			Iterator<SqlHandler> iteraotr = getSqlHandlerIterator();
			while(iteraotr.hasNext()) {
				SqlHandler handler = iteraotr.next();
				sql = handler.parseUpdateSql(statementScope, conn, sql, parameters, this);
				if(sql == null) break;
			}
		}
		
		if(sql != null) {
			if (sqlExecutor==null) {
				return super.executeUpdate(statementScope, conn, sql, parameters);
			} else {
				return sqlExecutor.executeUpdate(statementScope, conn, sql, parameters);
			}
		}
		return 0;
	}


	@Override
	public void addBatch(StatementScope statementScope, Connection conn,
			String sql, Object[] parameters) throws SQLException {
		if(sqlHandlers.size() > 0) {
			Iterator<SqlHandler> iteraotr = getSqlHandlerIterator();
			while(iteraotr.hasNext()) {
				SqlHandler handler = iteraotr.next();
				sql = handler.parseUpdateSql(statementScope, conn, sql, parameters, this);
				if(sql == null) break;
			}
		}
		
		if(sql != null) {
			if (sqlExecutor==null) {
				super.addBatch(statementScope, conn, sql, parameters);
			} else {
				sqlExecutor.addBatch(statementScope, conn, sql, parameters);
			}
		}
		
	}
	
}
