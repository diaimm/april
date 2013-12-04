/*
 * @fileName : QueryBindingHelper.java
 * @date : 2013. 6. 5.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.db.mybatis.framework.listener;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.defaults.DefaultSqlSession.StrictMap;

import java.util.List;

/**
 * @author diaimm
 * 
 */
public class QueryBindingHelper {
	private QueryBindingHelper() {
		throw new UnsupportedOperationException();
	}

	public static Object wrapCollection(final Object object) {
		if (object instanceof List) {
			StrictMap<Object> map = new StrictMap<Object>();
			map.put("list", object);
			return map;
		} else if (object != null && object.getClass().isArray()) {
			StrictMap<Object> map = new StrictMap<Object>();
			map.put("array", object);
			return map;
		}
		return object;
	}

	public static BoundSql getBoundSql(String sqlId, Configuration configuration, Object queryParams) {
		MappedStatement mappedStatement = configuration.getMappedStatement(sqlId);
		return mappedStatement.getBoundSql(wrapCollection(queryParams));
	}
}
