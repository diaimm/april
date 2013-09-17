/*
 * @fileName : BlockQueryManagerTest.java
 * @date : 2013. 6. 5.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.db.mybatis.blocker;

import java.util.Set;

import junit.framework.Assert;

import org.apache.ibatis.mapping.SqlCommandType;
import org.junit.Test;

import com.coupang.member.db.mybatis.blocker.BlockQueryManager;
import com.coupang.member.db.mybatis.blocker.QueryParserUtil;
import com.coupang.member.db.mybatis.blocker.BlockQueryManager.BlockQueryManagerRequestCommand;

/**
 * @author diaimm
 */
public class BlockQueryManagerTest {
	@Test
	public void normalizeTest() {
		BlockQueryManager target = new BlockQueryManager();
		Assert.assertEquals("CC_AAAABAC_1235_H$@@FN", QueryParserUtil.normalizeTableName("cc_aaAABAc_1235_h$@@FN"));
	}

	@Test
	public void getBlockedTablesTest() {
		BlockQueryManager target = new BlockQueryManager();

		Assert.assertTrue(target.getBlockedTables().isEmpty());

		target.block(new BlockQueryManagerRequestCommand(new String[]{"test"}, BlockQueryManager.COMMANDS_IUD));
		Assert.assertEquals(1, target.getBlockedTables().size());
		Assert.assertTrue(target.getBlockedTables().contains(QueryParserUtil.normalizeTableName("test")));

		target.block(new BlockQueryManagerRequestCommand(new String[]{"test2"}, SqlCommandType.INSERT));
		Assert.assertEquals(2, target.getBlockedTables().size());
		Assert.assertTrue(target.getBlockedTables().contains(QueryParserUtil.normalizeTableName("test")));
		Assert.assertTrue(target.getBlockedTables().contains(QueryParserUtil.normalizeTableName("test2")));
	}

	@Test
	public void getBlockedTest() {
		BlockQueryManager target = new BlockQueryManager();

		Assert.assertTrue(target.getBlockedTables().isEmpty());

		target.block(new BlockQueryManagerRequestCommand(new String[]{"test"}, BlockQueryManager.COMMANDS_IUD));
		target.block(new BlockQueryManagerRequestCommand(new String[]{"test"}, SqlCommandType.INSERT));
		target.block(new BlockQueryManagerRequestCommand(new String[]{"test"}, SqlCommandType.INSERT));
		Set<SqlCommandType> blocked = target.getBlocked("queryId", "test");
		Assert.assertEquals(3, blocked.size());

		target.block(new BlockQueryManagerRequestCommand(new String[]{"test2"}, SqlCommandType.INSERT));
		Assert.assertEquals(1, target.getBlocked("queryId", "test2").size());

		target.block(new BlockQueryManagerRequestCommand(new String[]{"test2"}, SqlCommandType.INSERT));
		Assert.assertEquals(1, target.getBlocked("queryId", "test2").size());

		target.block(new BlockQueryManagerRequestCommand(new String[]{"test2"}, SqlCommandType.UPDATE));
		Assert.assertEquals(2, target.getBlocked("queryId", "test2").size());
	}

	@Test
	public void releaseTest() {
		BlockQueryManager target = new BlockQueryManager();

		Assert.assertTrue(target.getBlockedTables().isEmpty());
		Assert.assertEquals(0, target.getBlocked("queryId", "test").size());

		String[] targetTables = new String[]{"test"};
		target.block(new BlockQueryManagerRequestCommand(targetTables, BlockQueryManager.COMMANDS_IUD));
		Assert.assertEquals(3, target.getBlocked("queryId", "test").size());
		target.release(new BlockQueryManagerRequestCommand(targetTables, SqlCommandType.INSERT));
		Assert.assertEquals(2, target.getBlocked("queryId", "test").size());
		target.block(new BlockQueryManagerRequestCommand(targetTables, SqlCommandType.INSERT));
		Assert.assertEquals(3, target.getBlocked("queryId", "test").size());

		target.release(new BlockQueryManagerRequestCommand(targetTables, SqlCommandType.INSERT));
		Assert.assertEquals(2, target.getBlocked("queryId", "test").size());
		target.release(new BlockQueryManagerRequestCommand(targetTables, SqlCommandType.UPDATE));
		Assert.assertEquals(1, target.getBlocked("queryId", "test").size());
		target.release(new BlockQueryManagerRequestCommand(targetTables, SqlCommandType.DELETE));
		Assert.assertEquals(0, target.getBlocked("queryId", "test").size());
	}

	/**
	 * 꺼내온 block 내용에서 제거하더래도 원본은 손상되지 않도록..
	 */
	@Test
	public void unchangeTest() {
		BlockQueryManager target = new BlockQueryManager();

		target.block(new BlockQueryManagerRequestCommand(new String[]{"test"}, BlockQueryManager.COMMANDS_IUD));
		Set<SqlCommandType> blocked = target.getBlocked("queryId", "test");
		blocked.remove(SqlCommandType.INSERT);
		Assert.assertEquals(2, blocked.size());

		Assert.assertEquals(3, target.getBlocked("queryId", "test").size());
	}
}
