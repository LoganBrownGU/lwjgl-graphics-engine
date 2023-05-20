package postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class ContrastChanger {
    private ImageRenderer imageRenderer;
    private ContrastShader shader;


    public ContrastChanger() {
        shader = new ContrastShader();
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
