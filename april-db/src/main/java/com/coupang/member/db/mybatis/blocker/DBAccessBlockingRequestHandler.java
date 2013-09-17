/*
 * @fileName : DBAccessBlockingRequestHandler.java
 * @date : 2013. 6. 5.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.db.mybatis.blocker;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;

import com.coupang.member.db.mybatis.blocker.BlockQueryManager.BlockQueryManagerRequestCommand;

/**
 * db block을 수행할 handler
 * 
 * @author diaimm
 */
enum DBAccessBlockingRequestHandler {
	BLOCK("/__DB__/BLOCK") {
		@Override
		public void handleRequest(HttpServletRequest request, BlockQueryManager blockQueryManager) {
			String encrypted = request.getParameter("v");
			BlockQueryManagerRequestCommand command = BlockQueryManagerRequestCommand.fromRequestParamValue(encrypted);
			blockQueryManager.block(command);
		}
	},
	RELEASE("/__DB__/RELEASE") {
		@Override
		public void handleRequest(HttpServletRequest request, BlockQueryManager blockQueryManager) {
			String encrypted = request.getParameter("v");
			BlockQueryManagerRequestCommand command = BlockQueryManagerRequestCommand.fromRequestParamValue(encrypted);
			blockQueryManager.release(command);
		}
	};

	private final String requestURI;

	DBAccessBlockingRequestHandler(String requestURI) {
		this.requestURI = requestURI;
	}

	/**
	 * request handler
	 * 
	 * @param request
	 * @param blockQueryManager
	 */
	public abstract void handleRequest(HttpServletRequest request, BlockQueryManager blockQueryManager);

	static HandlerExecutionChain getHandler(HttpServletRequest request, DBAccessBlockingInterceptor dbAccessBlockingInterceptor) {
		String requestURI = request.getRequestURI();
		for (DBAccessBlockingRequestHandler handler : DBAccessBlockingRequestHandler.values()) {
			if (StringUtils.isNotBlank(requestURI) && requestURI.startsWith(handler.requestURI)) {
				return createHandler(handler, dbAccessBlockingInterceptor);
			}
		}

		return null;
	}

	/**
	 * @param handler
	 * @param dbAccessBlockingInterceptor
	 * @return
	 */
	static HandlerExecutionChain createHandler(DBAccessBlockingRequestHandler handler, DBAccessBlockingInterceptor dbAccessBlockingInterceptor) {
		try {
			Method method = handler.getClass().getMethod("handleRequest", HttpServletRequest.class, BlockQueryManager.class);
			HandlerExecutionChain handlerExecutionChain = new HandlerExecutionChain(new HandlerMethod(handler, method));
			handlerExecutionChain.addInterceptor(dbAccessBlockingInterceptor);
			return handlerExecutionChain;
		} catch (Exception e) {
			// 발생안함
		}

		return null;
	}

	/**
	 * @return the requestURI
	 */
	String getRequestURI() {
		return requestURI;
	}
}