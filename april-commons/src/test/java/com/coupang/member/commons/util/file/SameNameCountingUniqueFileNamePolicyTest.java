package com.coupang.member.commons.util.file;

import com.coupang.member.commons.util.file.FileUtils;
import com.coupang.member.commons.util.file.SameNameCountingUniqueFileNamePolicyFactory.SameNameCountingUniqueFileNamePolicy;

import junit.framework.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 8. 26
 * Time: 오후 1:07
 */
public class SameNameCountingUniqueFileNamePolicyTest {
	@Test
	public void createFileNameTest() throws IOException {
		SameNameCountingUniqueFileNamePolicy target = new SameNameCountingUniqueFileNamePolicy(5);
		File testFile = new File("./SameNameCountingUniqueFileNamePolicyTest.java");
		String fileName = target.createFileName(testFile);

		Assert.assertEquals("./SameNameCountingUniqueFileNamePolicyTest(0).java", fileName);


		SameNameCountingUniqueFileNamePolicy target2 = new SameNameCountingUniqueFileNamePolicy(5);
		for (int index = 0; index < 10; index++) {
			Assert.assertEquals("./sample/sample(" + target2.getRetryCount() + ").test", target2.createFileName(new File("./sample/sample.test")));
		}
	}

	@Test
	public void getNextFileTest() throws IOException {
		File targetDirectory = new File("./sample/");
		FileUtils.deleteAnyway(targetDirectory);

		SameNameCountingUniqueFileNamePolicy target = new SameNameCountingUniqueFileNamePolicy(5);
		Assert.assertEquals("sample.test", createNewFile(target));
		Assert.assertEquals("sample(0).test", createNewFile(target));
		Assert.assertEquals("sample(1).test", createNewFile(target));
		Assert.assertEquals("sample(2).test", createNewFile(target));
		Assert.assertEquals("sample(3).test", createNewFile(target));
		Assert.assertEquals("sample(4).test", createNewFile(target));
		try {
			Assert.assertEquals("sample(5).test", createNewFile(target));
		} catch (IllegalStateException e) {
			Assert.assertEquals("./sample/sample.test 파일 수가 최대 수(5)를 초과했습니다", e.getMessage());
		}

		FileUtils.deleteAnyway(targetDirectory);
	}

	private String createNewFile(SameNameCountingUniqueFileNamePolicy target) throws IOException {
		File newFileName = target.getNextFile(new File("./sample/sample.test"));
		newFileName.getParentFile().mkdirs();
		newFileName.createNewFile();
		return newFileName.getName();
	}
}
