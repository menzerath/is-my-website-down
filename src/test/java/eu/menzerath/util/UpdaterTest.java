package eu.menzerath.util;

import eu.menzerath.imwd.Main;
import org.junit.Assert;
import org.junit.Test;

public class UpdaterTest {

    @Test
    public void testRefreshVersion() {
        Updater updater = new Updater();

        if (Main.VERSION.contains("SNAPSHOT")) {
            Assert.assertEquals("Error", updater.getServerVersion());
        } else {
            Assert.assertNotEquals("Error", updater.getServerVersion());
        }
    }

    @Test
    public void testRefreshChangelog() {
        Updater updater = new Updater();

        if (Main.VERSION.contains("SNAPSHOT")) {
            Assert.assertEquals("Error", updater.getServerChangelog());
        } else {
            Assert.assertNotEquals("Error", updater.getServerChangelog());
        }
    }
}
