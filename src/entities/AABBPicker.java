package entities;

import org.lwjgl.util.vector.Vector3f;

public class AABBPicker implements Picker {
    private final Vector3f min, max;

    @Override
    public boolean isIntersecting(Vector3f rayDirection, Vector3f rayPosition) {
        Vector3f tmin = new Vector3f(-1000, -1000, -1000);
        Vector3f tmax = new Vector3f(1000, 1000, 1000);

        float t0 = -1000;
        float t1 = 1000;

        Vector3f max = new Vector3f(this.max);
        Vector3f min = new Vector3f(this.min);
        max = Vector3f.sub(max, min, null);

        Vector3f r = rayDirection;
        Vector3f o = new Vector3f(rayPosition);
        o = Vector3f.sub(o, min, null);

        min = new Vector3f();

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
    }

    public AABBPicker(Vector3f min, Vector3f max) {
        this.min = min;
        this.max = max;
    }
}
