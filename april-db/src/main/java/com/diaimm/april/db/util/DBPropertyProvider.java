package com.diaimm.april.db.util;

import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 9. 4
 * Time: 오전 11:54
 */
public interface DBPropertyProvider {
	List<Properties> getProperties(ApplicationContext applicationContext, String propertyFilePath) throws IOException;

	public static class DefaultDBPropertyProvider implements DBPropertyProvider {

		@Override
		public List<Properties> getProperties(ApplicationContext applicationContext, String propertyFilePath) throws IOException {
			return DataSourceIntializePropertiesUtils.getPropertiesList(applicationContext, propertyFilePath);
		}
	}
}
