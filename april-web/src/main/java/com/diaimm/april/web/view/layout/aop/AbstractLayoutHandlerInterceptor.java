/**
 * @fileName : LayoutAdvice.java
 * @date : 2013. 3. 12.
 * @author : diaimm. Santorini.
 * @desc : 
 */
package com.diaimm.april.web.view.layout.aop;

import com.diaimm.april.commons.ByPhase;
import com.diaimm.april.web.view.dataview.DataViewEnvironmentAware;
import com.diaimm.april.web.view.layout.annotations.Layout;
import com.diaimm.april.web.view.layout.annotations.Layout.DefaultValues;
import com.diaimm.april.web.view.layout.annotations.LayoutConfigure;
import com.diaimm.april.web.view.layout.bean.LayoutConfig;
import com.diaimm.april.web.view.layout.bean.MobileDetectingLayoutConfig;
import com.diaimm.april.web.view.layout.data.LayoutDataAware;
import com.diaimm.april.web.view.layout.data.LayoutDataProvider;
import com.diaimm.april.web.view.layout.data.provider.*;
import com.diaimm.april.web.view.selector.JspViewNameSelector;
import com.diaimm.april.web.view.selector.MobileDetectingJspViewNameSelector;
import com.diaimm.april.web.view.selector.ViewNameSelector;
import com.diaimm.april.web.view.templateview.TemplateViewEnvironmentAware;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author diaimm
 */
public abstract class AbstractLayoutHandlerInterceptor implements HandlerInterceptor, BeanFactoryAware, ApplicationContextAware, InitializingBean {
	private static final Map<String, Object> LAYOUT_CONFIGURE_METHODS = new ConcurrentHashMap<String, Object>();
	private static final String NULL = "NULL";
	private final Object layoutConfigreMethodCacheLock = new Object();
	private List<LayoutDataProvider<?>> dataProviders;
	private List<LayoutDataProvider<?>> userDataProviders;
	private ApplicationContext applicationContext;
	private ViewNameSelector viewNameSelector;
	private BeanFactory beanFactory;
	@Autowired
	private ByPhase byPhase;

	/**
	 * @return the viewNameSelector
	 */
	public ViewNameSelector getViewNameSelector() {
		return viewNameSelector;
	}

	/**
	 * request 진입 시점에 layout 정보를 clear한다.
	 *
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	String generateKey(Class<?> beanType, String methodName) {
		return ClassUtils.getQualifiedName(beanType) + ":" + methodName;
	}

	Object getLayoutConfigureMethod(Class<?> beanType, String layoutConfigureMethodName) {
		String key = generateKey(beanType, layoutConfigureMethodName);
		if (!LAYOUT_CONFIGURE_METHODS.containsKey(key)) {
			synchronized (layoutConfigreMethodCacheLock) {
				if (!LAYOUT_CONFIGURE_METHODS.containsKey(key)) {
					Method method = getLayoutConfigureMethodInstance(beanType, layoutConfigureMethodName);
					LAYOUT_CONFIGURE_METHODS.put(key, method == null ? NULL : method);
				}
			}
		}

		return LAYOUT_CONFIGURE_METHODS.get(key);
	}

	Method getLayoutConfigureMethodInstance(Class<?> beanType, String layoutConfigureMethodName) {
		Set<Method> annotatedMethods = getLayoutConfigureMethods(beanType);
		if ("".equals(layoutConfigureMethodName)) {
			if (annotatedMethods.isEmpty()) {
				return null;
			}

			if (annotatedMethods.size() == 1) {
				return annotatedMethods.iterator().next();
			}

			layoutConfigureMethodName = Layout.DefaultValues.DEFAULT_METHOD;
		}

		Set<Method> matchedMethods = new HashSet<Method>();
		for (Method method : annotatedMethods) {
			if (layoutConfigureMethodName.equals(method.getName())) {
				matchedMethods.add(method);
			}
		}

		if (matchedMethods.isEmpty()) {
			return null;
		}

		if (matchedMethods.size() > 1) {
			throw new IllegalArgumentException("two or more " + layoutConfigureMethodName + " method is defined.");
		}

		return matchedMethods.iterator().next();
	}

	/**
	 * @param beanType
	 * @return
	 */
	Set<Method> getLayoutConfigureMethods(Class<?> beanType) {
		Set<Method> annotatedMethods = new HashSet<Method>();
		for (Method method : beanType.getDeclaredMethods()) {
			LayoutConfigure layoutConfig = AnnotationUtils.findAnnotation(method, LayoutConfigure.class);
			if (layoutConfig != null) {
				annotatedMethods.add(method);
			}
		}
		return annotatedMethods;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		LayoutHolder layoutHolder = getLayoutHolder();
		if (layoutHolder.layout() == null || layoutHolder.method() == null || isAvoidLayoutUsePrefix(modelAndView)
			|| !(handler instanceof HandlerMethod)) {
			if (!isAvoidLayoutUsePrefix(modelAndView)) {
				modelAndView.setViewName(getViewNameSelector().getViewName(request, modelAndView));
			}
			return;
		}

		LayoutConfig layoutConfig = getInitializedLayoutConfig(request, response, modelAndView);
		setContentsBodyView(request, modelAndView, layoutConfig);
		setIsMobileToLayoutConfig(request, layoutConfig);

		String fromHandler = processHandlerMethod((HandlerMethod)handler);
		String fromPreset = processPresetLayoutConfigurerMethod(request, response, modelAndView, layoutConfig);
		String fromControllerConfigurer = processControllerLayoutConfigurerMethod(request, response, ((HandlerMethod)handler).getBean(),
			modelAndView, layoutConfig);
		if (fromControllerConfigurer == null) {
			// @LayoutConfigureMethod가 발견되지 않은 케이스
			if (fromPreset != null) {
				fromControllerConfigurer = fromPreset;
			}
		}

		handoverLayoutConfigName(request, modelAndView, getLayoutName(fromHandler, fromControllerConfigurer, fromPreset));
	}

