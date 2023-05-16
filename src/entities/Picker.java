package entities;

import org.lwjgl.util.vector.Vector3f;

public interface Picker {
    boolean isIntersecting(Vector3f rayDirection, Vector3f rayPosition);
}
