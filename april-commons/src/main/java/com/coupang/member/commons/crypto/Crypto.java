/*
 * @fileName : CryptoAlgorithm.java
 * @date : 2013. 7. 10.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.crypto;

import com.coupang.member.commons.crypto.AES.AESType;
import com.coupang.member.commons.crypto.SHA.SHAType;

/**
 * @author diaimm
 * 
 */
public class Crypto {
	public static MD5 md5() {
		return new MD5();
	}

	public static AES aes(AESType aesType) {
		return new AES(AESType.AES128);
	}

	public static SHA sha(SHAType shaType) {
		return new SHA(shaType);
	}
}
