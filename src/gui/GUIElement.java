package gui;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;

import java.io.File;

public abstract class GUIElement {
    private Vector3f backgroundColour;
    private Vector3f foregroundColour;
    private ActionEvent actionEvent;
    private Vector2f position;
    private Vector2f size;
    private GUITexture texture;
    protected GUIText text;

    protected static FontType font = null;

    private boolean clicking = false;

    public static void setFont(Loader loader, String font) {
        GUIElement.font = new FontType(loader.loadTexture(font + ".png"), new File(font + ".fnt"));
    }

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
        this.position = position;

        System.out.println(size.x / Display.getWidth());
        size.x = size.x / Display.getWidth();
        size.y = size.y / Display.getHeight();
        System.out.println(size);

        this.size = size;
        this.text = new GUIText("", 1, font, position, 1, true);
        this.text.setColour(foregroundColour.x, foregroundColour.y, foregroundColour.z);
        TextMaster.loadText(text);

        texture = new GUITexture(backgroundColour, position, size);
    }

    public GUIElement(Vector3f backgroundColour, Vector3f foregroundColour, Vector2f position, Vector2f size, String text, float border) {
        this(backgroundColour, foregroundColour, position, size);
        this.text = new GUIText(text, 1, font, new Vector2f(position.x + border, position.y + border), size.x - 2 * border, true);
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
    }

    public void setText(GUIText text) {
        this.text = text;
    }

    public GUITexture getTexture() {
        return texture;
    }
}
