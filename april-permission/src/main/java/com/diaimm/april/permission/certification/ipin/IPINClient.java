/**
 * 
 */
package com.diaimm.april.permission.certification.ipin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Kisinfo.Check.IPIN2Client;


/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 17.
 */
public class IPINClient {
	private static Logger logger = LoggerFactory.getLogger(IPINClient.class);
	private final String S_SITE_CODE = "B827"; // CP 구분코드(한신평부여)
	private final String S_SITE_PW = "50161373"; // CP 비밀번호(한신평부여)

	/**
	 * IPIN 인증 요청 정보 만들기
	 * 
	 * @param returnURL
	 * @return
	 */
	public String getEncData(String returnURL) {
		IPIN2Client pClient = new IPIN2Client();
		String sCPRequest = pClient.getRequestNO(S_SITE_CODE);

		int iRtn = pClient.fnRequest(S_SITE_CODE, S_SITE_PW, sCPRequest, returnURL);
		return MakeEncResultProcessor.byCode(iRtn).process(pClient);
	}

	/**
	 * IPIN 인증 완료 정보 파싱
	 * 
	 * @param encData
	 * @return
	 */
	public IPIN getIPINInfo(String encData) {
		IPIN2Client pClient = new IPIN2Client();

		int iRtn = pClient.fnResponse(S_SITE_CODE, S_SITE_PW, encData);
		return IPINDataParsingResultProcessor.byCode(iRtn).process(pClient);
	}

	/**
	 * EncData 생성 결과 코드
	 * 
	 * @author 산토리니 윤영욱 (readytogo@coupang.com)
	 * @version 2013. 6. 21.
	 */
	enum MakeEncResultProcessor {
		/**	*/
		OK(0, "정상처리 되었습니다.") {
			/**
			 * @see IPINClient.MakeEncResultProcessor#process(int)
			 */
			@Override
			String process(IPIN2Client pClient) {
				return pClient.getCipherData();
			}
		},
		/**	*/
		ERROR_SYSTEM(-1, "암호화 시스템 오류"),
		/**	*/
		ERROR_CRYPTO(-2, "암호화 오류"),
		/**	*/
		ERROR_PARAM(-9, "입력 정보 오류"),
		/**	*/
		ERROR_ETC(-99, "예상치 못한 오류입니다.");

		private final int code;
		private final String msg;

		MakeEncResultProcessor(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		static MakeEncResultProcessor byCode(int code) {
			for (MakeEncResultProcessor makeEncResult : MakeEncResultProcessor.values()) {
				if (makeEncResult.code == code) {
					return makeEncResult;
				}
			}

			return MakeEncResultProcessor.ERROR_ETC;
		}

		String process(IPIN2Client pClient) {
			logger.error(this.msg + ", returnCode : " + code);
			return null;
		}
	}

	/**
	 * EncData 파싱 결과 코드
	 * 
	 * @author 산토리니 윤영욱 (readytogo@coupang.com)
	 * @version 2013. 6. 24.
	 */
	enum IPINDataParsingResultProcessor {
		RESULT_OK(1, "정상처리 되었습니다.") {
			@Override
			IPIN process(IPIN2Client pClient) {
				IPIN iPIN = new IPIN();

				iPIN.setVNumber(pClient.getVNumber()); // 가상주민번호 (13자리이며, 숫자 또는 문자 포함)
				iPIN.setName(pClient.getName()); // 이름
				iPIN.setDupInfo(pClient.getDupInfo()); // 중복가입 확인값 (DI - 64 byte 고유값)
				iPIN.setAgeCode(pClient.getAgeCode()); // 연령대 코드 (개발 가이드 참조)
				iPIN.setGenderCode(pClient.getGenderCode()); // 성별 코드 (개발 가이드 참조)
				iPIN.setBirthDate(pClient.getBirthDate()); // 생년월일 (YYYYMMDD)
				iPIN.setNationalInfo(pClient.getNationalInfo()); // 내/외국인 정보 (개발 가이드 참조)
				iPIN.setCPRequestNum(pClient.getCPRequestNO()); // CP 요청번호
				iPIN.setAuthInfo(pClient.getAuthInfo()); // 본인확인 수단 (개발 가이드 참조)
				iPIN.setCoInfo1(pClient.getCoInfo1()); // 연계정보 확인값 (CI - 88 byte 고유값)
				iPIN.setCIUpdate(pClient.getCIUpdate()); // CI 갱신정보
				iPIN.setCipherDateTime(pClient.getCipherDateTime());
				iPIN.setCipherIPAddress(pClient.getCipherIPAddress());
				return iPIN;
			}
		},
		ERROR_SYSTEM(-1, "복호화 시스템 오류."),
		ERROR_DECODING(-4, "복호화 처리 오류."),
		ERROR_HASH_NOT_MATCHED(-5, "HASH 검증 불일치."),
		ERROR_DECODING_DATA(-6, "복호화 데이터 오류."),
		ERROR_VERSION(-7, "INV CIPHER VERSION."),
		ERROR_INPUT_DATA(-9, "입력 정보 오류."),
		ERROR_PASSWORD(1, "CP 비밀번호 불일치."),
		ERROR_ETC(-99, "예상치 못한 오류입니다.");

		private final int code;
		private final String msg;

		IPINDataParsingResultProcessor(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		static IPINDataParsingResultProcessor byCode(int code) {
			for (IPINDataParsingResultProcessor processor : IPINDataParsingResultProcessor.values()) {
				if (processor.code == code) {
					return processor;
				}
			}

			return IPINDataParsingResultProcessor.ERROR_ETC;
		}

		IPIN process(IPIN2Client pClient) {
			logger.error(this.msg + ", returnCode : " + code);
			return null;
		}
	}
}