/*
 * @fileName : PropertyConfigurerTest.java
 * @date : 2013. 7. 11.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.commons.property;

import com.diaimm.april.commons.property.base.sample.SamplePropertyEnum;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.diaimm.april.commons.ByPhase;

import java.net.URL;

/**
 * @author diaimm
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:com/diaimm/april/commons/property/PropertyConfigurerTest.xml")
public class PropertyConfigurerTest {
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ByPhase byPhase;

    @Test
    public void resourceLoadTest(){
        Resource resource = applicationContext.getResource("classpath*:com/diaimm/april/commons/property/base/sample/SamplePropertyEnum.properties");
        System.out.println(resource.exists());

        Assert.assertEquals("classpath:" + PropertyKeyEnum.FilePath.get(SamplePropertyEnum.class) + ".properties", "classpath:com/diaimm/april/commons/property/base/sample/SamplePropertyEnum.properties");
        resource = applicationContext.getResource("classpath:com/diaimm/april/commons/property/base/sample/SamplePropertyEnum.properties");
        System.out.println(resource.exists());
        System.out.println(resource.isReadable());

        URL resource1 = SamplePropertyEnum.class.getResource("com/diaimm/april/commons/property/base/sample/SamplePropertyEnum.properties");
    }

	@Test
	public void testIt() {
        String property = byPhase.getProperty(SamplePropertyEnum.TEST);
        Assert.assertEquals("value1", property);
    }
}
