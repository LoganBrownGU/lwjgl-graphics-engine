package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import entities.Camera;
import entities.Light;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public abstract class ShaderProgram {

    /* todo rearrange shader loading so this constructor takes in a path to the shaders and each subclass of ShaderProgram
       adds its own suffix
    */

    public static final int MAX_LIGHTS = 28;

    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;
    protected int location_transformationMatrix;
    protected int location_projectionMatrix;
    protected int location_viewMatrix;
    protected int location_lightPositions[];
    protected int location_lightColours[];
    protected int location_attenuations[];
    protected int location_shineDamper;
    protected int location_reflectivity;
    protected int location_skyColour;
    protected int location_fogEnabled;
    protected int location_isEmissive;
    protected int location_numLights;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexFile, String fragmentFile) {
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
    }

    protected void getAllUniformLocations() {
        location_transformationMatrix = getUniformLocation("transformationMatrix");
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_shineDamper = getUniformLocation("shineDamper");
        location_reflectivity = getUniformLocation("reflectivity");
        location_reflectivity = getUniformLocation("reflectivity");
        location_skyColour = getUniformLocation("skyColour");
        location_fogEnabled = getUniformLocation("fogEnabled");
        location_numLights = getUniformLocation("num_lights");
        location_isEmissive = getUniformLocation("isEmissive");

        location_lightPositions = new int[MAX_LIGHTS];
        location_lightColours = new int[MAX_LIGHTS];
        location_attenuations = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++) {
            location_lightPositions[i] = getUniformLocation("lightPositions[" + i + "]");
            location_lightColours[i] = getUniformLocation("lightColours[" + i + "]");
            location_attenuations[i] = getUniformLocation("attenuations[" + i + "]");
        }
    }

    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    protected void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    protected void loadVector(int location, Vector3f vector) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadBoolean(int location, boolean value) {
        GL20.glUniform1f(location, value ? 1 : 0);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
        matrix.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(location, false, matrixBuffer);
    }

    protected void load2DVector(int location, Vector2f vector) {
        GL20.glUniform2f(location, vector.x, vector.y);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        loadMatrix(location_projectionMatrix, projection);
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader " + file);
            System.exit(-1);
        }
        return shaderID;
    }
    public void loadIsEmissive(boolean isEmissive) {
        loadBoolean(location_isEmissive, isEmissive);
    }

    public void loadShineVariables(float damper, float reflectivity) {
        loadFloat(location_shineDamper, damper);
        loadFloat(location_reflectivity, reflectivity);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadNumLights(int numLights) {
        this.loadFloat(location_numLights, (float) numLights);
    }

    public void loadFogEnabled(boolean fogEnabled) {
        this.loadBoolean(location_fogEnabled, fogEnabled);
    }

    public void loadSkyColour(Vector3f skyColour) {
        this.loadVector(location_skyColour, skyColour);
    }

    public void loadLights(List<Light> lights) {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            if (i < lights.size()) {
                this.loadVector(location_lightPositions[i], lights.get(i).getPosition());
                this.loadVector(location_lightColours[i], lights.get(i).getColour());
                this.loadVector(location_attenuations[i], lights.get(i).getAttenuation());
            } else {
                this.loadVector(location_lightPositions[i], new Vector3f(0,0,0));
                this.loadVector(location_lightColours[i], new Vector3f(0,0,0));
                this.loadVector(location_attenuations[i], new Vector3f(1,0,0));
            }
        }
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        this.loadMatrix(location_viewMatrix, viewMatrix);
    }

}
