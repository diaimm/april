/*
 * @fileName : QUeryParserUtilTest.java
 * @date : 2013. 6. 7.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.db.mybatis.blocker;

import com.diaimm.april.db.mybatis.blocker.QueryParserUtil.TableNamePatterns;
import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * @author diaimm
 */
public class QueryParserUtilTest {
	@Test
	public void removeCommentTest() {
		String target1 = "-- test \n test2 \r -- test3 \n -- test4";
		Assert.assertEquals("test2", target1.replaceAll("--[^\n^\r]*[\n\r$]{0,1}", "").trim());

		String sampleQuery = getSampleQuery();

		String removeComments = QueryParserUtil.removeComments(sampleQuery);
		System.out.println(removeComments);
	}

	@Test
	public void normalizeSqlTest() {
		String sampleQuery = getSampleQuery();
		String normalizeSql = QueryParserUtil.normalizeSql(sampleQuery);
		String[] queries = normalizeSql.split(";");
		for (String query : queries) {
			System.out.println(query);
		}
	}

	@Test
	public void selectTableTest() {
		String sampleQuery = getSampleQuery();
		String normalizeSql = QueryParserUtil.normalizeSql(sampleQuery);
		String[] queries = normalizeSql.split(";");

		Set<String> tableNames = new HashSet<String>();
		for (String query : queries) {
			tableNames.addAll(QueryParserUtil.extractTargetTableNamesFromNormalizedQuery(TableNamePatterns.SELECT.getPatterns(), query));
		}

		for (String tableName : tableNames) {
			System.out.println(tableName);
		}
//        Assert.assertEquals(8, tableNames.size());
	}

	@Test
	public void insertTableTest() {
		String sampleQuery = getSampleQuery();
		String normalizeSql = QueryParserUtil.normalizeSql(sampleQuery);
		String[] queries = normalizeSql.split(";");

		Set<String> tableNames = new HashSet<String>();
		for (String query : queries) {
			tableNames.addAll(QueryParserUtil.extractTargetTableNamesFromNormalizedQuery(TableNamePatterns.INSERT.getPatterns(), query));
		}

//		Assert.assertEquals(4, tableNames.size());
//		Assert.assertTrue(tableNames.contains("INSERT_TABLE1"));
//		Assert.assertTrue(tableNames.contains("INSERT_TABLE2"));
//		Assert.assertTrue(tableNames.contains("INSERT_TABLE3"));
//		Assert.assertTrue(tableNames.contains("INSERT_TABLE4"));
	}

	@Test
	public void deleteTableTest() {
		String sampleQuery = getSampleQuery();
		String normalizeSql = QueryParserUtil.normalizeSql(sampleQuery);
		String[] queries = normalizeSql.split(";");

		Set<String> tableNames = new HashSet<String>();
		for (String query : queries) {
			tableNames.addAll(QueryParserUtil.extractTargetTableNamesFromNormalizedQuery(TableNamePatterns.DELETE.getPatterns(), query));
		}

		for (String tableName : tableNames) {
			System.out.println(tableName);
		}
		Assert.assertEquals(4, tableNames.size());
	}

	@Test
	public void updateTableTest() {
		String sampleQuery = getSampleQuery();
		String normalizeSql = QueryParserUtil.normalizeSql(sampleQuery);
		String[] queries = normalizeSql.split(";");

		Set<String> tableNames = new HashSet<String>();
		for (String query : queries) {
			tableNames.addAll(QueryParserUtil.extractTargetTableNamesFromNormalizedQuery(TableNamePatterns.UPDATE.getPatterns(), query));
		}

		for (String tableName : tableNames) {
			System.out.println(tableName);
		}
		Assert.assertEquals(4, tableNames.size());
	}

	/**
	 * @throws IOException
	 */
	private String getSampleQuery() {
		InputStream resourceAsStream = null;
		try {
			resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("com/diaimm/april/db/mybatis/blocker/sampleSql.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));

			StringBuffer sampleQuery = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sampleQuery.append(line).append("\n");
			}

			String ret = sampleQuery.toString();
			System.out.println(ret);
			return ret;
		} catch (Exception e) {
			return "";
		} finally {
			IOUtils.closeQuietly(resourceAsStream);
		}
	}
}
