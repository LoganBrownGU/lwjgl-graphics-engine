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

    public boolean isIntersecting(Vector3f spherePosition, int radius) {
        // if the camera is inside the sphere then we can immediately return true
        if (Vector3f.sub(spherePosition, camera.getPosition(), null).lengthSquared() < radius*radius)
            return true;



        return false;
    }

    private Vector3f calculateMouseRay() {
        Vector2f glCoords = Maths.mouseCoordsToGLCoords(new Vector2f(Mouse.getX(), Mouse.getY()));
        Vector4f clipCoords = new Vector4f(glCoords.x, glCoords.y, -1, 1);
        Vector4f eyeCoords = Maths.clipSpaceToEyeSpace(clipCoords, projectionMatrix);
        return Maths.eyeSpaceToWorldSpace(eyeCoords, viewMatrix).normalise(null);
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }
}
