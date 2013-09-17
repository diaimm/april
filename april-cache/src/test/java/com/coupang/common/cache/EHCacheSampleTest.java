/*
 * @fileName : EHCacheSampleTest.java
 * @date : 2013. 5. 30.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.common.cache;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.coupang.common.util.DateUtil;

/**
 * @author diaimm
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:com/coupang/common/cache/cache-context.xml")
public class EHCacheSampleTest {
	private Logger logger = LoggerFactory.getLogger(EHCacheSampleTest.class);
	@Autowired
	private CacheManager cacheManager;

	public void testIt1() {
		// CacheManager cacheManager = CacheManager.create();
		// cacheManager.addCache(new Cache());
		// Cache cache = cacheManager.getCache("simpleBeanCache");
		//
		// SimpleBean newBean = new SimpleBean("id", "name");
		// Element newElement = new Element(newBean.getId(), newBean);
		// cache.put(newElement);
		//
		// Element element = cache.get(newBean.getId());
		// Assert.assertEquals("name", ((SimpleBean) element.getObjectValue()).getName());
	}

	@Test
	public void testIt() {
		Cache cache = cacheManager.getCache("test");

		SimpleBean newBean = new SimpleBean("id", "name");
		cache.put("test", newBean);

		logger.debug("start : {}", DateUtil.date("yyyy-MM-dd HH:mm:ss"));
		ValueWrapper valueWrapper = cache.get("test");
		SimpleBean fromCache = (SimpleBean) valueWrapper.get();
		System.out.print(fromCache);

		logger.debug("middle : {}", DateUtil.date("yyyy-MM-dd HH:mm:ss"));

		valueWrapper = cache.get("test");
		fromCache = (SimpleBean) valueWrapper.get();
		System.out.print(fromCache);

		logger.debug("end : {}", DateUtil.date("yyyy-MM-dd HH:mm:ss"));
	}

	private static class SimpleBean {
		private String name;
		private String id;

		/**
		 * @param string
		 * @param string2
		 */
		public SimpleBean(String id, String name) {
			this.id = id;
			this.name = name;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}
}
