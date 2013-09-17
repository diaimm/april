package com.diaimm.april.web.taglib.paging;

import com.diaimm.april.commons.util.FreeMarkerTemplateBuilder;
import com.diaimm.april.web.taglib.paging.PagingPolicy;
import com.diaimm.april.web.taglib.paging.PagingTag;
import freemarker.template.TemplateException;
import junit.framework.Assert;
import org.junit.Test;

import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 9. 13
 * Time: 오후 9:57
 */
public class PagingTagTest {
	@Test
	public void PagingPolicy_DEFAULT_test() {
		for (int index = 1; index < 6; index++) {
			Assert.assertEquals(1, PagingPolicy.FIXED_SCREEN.getScreenFirstIndex(index, 5));
			Assert.assertEquals(5, PagingPolicy.FIXED_SCREEN.getScreenLastIndex(index, 5));
			Assert.assertEquals(null, PagingPolicy.FIXED_SCREEN.getPrevIndex(index, 5));
			Assert.assertEquals((Integer)6, PagingPolicy.FIXED_SCREEN.getNextIndex(index, 5, 1000));
		}

		for (int index = 11; index < 15; index++) {
			Assert.assertEquals(11, PagingPolicy.FIXED_SCREEN.getScreenFirstIndex(index, 10));
			Assert.assertEquals(20, PagingPolicy.FIXED_SCREEN.getScreenLastIndex(index, 10));
			Assert.assertEquals((Integer)10, PagingPolicy.FIXED_SCREEN.getPrevIndex(index, 10));
			Assert.assertEquals((Integer)21, PagingPolicy.FIXED_SCREEN.getNextIndex(index, 10, 1000));
		}

		Assert.assertEquals(11, PagingPolicy.FIXED_SCREEN.getScreenFirstIndex(11, 5));
		Assert.assertEquals(15, PagingPolicy.FIXED_SCREEN.getScreenLastIndex(11, 5));
		Assert.assertEquals((Integer)10, PagingPolicy.FIXED_SCREEN.getPrevIndex(11, 5));
		Assert.assertEquals((Integer)16, PagingPolicy.FIXED_SCREEN.getNextIndex(11, 5, 1000));
	}

	@Test
	public void PagingTagTest() throws IOException, JspException {
		final StringBuffer out = new StringBuffer();
		PagingTag target = new PagingTag() {
			@Override
			void writeBody(FreeMarkerTemplateBuilder builder) throws IOException {
				try {
					out.append(builder.build());
				} catch (TemplateException e) {
				}
			}
		};
		target.setTotalItemCount(12341);

		target.setCurrent(3);
		target.doTag();

		target.setCurrent(21);
		target.doTag();
		System.out.print(out.toString());
	}
}
