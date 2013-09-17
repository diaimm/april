package com.diaimm.april.db.util;

import java.util.Map;
import java.util.Properties;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 8. 28
 * Time: 오후 1:02
 */
public interface DataSourceInitializerPropertyKey {
	void addToTemplateAttribute(Properties properties, Map<String, Object> attributes);
}
