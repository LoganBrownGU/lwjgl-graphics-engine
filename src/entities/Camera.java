package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class Camera {

    protected Vector3f position;
    protected Vector3f rotation;
    protected final float fov;

    public void move(Matrix4f projectionMatrix) {
        Matrix4f viewMatrix = Maths.createViewMatrix(this);

        Vector3f direction = Maths.screenCoordsToRay(new Vector2f((float) Display.getWidth() /2, (float) Display.getHeight() /2), projectionMatrix, viewMatrix);
        if (Keyboard.isKeyDown(Keyboard.KEY_W));
        else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            Vector3f w = new Vector3f(-direction.z, 0, direction.x);
            direction = (Vector3f) w.scale(1f / direction.length());
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            Vector3f w = new Vector3f(direction.z, 0, -direction.x);
            direction = (Vector3f) w.scale(1f / direction.length());
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            direction = (Vector3f) direction.negate();
        } else direction = null;

        if (direction != null) {
            direction.scale(0.5f);
            Vector3f.add(position, direction, position);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            position.y += 0.2f;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            position.y -= 0.2f;


        this.rotation.y += (float) Mouse.getDX() / 10;
        this.rotation.x -= (float) Mouse.getDY() / 10;

        if (this.rotation.x > 90) this.rotation.x = 90;
        if (this.rotation.x < -90) this.rotation.x = -90;
    }

    public Camera(Vector3f position, Vector3f rotation, float fov) {
        this.position = position;
        this.rotation = rotation;
        this.fov = fov;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public float getFov() {
        return fov;
    }
}
