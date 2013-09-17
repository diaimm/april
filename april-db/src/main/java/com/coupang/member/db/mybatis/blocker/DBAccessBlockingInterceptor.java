/*
 * @fileName : DBAccessBlockingInterceptor.java
 * @date : 2013. 6. 5.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.db.mybatis.blocker;

import com.coupang.member.commons.Env;
import com.coupang.member.commons.util.FreeMarkerTemplateBuilder;
import com.coupang.member.db.mybatis.datasource.RoutingSqlSessionTemplate;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <pre>
 * 1. query blocking용 request 처리(preHandle)
 * 2. query blocking 발생에 의해 생성된 exception 처리(afterCompletion)
 * </pre>
 *
 * @author diaimm
 */
public class DBAccessBlockingInterceptor implements HandlerInterceptor, InitializingBean, BeanFactoryAware {
    @Autowired
    private Map<String, RoutingSqlSessionTemplate> sessionTemplates;
    private BlockQueryManager blockQueryManager = new BlockQueryManager();
    private BeanFactory beanFactory;

    /**
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (DBAccessBlockingRequestHandler.class.isAssignableFrom(handlerMethod.getBeanType())) {
            DBAccessBlockingRequestHandler blockRequestHandler = (DBAccessBlockingRequestHandler) handlerMethod.getBean();
            blockRequestHandler.handleRequest(request, blockQueryManager);
            return false;
        }
        return true;
    }

    /**
     * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

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
        for (RoutingSqlSessionTemplate sessionTemplate : sessionTemplates.values()) {
            sessionTemplate.addListener(new BlockedQueryAccessListener(this.blockQueryManager));
        }

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(DBAccessBlockingRequestHandlerMapping.class);
        beanDefinitionBuilder.addPropertyValue("dbAccessBlockingInterceptor", this);

        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
        beanDefinitionRegistry.registerBeanDefinition("dbAccessBlockingRequestHandlerMapping", beanDefinitionBuilder.getBeanDefinition());

        BeanDefinitionBuilder blockedQueryExceptionResolverBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(BlockedQueryExceptionResolver.class);
        beanDefinitionRegistry.registerBeanDefinition("blockedQueryExceptionResolver", blockedQueryExceptionResolverBeanDefinitionBuilder.getBeanDefinition());
    }

    /**
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private static class BlockedQueryExceptionResolver implements HandlerExceptionResolver, Ordered {
        @Override
        public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
            if (ex != null && BlockedQueryExecutionException.class.isAssignableFrom(ex.getClass())) {
                // BlockedQueryExecutionException 이 발생한 경우는 block된 query에 접근한 케이스.. 여기서 처리해야 한다.
                redirectToBlockedAccess(request, response, handler, (BlockedQueryExecutionException) ex);
            }
            return new ModelAndView();
        }

        /**
         * @param request
         * @param response
         * @param handler
         * @param ex
         */
        private void redirectToBlockedAccess(HttpServletRequest request, HttpServletResponse response, Object handler, BlockedQueryExecutionException ex) {
            // 아무튼 여기서 block query접근에 대한 예외가 처리될 것이다.
            response.setStatus(200);
            try {
                FreeMarkerTemplateBuilder templateMaker = new FreeMarkerTemplateBuilder(this.getClass(), "_default_block_alarm_page.ftl");
                String alaramSource = templateMaker.build();
                response.setCharacterEncoding(Env.DEFAULT_ENCODING);
                response.getWriter().write(alaramSource);
                response.getWriter().flush();
            } catch (Exception e) {
            }
        }

        @Override
        public int getOrder() {
            return 0;
        }
    }

    private static class DBAccessBlockingRequestHandlerMapping implements HandlerMapping {
        private DBAccessBlockingInterceptor dbAccessBlockingInterceptor;

        /**
         * @param dbAccessBlockingInterceptor the dbAccessBlockingInterceptor to set
         */
        @SuppressWarnings("unused")
        public void setDbAccessBlockingInterceptor(DBAccessBlockingInterceptor dbAccessBlockingInterceptor) {
            this.dbAccessBlockingInterceptor = dbAccessBlockingInterceptor;
        }

        /**
         * @see org.springframework.web.servlet.HandlerMapping#getHandler(javax.servlet.http.HttpServletRequest)
         */
        @Override
        public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
            return DBAccessBlockingRequestHandler.getHandler(request, dbAccessBlockingInterceptor);
        }
    }
}
