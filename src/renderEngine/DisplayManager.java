package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;

import java.awt.*;

public class DisplayManager {

    public static int FPS_CAP = 60;

    public static void createDisplay(String title, int width, int height, boolean fullscreen) {
        init(title, width, height, fullscreen);
    }

    public static void createDisplay(String title, int width, int height, boolean fullscreen, int fpsCap) {
        init(title, width, height, fullscreen);
        DisplayManager.FPS_CAP = fpsCap;
    }

    private static void init(String title, int width, int height, boolean fullscreen) {
        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            if (!fullscreen) Display.setDisplayMode(new DisplayMode(width, height));
            else {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                DisplayMode finalMode = null;
                for (DisplayMode mode : modes) {
                    if (width == mode.getWidth() && height == mode.getHeight() && mode.getFrequency() == 60) {
                        finalMode = mode;
                        System.out.println(mode);
                        break;
                    }
                }

                if (finalMode == null)
                    throw new RuntimeException("display setting with refresh rate of 60 not found :(");
                else Display.setDisplayModeAndFullscreen(finalMode);
            }

            Display.create(new PixelFormat().withSamples(4), attribs);
            Display.setTitle(title);
            Display.setVSyncEnabled(false);

            GL11.glEnable(GL13.GL_MULTISAMPLE);

            Mouse.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);

        Display.update();
    }

    public static void closeDisplay() {

        Display.destroy();

    }

}
