package gui;

import java.util.ArrayList;

public class GUIMaster {

    private static final ArrayList<GUIElement> elements = new ArrayList<>();
    private static final ArrayList<GUITexture> guis = new ArrayList<>();

    public static void addElement(GUIElement element) {
        elements.add(element);
        guis.add(element.getTexture());
    }

    public static void removeElement(GUIElement element) {
        elements.remove(element);
        guis.remove(element.getTexture());
    }

    public static void checkEvents() {
        for (GUIElement element : elements) {
            if (element instanceof Button && element.wasClicked()) ((Button) element).getEvent().onClick();
        }
    }

    public static void render(GUIRenderer renderer) {
        renderer.render(guis);
    }
}
