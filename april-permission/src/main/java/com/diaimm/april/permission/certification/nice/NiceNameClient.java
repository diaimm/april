package com.diaimm.april.permission.certification.nice;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import com.google.common.io.Closeables;

/**
 * NICE 한국신용평가정보 를 통한 인증 - 이름인증 
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 8. 12.
 */
public class NiceNameClient {
	public int errCode;
	private String chkName;
	private String jumin;
	private String siteCode;
	private String encJumin;
	private int timeOut;
	final short PROC_OK = 0;
	final short DATA_ERR = 21;
	public final static String SITECODE = "L705";
	public final static String SITEPW = "91022891";
	public final static String OK = "1";

	public NiceNameClient() {
		errCode = 0;
		chkName = "";
		jumin = "";
		siteCode = NiceNameClient.SITECODE;
		encJumin = "";
		timeOut = 30000;
	}

	public String getEncJumin() {
		return encJumin;
	}

	private void setChkName(String s) {
		chkName = s;
		try {
			String as[] = null;
			as = (new String[] {"KS_C_5601-1987", "EUC-KR", "ISO_8859-1", "ISO_8859-2", "ISO-10646-UCS-2", "IBM037", "IBM273", "IBM277", "IBM278", "IBM280", "IBM284", "IBM285", "IBM297", "IBM420",
				"IBM424", "IBM437", "IBM500", "IBM775", "IBM850", "IBM852", "IBM855", "IBM857", "IBM860", "IBM861", "IBM862", "IBM863", "IBM864", "IBM865", "IBM866", "IBM868", "IBM869", "IBM870",
				"IBM871", "IBM918", "IBM1026", "Big5-HKSCS", "UNICODE-1-1", "UTF-16BE", "UTF-16LE", "UTF-16", "UTF-8", "ISO-8859-13", "ISO-8859-15", "GBK", "GB18030", "JIS_Encoding", "Shift_JIS",
				"Big5", "TIS-620", "us-ascii", "iso-8859-1", "iso-8859-2", "iso-8859-3", "iso-8859-4", "iso-8859-5", "iso-8859-6", "iso-8859-7", "iso-8859-8", "iso-8859-9", "koi8-r", "euc-cn",
				"euc-tw", "big5", "euc-jp", "shift_jis", "euc-kr"});
			for (int i = 0; i < as.length; i++) {
				for (int j = 0; j < as.length; j++) {
					if (i != j) {
						new String(s.getBytes(as[i]), as[j]);
					}
				}

			}

		} catch (Exception exception) {
		}
	}

	public String reqeust(String name, String jumin) {
		String rtn = setJumin(jumin);
		setChkName(name);
		if ("0".equals(rtn)) {
			return getRtn().trim();
		} else {
			return rtn;
		}
	}

	public String setJumin(String s) {
		jumin = s.trim() + NiceNameClient.SITEPW;
		return String.valueOf(getEncJumin(jumin, 21));
	}

	public void setSiteCode(String s) {
		siteCode = s;
	}

	public void setTimeOut(int i) {
		timeOut = i;
	}

	public String getRtn() {
		return getNameCheck();
	}

	private int getRandom() {
		return Math.abs((new Long(System.currentTimeMillis())).intValue());
	}

	private String getNameCheck() {
		String rtn = "";
		Socket socket = null;
		InputStream inputstream = null;
		PrintWriter printwriter = null;
		try {
			URL url = new URL("http://203.234.219.72/cnm.asp");
			String host = url.getHost();
			int port = 81 + getRandom() % 5;
			String file = url.getFile();
			socket = new Socket(host, port);
			socket.setSoTimeout(timeOut);
			printwriter = new PrintWriter(socket.getOutputStream(), false);
			inputstream = socket.getInputStream();
			StringBuffer param = new StringBuffer();
			param.append(URLEncoder.encode("a3", "euc-kr") + "=" + URLEncoder.encode(chkName, "euc-kr") + "&");
			param.append(URLEncoder.encode("a2", "euc-kr") + "=" + URLEncoder.encode(encJumin, "euc-kr") + "&");
			param.append(URLEncoder.encode("a1", "euc-kr") + "=" + URLEncoder.encode(siteCode, "euc-kr"));
			int length = param.toString().length();
			StringBuffer msg = new StringBuffer();
			msg.append("POST " + file + " HTTP/1.1\n");
			msg.append("Accept: */*\n");
			msg.append("Connection: close\n");
			msg.append("Host: wtname.creditbank.co.kr\n");
			msg.append("Content-Type: application/x-www-form-urlencoded\n");
			msg.append("Content-Length: " + length + "\r\n");
			msg.append("\r\n");
			msg.append(param.toString());
			printwriter.print(msg.toString());
			printwriter.flush();
			msg.setLength(0);

			int l = 0;
			for (boolean flag = true; flag && l != -1; flag = (l = inputstream.read()) == 114 ? (l = inputstream.read()) == 101 ? (l = inputstream.read()) == 115 ? (l = inputstream.read()) == 117
				? (l = inputstream.read()) == 108 ? (l = inputstream.read()) == 116 ? (l = inputstream.read()) != 61 : true : true : true : true : true : true)
				;
			byte response[] = new byte[2];
			inputstream.read(response);
			printwriter.close();
			inputstream.close();
			socket.close();
			socket = null;
			inputstream = null;
			printwriter = null;
			rtn = (new String(response, "KSC5601")).toString();
		} catch (MalformedURLException malformedurlexception) {
			rtn = "62";
		} catch (NoRouteToHostException noroutetohostexception) {
			rtn = "61";
		} catch (Exception exception) {
			rtn = "63";
		} finally {
			Closeables.closeQuietly(printwriter);
			Closeables.closeQuietly(inputstream);
			if (socket != null) {
				try {
					socket.close();
					socket = null;
				} catch (Exception exception13) {
				}
			}
		}
		return rtn;
	}

