package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.*;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import gui.*;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.*;
import textures.ModelTexture;
import toolbox.Colours;
import toolbox.MousePicker;
import toolbox.Settings;

public class MainGameLoop {

    private static void pause() {
        GUIMaster.addFromFile("assets/gui_config/pause_menu.xml");

        ((Button) GUIMaster.getElementByID("return")).setEvent(element -> unPause());
        ((Button) GUIMaster.getElementByID("endgame")).setEvent(guiElement -> System.out.println("fuidgfudykfg"));

        ((Button) GUIMaster.getElementByID("endgame")).getEvent().onClick(null);
    }

    private static void unPause() {
        System.out.println("unpause");
    }

    public static void main(String[] args) {

        Settings.updateSettings("assets/settings.cfg");
        DisplayManager.createDisplay("test", 1280, 720, false);
        Loader loader = new Loader();
        TextMaster.init(loader, "assets/shaders/fontVertex.glsl", "assets/shaders/fontFragment.glsl");
        FontType font = new FontType(loader.loadTexture("assets/fonts/arial.png"), new File("assets/fonts/arial.fnt"));
        GUIText text = new GUIText("sodhf d  dsifh sdiuf",1, font, new Vector2f(0, 0), 1, true);
        TextMaster.loadText(text);

        TexturedModel staticModel = new TexturedModel(OBJLoader.loadObjModel("assets/cart.obj", loader), new ModelTexture(loader.loadTexture("assets/test_texture.png"), false));
        staticModel.getTexture().setTransparent(true);
        TexturedModel emissiveModel = new TexturedModel(OBJLoader.loadObjModel("assets/cube.obj", loader), new ModelTexture(loader.loadTexture("assets/test_texture.png"), true));

        List<Entity> entities = new ArrayList<>();
        OBJLoader.loadObjModel("assets/cube.obj", loader);
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Vector3f pos = new Vector3f(random.nextFloat() * 30 - 15, 0, random.nextFloat() * -30);
            Entity e = new Entity(staticModel, pos, new Vector3f(0, 90, 0), new Vector3f(1, random.nextFloat(5), 1), new SpherePicker(pos, 1));
            entities.add(e);
        }

        entities.add(new Entity(emissiveModel, new Vector3f(0, 0, -10), new Vector3f(), 1, null));

        ArrayList<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1), false));
        lights.add(new Light(new Vector3f(0, 0, 0), new Vector3f(0, 0, 1), true));

        Camera camera = new Camera(new Vector3f(5,2,5), new Vector3f(0, 0, 0), 70);
        MasterRenderer renderer = new MasterRenderer("assets/shaders", "assets/textures/skybox/paris_low_res", camera);
        renderer.disableFog();

        MousePicker mp = new MousePicker(renderer.getProjectionMatrix(), camera);

        ArrayList<GUITexture> guis = new ArrayList<>();
        float aspect = (float) Display.getWidth() / Display.getHeight();
        float guiSize = .03f;
        guis.add(new GUITexture(loader.loadTexture("assets/crosshair.png"), new Vector2f(0, 0), new Vector2f(guiSize, guiSize * aspect)));

        GUIRenderer guiRenderer = new GUIRenderer(loader, "assets/shaders/guiVertexShader.glsl");

        //FBO fbo = new FBO(Display.getWidth(), Display.getHeight(), FBO.DEPTH_RENDER_BUFFER);
        //String[] effects = {"none"};
        //PostProcessing.init(loader, "assets/shaders/post_processing", effects);

        GUIMaster.setFont(loader, "assets/fonts/arial");
        Button button = new Button(Colours.RED, Colours.WHITE, new Vector2f(.5f, .5f), new Vector2f(.5f, .05f), "fdgsd fsdhfis sdf", 10, "");
        button.setEvent((element) -> System.out.println(":)"));
        button.add();

        GUIMaster.addFromFile("assets/gui_config/main.xml");
        pause();

        ArrayList<Terrain> terrains = new ArrayList<>();
        terrains.add(new Terrain(0, 0, new ModelTexture(loader.loadTexture("assets/test_texture.png"), false), OBJLoader.loadObjModel("assets/map.obj", loader), 100));


        while (!Display.isCloseRequested()) {
            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            GUIMaster.checkEvents();
            camera.move(renderer.getProjectionMatrix());

            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            //fbo.bindFrameBuffer();

            if (Keyboard.isKeyDown(Keyboard.KEY_P)) button.destroy();

            if (Mouse.isButtonDown(0)) {
                for (Entity entity : entities) {
                    if (entity.getPicker() == null) continue;

                    if (entity.getPicker().isIntersecting(mp.getCurrentRay(), camera.getPosition()))
                        System.out.println(entity);
                }
            }

            for (Entity entity : entities)
                renderer.processEntity(entity);

            renderer.render(terrains, lights, camera);
            //fbo.unbindFrameBuffer();
            //PostProcessing.doPostProcessing(fbo.getColourTexture());
            GUIMaster.render(guiRenderer);
            TextMaster.render();
            guiRenderer.render(guis);

            DisplayManager.updateDisplay();
        }

        TextMaster.cleanUp();
        //PostProcessing.cleanUp();
        //fbo.cleanUp();
        renderer.cleanUp();
        guiRenderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}
