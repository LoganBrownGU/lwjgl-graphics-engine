package gui;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class GUITexture {
    private final int texture;
    private final Vector3f colour;
    private Vector2f position;
    private Vector2f scale;

    public GUITexture(int texture, Vector2f position, Vector2f scale) {
        this.texture = texture;
        this.colour = null;
        this.position = position;
        this.scale = scale;
    }

    public GUITexture(Vector3f colour, Vector2f position, Vector2f scale) {
        this.texture = 0;
        this.colour = colour;
        this.position = position;
        this.scale = scale;
    }

    public int getTexture() {
        return texture;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    public Vector3f getColour() {
        return colour;
    }
}