	/**
	 * @return
	 */
	private LayoutHolder getLayoutHolder() {
		LayoutHolder layoutHolder = beanFactory.getBean(LayoutHolder.class);
		return layoutHolder;
	}

	/**
	 * @param fromHandler
	 * @param fromControllerConfigurer
	 * @param fromPreset
	 * @return
	 */
	private String getLayoutName(String fromHandler, String fromControllerConfigurer, String fromPreset) {
		String layoutName = fromPreset;
		if (!DefaultValues.NO_LAYOUT.equals(fromControllerConfigurer)) {
			layoutName = fromControllerConfigurer;
		}

		if (!DefaultValues.NO_LAYOUT.equals(fromHandler)) {
			layoutName = fromHandler;
		}
		return layoutName;
	}

	/**
	 * @param handler
	 * @return
	 */
	private String processHandlerMethod(HandlerMethod handler) {
		if (handler.getMethodAnnotation(Layout.class) != null) {
			return handler.getMethodAnnotation(Layout.class).layout();
		}

		return null;
	}

	/**
	 * @param request
	 * @param layoutConfig
	 */
	private void setIsMobileToLayoutConfig(HttpServletRequest request, LayoutConfig layoutConfig) {
		if (layoutConfig instanceof MobileDetectingLayoutConfig) {
			Object isMobileAttribute = request.getAttribute(MobileDetectingJspViewNameSelector.IS_MOBILE_SELECT_ATTRIBUTE_KEY);
			boolean isMobile = isMobileAttribute == null ? false : (Boolean)isMobileAttribute;
			((MobileDetectingLayoutConfig)layoutConfig).setMobileView(isMobile);
		}
	}

	/**
	 * @param modelAndView
	 * @return
	 */
	boolean isAvoidLayoutUsePrefix(ModelAndView modelAndView) {
		String viewName = modelAndView.getViewName();
		// redirect view 사용 제외
		if (StringUtils.startsWithIgnoreCase(viewName, UrlBasedViewResolver.REDIRECT_URL_PREFIX)) {
			return true;
		}

		// forward view 사용 제외
		if (StringUtils.startsWithIgnoreCase(viewName, UrlBasedViewResolver.FORWARD_URL_PREFIX)) {
			return true;
		}

		// template view 사용 제외
		if (StringUtils.startsWithIgnoreCase(viewName, TemplateViewEnvironmentAware.PREFIX)) {
			return true;
		}

		// data view 사용 제외
		if (StringUtils.startsWithIgnoreCase(viewName, DataViewEnvironmentAware.PREFIX)) {
			return true;
		}

		return false;
	}

	/**
	 * @param layoutConfigureMethod
	 * @return
	 */
	protected String getLayoutConfigName(Method layoutConfigureMethod) {
		LayoutHolder layoutHolder = getLayoutHolder();
		String presetLayout = null;
		if (layoutConfigureMethod != null) {
			LayoutConfigure layoutConfigure = AnnotationUtils.findAnnotation(layoutConfigureMethod, LayoutConfigure.class);
			presetLayout = layoutConfigure.layout();
		}

		return presetLayout != null ? presetLayout : layoutHolder.layout();
	}

