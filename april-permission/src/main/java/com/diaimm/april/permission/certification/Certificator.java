/*
 * @fileName : Cetificator.java
 * @date : 2013. 7. 9.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.permission.certification;

import java.util.Map;

import com.diaimm.april.permission.certification.ipin.IPINClient;
import com.diaimm.april.permission.certification.kmc.KMCClient;
import com.diaimm.april.permission.certification.nice.NiceNameClient;
import com.diaimm.april.permission.certification.nice.NiceSsnClient;
import com.diaimm.april.permission.certification.ipin.IPINClient;
import com.diaimm.april.permission.certification.kmc.KMCClient;
import com.diaimm.april.permission.certification.nice.NiceNameClient;
import com.diaimm.april.permission.certification.nice.NiceSsnClient;

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
