/**
 * 
 */
package com.diaimm.april.db.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.joda.time.DateTime;

/**
 * @author stager0909
 *
 */
@MappedTypes(value = DateTime.class)
public class JodaDatetimeTypeHandler extends BaseTypeHandler<DateTime> {

	@Override
	public DateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
		java.sql.Timestamp sqlTimestamp = rs.getTimestamp(columnName);
		if (sqlTimestamp != null) {
			return new DateTime(sqlTimestamp.getTime());
		}
		return null;
	}

	@Override
	public DateTime getNullableResult(ResultSet arg0, int arg1) throws SQLException {
		return null;
	}

	@Override
	public DateTime getNullableResult(CallableStatement arg0, int arg1) throws SQLException {
		return null;
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, DateTime datetime, JdbcType jdbcType) throws SQLException {
		ps.setTimestamp(i, new java.sql.Timestamp((datetime.toDate()).getTime()));
	}

}
