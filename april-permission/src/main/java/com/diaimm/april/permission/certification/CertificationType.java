/*
 * @fileName : CertificationType.java
 * @date : 2013. 7. 9.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.permission.certification;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.diaimm.april.permission.certification.kmc.KMC;
import com.diaimm.april.permission.certification.kmc.KMCClient;
import com.diaimm.april.permission.certification.nice.NiceNameClient;
import com.diaimm.april.permission.certification.nice.NiceSsnClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;

import com.coupang.commons.ByPhase;
import com.coupang.commons.crypto.AES.AESType;
import com.coupang.commons.crypto.Crypto;
import com.coupang.commons.enums.user.GenderType;
import com.coupang.commons.property.base.EtcProperties;
import com.coupang.commons.util.DateUtil;
import com.diaimm.april.permission.certification.ipin.IPIN;
import com.diaimm.april.permission.certification.kmc.KMC;
import com.diaimm.april.permission.certification.kmc.KMCClient;
import com.diaimm.april.permission.certification.nice.NiceNameClient;
import com.diaimm.april.permission.certification.nice.NiceSsnClient;

/**
 * Coupang 에서 사용하는 고객정보와 관련된 모든 인증 방식 정리<br>
 * Coupang 에서는 총 6가지 Case 로 인증하고 있다<br> 
 * <br> 
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 8. 12.
 */
