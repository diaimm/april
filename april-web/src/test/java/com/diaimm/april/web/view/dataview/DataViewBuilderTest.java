/*
 * @fileName : DataViewBuilderTest.java
 * @date : 2013. 5. 24.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.view.dataview;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.diaimm.april.web.view.dataview.DataType;
import com.diaimm.april.web.view.dataview.DataView;
import com.diaimm.april.web.view.dataview.DataViewEnvironmentAware;
import junit.framework.Assert;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author diaimm
 */
public class DataViewBuilderTest {
	@Test
	public void usePatter() {
		ModelAndView modelAndView = new ModelAndView();

		Assert.assertEquals(DataViewEnvironmentAware.PREFIX + "JSON", DataView.getBuilder(modelAndView).from(getSampleData()).to(DataType.JSON).build());
		System.out.println(modelAndView.getModelMap().get(DataViewEnvironmentAware.CONTEXT_KEY));
	}

	/**
	 * @return
	 */
	private RedirectQueries getSampleData() {
		RedirectQueries data = new RedirectQueries();
		for (int index = 0; index < 10; index++) {
			data.queries.add(new QueryInfo(index + "_query", index));
		}
		return data;
	}

	@XmlRootElement(name = "querieWrapper")
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RedirectQueries {
		@XmlElement(name = "queryList")
		List<QueryInfo> queries;

		public RedirectQueries() {
			this.queries = new ArrayList<QueryInfo>();
		}

		/**
		 * @return the queries
		 */
		public List<QueryInfo> getQueries() {
			return queries;
		}

		/**
		 * @param queries
		 *            the queries to set
		 */
		public void setQueries(List<QueryInfo> queries) {
			this.queries = queries;
		}
	}

	@XmlAccessorType(XmlAccessType.NONE)
	public static class QueryInfo {
		@XmlElement(name = "queryString")
		private String query;
		@XmlElement(name = "valueNumber")
		private int value;

		public QueryInfo() {
		}

		public QueryInfo(String query, int value) {
			this.query = query;
			this.value = value;
		}

		/**
		 * @return the query
		 */
		public String getQuery() {
			return query;
		}

		/**
		 * @param query
		 *            the query to set
		 */
		public void setQuery(String query) {
			this.query = query;
		}

		/**
		 * @return the value
		 */
		public int getValue() {
			return value;
		}

		/**
		 * @param value
		 *            the value to set
		 */
		public void setValue(int value) {
			this.value = value;
		}
	}
}
