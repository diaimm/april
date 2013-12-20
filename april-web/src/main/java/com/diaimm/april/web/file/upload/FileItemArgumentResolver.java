/*
 * @(#)FileItemArgumentResolver.java / version $Date$
 */
package com.diaimm.april.web.file.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * {@link FileItem}을 처리하기 위한 {@link org.springframework.web.bind.support.WebArgumentResolver}
 *
 * @author 이성준
 * @version $Rev$, $Date$
 */
public class FileItemArgumentResolver implements HandlerMethodArgumentResolver {
	private final Logger log = LoggerFactory.getLogger(FileItemArgumentResolver.class);

	/**
	 * {@link javax.servlet.http.HttpServletRequest}로부터 {@link FileItem}을 처리한다
	 *
	 * @param request {@link javax.servlet.http.HttpServletRequest}
	 * @return {@link FileItem} 또는 UNRESOLVED
	 */
	private Object processFileItem(NativeWebRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request.getNativeRequest();
		if (!(httpServletRequest instanceof MultipartHttpServletRequest)) {
			log.info("HttpRequest가 MultipartHttpServletRequest 타입이 아닙니다");
			return null;
		}

		final MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)httpServletRequest;
		final Map<String, MultipartFile> files = multipartRequest.getFileMap();

		if (CollectionUtils.isEmpty(files)) {
			log.debug("업로드 된 파일이 없습니다");
			return null;
		}

		if (files.size() > 1) {
			throw new IllegalStateException("업로드 된 파일의 갯수가 1개 이상입니다. FileItem[]을 사용하세요");
		}

		return createFileItem(files.values().iterator().next());
	}

	/**
	 * {@link javax.servlet.http.HttpServletRequest}로부터 {@link FileItem} 배열을 처리한다
	 *
	 * @param request {@link javax.servlet.http.HttpServletRequest}
	 * @return {@link FileItem} 배열 또는 UNRESOLVED
	 */
	private Object processFileItems(NativeWebRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request.getNativeRequest();
		if (!(httpServletRequest instanceof MultipartHttpServletRequest)) {
			log.info("HttpRequest가 MultipartHttpServletRequest 타입이 아닙니다");
			return null;
		}

		final MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)httpServletRequest;
		final Map<String, MultipartFile> files = multipartRequest.getFileMap();
		if (CollectionUtils.isEmpty(files)) {
			log.info("업로드 된 파일이 없습니다");
			return null;
		}

		List<FileItem> fileItems = new ArrayList<FileItem>();
		for (Entry<String, MultipartFile> entry : files.entrySet()) {
			List<MultipartFile> mFiles = multipartRequest.getFiles(entry.getKey());
			for (MultipartFile multipartFile : mFiles) {
				if (multipartFile.isEmpty()) {
					continue;
				}

				fileItems.add(createFileItem(multipartFile));
			}
		}

		// 파일 업로드를 위해 FileItem 배열을 생성한다.
		return fileItems.toArray(new FileItem[fileItems.size()]);
	}

	private FileItem createFileItem(MultipartFile multipartFile) {
		FileItem fileItem = new FileItem();
		fileItem.setMultipartFile(multipartFile);
		fileItem.setFieldName(multipartFile.getName());
		return fileItem;
	}

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		Class<?> type = methodParameter.getParameterType();
		if (FileItem.class.isAssignableFrom(type) || FileItem[].class.isAssignableFrom(type)) {
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory) throws Exception {
		Class<?> type = methodParameter.getParameterType();
		if (FileItem.class.isAssignableFrom(type)) {
			return this.processFileItem(webRequest);
		}

		return this.processFileItems(webRequest);
	}
}
