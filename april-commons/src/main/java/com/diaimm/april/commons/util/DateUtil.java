/*
 * @fileName : DateUtil.java
 * @date : 2013. 6. 4.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.diaimm.april.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author diaimm
 * 
 */
public class DateUtil {
	private DateUtil() {
		throw new UnsupportedOperationException();
	}

	private static SimpleDateFormat getDateFormat(String format) {
		return new SimpleDateFormat(format, Locale.KOREA);
	}

	/**
	 * 현재 시간에 대한 timestamp
	 * 
	 * @return
	 */
	public static long mktime() {
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis();
	}

	/**
	 * <pre>
	 * 지정된 날짜/포맷에 대한 timestampe
	 * 
	 * 	mktime("2013-01-05", "yyyy-MM-dd");
	 * </pre>
	 * 
	 * @param value
	 * @param dateFormat
	 * @return
	 * @throws ParseException
	 */
	public static long mktime(String value, String dateFormat) throws ParseException {
		SimpleDateFormat simpleDateFormat = getDateFormat(dateFormat);
		Date parsed = simpleDateFormat.parse(value);
		return parsed.getTime();
	}

	/**
	 * ParseException 발생시 내부에서 무시 하고, 0L을 리턴합니다.
	 * 
	 * @param value
	 * @param dateFormat
	 * @return
	 */
	public static long mktimeQuietly(String value, String dateFormat) {
		try {
			return mktime(value, dateFormat);
		} catch (ParseException e) {
			return 0L;
		}
	}

	/**
	 * 현재 시간을 지정된 포맷으로 리턴합니다.
	 * 
	 * @param string
	 * @return
	 */
	public static String date(String dateFormat) {
		return date(mktime(), dateFormat);
	}

	/**
	 * 지정된 시간(timestamp)를 지정된 포맷으로 작성합니다.
	 * 
	 * @param string
	 * @return
	 */
	public static String date(long timestamp, String dateFormat) {
		return getDateFormat(dateFormat).format(timestamp);
	}

	/**
	 * 현재 시간으로 부터 지정된 시간 까지의 간격을 구합니다.
	 * 
	 * @param toTimestamp
	 * @param timeUnit
	 * @return
	 */
	public static long interval(long toTimestamp, TimeUnit timeUnit) {
		return getInterval(mktime(), toTimestamp, timeUnit);
	}

	/**
	 * 지정된 두 시간 사이의 간격을 구합니다.
	 * 
	 * @param toTimestamp
	 * @param timeUnit
	 * @return
	 */
	public static long getInterval(long fromTimestamp, long toTimestamp, TimeUnit timeUnit) {
		return timeUnit.convert((toTimestamp - fromTimestamp), TimeUnit.MILLISECONDS);
	}

	/**
	 * 날짜 포맷을 확인하여, Date 형으로 변환합니다.
	 * 
	 * @param dateValue
	 * @param format
	 * @return
	 * @throws ParseException
	 *             포맷이 일치하지 않는 경우
	 * 
	 */
	public static Date toDate(String dateValue, String format) throws ParseException {
		if (dateValue == null) {
			throw new ParseException("date string to check is null", 0);
		}

		if (format == null) {
			throw new ParseException("format string to check date is null", 0);
		}

		SimpleDateFormat formatter = getDateFormat(format);
		Date date = formatter.parse(dateValue);

		if (!formatter.format(date).equals(dateValue)) {
			throw new ParseException("Out of bound date:\"" + dateValue + "\" with format \"" + format + "\"", 0);
		}

		return date;
	}

	/**
	 * 날짜 포맷을 확인하여, Date 형으로 변환합니다. 단, 포맷이 일치하지 않는 경우 null을 리턴합니다.
	 * 
	 * @param dateValue
	 * @param format
	 * @return
	 */
	public static Date toDateQuietly(String dateValue, String format) {
		try {
			return toDate(dateValue, format);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 현재 시간으로 부터 size 만큼 이동시킨다.
	 * 
	 * @param from
	 * @param size
	 * @param timeUnit
	 * @return
	 */
	public static long move(long size, TimeUnit timeUnit) {
		return move(mktime(), size, timeUnit);
	}

	/**
	 * 지정된 시간으로 부터 size 만큼 이동시킨다.
	 * 
	 * @param from
	 * @param size
	 * @param timeUnit
	 * @return
	 */
	public static long move(long from, long size, TimeUnit timeUnit) {
		long toMilliseconds = TimeUnit.MILLISECONDS.convert(size, timeUnit);
		return from + toMilliseconds;
	}
}
