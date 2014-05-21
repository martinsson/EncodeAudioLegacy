package encode.audio.entrypoint;


import org.approvaltests.Approvals;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class AudioAnnounceEngineTest {

	@Test
	public void checkJUnit()
	{
		Assert.fail("it failed");
	}

	@Test
	@Ignore
	// If the test cannot launch the default diff tool, try to set a
	// reporter with @UseReporter(...).
	// http://blog.approvaltests.com/2011/12/using-reporters-in-approval-tests.html
	public void checkApprovalTestsReporter() throws Exception {
		Approvals.verify("My diff reporter open a new window of my default diff tool. It works!");
	}
}
