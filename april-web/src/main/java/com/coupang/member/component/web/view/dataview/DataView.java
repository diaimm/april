/*
 * @fileName : DataView.java
 * @date : 2013. 5. 24.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.component.web.view.dataview;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author diaimm
 * 
 */
public class DataView extends AbstractUrlBasedView implements DataViewEnvironmentAware {
	public static SourceDataSetter getBuilder(ModelAndView modelAndView) {
		return new DataViewBuilderImpl(modelAndView);
	}

	/**
	 * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Object dataViewContextObj = request.getAttribute(CONTEXT_KEY);
		if (dataViewContextObj == null || !(dataViewContextObj instanceof DataViewContext)) {
			// view 정보 세팅 없음
			return;
		}

		DataViewContext dataViewContext = (DataViewContext)dataViewContextObj;
		response.setContentType(dataViewContext.getContentType());
		response.getWriter().write(dataViewContext.getResponseBody());
	}

	/**
	 * DataView building 용 class 들
	 * 
	 * @return
	 */
	public static interface SourceDataSetter {
		DataTypeSetter from(Object data);
	}

	public static interface DataTypeSetter {
		DataViewBuilder to(DataType dataType);
	}

	public static interface DataViewBuilder {
		String build();
	}

	/**
	 * 최종 builder 형태
	 * 
	 * @author diaimm
	 */
	static class DataViewBuilderImpl implements DataViewEnvironmentAware, SourceDataSetter, DataTypeSetter, DataViewBuilder {
		private ModelAndView modelAndView;
		private Object data;
		private DataType dataType;

		public DataViewBuilderImpl(ModelAndView modelAndView) {
			this.modelAndView = modelAndView;
		}

		public DataTypeSetter from(Object data) {
			this.data = data;
			return this;
		}

		public DataViewBuilder to(DataType dataType) {
			this.dataType = dataType;
			return this;
		}

		public String build() {
			DataViewContext viewContenxt = this.dataType.getViewContenxt(this.data, this.data.getClass());
			this.modelAndView.addObject(CONTEXT_KEY, viewContenxt);

			String viewName = PREFIX + this.dataType.name();
			this.modelAndView.setViewName(viewName);

			return viewName;
		}
	}
}
