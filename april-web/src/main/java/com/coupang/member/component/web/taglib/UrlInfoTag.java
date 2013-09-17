/**
 * 
 */
package com.coupang.member.component.web.taglib;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.coupang.member.commons.ByPhase;
import com.coupang.member.commons.Env;
import com.coupang.member.component.web.util.URLHelpers;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 7.
 */
public class UrlInfoTag extends SimpleTagSupport {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public static final String COMMON_VALUE_MAP_KEY = UrlInfoTag.class.getCanonicalName() + "_MAP";
	private String key;

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void doTag() throws JspException {
		try {
			if (StringUtils.isBlank(key)) {
				key = "";
			}

			this.getJspContext().getOut().print(MapUtils.getString(getCommonValueMap(), key.toUpperCase(), ""));
		} catch (Exception e) {
			logger.error("Fail to print commonValue", e);
		}
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> getCommonValueMap() {
		if (getJspContext().getAttribute(COMMON_VALUE_MAP_KEY, PageContext.REQUEST_SCOPE) == null) {
			getJspContext().setAttribute(COMMON_VALUE_MAP_KEY, initializeCommonValueMap(((PageContext)getJspContext()).getRequest()), PageContext.REQUEST_SCOPE);
		}

		return (Map<String, String>)getJspContext().getAttribute(COMMON_VALUE_MAP_KEY, PageContext.REQUEST_SCOPE);
	}

	/**
	 * @param request
	 * @return
	 */
	private Map<String, String> initializeCommonValueMap(ServletRequest request) {
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(((PageContext)getJspContext()).getServletContext());
		ByPhase byPhase = webApplicationContext.getBean(ByPhase.class);

		Map<String, String> commonValueMap = new HashMap<String, String>();

		// TODO : 일찍부터 분리하는게 좋을 것 같긴한데..
		commonValueMap.put("DEFAULT_HOST", Env.DEFAULT_HOST);
		commonValueMap.put("DEFAULT_ENCODING", Env.DEFAULT_ENCODING);

		// Append default view Env
		for (URLHelpers uRLHelpers : URLHelpers.values()) {
			commonValueMap.put(uRLHelpers.toString(), uRLHelpers.getValue(byPhase, (HttpServletRequest)request));
		}

		return commonValueMap;
	}
}