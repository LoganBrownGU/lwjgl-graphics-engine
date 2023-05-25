package gui;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Button extends GUIElement {

    private ActionEvent event;

    public Button(Vector3f backgroundColour, Vector3f foregroundColour, Vector2f position, Vector2f size, String text, float border) {
        super(backgroundColour, foregroundColour, position, size, text, border);
    }

    public void setEvent(ActionEvent event) {
        this.event = event;
    }

    public ActionEvent getEvent() {
        return event;
    }
}
