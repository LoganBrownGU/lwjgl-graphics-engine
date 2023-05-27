package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;

import java.awt.*;

public class DisplayManager {

    public static int fpsLimit = 999;

    private static boolean vsyncEnabled = false;

    public static void createDisplay(String title, int width, int height, boolean fullscreen) {
        vsyncEnabled = true;
        init(title, width, height, fullscreen);
    }

    public static void createDisplay(String title, int width, int height, int fpsLimit, boolean fullscreen) {
        vsyncEnabled = false;
        DisplayManager.fpsLimit = Math.min(fpsLimit, DisplayManager.fpsLimit);

        init(title, width, height, fullscreen);
    }

    private static void init(String title, int width, int height, boolean fullscreen) {
        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            if (!fullscreen) Display.setDisplayMode(new DisplayMode(width, height));
            else {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                java.awt.DisplayMode defaultMode = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[1].getDisplayMode();
                int refreshRate = defaultMode.getRefreshRate();
                DisplayMode finalMode = null;

                // to future self: sorry
                loop: for (int i = 0; i < 2; i++) {
                    for (DisplayMode mode : modes) {
                        if (width == mode.getWidth() && height == mode.getHeight() && (refreshRate == mode.getFrequency() || mode.getFrequency() == 60 && i == 1)) {
                            finalMode = mode;
                            System.out.println(mode);
                            break loop;
                        }
                    }
                }

                if (finalMode == null) throw new RuntimeException("display with refresh rate or 60 not found :(");
                else Display.setDisplayModeAndFullscreen(finalMode);
            }

            Display.create(new PixelFormat().withSamples(4), attribs);
            Display.setTitle(title);
            Display.setVSyncEnabled(vsyncEnabled);

            GL11.glEnable(GL13.GL_MULTISAMPLE);

            Mouse.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public static void updateDisplay() {

        if (!vsyncEnabled) Display.sync(fpsLimit);

        Display.update();
    }

    public static void closeDisplay() {

        Display.destroy();

    }

}