	/**
	 * @param request
	 * @param response
	 * @param modelAndView
	 * @param layoutConfig
	 * @return initializer method
	 * @throws Exception
	 */
	String processPresetLayoutConfigurerMethod(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView,
		LayoutConfig layoutConfig) throws Exception {
		LayoutHolder layoutHolder = getLayoutHolder();
		if (layoutHolder.getPresetClass() != Layout.DefaultValues.NO_PRESET) {
			Map<String, ?> initializers = applicationContext.getBeansOfType(layoutHolder.getPresetClass());
			for (Entry<String, ?> initializerEntry : initializers.entrySet()) {
				Object initializer = initializerEntry.getValue();
				String initializerMethodName = getUsableInitialzerMethod(initializer.getClass()).getName();

				String layoutName = processLayoutConfigureMethod(request, response, modelAndView, layoutConfig, initializer, initializerMethodName);
				if (layoutName != null) {
					return layoutName;
				}
			}
		}

		return null;
	}

	/**
	 * @param request
	 * @param response
	 * @param modelAndView
	 * @param layoutConfig
	 * @throws Exception
	 */
	String processControllerLayoutConfigurerMethod(HttpServletRequest request, HttpServletResponse response, Object bean, ModelAndView modelAndView,
		LayoutConfig layoutConfig) throws Exception {
		LayoutHolder layoutHolder = getLayoutHolder();
		return processLayoutConfigureMethod(request, response, modelAndView, layoutConfig, bean, layoutHolder.method());
	}

	/**
	 * @param request
	 * @param response
	 * @param modelAndView
	 * @param layoutConfig
	 * @param layoutConfigureBean
	 * @param layoutConfigureMethodName
	 * @return
	 * @throws Exception
	 */
	private String processLayoutConfigureMethod(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView,
		LayoutConfig layoutConfig, Object layoutConfigureBean, String layoutConfigureMethodName) throws Exception {
		Object layoutConfigureMethod = getLayoutConfigureMethod(layoutConfigureBean.getClass(), layoutConfigureMethodName);
		if (layoutConfigureMethod == null || NULL.equals(layoutConfigureMethod)) {
			return null;
		}

		Method method = (Method)layoutConfigureMethod;
		List<Object> parameterValues = getLayoutConfigurerMethodParameters(method, request, response, modelAndView, layoutConfig);

		try {
			method.setAccessible(true);
			Object[] params = parameterValues.toArray(new Object[parameterValues.size()]);
			ReflectionUtils.invokeMethod(method, layoutConfigureBean, params);

			return getLayoutConfigName(method);
		} catch (Exception e) {
			// TODO 적절한 logging 혹은 alarming
			throw e;
		}
	}

	/**
	 * @param request
	 * @param response
	 * @param modelAndView
	 * @param method
	 * @param layoutConfig
	 * @return parameter list
	 */
	private List<Object> getLayoutConfigurerMethodParameters(Method method, HttpServletRequest request, HttpServletResponse response,
		ModelAndView modelAndView, LayoutConfig layoutConfig) {
		List<Object> parameterValues = new ArrayList<Object>();
		for (Class<?> clazz : method.getParameterTypes()) {
			parameterValues.add(getValueForLayoutConfigureMethodParameter(clazz, request, response, modelAndView, layoutConfig, parameterValues));
		}
		return parameterValues;
	}

	/**
	 * <pre>
	 * LayoutConfigure annotation이 붙은 method에 대한 parameter 를 처리합니다
	 *
	 * 기본적으로 LayoutConfig, HttpServletRequest, HttpServletResponse, ModelMap, Map 형식에 대한 injection을 수행하며, 필요시 override하세요
	 * super.getInjectionValueForLayoutConfigureMethodParameter() 에서 null이 리턴되는 경우 필요시 추가 처리를 수행합니다.
	 *
	 *
	 * if (LayoutConfig.class.isAssignableFrom(clazz)) {
	 * 		return layoutConfig;
	 *    }
	 * </pre>
	 *
	 * @param clazz
	 * @param request
	 * @param response
	 * @param modelAndView
	 * @param layoutConfig
	 * @param parameterValues
	 */
	Object getValueForLayoutConfigureMethodParameter(Class<?> clazz, HttpServletRequest request, HttpServletResponse response,
		ModelAndView modelAndView, LayoutConfig layoutConfig, List<Object> parameterValues) {

		if (MobileDetectingLayoutConfig.class.isAssignableFrom(clazz) && layoutConfig instanceof MobileDetectingLayoutConfig) {
			return layoutConfig;
		} else if (LayoutConfig.class.isAssignableFrom(clazz)) {
			return layoutConfig;
		} else {
			for (LayoutDataProvider<?> provider : dataProviders) {
				if (LayoutDataAware.class.isAssignableFrom(layoutConfig.getClass())) {
					return provider.getValue(request, modelAndView);
				}
			}
		}

		return null;
	}

