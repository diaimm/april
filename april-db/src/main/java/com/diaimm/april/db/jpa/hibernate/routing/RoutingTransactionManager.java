package com.diaimm.april.db.jpa.hibernate.routing;

import com.diaimm.april.db.routing.AbstractRoutingTransactionManager;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 9. 23
 * Time: 오후 1:09
 */
public class RoutingTransactionManager extends AbstractRoutingTransactionManager {
	@Override
	public PlatformTransactionManager createNewDefaultTransactionManager(DataSource dataSource) {
		BeanFactory parentBeanFactory = this.getApplicationContext().getParentBeanFactory();
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(parentBeanFactory.getBean(EntityManagerFactory.class));
		jpaTransactionManager.setDataSource(dataSource);
		jpaTransactionManager.setBeanFactory(parentBeanFactory);
		return jpaTransactionManager;
	}

	@Override
	public void clearTransactions(List<Map<String, Object>> failRollbackTargets) {
		for (Map<String, Object> target : failRollbackTargets) {
			JpaTransactionManager transactionManager = (JpaTransactionManager)target.get("txm");
			DefaultTransactionStatus status = (DefaultTransactionStatus)target.get("status");
			JdbcTransactionObjectSupport transactionObjectSupport = (JdbcTransactionObjectSupport)status.getTransaction();

			DataSourceUtils.resetConnectionAfterTransaction(transactionObjectSupport.getConnectionHolder().getConnection(),
				transactionObjectSupport.getPreviousIsolationLevel());
			DataSourceUtils.releaseConnection(transactionObjectSupport.getConnectionHolder().getConnection(), transactionManager.getDataSource());
			transactionObjectSupport.getConnectionHolder().clear();
		}
	}
}
