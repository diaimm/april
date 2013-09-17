/**
 * 
 */
package com.diaimm.april.permission.certification.kmc;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.coupang.commons.enums.user.GenderType;
import com.coupang.commons.enums.user.NationType;
import com.icert.comm.secu.IcertSecuManager;
import com.icert.comm.secu.hmac.IcertHmac;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 21.
 */
public class KMCClient {
	private final static String CP_ID = "COPM1001";
	private final static String DEFAULT_EXTENDVAR = "0000000000000000";
	private final static String SPEPRATOR = "/";
	private final static DateTimeFormatter FORMATTER_YYYYMMDDHHMMSS = DateTimeFormat.forPattern("yyyyMMddHHmmss");
	private final static DateTimeFormatter FORMATTER_YYYYMMDD = DateTimeFormat.forPattern("yyyyMMdd");
	private final static AtomicInteger COUNTER = new AtomicInteger();
	public final static String KMC_SEPERATOR = "&";
	private Map<String, String> kmcUrlCodeMapping;

	public KMCClient(Map<String, String> kmcUrlCodeMapping) {
		this.kmcUrlCodeMapping = kmcUrlCodeMapping;
	}

	/**
	 * KMC 요청 빌드
	 * 
	 * @param urlCode
	 * @return
	 */
	public String buildRequest(String requestUrl) {
		String urlCode = MapUtils.getString(kmcUrlCodeMapping, requestUrl, null);
		if (StringUtils.isBlank(urlCode)) {
			return null;
		}

		DateTime currentDateTime = new DateTime();
		String date = currentDateTime.toString(FORMATTER_YYYYMMDDHHMMSS);
		String certNum = date + "_" + COUNTER.incrementAndGet();
		String birthday = StringUtils.EMPTY;
		String gender = StringUtils.EMPTY;
		String name = StringUtils.EMPTY;
		String phoneNo = StringUtils.EMPTY;
		String phoneCorp = StringUtils.EMPTY;
		String nation = StringUtils.EMPTY;
		String plusInfo = StringUtils.EMPTY;
		String[] certificationRequestArgs = { CP_ID, urlCode, certNum, date, "M", birthday, gender, name, phoneNo, phoneCorp, nation, plusInfo,
				DEFAULT_EXTENDVAR };

		IcertSecuManager seed = new IcertSecuManager();
		String encryptedCertRequestKey = seed.getEnc(StringUtils.join(certificationRequestArgs, "/"), "");
		String validateKeyForEncrypedCertKey = IcertHmac.HMacEncript(encryptedCertRequestKey);

		return seed.getEnc(encryptedCertRequestKey + "/" + validateKeyForEncrypedCertKey + "/" + DEFAULT_EXTENDVAR, "");
	}

	/**
	 * KMC 인증 결과 파싱
	 * 
	 * @param encryptedCertificationDataSet
	 * @param encryptKey
	 * @return
	 */
	public KMC getKMCInfo(String encryptedCertificationDataSet, String encryptKey) {
		Validate.isTrue(isVaild(encryptedCertificationDataSet, encryptKey));

		IcertSecuManager seed = new IcertSecuManager();
		String certificationDataSet = seed.getDec(encryptedCertificationDataSet, encryptKey);
		String[] certifications = StringUtils.splitPreserveAllTokens(certificationDataSet, SPEPRATOR);

		String encryptedPrivateDataSet = certifications[0];
		String[] privateParams = StringUtils.splitPreserveAllTokens(seed.getDec(encryptedPrivateDataSet, encryptKey), SPEPRATOR);

		return convertParamTOKMC(privateParams, encryptKey);
	}

	/**
	 * KMC 인증 데이터 유효성 분석 Hash 값 검증
	 * 
	 * @param encryptedCertificationDataSet
	 * @param encryptKey
	 * @return
	 */
	private boolean isVaild(String encryptedCertificationDataSet, String encryptKey) {
		IcertSecuManager seed = new IcertSecuManager();
		String certificationDataSet = seed.getDec(encryptedCertificationDataSet, encryptKey);
		String[] certifications = StringUtils.splitPreserveAllTokens(certificationDataSet, SPEPRATOR);

		String encryptedPrivateDataSet = certifications[0];
		String validateHashKey = certifications[1];
		String encryptedPrivateDataSetHashKey = seed.getMsg(encryptedPrivateDataSet);

		return encryptedPrivateDataSetHashKey.equals(validateHashKey);
	}

	/**
	 * 인증 결과 Convert
	 * 
	 * @param privateParams
	 * @param encryptKey
	 * @return
	 */
	private KMC convertParamTOKMC(String[] privateParams, String encryptKey) {
		IcertSecuManager seed = new IcertSecuManager();

		String result = privateParams[9];
		Validate.isTrue("Y".equals(result));

		String name = privateParams[8];
		NationType nation = ("0".equals(privateParams[7]) ? NationType.LOCAL : NationType.FOREIGNER);
		GenderType gender = ("0".equals(privateParams[6]) ? GenderType.MALE : GenderType.FEMALE);
		LocalDate birthDay = FORMATTER_YYYYMMDD.parseLocalDate(privateParams[5]);
		String ci = seed.getDec(privateParams[2], encryptKey);
		String di = seed.getDec(privateParams[17], encryptKey);
		String ipAddress = privateParams[11];

		return new KMC(name, nation, gender, birthDay, ci, di, ipAddress);
	}
}
