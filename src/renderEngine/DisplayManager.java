package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

public class DisplayManager {

    public static final int FPS_CAP = 120;

    private static boolean vsyncEnabled = false;

    public static void createDisplay(String title, int width, int height, boolean vsync, boolean fullscreen) {
        vsyncEnabled = vsync;
        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            if (!fullscreen) Display.setDisplayMode(new DisplayMode(width, height));
            else Display.setFullscreen(true);

            Display.create(new PixelFormat().withSamples(4), attribs);
            Display.setTitle(title);
            Display.setVSyncEnabled(vsync);
            GL11.glEnable(GL13.GL_MULTISAMPLE);

            Mouse.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public static void updateDisplay() {

        if (!vsyncEnabled) Display.sync(FPS_CAP);

        Display.update();
    }

    public static void closeDisplay() {

        Display.destroy();

    }

}