public enum CertificationType {
	/**	핸드폰 번호를 입력받아 인증 번호를 전송하고 값을 확인하는 인증	*/
	PHONE_NUMBER {
		@Override
		public String encoding(ByPhase byPhase, Object planAuthData) throws Exception {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

			objectOutputStream.writeObject(planAuthData);
			String objectStr = new String(Base64.encodeBase64String(byteArrayOutputStream.toByteArray()));

			String key = byPhase.getProperty(EtcProperties.SMS_AUTH_ENCRYPT_KEY);
			String confirmTicket = Crypto.aes(AESType.AES128).encrypt(objectStr + key + DateUtil.date("yyyyMMddHHmmss"), key);

			return Base64.encodeBase64String(confirmTicket.getBytes());
		}

		@Override
		public CertifyData decoding(ByPhase byPhase, String encodedAuthData) throws Exception {
			String key = byPhase.getProperty(EtcProperties.SMS_AUTH_ENCRYPT_KEY);
			byte[] authNumConfirmAsByte = Base64.decodeBase64((String)encodedAuthData);
			String authNumConfirm = Crypto.aes(AESType.AES128).decrypt(new String(authNumConfirmAsByte), key);
			String[] certificateNumbers = StringUtils.split(authNumConfirm, key);

			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decodeBase64(certificateNumbers[0]));
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

			return (CertifyData)objectInputStream.readObject();
		}
	},
	/**	IPIN 을 사용하여 고객정보 획득, 고유 CI/DI 생성됨	*/
	IPIN {
		@Override
		public String encoding(ByPhase byPhase, Object planAuthData) {
			return certificator.ipin().getEncData((String)planAuthData);
		}

		@Override
		public CertifyData decoding(ByPhase byPhase, String encodedAuthData) {
			IPIN iPIN = certificator.ipin().getIPINInfo(encodedAuthData);

			CertifyData certifyData = new CertifyData();
			certifyData.setSucc(true);
			certifyData.setCi(iPIN.getCoInfo1());
			certifyData.setDi(iPIN.getDupInfo());
			certifyData.setName(iPIN.getName());
			certifyData.setBirthday(iPIN.getBirthDate());
			certifyData.setGenderType(StringUtils.equals(iPIN.getGenderCode(), "0") ? GenderType.FEMALE : GenderType.MALE);
			certifyData.setOver14(Integer.parseInt(iPIN.getAgeCode()) > 3);

			// 14세 미만 정보 조회
			if (certifyData.isOver14() == false) {
				certifyData.setSucc(false);
				certifyData.setErrorMessage("14세미만 회원입니다.");
			}

			return certifyData;
		}
	},
	/**	KMC 을 사용하여 고객정보 획득, 고유 CI/DI 생성됨	*/
	KMC {
		@Override
		public String encoding(ByPhase byPhase, Object planAuthData) {
			return certificator.kmc().buildRequest((String)planAuthData);
		}

		@Override
		public CertifyData decoding(ByPhase byPhase, String encodedAuthData) {
			String[] encodedAuthDatas = StringUtils.split(encodedAuthData, SEPERATOR);
			com.diaimm.april.permission.certification.kmc.KMC kMC = certificator.kmc().getKMCInfo(encodedAuthDatas[0], encodedAuthDatas[1]);

			CertifyData certifyData = new CertifyData();
			certifyData.setSucc(true);
			certifyData.setCi(kMC.getCi());
			certifyData.setDi(kMC.getDi());
			certifyData.setName(kMC.getName());
			certifyData.setBirthday(kMC.getBirthday().toString(DateTimeFormat.forPattern("yyyyMMdd")));
			certifyData.setGenderType(kMC.getGender());
			certifyData.setOver14(kMC.isOver14());

			// 14세 미만 정보 조회
			if (certifyData.isOver14() == false) {
				certifyData.setSucc(false);
				certifyData.setErrorMessage("14세미만 회원입니다.");
			}

			return certifyData;
		}
	},
	/**	ID 를 기반으로 등록된 EMAIL 주소가 있으면, 인증 메일을 전송하고 값을 확인하는 인증	*/
	EMAIL_ADDRESS_BY_REGISTERED_ID {
		private final String FIND_PASSWORD_KEY = "changePwdByEmail";

		@Override
		public String encoding(ByPhase byPhase, Object planAuthData) throws UnsupportedEncodingException, Exception {
			return URLEncoder.encode(Crypto.aes(AESType.AES128).encrypt((String)planAuthData, FIND_PASSWORD_KEY), "UTF-8");
		}

		@Override
		public CertifyData decoding(ByPhase byPhase, String encodedAuthData) throws UnsupportedEncodingException, Exception {
			String certifySeq = Crypto.aes(AESType.AES128).decrypt(URLDecoder.decode(encodedAuthData, "UTF-8"), FIND_PASSWORD_KEY);

			CertifyData certifyData = new CertifyData();
			certifyData.setCertifySeq(certifySeq);

			return certifyData;
		}
	},
	/**	ID 를 기반으로 등록된 핸드폰 번호가 있으면, 인증 번호를 전송하고 값을 확인하는 인증	*/
	PHONE_NUMBER_BY_REGISTERED_ID {
		private final String FIND_PASSWORD_KEY = "changePwdByPhone";

		@Override
		public String encoding(ByPhase byPhase, Object planAuthData) throws UnsupportedEncodingException, Exception {
			return URLEncoder.encode(Crypto.aes(AESType.AES128).encrypt((String)planAuthData, FIND_PASSWORD_KEY), "UTF-8");
		}

		@Override
		public CertifyData decoding(ByPhase byPhase, String encodedAuthData) throws UnsupportedEncodingException, Exception {
			String certifySeq = Crypto.aes(AESType.AES128).decrypt(URLDecoder.decode(encodedAuthData, "UTF-8"), FIND_PASSWORD_KEY);

			CertifyData certifyData = new CertifyData();
			certifyData.setCertifySeq(certifySeq);

			return certifyData;
		}
	},
	/**	한신평을 통하여 고객정보 획득, 이름과 주민번호가 일치하는지 여부만 판단됨	*/
	SOCIAL_SECURITY_NUMBER {
		@Override
		public String encoding(ByPhase byPhase, Object planAuthData) throws UnsupportedEncodingException, Exception {
			CertifyData certifyData = (CertifyData)planAuthData;

			// 본인 인증 처리 
			NiceNameClient niceNameClient = new NiceNameClient();
			String result = niceNameClient.reqeust(certifyData.getName(), certifyData.getSsn());

			// 본인 인증 성공시에만 고객 정보를 만들어 준다. 
			if (StringUtils.equals(result, "1")) {
				certifyData.setSucc(true);

				// 주민등록번호를 가지고 추가 정보를 넣어준다.
				certifyData.setBirthday(NiceSsnClient.getBirthdayFromSsn(certifyData.getSsn()).toString(DateTimeFormat.forPattern("yyyyMMdd")));
				certifyData.setCi(NiceSsnClient.getCI(certifyData.getSsn()));
				certifyData.setDi(NiceSsnClient.getDI(certifyData.getSsn()));
				certifyData.setGenderType(NiceSsnClient.getGenderFromSsn(certifyData.getSsn()));
			} else {
				certifyData.setSucc(false);
				certifyData.setErrorMessage(niceNameClient.getResultMsg(result));
			}

			// 주민등록 번호 제거
			certifyData.setSsn("");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

			objectOutputStream.writeObject(certifyData);
			String objectStr = new String(Base64.encodeBase64String(byteArrayOutputStream.toByteArray()));

			return objectStr;
		}

		@Override
		public CertifyData decoding(ByPhase byPhase, String encodedAuthData) throws UnsupportedEncodingException, Exception {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decodeBase64(encodedAuthData));
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

			return (CertifyData)objectInputStream.readObject();
		}
	};

	/**
	 * 회원 정보 객체를 시리얼 라이즈 해서 String 만들어서 화면으로 내린다. 
	 * @param byPhase
	 * @param planAuthData
	 * @return
	 */
	public abstract String encoding(ByPhase byPhase, Object planAuthData) throws UnsupportedEncodingException, Exception;

	/**
	 * 시리얼 라이즈 된 회원정보를 받아서 객체로 보낸다.  
	 * @param byPhase
	 * @param encodedAuthData
	 * @return
	 */
	public abstract CertifyData decoding(ByPhase byPhase, String encodedAuthData) throws UnsupportedEncodingException, Exception;;

	public static final String SEPERATOR = KMCClient.KMC_SEPERATOR;
	private static Certificator certificator;

	public static void setCertificator(Certificator certificator) {
		CertificationType.certificator = certificator;
	}
}
