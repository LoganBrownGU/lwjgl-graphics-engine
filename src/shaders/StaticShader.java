package shaders;

import org.lwjgl.util.vector.Matrix4f;

import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

import entities.Camera;
import entities.Light;

import java.util.ArrayList;

public class StaticShader extends ShaderProgram {

    private int location_num_lights;

    public StaticShader(String shaderPath) {
        super(shaderPath + "/vertexShader.glsl", shaderPath + "/fragmentShader.glsl");
    }

    @Override
    protected void getAllUniformLocations() {
        super.getAllUniformLocations();
        location_num_lights = getUniformLocation("num_lights");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normal");
    }

    public void loadNumLights(int numLights) {
        super.loadFloat(location_num_lights, (float) numLights);
    }

    public void loadFogEnabled(boolean fogEnabled) {
        super.loadBoolean(location_fogEnabled, fogEnabled);
    }

    public void loadSkyColour(Vector3f skyColour) {
        super.loadVector(location_skyColour, skyColour);
    }

    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadLights(ArrayList<Light> lights) {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            if (i < lights.size()) {
                super.loadVector(location_lightPositions[i], lights.get(i).getPosition());
                super.loadVector(location_lightColours[i], lights.get(i).getColour());
                super.loadVector(location_attenuations[i], lights.get(i).getAttenuation());
            } else {
                super.loadVector(location_lightPositions[i], new Vector3f(0,0,0));
                super.loadVector(location_lightColours[i], new Vector3f(0,0,0));
                super.loadVector(location_attenuations[i], new Vector3f(1,0,0));
            }
        }
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix(location_projectionMatrix, projection);
    }


}
