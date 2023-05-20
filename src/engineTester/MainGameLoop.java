package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.*;
import gui.GUIRenderer;
import gui.GUITexture;
import models.TexturedModel;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import postProcessing.FBO;
import postProcessing.PostProcessing;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;
import toolbox.Maths;
import toolbox.MousePicker;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay("test", 1280, 720, true, true);
        Loader loader = new Loader();

        TexturedModel staticModel = new TexturedModel(OBJLoader.loadObjModel("assets/pn.obj", loader), new ModelTexture(loader.loadTexture("assets/test_texture.png")));
        staticModel.getTexture().setTransparent(true);

        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Vector3f pos = new Vector3f(random.nextFloat() * 30 - 15, 0, random.nextFloat() * -30);
            Entity e = new Entity(staticModel, pos, 0, 0, 0, 1, new SpherePicker(pos, 1));
            entities.add(e);
        }

        staticModel = new TexturedModel(OBJLoader.loadObjModel("assets/plane.obj", loader), new ModelTexture(loader.loadTexture("assets/test_texture.png")));

        Vector3f pos = new Vector3f(5, 0, 5);
        Vector3f min = new Vector3f(pos.x - 1, pos.y, pos.z - 1);
        Vector3f max = new Vector3f(pos.x + 1, pos.y, pos.z + 1);
        entities.add(new Entity(staticModel, pos, 0, 0, 0, 1, new AABBPicker(min, max)));

        Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));

        Camera camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 70);
        MasterRenderer renderer = new MasterRenderer("assets/shaders", "assets/textures/skybox/paris", camera);
        renderer.disableFog();

        MousePicker mp = new MousePicker(renderer.getProjectionMatrix(), camera);

        ArrayList<GUITexture> guis = new ArrayList<>();
        float aspect = (float) Display.getWidth() / Display.getHeight();
        float guiSize = .03f;
        guis.add(new GUITexture(loader.loadTexture("assets/crosshair.png"), new Vector2f(0, 0), new Vector2f(guiSize, guiSize * aspect)));

        GUIRenderer guiRenderer = new GUIRenderer(loader);

        FBO fbo = new FBO(Display.getWidth(), Display.getHeight(), FBO.DEPTH_RENDER_BUFFER);
        String[] effects = {"contrast"};
        PostProcessing.init(loader, "assets/shaders/post_processing", effects);

        while (!Display.isCloseRequested()) {
            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            if (Mouse.next() && !Mouse.getEventButtonState() && Mouse.getEventButton() == 0) System.out.println("dsfjk");
            camera.move(renderer.getProjectionMatrix(), Maths.createViewMatrix(camera));

            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            fbo.bindFrameBuffer();

            if (Mouse.isButtonDown(0)) {
                for (Entity entity : entities) {
                    if (entity.getPicker().isIntersecting(mp.getCurrentRay(), camera.getPosition()))
                        System.out.println(entity);
                }
            }

            for (Entity entity : entities)
                renderer.processEntity(entity);

            renderer.render(light, camera);
            fbo.unbindFrameBuffer();

            PostProcessing.doPostProcessing(fbo.getColourTexture());

            guiRenderer.render(guis);
            DisplayManager.updateDisplay();
        }

        PostProcessing.cleanUp();
        fbo.cleanUp();
        renderer.cleanUp();
        guiRenderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}
