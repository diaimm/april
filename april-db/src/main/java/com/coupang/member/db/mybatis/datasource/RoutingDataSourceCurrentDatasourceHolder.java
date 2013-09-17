package com.coupang.member.db.mybatis.datasource;

/**
 * {@link RoutingDataSource}를 위한 Context Holder
 * 
 * @author diaimm
 * @version $Rev$, $Date$
 */
final class RoutingDataSourceCurrentDatasourceHolder {
	private static final ThreadLocal<Object> HOLDER = new ThreadLocal<Object>();

	private RoutingDataSourceCurrentDatasourceHolder() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Context를 설정한다
	 * 
	 * @param context
	 *            Context
	 */
	public static void setContext(Object context) {
		HOLDER.set(context);
	}

	/**
	 * Context를 가져온다
	 * 
	 * @return Context
	 */
	public static Object getContext() {
		return HOLDER.get();
	}

	/**
	 * Context 정보를 삭제한다
	 */
	public static void clear() {
		HOLDER.remove();
	}
}
