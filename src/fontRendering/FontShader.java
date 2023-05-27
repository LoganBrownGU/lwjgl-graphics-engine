package fontRendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import shaders.ShaderProgram;

public class FontShader extends ShaderProgram {

    private int location_colour;
    private int location_translation;

    public FontShader(String vertex, String fragment) {
        super(vertex, fragment);
    }

    @Override
    protected void getAllUniformLocations() {
        location_colour = super.getUniformLocation("colour");
        location_translation = super.getUniformLocation("translation");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }


    protected void loadColour(Vector3f colour) {
        super.loadVector(location_colour, colour);
    }

    protected void loadTranslation(Vector2f translation) {
        super.load2DVector(location_translation, translation);
    }

}