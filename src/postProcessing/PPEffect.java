package postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class PPEffect {
    private final ImageRenderer imageRenderer;
    private final PPShader shader;


    public PPEffect(String vertex, String fragment) {
        shader = new PPShader(vertex, fragment);
        imageRenderer = new ImageRenderer();
    }

    public void render(int texture) {
        shader.start();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        imageRenderer.renderQuad();
        shader.stop();
    }

    public void cleanUp() {
        imageRenderer.cleanUp();
        shader.cleanUp();
    }
}
