/*
 * @fileName : SqlMapClientTemplateInitializerTest.java
 * @date : 2013. 5. 30.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.db.mybatis;

import com.diaimm.april.commons.util.EnumUtils;
import com.diaimm.april.db.mybatis.MapperScannerInitializer.BeanNamePostfixes;
import com.diaimm.april.db.util.DataSourceIntializePropertiesUtils;
import freemarker.core.InvalidReferenceException;
import freemarker.template.TemplateException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author diaimm
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:com/diaimm/april/db/mybatis/SqlSessionTemplateInitializerTest.xml")
public class MapperScannerInitializerTest {
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private MapperScannerInitializer initializer;

	@Test
	public void postProcessBeanFactoryTest() {
		MapperScannerInitializer target = new MapperScannerInitializer() {
			@Override
			List<Map<String, Object>> getAllDataSourcePropertyFiles() throws IOException {
				throw new IOException("test");
			}
		};

		try {
			target.postProcessBeanFactory(null);
		} catch (BeansException e) {
			Assert.assertEquals(IOException.class, e.getCause().getClass());
			Assert.assertEquals("test; nested exception is java.io.IOException: test", e.getMessage());
		}

		target = new MapperScannerInitializer() {
			@Override
			List<Map<String, Object>> getAllDataSourcePropertyFiles() throws IOException {
				return new ArrayList<Map<String, Object>>();
			}
		};

		try {
			target.postProcessBeanFactory(null);
		} catch (Exception e) {
			Assert.assertEquals(NullPointerException.class, e.getClass());
		}
	}

	@Test
	public void getAllDataSourcePropertyFilesTest() {
		MapperScannerInitializer target = new MapperScannerInitializer();
		target.setApplicationContext(applicationContext);

		List<String> locations = new ArrayList<String>();
		locations.add("classpath:com/coupang/member/db/mybatis/dbs");
		target.setProperties(locations);

		try {
			List<Map<String, Object>> allDataSourcePropertyFiles = target.getAllDataSourcePropertyFiles();
			Assert.assertEquals(2, allDataSourcePropertyFiles.size());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Ignore
	@Test
	public void getDataSourceBeanConfigurationTest() {
		MapperScannerInitializer target = new MapperScannerInitializer();
		target.setApplicationContext(applicationContext);

		try {
			List<Map<String, Object>> templateAttributes = new ArrayList<Map<String, Object>>();
			String dataSourceBeanConfiguration = getDataSourceBeanConfiguration(templateAttributes);
			Assert.assertTrue(!StringUtils.isEmpty(dataSourceBeanConfiguration));

			Map<String, Object> dataSourceInfo = new HashMap<String, Object>();
			templateAttributes.add(dataSourceInfo);
			dataSourceInfo.put("id", "test");

			try {
				// 필수값이 없는 설정은 exception
				dataSourceBeanConfiguration = DataSourceIntializePropertiesUtils.getDataSourceBeanConfiguration(MapperScannerInitializer.class, templateAttributes, MapperScannerInitializer.SQLMAP_CLIENT_TEMPLATE_INITIALIZE_TEMPLATE_FTL).build();
				Assert.fail();
			} catch (Exception e) {
				Assert.assertEquals(InvalidReferenceException.class, e.getClass());
			}

			// 필수값 5가지
			dataSourceInfo.put("id", "id");
			dataSourceInfo.put("driverClassName", "driverClassName");
			dataSourceInfo.put("url", "url");
			dataSourceInfo.put("username", "username");
			dataSourceInfo.put("password", "password");
			dataSourceInfo.put("myBatisConfigLocation", "myBatisConfigLocation");
			dataSourceInfo.putAll(EnumUtils.map(BeanNamePostfixes.class)); // bean name postfiex 들 추가

			dataSourceBeanConfiguration = DataSourceIntializePropertiesUtils.getDataSourceBeanConfiguration(MapperScannerInitializer.class, templateAttributes, MapperScannerInitializer.SQLMAP_CLIENT_TEMPLATE_INITIALIZE_TEMPLATE_FTL).build();
			Assert.assertTrue(!StringUtils.isEmpty(dataSourceBeanConfiguration));
		} catch (Exception e) {
			Assert.fail();
		}
	}

	private String getDataSourceBeanConfiguration(List<Map<String, Object>> templateAttributes) throws TemplateException, IOException {
		return DataSourceIntializePropertiesUtils.getDataSourceBeanConfiguration(MapperScannerInitializer.class, templateAttributes, MapperScannerInitializer.SQLMAP_CLIENT_TEMPLATE_INITIALIZE_TEMPLATE_FTL).build();
	}

	@Test
	public void feedToBeanFactoryTest() {
		MapperScannerInitializer target = new MapperScannerInitializer();
		target.setApplicationContext(applicationContext);

		try {
			List<Map<String, Object>> templateAttributes = new ArrayList<Map<String, Object>>();
			String dataSourceBeanConfiguration = getDataSourceBeanConfiguration(templateAttributes);

			ConfigurableListableBeanFactory beanFactory = new DefaultListableBeanFactory();
			DataSourceIntializePropertiesUtils.feedToBeanFactory(beanFactory, dataSourceBeanConfiguration);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Ignore
	@Test
	public void initializeSqlMapClientTemplateTest() {
		final Set<String> isCalled = new HashSet<String>();
		MapperScannerInitializer target = new MapperScannerInitializer() {
		};
		target.setApplicationContext(applicationContext);
		DataSourceIntializePropertiesUtils.initializeByTemplate(MapperScannerInitializer.class, null, null, null);

		Assert.assertEquals(2, isCalled.size());
		Assert.assertTrue(isCalled.contains("getDataSourceBeanConfiguration"));
		Assert.assertTrue(isCalled.contains("feedToBeanFactory"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void PropertyKeysTest() {
		Properties properties = new Properties();
		Assert.assertEquals("com/diaimm/april/db/mybatis/default-mybatis-config.xml", MapperScannerInitializer.MapperScannerPropertyKeys.MYBATIS_CONFIG_LOCATION.getValue(properties));

		Object mappingLocations = MapperScannerInitializer.MapperScannerPropertyKeys.MYBATIS_MAPPING_LOCATION.getValue(properties);
		Assert.assertTrue(List.class.isAssignableFrom(mappingLocations.getClass()));
		Assert.assertEquals(0, ((List<String>) mappingLocations).size());

		properties.put("mybatis.configLocation", "test");
		properties.put("mybatis.mappingLocation", "test1 , test2,");
		Assert.assertEquals("test", MapperScannerInitializer.MapperScannerPropertyKeys.MYBATIS_CONFIG_LOCATION.getValue(properties));

		mappingLocations = MapperScannerInitializer.MapperScannerPropertyKeys.MYBATIS_MAPPING_LOCATION.getValue(properties);
		Assert.assertEquals(2, ((List<String>) mappingLocations).size());
		Assert.assertEquals("test1", ((List<String>) mappingLocations).get(0));
		Assert.assertEquals("test2", ((List<String>) mappingLocations).get(1));
	}
}
