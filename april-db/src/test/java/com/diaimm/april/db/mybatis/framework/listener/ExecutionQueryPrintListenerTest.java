/*
 * @fileName : ExecutionQueryPrintListenerTest.java
 * @date : 2013. 6. 4.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.db.mybatis.framework.listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @author diaimm
 * 
 */
public class ExecutionQueryPrintListenerTest {
	@Test
	public void testIt() {
		StringBuffer query = new StringBuffer();
		query.append("select 1 \n	from testaa \n\n where \n? = ?");

		Pattern pattern = Pattern.compile("\\?");
		Matcher matcher = pattern.matcher(query);

		StringBuffer result = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(result, "111");
		}

		System.out.println(result);
	}
}
