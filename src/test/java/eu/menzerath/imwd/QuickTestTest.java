package eu.menzerath.imwd;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit-Tests for eu.menzerath.imwd.QuickTest
 */
public class QuickTestTest {
	private static final String GOOD_URL = "http://google.com";
	private static final String GOOD_URL_NO_PROTOCOL = "google.com";
	private static final String BAD_URL = "http://subdomain.not-used-domain-123-qwertz.tld";
	private static final String BAD_URL_NO_PROTOCOL = "subdomain.not-used-domain-123-qwertz.tld";

	@Test
	public void testGoodUrl() {
		Assert.assertTrue(new QuickTest(GOOD_URL).run().equalsIgnoreCase("OK"));
	}

	@Test
	public void testGoodUrl2() {
		Assert.assertTrue(new QuickTest(GOOD_URL_NO_PROTOCOL).run().equalsIgnoreCase("OK"));
	}

	@Test
	public void testBadUrl() {
		Assert.assertTrue(new QuickTest(BAD_URL).run().equalsIgnoreCase("Not Reachable"));
	}

	@Test
	public void testBadUrl2() {
		Assert.assertTrue(new QuickTest(BAD_URL_NO_PROTOCOL).run().equalsIgnoreCase("Not Reachable"));
	}
}