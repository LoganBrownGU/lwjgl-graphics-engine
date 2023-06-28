package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Terrain;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import org.lwjgl.util.vector.Vector3f;
import shaders.ShaderProgram;
import shaders.StaticShader;
import shaders.TerrainShader;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MasterRenderer {
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Camera camera;

    private Matrix4f projectionMatrix;
    private final StaticShader shader;
    private final EntityRenderer entityRenderer;
    private final Vector3f skyColour = new Vector3f(.8f, .8f, .9f);
    private boolean fogEnabled = true;
    private final SkyboxRenderer skyboxRenderer;
    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader;

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    public MasterRenderer(String skyboxPath, Camera camera) {
        this.shader = new StaticShader();
        this.terrainShader = new TerrainShader();

        this.camera = camera;
        enableCulling();
        createProjectionMatrix();

        this.entityRenderer = new EntityRenderer(shader, projectionMatrix);
        this.skyboxRenderer = new SkyboxRenderer(new Loader(), projectionMatrix, skyboxPath);
        this.terrainRenderer = new TerrainRenderer(this.terrainShader, this.projectionMatrix);
    }

    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void prepareShader(ShaderProgram shaderProgram, List<Light> lights, Camera camera) {
        shaderProgram.loadLights(lights);
        shaderProgram.loadViewMatrix(camera);
        shaderProgram.loadSkyColour(skyColour);
        shaderProgram.loadFogEnabled(fogEnabled);
        shaderProgram.loadNumLights(lights.size());
    }

    public void render(List<Terrain> terrains, ArrayList<Light> lights, Camera camera) {
        prepare();
        shader.start();
        prepareShader(shader, lights, camera);
        entityRenderer.render(entities);
        shader.stop();

        terrainShader.start();
        prepareShader(terrainShader, lights, camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();

        skyboxRenderer.render(camera);

        entities.clear();
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void cleanUp() {
        shader.cleanUp();
        terrainShader.cleanUp();
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