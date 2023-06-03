package gui;

import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Text;

public abstract class GUIElement {
    private Vector3f backgroundColour;
    private Vector3f foregroundColour;
    private Vector2f position;
    private Vector2f size;
    private final GUITexture texture;
    private final String id;
    private String group = "";

    protected GUIText text;

    private boolean clicking = false;

    public boolean wasClicked() {
        Vector2f mp = new Vector2f((float) Mouse.getX() / Display.getWidth(), 1 - (float) Mouse.getY() / Display.getHeight());

        if (Mouse.isButtonDown(0) && !clicking && mp.x > position.x - size.x / 2 && mp.x < position.x + size.x / 2 && mp.y > position.y - size.y / 2 && mp.y < position.y + size.y / 2) {
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

    public GUIElement(Vector3f backgroundColour, Vector3f foregroundColour, Vector2f position, Vector2f size, String id) {
        this.backgroundColour = backgroundColour;
        this.foregroundColour = foregroundColour;
        this.position = position;
        this.size = size;
        this.id = id;

        texture = new GUITexture(backgroundColour, this.position, this.size);
    }


    // position and scale are given from 0 to 1 with 0, 0 being top left and 1, 1 being bottom right
    public GUIElement(Vector3f backgroundColour, Vector3f foregroundColour, Vector2f position, Vector2f size, String text, float border, String id) {
        this(backgroundColour, foregroundColour, position, size, id);

        this.text = new GUIText(text, 1, GUIMaster.font, position, size.x - 2 * (border / Display.getWidth()), true);
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
        TextMaster.removeText(this.text);
        this.text = text;
        TextMaster.loadText(this.text);
    }

    public void setText(String text) {
        TextMaster.removeText(this.text);
        this.text.setTextString(text);
        TextMaster.loadText(this.text);

        // todo update the size of the textfield if it is set to wrap-content
    }

    public GUITexture getTexture() {
        return texture;
    }

    public GUIText getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
