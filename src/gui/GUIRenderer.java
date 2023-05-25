package gui;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import toolbox.Maths;

import java.util.Collection;
import java.util.List;

public class GUIRenderer {

    private final RawModel quad;
    private final GUIShader textureShader;
    private final GUIShader solidShader;

    public void render(Collection<GUITexture> guis) {

        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        for (GUITexture gui : guis) {
            if (gui == null) continue;

            if (gui.getColour() == null) renderGUI(gui, textureShader);
            else renderGUI(gui, solidShader);
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    private void renderGUI(GUITexture gui, GUIShader shader) {
        shader.start();

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
        Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
        shader.loadTransformation(matrix);
        shader.loadColour(gui.getColour());
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());

        shader.stop();
    }

    public void cleanUp() {
        textureShader.cleanUp();
        solidShader.cleanUp();
    }

    public GUIRenderer(Loader loader, String vertex) {
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        quad = loader.loadToVAO(positions, 2);

        String fragment = vertex.substring(0, vertex.indexOf("/gui"));
        textureShader = new GUIShader(vertex, fragment + "/guiFragmentShader.glsl");
        solidShader = new GUIShader(vertex, fragment + "/solidGUIFragment.glsl");
    }
}
