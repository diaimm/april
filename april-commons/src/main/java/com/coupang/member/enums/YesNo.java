/**
 * 
 */
package com.coupang.member.enums;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 25.
 */
public enum YesNo {
	YES("y", "yes", "true"), //
	NO;//

	private final String[] acceptableValues;

	YesNo(String... acceptableValues) {
		this.acceptableValues = acceptableValues;
	}

	public static YesNo is(Object value) {
		for (String acceptableValue : YES.acceptableValues) {
			if (acceptableValue.equalsIgnoreCase(value.toString())) {
				return YES;
			}
		}

		return NO;
	}
}
