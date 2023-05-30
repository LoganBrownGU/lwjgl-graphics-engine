package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class FocusedCamera extends Camera {

    @Override
    public void move(Matrix4f projectionMatrix) {
        Vector3f toOrigin = getPosition().negate(null);
        Vector3f rotation = this.getRotation();
        Vector3f position = this.getPosition();

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) rotation.y -= 1f;
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) rotation.y += 1f;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) position.y -= .1f;
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) position.y += .1f;
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) position.z += .1f;
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) position.z -= .1f;
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) position.x -= .1f;
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) position.x += .1f;

        this.setRotation(Maths.createRotationVector(toOrigin));
        this.getRotation().x = 45;
    }

    public FocusedCamera(Vector3f position, Vector3f rotation, float fov) {
        super(position, rotation, fov);
        Vector3f toOrigin = getPosition().negate(null);
        this.setRotation(Maths.createRotationVector(toOrigin));
    }
}