	/**
	 * @param request
	 * @param modelAndView
	 * @return
	 */
	LayoutConfig getInitializedLayoutConfig(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
		LayoutHolder layoutHolder = getLayoutHolder();
		LayoutConfig layoutConfig = layoutHolder.layoutConfig();
		injectValuesForLayoutConfig(layoutConfig, request, modelAndView);
		layoutConfig.initialize();

		request.setAttribute(LayoutConfig.LAYOUT_CONFIG, layoutConfig);
		return layoutConfig;
	}

	/**
	 * <pre>
	 * LayoutConfig 객체에 대한 injection을 수행합니다.
	 *
	 * 기본적으로 LayoutConfig, HttpServletRequest, HttpServletResponse, ModelMap, Map 형식에 대한 injection을 수행하며, 필요시 override하세요
	 *
	 * 	if (layoutConfig instanceof ApplicationContextAware) {
	 * 		((ApplicationContextAware) layoutConfig).setApplicationContext(applicationContext);
	 *    }
	 * </pre>
	 *
	 * @param layoutConfig
	 * @param request
	 * @param modelAndView
	 */
	void injectValuesForLayoutConfig(LayoutConfig layoutConfig, HttpServletRequest request, ModelAndView modelAndView) {
		if (layoutConfig instanceof ApplicationContextAware) {
			((ApplicationContextAware)layoutConfig).setApplicationContext(applicationContext);
		}

		layoutConfig.setHttpServletRequest(request);
		layoutConfig.setModelAndView(modelAndView);

		LayoutDataProvider<?>[] providers = new LayoutDataProvider<?>[dataProviders.size()];
		dataProviders.toArray(providers);
		layoutConfig.addLayoutDataProvider(providers);
	}

	private Method getUsableInitialzerMethod(Class<?> initalizerClass) {
		for (Method initializeMethod : initalizerClass.getDeclaredMethods()) {
			LayoutConfigure initializeMethodLayoutConfigureAnnotation = AnnotationUtils.findAnnotation(initializeMethod, LayoutConfigure.class);
			if (initializeMethodLayoutConfigureAnnotation != null) {
				return initializeMethod;
			}
		}

		return null;
	}

	/**
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		dataProviders = new ArrayList<LayoutDataProvider<?>>();
		dataProviders.add(new HttpServletRequestProvider());
		dataProviders.add(new ModelMapProvider());
		dataProviders.add(new ModelAndViewProvider());
		dataProviders.add(new ParameterMapProvider());
		dataProviders.add(new ViewNameProvider());
		dataProviders.add(new ByPhaseProvider(byPhase));

		if (this.userDataProviders != null) {
			dataProviders.addAll(userDataProviders);
		}

		Map<String, ViewNameSelector> beansOfType = applicationContext.getBeansOfType(ViewNameSelector.class);
		if (beansOfType != null && !beansOfType.isEmpty()) {
			this.viewNameSelector = beansOfType.values().iterator().next();
		} else {
			this.viewNameSelector = new JspViewNameSelector();
		}

		BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry)beanFactory;
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(LayoutHolder.class);
		beanDefinitionBuilder.setScope("request");
		beanDefinitionRegistry.registerBeanDefinition("layoutHolder", beanDefinitionBuilder.getBeanDefinition());
	}

	/**
	 * @param userDataProviders the userDataProviders to set
	 */
	public void setUserDataProviders(List<LayoutDataProvider<?>> userDataProviders) {
		this.userDataProviders = userDataProviders;
	}

	/**
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * layout frameworkd에 맞추어 layout 설정명을 전달하도록 한다.
	 *
	 * @param layoutName
	 */
	abstract public void handoverLayoutConfigName(HttpServletRequest request, ModelAndView modelAndView, String layoutName);

	/**
	 * layout framework에 맞추어 content body에 해당되는 view를 지정하도록 한다.
	 *
	 * @param modelAndView
	 * @param layoutConfig
	 */
	abstract public void setContentsBodyView(HttpServletRequest request, ModelAndView modelAndView, LayoutConfig layoutConfig);
}
