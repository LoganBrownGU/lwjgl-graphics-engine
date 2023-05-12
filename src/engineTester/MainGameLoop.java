package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
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

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay("test", 1280, 720, true, false);
        Loader loader = new Loader();


        RawModel model = OBJLoader.loadObjModel("assets/tree.obj", loader);
        ModelTexture modelTexture = new ModelTexture(loader.loadTexture("assets/tree.png"));
        modelTexture.setTransparent(true);
        TexturedModel staticModel = new TexturedModel(model, modelTexture);

        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 500; i++)
            entities.add(new Entity(staticModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 3));


        Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));

        Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("assets/grass.png")));
        Terrain terrain2 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("assets/grass.png")));

        Camera camera = new Camera(new Vector3f(0, 5, 0), new Vector3f(0, 0, 0), 70);
        MasterRenderer renderer = new MasterRenderer("assets/shaders", camera);
        renderer.disableFog();

        while (!Display.isCloseRequested()) {
            camera.move();

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            for (Entity entity : entities) {
                renderer.processEntity(entity);
            }
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}
