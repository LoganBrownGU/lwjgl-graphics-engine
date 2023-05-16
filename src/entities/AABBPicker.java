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

        float div = 1 / r.x;
        if (div >= 0) {
            tmin.x = (min.x - o.x) * div;
            tmax.x = (max.x - o.x) * div;
        } else {
            tmin.x = (max.x - o.x) * div;
            tmax.x = (min.x - o.x) * div;
        }

        div = 1 / r.y;
        if (div >= 0) {
            tmin.y = (min.y - o.y) * div;
            tmax.y = (max.y - o.y) * div;
        } else {
            tmin.y = (max.y - o.y) * div;
            tmax.y = (min.y - o.y) * div;
        }
        if (tmin.x > tmax.y || tmin.y > tmax.x)
            return false;

        if (tmin.y > tmin.x)
            tmin.x = tmin.y;
        if (tmax.y < tmax.x)
            tmax.x = tmax.y;

        div = 1 / r.z;
        if (div >= 0) {
            tmin.z = (min.z - o.z) * div;
            tmax.z = (max.z - o.z) * div;
        } else {
            tmin.z = (max.z - o.z) * div;
            tmax.z = (min.z - o.z) * div;
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
