package com.diaimm.april.web.view.templateview;

import com.diaimm.april.commons.Env;
import com.diaimm.april.commons.util.FreeMarkerTemplateBuilder;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 9. 10
 * Time: 오후 8:40
 */
class TemplateView extends AbstractUrlBasedView implements TemplateViewEnvironmentAware {
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String url = this.getUrl();
		String viewName = url.replace(PREFIX, "");

		FreeMarkerTemplateBuilder templateBuilder = new FreeMarkerTemplateBuilder(this.getClass(), viewName);
		FreeMarkerTemplateBuilder.AttributeBuilder attribute = templateBuilder.attribute();
		for (Map.Entry<String, Object> entry : model.entrySet()) {
			attribute.set(entry.getKey(), entry.getValue());
		}

		Enumeration attributeNames = request.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String attributeKey = (String)attributeNames.nextElement();
			attribute.set(attributeKey, request.getAttribute(attributeKey));
		}

		String responseBody = templateBuilder.build();
		response.setContentType("text/html");
		response.setCharacterEncoding(Env.DEFAULT_ENCODING);
		response.getWriter().write(responseBody);
	}
}
