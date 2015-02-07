package eu.menzerath.imwd;

import eu.menzerath.imwd.checker.Checker;
import org.junit.Assert;
import org.junit.Test;

public class CheckerTest {
    private static final String GOOD_URL = "http://google.com";
    private static final String BAD_URL = "http://subdomain.not-used-domain-123-qwertz.tld";

    @Test
    public void testContentTest() {
        Checker checker = new Checker(0, GOOD_URL, 30, true, true, false, false);
        Assert.assertTrue(checker.testContent());
    }

    @Test
    public void testContentTest2() {
        Checker checker = new Checker(0, GOOD_URL, 30, true, true, false, false, null);
        Assert.assertTrue(checker.testContent());
    }

    @Test
    public void testContentTest3() {
        Checker checker = new Checker(0, BAD_URL, 30, true, true, false, false);
        Assert.assertFalse(checker.testContent());
    }

    @Test
    public void testContentTest4() {
        Checker checker = new Checker(0, BAD_URL, 30, true, true, false, false, null);
        Assert.assertFalse(checker.testContent());
    }

    @Test
    public void testPingTest() {
        Checker checker = new Checker(0, GOOD_URL, 30, true, true, false, false);
        Assert.assertTrue(checker.testPing());
    }

    @Test
    public void testPingTest2() {
        Checker checker = new Checker(0, GOOD_URL, 30, true, true, false, false, null);
        Assert.assertTrue(checker.testPing());
    }

    @Test
    public void testPingTest3() {
        Checker checker = new Checker(0, BAD_URL, 30, true, true, false, false);
        Assert.assertFalse(checker.testPing());
    }

    @Test
    public void testPingTest4() {
        Checker checker = new Checker(0, BAD_URL, 30, true, true, false, false, null);
        Assert.assertFalse(checker.testPing());
    }
}