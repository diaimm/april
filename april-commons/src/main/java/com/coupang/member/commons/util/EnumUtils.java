/*
 * @fileName : EnumUtils.java
 * @date : 2013. 6. 7.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.core.Ordered;

/**
 * @author diaimm
 * 
 */
public class EnumUtils {
	private EnumUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * <pre>
	 * 해당 enum의 name이 일치되는 값을 찾습니다.(ignore cases)
	 * 매칭되는 값이 없을 때, null이 리턴됩니다.
	 * </pre>
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	public static <T extends Enum<T>> T valueOf(Class<T> type, String name) {
		return valueOf(type, name, null);
	}

	/**
	 * <pre>
	 * 해당 enum의 name이 일치되는 값을 찾습니다.(ignore cases)
	 * 매칭되는 값이 없을 때, defaultValue가 리턴됩니다.
	 * </pre>
	 * 
	 * @param type
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public static <T extends Enum<T>> T valueOf(Class<T> type, String name, T defaultValue) {
		List<T> values = values(type);
		for (T value : values) {
			if (value.name().equalsIgnoreCase(name)) {
				return value;
			}
		}

		return defaultValue;
	}

	/**
	 * Ordered를 구현한 enum을 order에 맞춰 sorting 합니다.
	 * 
	 * @param type
	 * @return
	 */
	public static <T extends Enum<T> & Ordered> List<T> sorted(Class<T> type) {
		List<T> ret = new ArrayList<T>(EnumSet.allOf(type));

		Collections.sort(ret, new Comparator<Ordered>() {
			@Override
			public int compare(Ordered o1, Ordered o2) {
				return o1.getOrder() - o2.getOrder();
			}
		});
		return ret;
	}

	/**
	 * 지정된 타입의 enum 값들을 반환합니다.
	 * 
	 * @param type
	 * @return
	 */
	public static <T extends Enum<T>> List<T> values(Class<T> type) {
		return new ArrayList<T>(EnumSet.allOf(type));
	}

	/**
	 * 지정된 타입의 enum에 대한 iterator를 반환합니다.
	 * 
	 * @param enumClass
	 * @return
	 */
	public static <T extends Enum<T>> Iterator<T> iterator(Class<T> enumClass) {
		return values(enumClass).iterator();
	}

	/**
	 * 지정된 타입의 enum에 대한 map(key = name())을 반환합니다.
	 * 
	 * @param enumClass
	 * @return
	 */
	public static <T extends Enum<T>> Map<String, Enum<T>> map(Class<T> enumClass) {
		Map<String, Enum<T>> map = new HashMap<String, Enum<T>>();
		Iterator<T> iterator = iterator(enumClass);

		while (iterator.hasNext()) {
			Enum<T> enm = iterator.next();
			map.put(enm.name(), enm);
		}

		return map;
	}
}
