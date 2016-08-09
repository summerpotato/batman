package net.spirit.batman.util.ibatis;

import java.sql.Connection;
import java.sql.SQLException;

import com.ibatis.sqlmap.engine.mapping.statement.RowHandlerCallback;
import com.ibatis.sqlmap.engine.scope.StatementScope;

/**
 * <p>Description: 拦截ibatis最终执行SQL时, 暴露于外部对SQL处理接口</p>
 * 
 * @author SummerPotato
 */
public interface SqlHandler {
	
	/**
	 * 解析SQL
	 * @param statementScope: 操用域
	 * @param conn: 数据库连接
	 * @param sql: 最终执行的SQL
	 * @param parameters: SQL中被转化为?后所对应的params
	 * @param skipResults: 分页参数(跳过多少行)
	 * @param maxResults: 分页参数(最大查多少行)
	 * @param callback: 查询回调方法
	 * @param sqlExecutor: ExtSqlExecutor对象
	 * @return
	 */
	public String parseQuerySql(StatementScope statementScope, Connection conn, String sql, Object[] parameters, 
			int skipResults, int maxResults, RowHandlerCallback callback, ExtSqlExecutor sqlExecutor) throws SQLException ;
	
	/**
	 * @param sql
	 * @return
	 */
	String parseUpdateSql(StatementScope statementScope, Connection conn, String sql, Object[] parameters, ExtSqlExecutor sqlExecutor) throws SQLException ;
	
}
