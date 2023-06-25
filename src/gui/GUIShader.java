package gui;

import org.lwjgl.util.vector.Matrix4f;

import org.lwjgl.util.vector.Vector3f;
import shaders.ShaderProgram;

public class GUIShader extends ShaderProgram {

    private int location_transformationMatrix;
    private int location_colour;

    public GUIShader() {
        super("gui");
    }

    public GUIShader(String fragmentName) {
        super("gui", fragmentName);
    }

    public void loadTransformation(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadColour(Vector3f colour) {
        if (colour != null) super.loadVector(location_colour, colour);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_colour = super.getUniformLocation("colour");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}