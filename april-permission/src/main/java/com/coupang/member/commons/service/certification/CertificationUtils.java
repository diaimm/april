/**
 * 
 */
package com.coupang.member.commons.service.certification;

import java.util.Random;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 18.
 */
public final class CertificationUtils {
	/**
	 * 본인 인증, 핸드폰 인증을 위한 인증 번호 생성
	 * @return
	 */
	public static String makeCertificationNumber() {
		Random oRandom = new Random();
		int certificationNumber = oRandom.nextInt(1000000);
		if (certificationNumber < 100000) {
			certificationNumber += 100000;
		}

		return String.valueOf(certificationNumber);
	}

	/**
	 * 비밀번호 찾기 인증 번호 생성
	 * @return
	 */
	public static String makeCertificationRandomString(long length) {
		StringBuffer buffer = new StringBuffer();
		Random random = new Random();

		String chars[] = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,0,2,3,4,5,6,7,8,9".split(",");

		for (int i = 0; i < length; i++) {
			buffer.append(chars[random.nextInt(chars.length)]);
		}

		return buffer.toString();
	}

	/**
	 * 이메일 주소를 받아서 *** 포함된 주소로 변경해 준다.
	 * @param email
	 * @return
	 */
	public static String makeCoverdEmail(String email) {
		if (StringUtils.isNotBlank(email) && StringUtils.contains(email, "@")) {
			String[] emails = StringUtils.split(email, "@");

			if (ArrayUtils.isNotEmpty(emails) && emails.length > 1) {
				int maxWidth = (emails[0].length() - 1) > 3 ? (emails[0].length() - 1) : 3;

				return StringUtils.abbreviate(emails[0], maxWidth) + "@" + emails[1];
			}
		}

		return email;
	}
}
