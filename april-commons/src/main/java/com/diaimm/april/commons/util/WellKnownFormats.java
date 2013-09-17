package com.diaimm.april.commons.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public enum WellKnownFormats {
	/**
	 * 이메일 형식, formatter 없음
	 */
	EMAIL("^([_A-Za-z0-9-_]+[._A-Za-z0-9-_]*)@((?:[_A-Za-z0-9-]+\\.)+\\w+)$") {
		@Override
		public Formatter formatter() {
			return null;
		}
	},
	BIRTHDAY("^((19|20)\\d\\d)(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])$") {
		@Override
		public Formatter formatter() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	PASSWORD("((?=.*\\d)(?=.*[a-z])(?=.*[0-9]).{6,15})") {
		@Override
		public Formatter formatter() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	/**
	 * 모바일 전화번호, formatter 있음
	 */
	MOBILE("^(01[016789])-(\\d{3,4})-(\\d{4})$") {
		@Override
		public Formatter formatter() {
			return new PhoneNumberFormatter(this);
		}

		@Override
		public boolean isValid(String formattedValue) {
			if (StringUtils.isEmpty(formattedValue)) {
				return Boolean.FALSE;
			}
			/**
			 * 010 번호는 중간번호의 시작이 0,1 이 될 수 없다.
			 * http://blog.daum.net/kk1990/488
			 */
			if (StringUtils.startsWith(formattedValue, "010-0") || StringUtils.startsWith(formattedValue, "010-1")) {
				return Boolean.FALSE;
			}

			Pattern p = Pattern.compile(MOBILE.pattern);
			Matcher m = p.matcher(formattedValue);
			if (m.matches()) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
	},
	/**
	 * 집 전화번호, formatter 있음
	 */
	PHONE("^(\\d{2,6})-(\\d{3,4})-(\\d{4})$") {
		@Override
		public Formatter formatter() {
			return new PhoneNumberFormatter(this);
		}
	};

	private final String pattern;

	WellKnownFormats(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * 포맷확인
	 * 
	 * @param formattedValue
	 * @return
	 */
	public boolean isValid(String formattedValue) {
		if (StringUtils.isEmpty(formattedValue)) {
			return Boolean.FALSE;
		}

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(formattedValue);
		if (m.matches()) {
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	/**
	 * 사용할 수 있는 Formatter를 return합니다. 없다면, null이 리턴될 수 있습니다.
	 * 
	 * @return
	 */
	public abstract Formatter formatter();

	/**
	 * formatter interface
	 * 
	 * @author diaimm
	 * 
	 */
	public static interface Formatter {
		/**
		 * 필요한 개수 만큼을 받습니다. 내부적 조건에 맞지 않으면 null을 리턴합니다.
		 * 
		 * @param value
		 * @return
		 */
		String format(Object... value);

		/**
		 * 적절히 분리된 형태의 array로 리턴합니다. 내부적 조건에 맞지 않으면 null을 리턴합니다.
		 * 
		 * @param phoneNumber
		 * @return
		 */
		String[] unformat(String formattedValue);
	}

	static class EmailFormatter implements Formatter {
		private final WellKnownFormats wellKnownFormats;

		/**
		 * @param wellKnownFormats
		 */
		EmailFormatter(WellKnownFormats wellKnownFormats) {
			this.wellKnownFormats = wellKnownFormats;
		}

		/**
		 * @see WellKnownFormats.Formatter#format(java.lang.Object[])
		 */
		@Override
		public String format(Object... value) {
			if (value.length != 2) {
				return null;
			}
			return value[0].toString() + "@" + value[1].toString();
		}

		/**
		 * @see WellKnownFormats.Formatter#unformat(java.lang.String)
		 */
		@Override
		public String[] unformat(String formattedValue) {
			if (!wellKnownFormats.isValid(formattedValue)) {
				return null;
			}

			Pattern pattern = Pattern.compile(wellKnownFormats.pattern);
			Matcher matcher = pattern.matcher(formattedValue);
			if (matcher.find()) {
				String[] email = new String[matcher.groupCount()];
				for (int index = 0; index < matcher.groupCount(); index++) {
					email[index] = matcher.group(index + 1);
				}

				return email;
			}

			return null;
		}

	}

	static class PhoneNumberFormatter implements Formatter {
		private final WellKnownFormats wellKnownFormats;

		/**
		 * @param wellKnownFormats
		 */
		PhoneNumberFormatter(WellKnownFormats wellKnownFormats) {
			this.wellKnownFormats = wellKnownFormats;
		}

		/**
		 * @see WellKnownFormats.Formatter#format(java.lang.Object[])
		 */
		@Override
		public String format(Object... values) {
			if (values.length != 3) {
				return null;
			}

			for (Object value : values) {
				if (!checkNumber(value)) {
					return null;
				}
			}

			String num1 = values[0].toString();
			String num2 = values[1].toString();
			String num3 = values[2].toString();

			if (StringUtils.isBlank(num1) || StringUtils.isBlank(num2) || StringUtils.isBlank(num3)) {
				return null;
			}

			String result = StringUtils.trimToEmpty(num1) + "-" + StringUtils.trimToEmpty(num2) + "-" + StringUtils.trimToEmpty(num3);
			if (!this.wellKnownFormats.isValid(result)) {
				return null;
			}

			return result;
		}

		private boolean checkNumber(Object valueOrg) {
			if (valueOrg == null) {
				return false;
			}

			String value = valueOrg.toString();
			if (StringUtils.isEmpty(value) || !StringUtils.isNumericSpace(value)) {
				return false;
			}

			return true;
		}

		/**
		 * @see WellKnownFormats.Formatter#unformat(java.lang.String)
		 */
		@Override
		public String[] unformat(String formattedValue) {
			if (!wellKnownFormats.isValid(formattedValue)) {
				return null;
			}

			Pattern pattern = Pattern.compile(wellKnownFormats.pattern);
			Matcher matcher = pattern.matcher(formattedValue);
			if (matcher.find()) {
				String[] phone = new String[matcher.groupCount()];
				for (int index = 0; index < matcher.groupCount(); index++) {
					phone[index] = matcher.group(index + 1);
				}

				return phone;
			}

			return null;
		}
	}
}
