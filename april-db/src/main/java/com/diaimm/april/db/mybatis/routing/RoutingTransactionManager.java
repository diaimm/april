package com.diaimm.april.db.mybatis.routing;

import com.diaimm.april.db.routing.AbstractRoutingTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author diaimm
 * @version $Rev$, $Date$
 */
public class RoutingTransactionManager extends AbstractRoutingTransactionManager {
	@Override
	public void clearTransactions(List<Map<String, Object>> failRollbackTargets) {
		for (Map<String, Object> target : failRollbackTargets) {
			DataSourceTransactionManager transactionManager = (DataSourceTransactionManager)target.get("txm");
			DefaultTransactionStatus status = (DefaultTransactionStatus)target.get("status");
			JdbcTransactionObjectSupport transactionObjectSupport = (JdbcTransactionObjectSupport)status.getTransaction();

			DataSourceUtils.resetConnectionAfterTransaction(transactionObjectSupport.getConnectionHolder().getConnection(),
				transactionObjectSupport.getPreviousIsolationLevel());
			DataSourceUtils.releaseConnection(transactionObjectSupport.getConnectionHolder().getConnection(), transactionManager.getDataSource());
			transactionObjectSupport.getConnectionHolder().clear();
		}
	}

	@Override
	public PlatformTransactionManager createNewDefaultTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
