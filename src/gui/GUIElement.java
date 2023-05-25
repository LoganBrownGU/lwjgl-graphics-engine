package gui;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public abstract class GUIElement {
    private Vector3f backgroundColour;
    private Vector3f foregroundColour;
    private ActionEvent actionEvent;
    private Vector2f position;
    private GUITexture texture;

    public GUIElement(Vector3f backgroundColour, Vector3f foregroundColour, Vector2f position) {
        this.backgroundColour = backgroundColour;
        this.foregroundColour = foregroundColour;
        this.position = position;
    }

    public Vector3f getBackgroundColour() {
        return backgroundColour;
    }

    public void setBackgroundColour(Vector3f backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    public Vector3f getForegroundColour() {
        return foregroundColour;
    }

    public void setForegroundColour(Vector3f foregroundColour) {
        this.foregroundColour = foregroundColour;
    }

    public ActionEvent getActionEvent() {
        return actionEvent;
    }

    public void setActionEvent(ActionEvent actionEvent) {
        this.actionEvent = actionEvent;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }
}
