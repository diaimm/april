package com.diaimm.april.db.mybatis.datasource;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

/**
 * {@link RoutingDataSource}를 위한 {@link TransactionStatus}
 * 
 * @author diaimm
 * @version $Rev$, $Date$
 */
public class RoutingTransactionStatus implements TransactionStatus {
	private boolean newTransaction;
	private boolean rollbackOnly;
	// key 관리와 순서 보장을 위해 TreeMap을 사용합니다.
	private final Map<String, TransactionStatus> transactionStatuses = new TreeMap<String, TransactionStatus>();
	private final Logger log = LoggerFactory.getLogger(RoutingTransactionStatus.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCompleted() {
		for (Entry<String, TransactionStatus> transactionStatus : transactionStatuses.entrySet()) {
			if (!transactionStatus.getValue().isRollbackOnly()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNewTransaction() {
		return newTransaction;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRollbackOnly() {
		return (isLocalRollbackOnly() || isGlobalRollbackOnly());
	}

	/**
	 * 하나의 {@link DataSource}에 대한 rollback 처리
	 * 
	 * @return rollback 여부
	 */
	public boolean isLocalRollbackOnly() {
		return this.rollbackOnly;
	}

	/**
	 * 여러 개의 하나의 {@link DataSource}에 대한 rollback 처리
	 * 
	 * @return rollback 여부
	 */
	public boolean isGlobalRollbackOnly() {
		for (Entry<String, TransactionStatus> transactionStatus : transactionStatuses.entrySet()) {
			if (transactionStatus.getValue().isRollbackOnly()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * {@link TransactionStatus}를 추가한다
	 * 
	 * @param transactionStatus
	 *            {@link TransactionStatus}
	 */
	public void addSubTransactionStatus(String key, TransactionStatus transactionStatus) {
		transactionStatuses.put(key, transactionStatus);
	}

	public Iterator<TransactionStatus> getSubTransactionStatus() {
		return transactionStatuses.values().iterator();
	}

	/**
	 * {@link TransactionStatus} 정보를 리턴한다
	 * 
	 * @param index
	 *            인덱스
	 * @return {@link TransactionStatus}
	 */
	public TransactionStatus getTransactionStatus(String key) {
		return transactionStatuses.get(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRollbackOnly() {
		this.rollbackOnly = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasSavepoint() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object createSavepoint() throws TransactionException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void releaseSavepoint(Object savepoint) throws TransactionException {
		log.debug("releaseSavePoint");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollbackToSavepoint(Object savepoint) throws TransactionException {
		log.debug("rollbackToSavepoint");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() {
		log.debug("flush");
	}
}
