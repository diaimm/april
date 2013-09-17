package com.coupang.member.component.web.taglib.paging;

import com.coupang.member.commons.util.FreeMarkerTemplateBuilder;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 9. 13
 * Time: 오후 9:21
 */
public class PagingTag extends SimpleTagSupport {
	public static final String SOURCE_TEMPLATE = "paging.ftl";
	private Logger logger = LoggerFactory.getLogger(PagingTag.class);
	private int totalItemCount;
	private PagingPolicy policy = PagingPolicy.FIXED_SCREEN;
	private String pagingId = "paging";
	private String onClickCallback = "onPageSelected";
	private String wrapperClass = "text-center nav-justified";
	private String ulClass = "pagination pagination-sm";
	private int pageSizePerScreen = 10;
	private int itemCountPerPage = 10;
	private int current = 1;
	private boolean usePrev = true;
	private boolean useNext = true;
	private boolean useFirst = false;
	private boolean useLast = false;

	@Override
	public void doTag() throws JspException, IOException {
		FreeMarkerTemplateBuilder builder = new FreeMarkerTemplateBuilder(this.getClass(), SOURCE_TEMPLATE);
		builder.attribute().set("pagingId", this.getPagingId());
		builder.attribute().set("wrapperClass", this.getWrapperClass());
		builder.attribute().set("onClickCallback", this.getOnClickCallback());
		builder.attribute().set("ulClass", this.getUlClass());
		builder.attribute().set("current", this.getCurrent());

		builder.attribute().set("usePrev", this.isUsePrev());
		builder.attribute().set("useNext", this.isUseNext());
		builder.attribute().set("useFirst", this.isUseFirst());
		builder.attribute().set("useLast", this.isUseLast());

		builder.attribute().set("screenFirstIndex", getPolicy().getScreenFirstIndex(this.getCurrent(), this.getPageSizePerScreen()));
		builder.attribute().set("prevIndex", getPolicy().getPrevIndex(this.getCurrent(), this.getPageSizePerScreen()));

		builder.attribute().set("screenLastIndex", getPolicy().getScreenLastIndex(this.getCurrent(), this.getPageSizePerScreen()));
		builder.attribute().set("nextIndex", getPolicy().getNextIndex(this.getCurrent(), this.getPageSizePerScreen(), this.getMaxPageIndex()));

		builder.attribute().set("firstIndex", 1);
		builder.attribute().set("lastIndex", this.getMaxPageIndex());

		writeBody(builder);
	}

	void writeBody(FreeMarkerTemplateBuilder builder) throws IOException {
		try {
			this.getJspContext().getOut().write(builder.build());
		} catch (TemplateException e) {
			logger.warn(e.getMessage(), e);
		}
	}

	public int getMaxPageIndex() {
		return (int)Math.ceil(getTotalItemCount() / getItemCountPerPage());
	}

	public PagingPolicy getPolicy() {
		return policy;
	}

	public void setPolicy(PagingPolicy policy) {
		this.policy = policy;
	}

	public String getWrapperClass() {
		return wrapperClass;
	}

	public void setWrapperClass(String wrapperClass) {
		this.wrapperClass = wrapperClass;
	}

	public String getUlClass() {
		return ulClass;
	}

	public void setUlClass(String ulClass) {
		this.ulClass = ulClass;
	}

	public int getPageSizePerScreen() {
		return pageSizePerScreen;
	}

	public void setPageSizePerScreen(int pageSizePerScreen) {
		this.pageSizePerScreen = pageSizePerScreen;
	}

	public String getOnClickCallback() {
		return onClickCallback;
	}

	public void setOnClickCallback(String onClickCallback) {
		this.onClickCallback = onClickCallback;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getTotalItemCount() {
		return totalItemCount;
	}

	public void setTotalItemCount(int totalItemCount) {
		this.totalItemCount = totalItemCount;
	}

	public int getItemCountPerPage() {
		return itemCountPerPage;
	}

	public void setItemCountPerPage(int itemCountPerPage) {
		this.itemCountPerPage = itemCountPerPage;
	}

	public boolean isUseNext() {
		return useNext;
	}

	public void setUseNext(boolean useNext) {
		this.useNext = useNext;
	}

	public boolean isUsePrev() {
		return usePrev;
	}

	public void setUsePrev(boolean usePrev) {
		this.usePrev = usePrev;
	}

	public boolean isUseLast() {
		return useLast;
	}

	public void setUseLast(boolean useLast) {
		this.useLast = useLast;
	}

	public boolean isUseFirst() {
		return useFirst;
	}

	public void setUseFirst(boolean useFirst) {
		this.useFirst = useFirst;
	}

	public String getPagingId() {
		return pagingId;
	}

	public void setPagingId(String pagingId) {
		this.pagingId = pagingId;
	}
}
