package shaders;

public class TerrainShader extends ShaderProgram {

    @Override
    protected void bindAttributes() {

    }

    public TerrainShader(String vertexFile, String fragmentFile) {
        super("assets/shaders/terrainVertex.glsl", "assets/shaders/terrainFragment.glsl");
    }
}
