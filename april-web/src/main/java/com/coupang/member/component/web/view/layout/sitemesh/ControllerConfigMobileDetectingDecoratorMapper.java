/**
 * @fileName : ControllerConfigDecoratorMapper.java
 * @date : 2013. 5. 22.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.component.web.view.layout.sitemesh;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.coupang.member.component.web.view.selector.MobileDetectingJspViewNameSelector;
import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.mapper.AbstractDecoratorMapper;
import com.opensymphony.module.sitemesh.mapper.ConfigLoader;
import com.opensymphony.module.sitemesh.mapper.DefaultDecorator;

/**
 * @author diaimm
 */
public class ControllerConfigMobileDetectingDecoratorMapper extends AbstractDecoratorMapper {
	private static Map<String, Boolean> mobileViewFileExistsCache = new ConcurrentHashMap<String, Boolean>();
	private String mobilePrefix = "/WEB-INF/views_mobile";
	private String webPrefix = "/WEB-INF/views";
	private ConfigLoader configLoader = null;

	public void init(Config config, Properties properties, DecoratorMapper parent) throws InstantiationException {
		super.init(config, properties, parent);
		try {
			String fileName = properties.getProperty("config", "/WEB-INF/decorators.xml");
			configLoader = new ConfigLoader(fileName, config);
		} catch (Exception e) {
			throw new InstantiationException(e.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Decorator getNamedDecorator(HttpServletRequest request, String name) {
		if (name == null && request.getAttribute("controllerConfigDecorator") != null) {
			String layoutDecorator = (String)request.getAttribute("controllerConfigDecorator");
			// 레이아웃 지정용 decorator의 사용이 끝나면 제거한다.
			request.removeAttribute("controllerConfigDecorator");
			return getNamedDecoratorOrg(request, layoutDecorator);
		}

		return getNamedDecoratorOrg(request, name);
	}

	@Override
	public Decorator getDecorator(HttpServletRequest request, Page page) {
		String thisPath = request.getServletPath();

		if (thisPath == null) {
			String requestURI = request.getRequestURI();
			if (request.getPathInfo() != null) {
				thisPath = requestURI.substring(0, requestURI.indexOf(request.getPathInfo()));
			} else {
				thisPath = requestURI;
			}
		} else if ("".equals(thisPath)) {
			thisPath = request.getPathInfo();
		}

		String name = null;
		try {
			name = configLoader.getMappedName(thisPath);
		} catch (ServletException e) {
			e.printStackTrace();
		}

		final Decorator result = getNamedDecorator(request, name);
		if (result == null) {
			return super.getDecorator(request, page);
		}

		//		return modifyPath(result, request);
		return result;
	}

	/**
	 * @param mobilePrefix
	 *            the mobilePrefix to set
	 */
	public void setMobilePrefix(String mobilePrefix) {
		this.mobilePrefix = mobilePrefix;
	}

	/**
	 * @param webPrefix
	 *            the webPrefix to set
	 */
	public void setWebPrefix(String webPrefix) {
		this.webPrefix = webPrefix;
	}

	/**
	 * @param page
	 * @param request
	 * @return
	 */
	private Decorator modifyPath(final Decorator decorator, HttpServletRequest request) {
		String webViewPath = decorator.getPage();
		String mobileViewPath = webViewPath.replace(webPrefix, mobilePrefix);

		Object isMobileViewSelected = request.getAttribute(MobileDetectingJspViewNameSelector.IS_MOBILE_SELECT_ATTRIBUTE_KEY);
		if (isMobileViewSelected != null && ((Boolean)isMobileViewSelected)) {
			if (isMobileViewFileExists(request, mobileViewPath)) {
				return new DefaultDecorator(decorator.getName(), mobileViewPath, null) {
					public String getInitParameter(String paramName) {
						return decorator.getInitParameter(paramName);
					}
				};
			}
		}
		return decorator;
	}

	/**
	 * @param realPath
	 * @return
	 */
	private boolean isMobileViewFileExists(HttpServletRequest request, String mobileViewPath) {
		if (!mobileViewFileExistsCache.containsKey(mobileViewPath)) {
			synchronized (mobileViewFileExistsCache) {
				if (!mobileViewFileExistsCache.containsKey(mobileViewPath)) {
					String realPath = request.getSession().getServletContext().getRealPath(mobileViewPath);
					File file = new File(realPath);
					mobileViewFileExistsCache.put(mobileViewPath, file.exists() && file.isFile());
				}
			}
		}

		return mobileViewFileExistsCache.get(mobileViewPath);
	}

	private Decorator getNamedDecoratorOrg(HttpServletRequest request, String name) {
		Decorator result = null;
		try {
			result = configLoader.getDecoratorByName(name);
		} catch (ServletException e) {
			e.printStackTrace();
		}

		if (result == null || (result.getRole() != null && !request.isUserInRole(result.getRole()))) {
			// if the result is null or the user is not in the role
			return super.getNamedDecorator(request, name);
		} else {
			//			return result;
			return modifyPath(result, request);
		}
	}
}
