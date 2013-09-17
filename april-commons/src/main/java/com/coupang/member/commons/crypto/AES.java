/**
 * 
 */
package com.coupang.member.commons.crypto;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.coupang.auth.Encryption;
import com.coupang.member.commons.Env;

/**
 * 암호화 유틸
 * 
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 3.
 */
public final class AES {
	private static final int byteLength = 16;
	private static Encryption encryption = new Encryption();
	private static Charset CHARSET = Charset.forName(Env.DEFAULT_ENCODING);
	private AESType aesType = AESType.AES128;

	/**
	 * @param aes128
	 */
	AES(AESType aesType) {
		this.aesType = aesType;
	}

	public SecretKeySpec getKeySpec(String key) throws IOException, NoSuchAlgorithmException {
		Base64 encoder = new Base64();
		// asd333
		byte[] bytes = encoder.encode(key.getBytes(CHARSET));
		byte[] keyBytes = new byte[byteLength];
		if (bytes.length > byteLength) {
			for (int i = 0; i < byteLength; i++) {
				keyBytes[i] = bytes[i];
			}
		} else if (bytes.length < byteLength) {
			for (int i = 0; i < byteLength; i++) {
				if (i < bytes.length) {
					keyBytes[i] = bytes[i];
				} else {
					keyBytes[i] = new Byte("0");
				}
			}
		} else {
			keyBytes = bytes;
		}
		return new SecretKeySpec(keyBytes, aesType.cipherType);
	}

	public String encrypt(String planAuthData, String key) throws Exception {
		SecretKeySpec spec = getKeySpec(key);

		Cipher cipher = Cipher.getInstance(aesType.cipherType);
		cipher.init(Cipher.ENCRYPT_MODE, spec);
		Base64 encoder = new Base64();
		byte[] base64 = encoder.encode(cipher.doFinal(planAuthData.getBytes(CHARSET)));

		char[] encryptChar = new String(base64, CHARSET).toCharArray();
		StringBuffer reverseBuffer = new StringBuffer();
		for (int i = encryptChar.length - 1; i >= 0; i--) {
			reverseBuffer.append(encryptChar[i]);
		}
		// return reverseBuffer.toString();
		return new String(encoder.encode(cipher.doFinal(reverseBuffer.toString().getBytes(CHARSET))), CHARSET);
	}

	public String decrypt(String encText, String key) throws Exception {
		encText = encText.replace(' ', '+');
		SecretKeySpec spec = getKeySpec(key);
		Cipher cipher = Cipher.getInstance(aesType.cipherType);
		cipher.init(Cipher.DECRYPT_MODE, spec);
		Base64 encoder = new Base64();

		String decryptText1 = new String(cipher.doFinal(encoder.decode(encText)), CHARSET);

		char[] textChar = decryptText1.toCharArray();
		StringBuffer reverseBuffer = new StringBuffer();
		for (int i = textChar.length - 1; i >= 0; i--) {
			reverseBuffer.append(textChar[i]);
		}

		String decryptText = new String(cipher.doFinal(encoder.decode(reverseBuffer.toString())), CHARSET);
		return decryptText;
	}

	public String encryptKey(String orgText) {
		try {
			return encryption.encryptKey(orgText);
		} catch (Exception e) {
			return null;
		}
	}

	public String decryptKey(String encText) {
		try {
			return encryption.decryptKey(encText);
		} catch (Exception e) {
			return null;
		}
	}

	public static enum AESType {
		AES128("AES");

		private final String cipherType;

		AESType(String cipherType) {
			this.cipherType = cipherType;
		}
	}
}