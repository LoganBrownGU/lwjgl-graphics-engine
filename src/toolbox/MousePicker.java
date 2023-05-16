package toolbox;

import entities.Camera;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class MousePicker {
    private Vector3f currentRay;
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;

    public MousePicker(Matrix4f projectionMatrix, Camera camera) {
        this.projectionMatrix = projectionMatrix;
        this.camera = camera;
        this.viewMatrix = Maths.createViewMatrix(camera);
    }

    public void update() {
        viewMatrix = Maths.createViewMatrix(camera);
        currentRay = calculateMouseRay();
    }

    private Vector3f calculateMouseRay() {
        Vector3f rot = new Vector3f(camera.getRotation());
        rot.x = (float) Math.toRadians(rot.x);
        rot.y = (float) Math.toRadians(rot.y);
        rot.z = (float) Math.toRadians(rot.z);

        Vector3f resultant = new Vector3f();
        resultant.x = (float) (Math.sin(rot.y) * Math.cos(rot.x));
        resultant.y = (float) -(Math.cos(rot.y) * Math.sin(rot.x));
        resultant.z = (float) -(Math.cos(rot.y) * Math.cos(rot.x));

        return resultant;
    }

    public Vector3f getCurrentRay() {
        currentRay = calculateMouseRay();
        return currentRay;
    }
}
