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

    public boolean isIntersecting(Vector3f spherePosition, float radius) {
        // if the camera is inside the sphere then we can immediately return true
        if (Vector3f.sub(spherePosition, camera.getPosition(), null).lengthSquared() < radius*radius)
            return true;

        int range = 100;

        Vector3f ray = calculateMouseRay();
        Vector3f p1 = camera.getPosition();
        Vector3f p2 = new Vector3f(p1.x + ray.x * range, p1.y + ray.y * range, p1.z + ray.z * range);
        Vector3f p3 = new Vector3f(spherePosition);
        p3.y += radius;
        // a = (x2 - x1)2 + (y2 - y1)2 + (z2 - z1)2
        float a = (float) (Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2) + Math.pow(p2.z - p1.z, 2));
        // b = 2[ (x2 - x1) (x1 - x3) + (y2 - y1) (y1 - y3) + (z2 - z1) (z1 - z3) ]
        float b = 2*((p2.x - p1.x)*(p1.x - p3.x) + (p2.y - p1.y)*(p1.y - p3.y) + (p2.z - p1.z)*(p1.z - p3.z));
        // c = x32 + y32 + z32 + x12 + y12 + z12 - 2[x3 x1 + y3 y1 + z3 z1] - r2
        float c = p3.x*p3.x + p3.y*p3.y + p3.z*p3.z + p1.x*p1.x + p1.y*p1.y + p1.z*p1.z - 2*(p3.x*p1.x + p3.y*p1.y + p3.z*p1.z) - radius*radius;

        float determinant = b*b - 4*a*c;

        return determinant >= 0;
    }

    private Vector3f calculateMouseRay() {
        /*Vector2f glCoords = Maths.mouseCoordsToGLCoords(new Vector2f(Mouse.getX(), Mouse.getY()));
        Vector4f clipCoords = new Vector4f(glCoords.x, glCoords.y, -1, 1);
        Vector4f eyeCoords = Maths.clipSpaceToEyeSpace(clipCoords, projectionMatrix);
        return Maths.eyeSpaceToWorldSpace(eyeCoords, viewMatrix).normalise(null);*/

        return new Vector3f((float) Math.sin(Math.toRadians(camera.getRotation().y)), (float) -Math.sin(Math.toRadians(camera.getRotation().x)), (float) -Math.cos(Math.toRadians(camera.getRotation().y)));
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }
}
