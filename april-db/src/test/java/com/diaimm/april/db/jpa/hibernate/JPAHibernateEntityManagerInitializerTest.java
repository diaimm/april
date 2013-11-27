package com.diaimm.april.db.jpa.hibernate;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 8. 28
 * Time: 오전 10:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:com/diaimm/april/db/jpa/hibernate/JPAHibernateEntityManagerIntializer-context.xml")
public class JPAHibernateEntityManagerInitializerTest {
	@Test
	public void contextLoagindTest() {
		Assert.assertTrue(true);
	}
}
