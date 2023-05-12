package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position;
    private Vector3f rotation;
    private float fov;

    public void move() {
        /*Vector3f direction = new Vector3f(rotation);
        System.out.print("rotation: " + rotation);

        if (this.rotation.x == 0 && this.rotation.y == 0 && this.rotation.z == 0)
            direction = new Vector3f(0, 0, -1);
        else
            direction.normalise();
        System.out.println(" direction: " + direction);*/
        System.out.println(rotation);

        Vector3f direction = null;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            direction = new Vector3f((float) Math.sin(Math.toRadians(rotation.y)), (float) Math.sin(Math.toRadians(rotation.x)), (float) Math.cos(Math.toRadians(rotation.y)));
            this.position.x += Math.sin(Math.toRadians(rotation.y));
            this.position.y -= Math.sin(Math.toRadians(rotation.x));
            this.position.z -= Math.cos(Math.toRadians(rotation.y));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            this.position.x += Math.sin(Math.toRadians(rotation.y + 90));
            this.position.y -= Math.sin(Math.toRadians(rotation.x));
            this.position.z -= Math.cos(Math.toRadians(rotation.y + 90));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x -= 0.2f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
            position.z += 0.2f;
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            position.y += 0.2f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            position.y -= 0.2f;
        }

        if (rotation != null) {

        }

        if (Mouse.isButtonDown(2)) {
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

    public void setFov(float fov) {
        this.fov = fov;
    }
}
