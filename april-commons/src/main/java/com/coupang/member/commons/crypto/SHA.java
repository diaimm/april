package com.coupang.member.commons.crypto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {
	private SHAType shaType = SHAType.SHA256;

	/**
	 * @param shaType2
	 */
	SHA(SHAType shaType) {
		this.shaType = shaType;
	}

	public String get(String plainText) throws NoSuchAlgorithmException {
		try {
			return get(new ByteArrayInputStream(plainText.getBytes()));
		} catch (IOException e) {
			// not happens
			return null;
		}
	}

	public String get(InputStream inputStream) throws IOException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(shaType.algorithmName);
		byte[] dataBytes = new byte[1024];
		int nread = 0;
		while ((nread = inputStream.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);
		}

		byte[] mdbytes = md.digest();

		// convert the byte to hex format
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}

	public static enum SHAType {
		SHA1("SHA1"), //
		SHA256("SHA-256");//

		private final String algorithmName;

		SHAType(String algorithmName) {
			this.algorithmName = algorithmName;
		}
	}
}
