package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import toolbox.MousePicker;

public class Camera {

    private Vector3f position;
    private Vector3f rotation;
    private final float fov;

    public void move(MousePicker mp) {

        Vector3f direction = null;
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
            //direction = new Vector3f((float) Math.sin(Math.toRadians(rotation.y)), (float) -Math.sin(Math.toRadians(rotation.x)), (float) -Math.cos(Math.toRadians(rotation.y)));
            direction = mp.getCurrentRay();

        if (Keyboard.isKeyDown(Keyboard.KEY_D))
            direction = new Vector3f((float) Math.sin(Math.toRadians(rotation.y + 90)), 0, (float) -Math.cos(Math.toRadians(rotation.y + 90)));

        if (Keyboard.isKeyDown(Keyboard.KEY_A))
            direction = new Vector3f((float) Math.sin(Math.toRadians(rotation.y - 90)), 0, (float) -Math.cos(Math.toRadians(rotation.y - 90)));

        if (Keyboard.isKeyDown(Keyboard.KEY_S))
            direction = new Vector3f((float) -Math.sin(Math.toRadians(rotation.y)), (float) Math.sin(Math.toRadians(rotation.x)), (float) Math.cos(Math.toRadians(rotation.y)));

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            position.y += 0.2f;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            position.y -= 0.2f;


        if (direction != null) {
            direction.scale(0.2f);
            Vector3f.add(position, direction, position);
        }

        if (Mouse.isButtonDown(2) || Mouse.isButtonDown(1)) {
            this.rotation.y += (float) Mouse.getDX() / 10;
            this.rotation.x -= (float) Mouse.getDY() / 10;
        }
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
