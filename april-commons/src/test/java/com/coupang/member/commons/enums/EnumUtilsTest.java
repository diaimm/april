/*
 * @fileName : EnumUtilsTest.java
 * @date : 2013. 6. 18.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.enums;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.Ordered;

import com.coupang.member.commons.util.EnumUtils;

/**
 * @author diaimm
 * 
 */
public class EnumUtilsTest {
	@Test
	public void valueOfTest() {

		Assert.assertNull(EnumUtils.valueOf(SampleEnum.class, "value4"));
		Assert.assertEquals(SampleEnum.ValuE1, EnumUtils.valueOf(SampleEnum.class, "value4", SampleEnum.ValuE1));
		Assert.assertEquals(SampleEnum.ValuE1, EnumUtils.valueOf(SampleEnum.class, "value1"));
	}

	@Test
	public void valuesTest() {
		Assert.assertEquals(3, EnumUtils.values(SampleEnum.class).size());
		Assert.assertEquals(3, EnumUtils.sorted(SampleEnum.class).size());
		Assert.assertEquals(3, EnumUtils.map(SampleEnum.class).size());
		Assert.assertEquals(SampleEnum.VALUe2, EnumUtils.sorted(SampleEnum.class).get(0));
		Assert.assertNotNull(EnumUtils.iterator(SampleEnum.class));
	}

	private static enum SampleEnum implements Ordered {
		ValuE1(2), VALUe2(1), VAlue3(3);

		private final int order;

		SampleEnum(int order) {
			this.order = order;
		}

		/**
		 * @see org.springframework.core.Ordered#getOrder()
		 */
		@Override
		public int getOrder() {
			return order;
		}
	}
}
