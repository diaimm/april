/**
 * @fileName : LayoutHolder.java
 * @date : 2013. 3. 12.
 * @author : diaimm. Santorini.
 * @desc : 
 */
package com.coupang.member.component.web.view.layout.aop;

import com.coupang.member.component.web.view.layout.bean.LayoutConfig;

/**
 * @author diaimm
 * 
 */
public class LayoutHolder {
	private LayoutInfo layoutInfo;

	void init(LayoutInfo layoutInfo) {
		this.layoutInfo = layoutInfo;
	}

	public String layout() {
		return layoutInfo == null ? null : layoutInfo.getLayout();
	}

	public String method() {
		return layoutInfo == null ? null : layoutInfo.getMethod();
	}

	public LayoutConfig layoutConfig() {
		return layoutInfo == null ? null : layoutInfo.getLayoutConfig();
	}

	public Class<?> getPresetClass() {
		return layoutInfo == null ? null : layoutInfo.getPresetClass();
	}

	/**
	 * 
	 * @author diaimm
	 * @version $Rev$, $Date$
	 */
	static class LayoutInfo {
		/**
		 * layout framework definition name
		 */
		private final String layout;
		/**
		 * layout configuring method name (@LayoutConfigure annotated method)
		 */
		private final String method;
		private final LayoutConfig layoutConfig;
		private final Class<?> presetClass;

		public LayoutInfo(String definitionName, String method, Class<?> presetClass, LayoutConfig layoutConfig) {
			this.layout = definitionName;
			this.method = method;
			this.presetClass = presetClass;
			this.layoutConfig = layoutConfig;
		}

		/**
		 * @return the layoutConfig
		 */
		public LayoutConfig getLayoutConfig() {
			return layoutConfig;
		}

		public String getLayout() {
			return layout;
		}

		public String getMethod() {
			return method;
		}

		/**
		 * @return the presetClass
		 */
		public Class<?> getPresetClass() {
			return presetClass;
		}
	}
}
