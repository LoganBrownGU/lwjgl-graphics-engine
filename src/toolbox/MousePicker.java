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

    public boolean isIntersectingPlane(Vector3f position, Vector3f[] vertices) {

        Vector3f tmin = new Vector3f(-1000, -1000, -1000);
        Vector3f tmax = new Vector3f(1000, 1000, 1000);

        float t0 = -1000;
        float t1 = 1000;

        Vector3f min = vertices[0];
        Vector3f max = vertices[1];
        Vector3f r = calculateMouseRay();
        Vector3f o = camera.getPosition();

        if (r.x >= 0) {
            tmin.x = (min.x - o.x) / r.x;
            tmax.x = (max.x - o.x) / r.x;
        } else {
            tmin.x = (max.x - o.x) / r.x;
            tmax.x = (min.x - o.x) / r.x;
        }
        if (r.y >= 0) {
            tmin.y = (min.y - o.y) / r.y;
            tmax.y = (max.y - o.y) / r.y;
        } else {
            tmin.y = (max.y - o.y) / r.y;
            tmax.y = (min.y - o.y) / r.y;
        }
        if (tmin.x > tmax.y || tmin.y > tmax.x)
            return false;

        if (tmin.y > tmin.x)
            tmin.x = tmin.y;
        if (tmax.y < tmax.x)
            tmax.x = tmax.y;

        if (r.z >= 0) {
            tmin.z = (min.z - o.z) / r.z;
            tmax.z = (max.z - o.z) / r.z;
        } else {
            tmin.z = (max.z - o.z) / r.z;
            tmax.z = (min.z - o.z) / r.z;
        }

        if (tmin.x > tmax.z || tmin.z > tmax.x)
            return false;
        if (tmin.z > tmin.x)
            tmin.x = tmin.z;
        if (tmax.z < tmax.x)
            tmax.x = tmax.z;

        return (tmin.x < t1) && (tmax.x > t0);

        /*Vector3f a = new Vector3f(vertices[0]);
        Vector3f b = new Vector3f(vertices[1]);
        Vector3f c = new Vector3f(vertices[2]);
        Vector3f o = new Vector3f(camera.getPosition());
        Vector3f d = new Vector3f(calculateMouseRay());

        Vector3f aSubb = Vector3f.sub(a, b, null);
        Vector3f bSubc = Vector3f.sub(b, c, null);
        Vector3f n = Vector3f.cross(aSubb, bSubc, null);

        float t = (a.x*n.x + a.y*n.y + a.z*n.z - n.x*o.x - n.y*o.y - n.z*o.z) /
                (d.x*n.x - d.y*n.y + d.z*n.z);

        System.out.println(t);*/
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
        return currentRay;
    }
}
