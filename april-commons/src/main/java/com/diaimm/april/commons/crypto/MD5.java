/**
 * 
 */
package com.diaimm.april.commons.crypto;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author diaimm
 * @version 2013. 6. 5.
 */
public final class MD5 {
	MD5() {
	}

	/**
	 * MD5 암호화
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public String get(String plainText) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plainText.getBytes());

		byte byteData[] = md.digest();

		//convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}

	/**
	 * MD5 암호화 + Base64
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException 
	 */
	public String getBase64(InputStream plainInputStream) throws NoSuchAlgorithmException, IOException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		byte[] buffer = new byte[8192];
		int length;

		while ((length = plainInputStream.read(buffer)) != -1) {
			messageDigest.update(buffer, 0, length);
		}

		byte[] raw = messageDigest.digest();
		// printout in 64 base
		Base64 encoder = new Base64();
		byte[] base64 = encoder.encode(raw);

		return new String(base64);
	}
}
