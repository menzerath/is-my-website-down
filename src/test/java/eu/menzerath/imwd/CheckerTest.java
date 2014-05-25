package eu.menzerath.imwd;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CheckerTest {
    private static final String GOOD_URL = "http://google.com";
    private static final String BAD_URL = "http://subdomain.not-used-domain-123-qwertz.tld";
    private Checker checker;

    @Before
    public void prepareChecker() {
        checker = new Checker(0, GOOD_URL, 30, true, true, false, false, false);
    }

    @Test
    public void testContentTest() {
        Assert.assertTrue(checker.testContent(GOOD_URL));
    }

    @Test
    public void testContentTest2() {
        Assert.assertFalse(checker.testContent(BAD_URL));
    }

    @Test
    public void testPingTest() {
        Assert.assertTrue(checker.testPing(GOOD_URL));
    }

    @Test
    public void testPingTest2() {
        Assert.assertFalse(checker.testPing(BAD_URL));
    }
}
