package fontRendering;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMeshData;
import renderEngine.Loader;

import java.util.ArrayList;
import java.util.HashMap;

public class TextMaster {

    private static Loader loader;
    private static HashMap<FontType, ArrayList<GUIText>> texts = new HashMap<>();
    private static FontRenderer renderer;

    public static void init(Loader loader) {
        renderer = new FontRenderer();
        TextMaster.loader = loader;
    }

    public static void loadText(GUIText text) {
        FontType font = text.getFont();
        TextMeshData data = font.loadText(text);

        int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
        text.setMeshInfo(vao, data.getVertexCount());
        ArrayList<GUIText> textBatch = texts.computeIfAbsent(font, k -> new ArrayList<>());

        textBatch.add(text);
    }

    public static void removeText(GUIText text) {
        ArrayList<GUIText> textBatch = texts.get(text.getFont());
        textBatch.remove(text);

        if (textBatch.isEmpty()) texts.remove(text.getFont());
    }

    public static void cleanUp() {
        renderer.cleanUp();
    }
}
