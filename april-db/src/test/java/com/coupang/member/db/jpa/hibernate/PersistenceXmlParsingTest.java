package com.coupang.member.db.jpa.hibernate;

import com.diaimm.april.commons.util.JaxbObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.diaimm.april.db.jpa.hibernate.JPAHibernateEntityManagerInitializer.Persistence;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 8. 28
 * Time: 오후 3:11
 */
public class PersistenceXmlParsingTest {
	@Test
	public void parsingTest() throws JAXBException, IOException {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/coupang/member/db/jpa/hibernate/dbs/persistence-login.xml");
		String source = IOUtils.toString(inputStream, "UTF-8");
		Persistence persistence = JaxbObjectMapper.XML.objectify(source, Persistence.class);
		System.out.println(persistence);
	}
}
