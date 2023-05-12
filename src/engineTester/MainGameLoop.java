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

        TexturedModel staticModel = new TexturedModel(OBJLoader.loadObjModel("assets/tree.obj", loader), new ModelTexture(loader.loadTexture("assets/tree.png")));
        staticModel.getTexture().setTransparent(true);

        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++)
            entities.add(new Entity(staticModel, new Vector3f(random.nextFloat() * 80 - 40, 0, random.nextFloat() * -60), 0, 0, 0, 3, 2));

        Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));

        Camera camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 70);
        MasterRenderer renderer = new MasterRenderer("assets/shaders", camera);
        renderer.disableFog();

        MousePicker mp = new MousePicker(renderer.getProjectionMatrix(), camera);

        ArrayList<GUITexture> guis = new ArrayList<>();
        guis.add(new GUITexture(loader.loadTexture("assets/grass.png"), new Vector2f(0, 0), new Vector2f(.01f, .01f)));

        GUIRenderer guiRenderer = new GUIRenderer(loader);

        while (!Display.isCloseRequested()) {
            camera.move();
            mp.update();

            if (Mouse.isButtonDown(0)) {
                /*Vector3f ray = mp.getCurrentRay();
                Vector3f newPos = new Vector3f();
                Vector3f direction = new Vector3f((float) Math.sin(Math.toRadians(camera.getRotation().y)), (float) -Math.sin(Math.toRadians(camera.getRotation().x)), (float) -Math.cos(Math.toRadians(camera.getRotation().y)));
                int scale = 100;
                newPos.x = camera.getPosition().x + direction.x * scale;
                newPos.y = camera.getPosition().y + direction.y * scale;
                newPos.z = camera.getPosition().z + direction.z * scale;

                System.out.println(direction + " " + newPos);
                entities.add(new Entity(staticModel, newPos, 0, 0, 0, 3, 1));*/

                for (Entity entity : entities) {
                    if (mp.isIntersecting(entity.getPosition(), entity.hitRadius))
                        System.out.println(entity);
                }
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
