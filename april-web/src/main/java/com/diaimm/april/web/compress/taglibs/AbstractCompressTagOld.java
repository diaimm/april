package com.diaimm.april.web.compress.taglibs;

import com.diaimm.april.web.compress.version.manager.CompressFileProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * CSS, JS 태그 라이브러리의 상위 클래스
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
public abstract class AbstractCompressTagOld extends SimpleTagSupport {
	private Logger logger = LoggerFactory.getLogger(AbstractCompressTagOld.class);
	protected String prefix = null;

	/**
	 * {@link com.diaimm.april.web.compress.version.manager.CompressFileProvider}를 리턴한다.
	 * 
	 * @return {@link com.diaimm.april.web.compress.version.manager.CompressFileProvider}
	 */
	protected CompressFileProvider getCompressFileProvider() {
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(((PageContext) getJspContext())
				.getServletContext());
		return applicationContext.getBean(CompressFileProvider.class);
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	@Override
	public void doTag() throws JspException, IOException {
		JspWriter writer = getJspContext().getOut();

		try {
			String fileName = getCompressFileProvider().getUsingFileName(prefix);
			if (StringUtils.isBlank(fileName)) {
				logger.debug("{}에 대한 매칭을 찾을 수 없습니다.", prefix);
				return;
			}

			write(writer, fileName);
		} catch (Exception e) {
			throw new JspException("fail in writing message", e);
		} finally {
		}
	}

	/**
	 * writer에 파일 정보를 출력한다.
	 * 
	 * @param writer
	 *            {@link JspWriter}
	 * @param files
	 *            파일 목록
	 * @throws IOException
	 */
	protected abstract void write(JspWriter writer, String files) throws IOException;
}
