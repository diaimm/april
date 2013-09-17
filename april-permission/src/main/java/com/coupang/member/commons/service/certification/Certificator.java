/*
 * @fileName : Cetificator.java
 * @date : 2013. 7. 9.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.service.certification;

import java.util.Map;

import com.coupang.member.commons.service.certification.ipin.IPINClient;
import com.coupang.member.commons.service.certification.kmc.KMCClient;
import com.coupang.member.commons.service.certification.nice.NiceNameClient;
import com.coupang.member.commons.service.certification.nice.NiceSsnClient;

/**
 * @author diaimm
 * 
 */
public class Certificator {
	private Map<String, String> kmcUrlCode;

	public IPINClient ipin() {
		return new IPINClient();
	}

	public KMCClient kmc() {
		return new KMCClient(kmcUrlCode);
	}

	public void setKmcUrlCode(Map<String, String> kmcUrlCode) {
		this.kmcUrlCode = kmcUrlCode;
		// CertificationType 초기화
		CertificationType.setCertificator(this);
	}

	public NiceNameClient niceName() {
		return new NiceNameClient();
	}

	public NiceSsnClient niceSsn() {
		return new NiceSsnClient();
	}
}
