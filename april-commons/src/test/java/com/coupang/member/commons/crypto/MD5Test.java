package com.coupang.member.commons.crypto;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import com.coupang.member.commons.crypto.Crypto;
import com.coupang.member.commons.crypto.SHA.SHAType;

public class MD5Test {
	@Test
	public void test() throws NoSuchAlgorithmException {
		System.out.println(Crypto.sha(SHAType.SHA256).get(Crypto.md5().get("1q2w3e4r")));
	}
}