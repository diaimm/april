package com.coupang.member.db.mybatis.datasource;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 라우팅을 위한 {@link DataSource}
 * 
 * @author diaimm
 * @version $Rev$, $Date$
 */
public class RoutingDataSource extends AbstractRoutingDataSource {
	private Map<?, ?> targetDataSources;

	public Map<?, ?> getTargetDataSources() {
		return targetDataSources;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setTargetDataSources(Map targetDataSources) {
		super.setTargetDataSources(targetDataSources);
		this.targetDataSources = targetDataSources;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		return RoutingDataSourceCurrentDatasourceHolder.getContext();
	}

	/**
	 * 현재 {@link DataSource}를 리턴한다
	 * 
	 * @return 현재 {@link DataSource}
	 */
	public DataSource getCurrentDataSource() {
		return determineTargetDataSource();
	}
}
