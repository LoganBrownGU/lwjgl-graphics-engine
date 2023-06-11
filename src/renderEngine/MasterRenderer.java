package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import org.lwjgl.util.vector.Vector3f;
import shaders.StaticShader;
import skybox.SkyboxRenderer;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MasterRenderer {
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Camera camera;

    private Matrix4f projectionMatrix;
    private StaticShader shader;
    private EntityRenderer renderer;
    private Vector3f skyColour = new Vector3f(.3f, .3f, .3f);
    private boolean fogEnabled = true;
    private SkyboxRenderer skyboxRenderer;


    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    public MasterRenderer(String shaderPath, String skyboxPath, Camera camera) {
        shader = new StaticShader(shaderPath);
        this.camera = camera;
        enableCulling();
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(new Loader(), projectionMatrix, skyboxPath);
    }

    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void render(ArrayList<Light> lights, Camera camera) {
        shader.start();
        prepare();
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        shader.loadSkyColour(skyColour);
        shader.loadFogEnabled(fogEnabled);
        shader.loadNumLights(lights.size());
        renderer.render(entities);
        shader.stop();
        skyboxRenderer.render(camera);
        entities.clear();
    }
    public void processEntity(Entity entity) {
        TexturedModel[] entityModels = entity.getModels();

        for (TexturedModel entityModel : entityModels) {
            List<Entity> batch = entities.get(entityModel);
            if (batch != null) {
                batch.add(entity);
            } else {
                List<Entity> newBatch = new ArrayList<>();
                newBatch.add(entity);
                entities.put(entityModel, newBatch);
            }
        }
    }

    public void cleanUp() {
        shader.cleanUp();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(skyColour.x, skyColour.y, skyColour.z, 1);
    }

    private void createProjectionMatrix(){
        projectionMatrix = new Matrix4f();
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(camera.getFov()) / 2f)));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void enableFog() { this.fogEnabled = true; }
    public void disableFog() { this.fogEnabled = false; }
}
