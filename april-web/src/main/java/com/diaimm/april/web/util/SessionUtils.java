/**
 * 
 */
package com.diaimm.april.web.util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.UUID;
import java.util.zip.CRC32;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.google.common.base.Charsets;
import com.google.common.primitives.Longs;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 4.
 */
public final class SessionUtils {
	static Hex hexCoder = new Hex();

	public static boolean isValid(String sid) {
		if (sid == null || sid.length() != 32 + 8) {
			return false;
		}
		try {
			byte[] data = (byte[]) hexCoder.decode(sid.substring(0, 32));
			byte[] checksum = (byte[]) hexCoder.decode(sid.substring(32, 32 + 8));
			return Arrays.equals(crc32(data), checksum);
		} catch (DecoderException e) {
			throw new RuntimeException("Error while checking validity of the session id", e);
		}
	}

	public static String generate() {
		return generate(UUID.randomUUID());
	}

	static String generate(UUID uuid) {
		ByteBuffer data = ByteBuffer.allocate(8 * 2);
		data.putLong(uuid.getMostSignificantBits());
		data.putLong(uuid.getLeastSignificantBits());
		data.flip();
		byte[] checksum = crc32(data.array());
		StringBuilder result = new StringBuilder();
		result.append(Long.toHexString(uuid.getMostSignificantBits()));
		result.append(Long.toHexString(uuid.getLeastSignificantBits()));
		result.append(new String(hexCoder.encode(checksum), Charsets.UTF_8));
		return result.toString();
	}

	static byte[] md5(byte[] data) {
		try {
			MessageDigest checksum = MessageDigest.getInstance("MD5");
			checksum.update(data, 0, data.length);
			return checksum.digest();
		} catch (Exception e) {
			throw new RuntimeException("Error while calculating checksum", e);
		}
	}

	static byte[] crc32(byte[] data) {
		try {
			CRC32 checksum = new CRC32();
			checksum.update(data, 0, data.length);
			byte[] b64 = Longs.toByteArray(checksum.getValue());
			byte[] b32 = new byte[4];
			System.arraycopy(b64, 4, b32, 0, 4);
			return b32;
		} catch (Exception e) {
			throw new RuntimeException("Error while calculating checksum", e);
		}
	}
}