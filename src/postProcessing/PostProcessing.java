package postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;
import renderEngine.Loader;

import java.util.ArrayList;

public class PostProcessing {

    private static final float[] POSITIONS = {-1, 1, -1, -1, 1, 1, 1, -1};
    private static RawModel quad;
    private static final ArrayList<PPEffect> effects = new ArrayList<>();

    public static void init(Loader loader, String shaderPath, String[] effectNames) {
        quad = loader.loadToVAO(POSITIONS, 2);

        for (String effectName : effectNames) {
            effectName = shaderPath + "/" + effectName;
            effects.add(new PPEffect(effectName + "Vertex.glsl", effectName + "Fragment.glsl"));
        }
    }

    public static void doPostProcessing(int colourTexture) {
        start();
        for (PPEffect effect : effects)
            effect.render(colourTexture);
        end();
    }

    public static void cleanUp() {
        for (PPEffect effect : effects)
            effect.cleanUp();
    }

    private static void start() {
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    private static void end() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }


}
