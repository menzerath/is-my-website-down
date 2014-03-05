package eu.menzerath.imwd;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class GuiApplicationTest {

    @Test
    public void testCreateGui() {
        if (GraphicsEnvironment.isHeadless()) {
            Assert.fail("isHeadless() ==> true");
        } else {
            GuiApplication.startGUI();
        }
    }
}
