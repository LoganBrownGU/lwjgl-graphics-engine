package postProcessing;

import shaders.ShaderProgram;

public class PPShader extends ShaderProgram {

    public PPShader(String vertexPath, String fragmentPath) {
        super(vertexPath, fragmentPath);
    }

    @Override
    protected void getAllUniformLocations() {
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}

