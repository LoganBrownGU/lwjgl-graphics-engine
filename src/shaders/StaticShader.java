package shaders;

public class StaticShader extends ShaderProgram {

    public StaticShader() {
        super("default");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normal");
    }

}
