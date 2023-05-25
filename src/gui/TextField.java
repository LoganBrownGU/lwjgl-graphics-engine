package gui;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class TextField extends GUIElement {

    public TextField(Vector3f backgroundColour, Vector3f foregroundColour) {
        super(backgroundColour, foregroundColour, new Vector2f(0, 0));
    }
}