	private int getEncJumin(String s, int i) {
		String s1 = "13814175622071120141181061768611993108841416921423107181672510714175411266712670119411737212225184002090820525212741182820947153241426022005196831631213938161862274312787181662007815703134602165910388182131264812368213511080911151159881253313998114131777122809215401592122930118692301418370102821668712210148061538513870120181727420355200961795413534192821169714960142231124510693129551063218404145651690617787165521359419983207241159515423148221137115237203671177021155195251257514999190251531020044";
		int j = 0;
		int k = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		String s4 = s;
		String s6 = "";
		String s8 = "";
		s4.trim();
		if (i != s4.length())
			return 21;
		j = i - (i / 3) * 3;
		if (j == 2)
			s4 = s4 + "00";
		else if (j == 1)
			s4 = s4 + "0";
		k = (int)(Math.random() * 100D);
		s8 = s1.substring(k * 5, k * 5 + 5);
		l = Integer.valueOf(s8).intValue();
		i1 = s4.length() / 3 / 2;
		DecimalFormat decimalformat = null;
		decimalformat = new DecimalFormat("00");
		s6 = decimalformat.format(k);
		decimalformat = new DecimalFormat("00000");
		for (j1 = 0; j1 < i1; j1++) {
			String s9 = s4.substring(j1 * 2 * 3, j1 * 2 * 3 + 3);
			String s11 = decimalformat.format((new Integer(s9)).intValue() + l);
			s9 = s4.substring((j1 * 2 + 1) * 3, (j1 * 2 + 1) * 3 + 3);
			String s13 = decimalformat.format((new Integer(s9)).intValue() + l);
			s6 = s6 + s13;
			s6 = s6 + s11;
		}

		if (i1 * 2 < s4.length() / 3) {
			String s10 = s4.substring(j1 * 2 * 3, j1 * 2 * 3 + 3);
			String s12 = decimalformat.format((new Integer(s10)).intValue() + l);
			s6 = s6 + s12;
		}
		encJumin = s6;
		return 0;
	}

	public String getResultMsg(String result) {
		String returnMsg = "";
		switch (Integer.parseInt(result)) {
			case 1:
				returnMsg = "본인 맞음";
				break;
			case 2:
				returnMsg = "이름과 주민번호가 일치하지 않습니다. <a href='http://www.namecheck.co.kr/per_callcenter.asp' target='_blank'>[여기]</a>로 이동하신후 실명등록을 확인해보세요.";
				break;
			case 3:
				returnMsg = "실명인증이 확인되지 않습니다. <a href='http://www.namecheck.co.kr/per_callcenter.asp' target='_blank'>[여기]</a>로 이동하신후 실명등록을 확인해보세요.";
				break;
			case 4:
				returnMsg = "죄송합니다. 네트워크 장애로 확인이 지연되오니 잠시 후 다시 시도해주세요.";
				break;
			case 5:
				returnMsg = "정확한 주민번호를 입력해 주세요.";
				break;
			case 6:
				returnMsg = "만19세 미만입니다.";
				break;
			case 9:
				returnMsg = "전달된 정보가 부족합니다. 관리자에게 문의바랍니다.";
				break;
			case 10:
				returnMsg = "사이트 아이디가 잘못됐습니다. 관리자에게 문의바랍니다.";
				break;
			case 11:
				returnMsg = "관리자에게 문의바랍니다.";
				break;
			case 12:
				returnMsg = "사이트 비밀번호가 잘못됐습니다. 관리자에게 문의바랍니다.";
				break;
			case 13:
				returnMsg = "인증시스템 장애입니다. 잠시후 다시 시도해 주시기 바랍니다.";
				break;
			case 15:
			case 16:
				returnMsg = "Decoding 에러입니다. 관리자에게 문의바랍니다.";
				break;
			case 21:
			case 24:
				returnMsg = "주민번호를 확인해 주시기 바랍니다.";
				break;
			case 50:
				returnMsg = "정보도용 차단 요청된 주민번호입니다.";
				break;
			case 55:
			case 56:
			case 57:
				returnMsg = "외국인 번호를 확인해 주시기 바랍니다.";
				break;
			case 58:
				returnMsg = "출입국 관리소 통신 오류입니다. 잠시후 다시 시도해 주시기 바랍니다.";
				break;
			case 61:
			case 62:
			case 63:
				returnMsg = "네트워크 관련 에러입니다. 관리자에게 문의해 주시기 바랍니다.";
				break;
		}

		return returnMsg;
	}
}
