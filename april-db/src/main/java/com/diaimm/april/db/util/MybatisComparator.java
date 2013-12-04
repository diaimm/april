package com.diaimm.april.db.util;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

public class MybatisComparator {

	public static boolean isEmpty(Object obj) {
		if (obj instanceof String) {
			return StringUtils.isBlank((String) obj);
		} else if (obj instanceof List) {
			return CollectionUtils.isEmpty((List) obj);
		} else if (obj instanceof Map) {
			return CollectionUtils.isEmpty((Map) obj);
		} else if (obj instanceof Object[]) {
			return obj == null || Array.getLength(obj)==0;
		} else {
			return obj == null;
		}
	}
	
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}
	
}
