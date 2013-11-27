/*
 * @fileName : QUeryParserUtil.java
 * @date : 2013. 6. 7.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.db.mybatis.blocker;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author diaimm
 */
public class QueryParserUtil {
	private QueryParserUtil() {
		throw new UnsupportedOperationException();
	}

	/**
	 * table 명을 normalize합니다.
	 *
	 * @param tableName
	 * @return
	 */
	public static String normalizeTableName(String tableName) {
		if (StringUtils.isBlank(tableName)) {
			return tableName;
		}

		return StringUtils.upperCase(tableName);
	}

	public static Map<String, Set<SqlCommandType>> extractTableNames(String sql) {
		Map<String, Set<SqlCommandType>> ret = new HashMap<String, Set<SqlCommandType>>();
		if (StringUtils.isBlank(sql)) {
			return ret;
		}

		String normalizedSql = normalizeSql(sql);
		for (TableNamePatterns tableNamePattern : TableNamePatterns.values()) {
			Set<String> tableNames = extractTargetTableNamesFromNormalizedQuery(tableNamePattern.patterns, normalizedSql);
			for (String tableName : tableNames) {
				if (!ret.containsKey(tableName)) {
					ret.put(tableName, new HashSet<SqlCommandType>());
				}

				ret.get(tableName).add(tableNamePattern.commandType);
			}
		}

		return ret;
	}

	/**
	 * @param patterns
	 * @param query
	 * @return
	 */
	static Set<String> extractTargetTableNamesFromNormalizedQuery(String[] patterns, String query) {
		Set<String> retTableNames = new HashSet<String>();
		for (String pattern : patterns) {
			Pattern compile = Pattern.compile(pattern);
			Matcher matcher = compile.matcher(query);
			while (matcher.find()) {
				if (matcher.group(1) != null) {
					String[] tableNames = matcher.group(1).split(",");
					for (String tableName : tableNames) {
						tableName = tableName.replaceAll("\\^AS\\^\\p{Graph}*$", "");
						tableName = tableName.replaceAll("^.*\\.", "");
						if (StringUtils.isNotBlank(tableName)) {
							retTableNames.add(tableName);
						}
					}
				}
			}
		}

		return retTableNames;
	}

	static String normalizeSql(String sql) {
		if (StringUtils.isBlank(sql)) {
			return sql;
		}

		sql = sql.toUpperCase();
		// 주석 제거
		sql = removeComments(sql);
		sql = sql.replaceAll("[\n\r\t ]+", " ");
		sql = sql.replaceAll(", ", ",");
		sql = sql.replaceAll(" AS ", "^AS^");
		sql = sql.trim();

		return sql;
	}

	/**
	 * @param sql
	 * @return
	 */
	static String removeComments(String sql) {
		sql = sql.replaceAll("/\\*.*\\*/", "");
		sql = sql.replaceAll("--[^\n^\r]*[\n\r$]{0,1}", "");
		sql = sql.replaceAll("//[^\n^\r]*[\n\r$]{0,1}", "");
		return sql;
	}

	static enum TableNamePatterns {
		SELECT(SqlCommandType.SELECT, "SELECT .* FROM ([\\p{Graph}&&[^(]]*)[ ]*", " JOIN ([\\p{Graph}&&[^(]]*)[ ]*"),
		INSERT(SqlCommandType.INSERT, "INSERT INTO ([\\p{Graph}&&[^(]]*)[ ]*"),
		UPDATE(SqlCommandType.UPDATE, "UPDATE ([\\p{Graph}&&[^(]]*)[ ]*"),
		DELETE(SqlCommandType.DELETE, "DELETE FROM ([\\p{Graph}&&[^(]]*)[ ]*");
		private final String[] patterns;
		private final SqlCommandType commandType;

		TableNamePatterns(SqlCommandType commandType, String... patterns) {
			this.commandType = commandType;
			this.patterns = patterns;
		}

		/**
		 * @return the patterns
		 */
		String[] getPatterns() {
			return patterns;
		}
	}
}
