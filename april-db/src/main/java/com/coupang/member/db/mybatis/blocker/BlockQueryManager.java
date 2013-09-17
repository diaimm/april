/*
 * @fileName : BlockQueryManager.java
 * @date : 2013. 6. 5.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.db.mybatis.blocker;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.SqlCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coupang.member.commons.Env;
import com.coupang.member.commons.crypto.AES.AESType;
import com.coupang.member.commons.crypto.Crypto;
import com.coupang.member.commons.util.DateUtil;

/**
 * @author diaimm
 */
class BlockQueryManager {
	public static final SqlCommandType[] COMMANDS_IUD = new SqlCommandType[]{SqlCommandType.INSERT, SqlCommandType.UPDATE, SqlCommandType.DELETE};
	private final Object lock = new Object();
	/**
	 * tablename : sql command type
	 */
	private Map<String, Set<SqlCommandType>> blockTargets = new ConcurrentHashMap<String, Set<SqlCommandType>>();
	private Map<String, Boolean> byStatementBlockInfo = new ConcurrentHashMap<String, Boolean>();

	/**
	 * 지정된 테이블의 command type들을 block 합니다.
	 *
	 * @param command
	 */
	void block(BlockQueryManagerRequestCommand command) {
		String[] tableNames = command.tableNames;
		SqlCommandType[] sqlCommandTypes = command.sqlCommandTypes;

		for (String tableName : tableNames) {
			String key = QueryParserUtil.normalizeTableName(tableName);
			if (!blockTargets.containsKey(key)) {
				synchronized (lock) {
					if (!blockTargets.containsKey(key)) {
						blockTargets.put(key, new ConcurrentSkipListSet<SqlCommandType>());
					}
				}
			}

			synchronized (lock) {
				List<SqlCommandType> asList = Arrays.asList(sqlCommandTypes);
				blockTargets.get(key).addAll(asList);

				// 테이블이 하나라도 block 되면 중간 cache를 지운다.
				byStatementBlockInfo.clear();
			}
		}
	}

	/**
	 * 지정된 테이블의 block된 command type들을 release 합니다.
	 *
	 * @param command
	 */
	void release(BlockQueryManagerRequestCommand command) {
		String[] tableNames = command.tableNames;
		SqlCommandType[] sqlCommandTypes = command.sqlCommandTypes;

		for (String tableName : tableNames) {
			String key = QueryParserUtil.normalizeTableName(tableName);
			if (!blockTargets.containsKey(key)) {
				synchronized (lock) {
					if (!blockTargets.containsKey(key)) {
						return;
					}
				}
			}

			synchronized (lock) {
				blockTargets.get(key).removeAll(Arrays.asList(sqlCommandTypes));

				// 제거 후 block이 없다면, 해당 set은 완전히 제거한다.
				if (blockTargets.get(key).isEmpty()) {
					blockTargets.remove(key);
				}

				// 테이블이 하나라도 release 되면 중간 cache를 지운다.
				byStatementBlockInfo.clear();
			}
		}
	}

	public Boolean getBlockedByStatementName(String statementName) {
		return byStatementBlockInfo.get(statementName);
	}

	/**
	 * <pre>
	 * block된 table이 존재하는 모든 command 목록을 반환합니다.
	 * 없으면 비어있는 set이 리턴됩니다.
	 * </pre>
	 *
	 * @return
	 */
	Set<SqlCommandType> getBlockedCommandTypes() {
		synchronized (lock) {
			Set<SqlCommandType> allBlockedTypes = new HashSet<SqlCommandType>();
			for (Set<SqlCommandType> blockedTypes : blockTargets.values()) {
				allBlockedTypes.addAll(blockedTypes);
			}

			return allBlockedTypes;
		}
	}

	/**
	 * <pre>
	 * 특정 table에 block된 command 목록을 반환합니다.
	 * 없으면 비어있는 set이 리턴됩니다.
	 * </pre>
	 *
	 * @param tableName
	 * @return
	 */
	Set<SqlCommandType> getBlocked(String statementName, String tableName) {
		synchronized (lock) {
			String key = QueryParserUtil.normalizeTableName(tableName);
			if (!blockTargets.containsKey(key)) {
				// statement 단위 block 여부 정보 cache
				byStatementBlockInfo.put(statementName, Boolean.FALSE);
				return new HashSet<SqlCommandType>();
			}

			// statement 단위 block 여부 정보 cache
			byStatementBlockInfo.put(statementName, Boolean.TRUE);
			return new HashSet<SqlCommandType>(blockTargets.get(key));
		}
	}

	/**
	 * block이 있는 table 목록을 반환합니다. 없으면 비어있는 set이 리턴됩니다.
	 *
	 * @return
	 */
	Set<String> getBlockedTables() {
		return blockTargets.keySet();
	}

	static class BlockQueryManagerRequestCommand {
		private static final String ENC_KEY = "##SFBA9943^__^;";
		private static Logger logger = LoggerFactory.getLogger(BlockQueryManagerRequestCommand.class);
		private String[] tableNames;
		private SqlCommandType[] sqlCommandTypes;

		BlockQueryManagerRequestCommand() {
		}

		BlockQueryManagerRequestCommand(String[] tableNames, SqlCommandType... sqlCommandTypes) {
			this.tableNames = tableNames;
			this.sqlCommandTypes = sqlCommandTypes;
		}

		static BlockQueryManagerRequestCommand fromRequestParamValue(String encrypted) {
			try {
				String decode = URLDecoder.decode(encrypted, Env.DEFAULT_ENCODING);
				String decrypted = Crypto.aes(AESType.AES128).decrypt(decode, ENC_KEY);

				String[] split = StringUtils.split(decrypted, "///");
				String tableName = StringUtils.splitByWholeSeparator(split[1], "^^^")[1];
				String sqlCommandTypesString = StringUtils.splitByWholeSeparator(split[2], "^^^")[1];
				sqlCommandTypesString = sqlCommandTypesString.replaceAll("[\\[\\]]{0,1}", "");
				String[] sqlCommandTypes = sqlCommandTypesString.split(",");
				SqlCommandType[] sqlCommandTypeEnums = new SqlCommandType[sqlCommandTypes.length];
				for (int index = 0; index < sqlCommandTypes.length; index++) {
					String sqlCommandType = sqlCommandTypes[index];
					try {
						sqlCommandTypeEnums[index] = SqlCommandType.valueOf(sqlCommandType.trim());
					} catch (Exception e) {
						// igrnoer
					}
				}

				return new BlockQueryManagerRequestCommand(tableName.split(","), sqlCommandTypeEnums);
			} catch (Exception e) {
				logger.debug(e.getMessage(), e);
			}
			return null;
		}

		static String toRequestParamValue(String tableName, SqlCommandType[] sqlCommandTypes) {
			StringBuffer ret = new StringBuffer();
			ret.append(DateUtil.mktime()).append("///");
			ret.append("tableName^^^").append(tableName).append("///");
			ret.append("sqlCommandTypes^^^").append(Arrays.toString(sqlCommandTypes));

			try {
				return URLEncoder.encode(Crypto.aes(AESType.AES128).encrypt(ret.toString(), ENC_KEY), Env.DEFAULT_ENCODING);
			} catch (Exception e) {
				logger.debug(e.getMessage(), e);
			}
			return "";
		}
	}
}
