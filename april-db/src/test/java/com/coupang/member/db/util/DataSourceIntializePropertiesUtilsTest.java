/*
 * @fileName : SqlSessionTemplatePropertiesUtilsTest.java
 * @date : 2013. 6. 3.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.db.util;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.diaimm.april.db.util.DataSourceIntializePropertiesUtils;

import java.io.IOException;
import java.util.List;
import java.util.Properties;


/**
 * @author diaimm
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:com/coupang/member/db/mybatis/empty.xml")
public class DataSourceIntializePropertiesUtilsTest {
	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void getResourcesTest() throws IOException {
		String location = "classpath:com/coupang/member/db/mybatis/dbs" + "/**/*.properties";
		Resource[] resources = applicationContext.getResources(location);
		Assert.assertEquals(2, resources.length);
	}

	@Test
	public void SqlSessionProvertiesListProviderTest() {
		try {
			String location = "classpath:com/coupang/member/db/mybatis/dbs";

			List<Properties> propertiesList = DataSourceIntializePropertiesUtils.getPropertiesList(applicationContext, location);
			Assert.assertEquals(2, propertiesList.size());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			String location = "classpath:com/coupang/member/db/mybatis/dbs/login.properties";
			List<Properties> propertiesList = DataSourceIntializePropertiesUtils.getPropertiesList(applicationContext, location);
			Assert.assertEquals(1, propertiesList.size());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
