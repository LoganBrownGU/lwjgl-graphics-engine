package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gui.GUIRenderer;
import gui.GUITexture;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import entities.Camera;
import entities.Entity;
import entities.Light;
import toolbox.MousePicker;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay("test", 1280, 720, true, false);
        Loader loader = new Loader();

        TexturedModel staticModel = new TexturedModel(OBJLoader.loadObjModel("assets/pn.obj", loader), new ModelTexture(loader.loadTexture("assets/test_texture.png")));
        staticModel.getTexture().setTransparent(true);

        TexturedModel sphere = new TexturedModel(OBJLoader.loadObjModel("assets/sphere.obj", loader), new ModelTexture(loader.loadTexture("assets/test_texture.png")));

        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Entity e = new Entity(staticModel, new Vector3f(random.nextFloat() * 30 - 15, 0, random.nextFloat() * -30), 0, 0, 0, 1, 1);
            entities.add(e);
            Vector3f pos = new Vector3f(e.getPosition());
            pos.y += 1;
            entities.add(new Entity(sphere, pos, 0, 0, 0, e.hitRadius, 0));
        }

        staticModel = new TexturedModel(OBJLoader.loadObjModel("assets/plane.obj", loader), new ModelTexture(loader.loadTexture("assets/test_texture.png")));
        entities.add(new Entity(staticModel, new Vector3f(0, 0, 0), 0, 0, 0, 1, 1));

        Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));

        Camera camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 70);
        MasterRenderer renderer = new MasterRenderer("assets/shaders", camera);
        renderer.disableFog();

        MousePicker mp = new MousePicker(renderer.getProjectionMatrix(), camera);

        ArrayList<GUITexture> guis = new ArrayList<>();
        float aspect = (float) Display.getWidth() / Display.getHeight();
        float guiSize = .03f;
        guis.add(new GUITexture(loader.loadTexture("assets/crosshair.png"), new Vector2f(0, 0), new Vector2f(guiSize, guiSize * aspect)));

        GUIRenderer guiRenderer = new GUIRenderer(loader);

        Vector3f[] vertices = {
                new Vector3f(-1, -1, -1),
                new Vector3f(1, 1, 1),
        };

        while (!Display.isCloseRequested()) {
            camera.move(mp);
            mp.update();

            if (Mouse.isButtonDown(0)) {
                for (Entity entity : entities) {
                    if (mp.isIntersecting(entity.getPosition(), entity.hitRadius))
                        System.out.println(entity);
                }

                System.out.println(mp.isIntersectingPlane(null, vertices));
            }

            for (Entity entity : entities)
                renderer.processEntity(entity);

            renderer.render(light, camera);
            guiRenderer.render(guis);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        guiRenderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}
