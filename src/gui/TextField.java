package gui;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class TextField extends GUIElement {

    public TextField(Vector3f backgroundColour, Vector3f foregroundColour, Vector2f position, Vector2f size, String text, float border, String id) {
        super(backgroundColour, foregroundColour, position, size, text, border, id);
    }

    public TextField(Vector3f backgroundColour, Vector3f foregroundColour, Vector2f position, Vector2f size, String text, float border) {
        super(backgroundColour, foregroundColour, position, size, text, border, "");
    }
}
