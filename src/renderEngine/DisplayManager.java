package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	public static final int FPS_CAP = 120;

	private static boolean vsyncEnabled = false;
	
	public static void createDisplay(String title, int width, int height, boolean vsync, boolean fullscreen){
		vsyncEnabled = vsync;
		ContextAttribs attribs = new ContextAttribs(3,2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try {
			if (!fullscreen) Display.setDisplayMode(new DisplayMode(width, height));
			else Display.setFullscreen(true);

			Display.create(new PixelFormat(), attribs);
			Display.setTitle(title);
			Display.setVSyncEnabled(vsync);
			Mouse.setGrabbed(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0,0, Display.getWidth(), Display.getHeight());
	}
	
	public static void updateDisplay(){
		
		if (!vsyncEnabled) Display.sync(FPS_CAP);

		Display.update();

		if (Display.isCloseRequested())
			Display.destroy();
	}
	
	public static void closeDisplay(){
		
		Display.destroy();
		
	}

}
