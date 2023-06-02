package gui;

import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public abstract class GUIElement {
    private Vector3f backgroundColour;
    private Vector3f foregroundColour;
    private ActionEvent actionEvent;
    private Vector2f position;
    private Vector2f size;
    private GUITexture texture;
    protected GUIText text;

    private boolean clicking = false;

    public boolean wasClicked() {
        Vector2f mp = new Vector2f(((float) Mouse.getX() / Display.getWidth()) * 2  - 1, ((float) Mouse.getY() / Display.getHeight()) * 2 - 1);

        if (Mouse.isButtonDown(0) && !clicking && mp.x > position.x - size.x && mp.x < position.x + size.x && mp.y > position.y - size.y && mp.y < position.y + size.y) {
            clicking = true;
            return true;
        }

        if (!Mouse.isButtonDown(0)) clicking = false;

        return false;
    }

    public void add() {
        GUIMaster.addElement(this);
    }

    public void destroy() {
        GUIMaster.removeElement(this);
    }

    public GUIElement(Vector3f backgroundColour, Vector3f foregroundColour, Vector2f position, Vector2f size) {
        this.backgroundColour = backgroundColour;
        this.foregroundColour = foregroundColour;
        this.position = new Vector2f(position);
        this.position.x /= Display.getWidth();
        this.position.x *= 2;
        this.position.x -= 1;
        this.position.y /= Display.getHeight();
        this.position.y = 1 - this.position.y * 2;

        size.x /= Display.getWidth();
        size.y /= Display.getHeight();

        this.size = size;

        texture = new GUITexture(backgroundColour, this.position, size);
    }


    // position and scale are given in number of pixels from top left
    public GUIElement(Vector3f backgroundColour, Vector3f foregroundColour, Vector2f position, Vector2f size, String text, float border) {
        this(backgroundColour, foregroundColour, position, size);
        border /= Display.getWidth();

        this.text = new GUIText(text, 1, GUIMaster.font, new Vector2f(position.x / Display.getWidth() - .5f, position.y / Display.getHeight()), size.x - 2 * border, true);
        //this.text.setPosition(new Vector2f(this.text.getPosition().x / 2, this.text.getPosition().y));
        this.text.setColour(foregroundColour.x, foregroundColour.y, foregroundColour.z);
        TextMaster.loadText(this.text);
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

    public void setFontSize(float fontSize) {
        text.setFontSize(fontSize);
    }

    public void setFontColour(Vector3f colour) {
        text.setColour(colour.x, colour.y, colour.z);
    }

    public void setFontLineMaxSize(float size) {
        text.setLineMaxSize(size);
    }

    public void setCenterText(boolean centerText) {
        text.setCenterText(centerText);
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
        this.getTexture().setScale(size);
    }

    public void setText(GUIText text) {
        this.text = text;
    }

    public GUITexture getTexture() {
        return texture;
    }

    public GUIText getText() {
        return text;
    }
}
