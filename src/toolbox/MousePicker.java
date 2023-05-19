package toolbox;

import entities.Camera;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class MousePicker {
    private final Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private final Camera camera;

    public MousePicker(Matrix4f projectionMatrix, Camera camera) {
        this.projectionMatrix = projectionMatrix;
        this.camera = camera;
        this.viewMatrix = Maths.createViewMatrix(camera);
    }

    private void update() {
        viewMatrix = Maths.createViewMatrix(camera);
    }

    private Vector3f calculateMouseRay() {
        update();

        Vector2f coords = new Vector2f(Mouse.getX(), Mouse.getY());
        return Maths.screenCoordsToRay(coords, projectionMatrix, viewMatrix);
    }

    public Vector3f getCurrentRay() {
        update();
        return calculateMouseRay();
    }
}
