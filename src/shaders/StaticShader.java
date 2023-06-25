package shaders;

public class StaticShader extends ShaderProgram {

    public StaticShader(String shaderPath) {
        super(shaderPath + "/vertexShader.glsl", shaderPath + "/fragmentShader.glsl");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normal");
    }

}
