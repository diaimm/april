package com.diaimm.april.web.compress.taglibs;

import com.diaimm.april.web.compress.version.manager.CompressFileProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * CSS, JS 태그 라이브러리의 상위 클래스
 *
 * @author 이성준
 * @version $Rev$, $Date$
 */
public abstract class AbstractCompressTag extends RequestContextAwareTag {
	protected String prefix = null;
	private Logger logger = LoggerFactory.getLogger(AbstractCompressTagOld.class);

	/**
	 * {@link com.diaimm.april.web.compress.version.manager.CompressFileProvider}를 리턴한다.
	 *
	 * @return {@link com.diaimm.april.web.compress.version.manager.CompressFileProvider}
	 */
	protected CompressFileProvider getCompressFileProvider() {
		WebApplicationContext applicationContext = getRequestContext().getWebApplicationContext();
		return applicationContext.getBean(CompressFileProvider.class);
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	protected int doStartTagInternal() throws Exception {
		JspWriter writer = pageContext.getOut();

		try {
			String fileName = getCompressFileProvider().getUsingFileName(prefix);
			if (StringUtils.isBlank(fileName)) {
				logger.debug("{}에 대한 매칭을 찾을 수 없습니다.", prefix);
				return SKIP_BODY;
			}

			write(writer, fileName);
		} catch (Exception e) {
			throw new JspException("fail in writing message", e);
		} finally {
		}
		return SKIP_BODY;
	}

	@Override
	public int doAfterBody() throws JspException {
		return EVAL_PAGE;
	}

	/**
	 * writer에 파일 정보를 출력한다.
	 *
	 * @param writer
	 *            {@link javax.servlet.jsp.JspWriter}
	 * @param files
	 *            파일 목록
	 * @throws java.io.IOException
	 */
	protected abstract void write(JspWriter writer, String files) throws IOException;
}
