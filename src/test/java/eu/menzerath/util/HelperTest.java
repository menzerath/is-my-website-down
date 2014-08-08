package eu.menzerath.util;

import eu.menzerath.imwd.Main;
import org.junit.Assert;
import org.junit.Test;

public class HelperTest {

    @Test
    public void testWebConnectionTest() {
        Assert.assertTrue(Helper.testWebConnection());
    }

    @Test
    public void testUrlValidator() {
        Assert.assertTrue(Helper.validateUrlInput("http://google.com"));
    }

    @Test
    public void testUrlValidator2() {
        Assert.assertTrue(Helper.validateUrlInput("https://google.com"));
    }

    @Test
    public void testUrlValidator3() {
        Assert.assertFalse(Helper.validateUrlInput("google.com"));
    }

    @Test
    public void testUrlValidator4() {
        Assert.assertFalse(Helper.validateUrlInput("ftp://google.com"));
    }

    @Test
    public void testUrlValidator5() {
        Assert.assertFalse(Helper.validateUrlInput("sftp://google.com"));
    }

    @Test
    public void testIntervalValidator() {
        Assert.assertTrue(Helper.validateIntervalInput(Main.MIN_INTERVAL));
    }

    @Test
    public void testIntervalValidator2() {
        Assert.assertTrue(Helper.validateIntervalInput(Main.MAX_INTERVAL));
    }

    @Test
    public void testIntervalValidator3() {
        Assert.assertFalse(Helper.validateIntervalInput(0));
    }

    @Test
    public void testIntervalValidator4() {
        Assert.assertFalse(Helper.validateIntervalInput(1));
    }

    @Test
    public void testIntervalValidator5() {
        Assert.assertFalse(Helper.validateIntervalInput(1000));
    }

    @Test
    public void testIntegerParser() {
        Assert.assertEquals(0, Helper.parseInt("0"));
    }

    @Test
    public void testIntegerParser2() {
        Assert.assertEquals(100, Helper.parseInt("100"));
    }

    @Test
    public void testIntegerParser3() {
        Assert.assertEquals(2147483647, Helper.parseInt("2147483647"));
    }

    @Test
    public void testIntegerParser4() {
        Assert.assertEquals(0, Helper.parseInt("FISH"));
    }
}