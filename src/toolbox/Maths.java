package toolbox;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import org.lwjgl.util.vector.Vector4f;

public class Maths {

    public static Vector3f hexColourToFloat(Vector3f colour) {
        return null;
    }

    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 1), matrix, matrix);
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
                                                      float rz, Vector3f scale) {
        Matrix4f matrix = createTransformationMatrix(translation, rx, ry, rz);
        Matrix4f.scale(scale, matrix, matrix);
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getRotation().x), new Vector3f(1, 0, 0), viewMatrix,
                viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRotation().y), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRotation().z), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }

    public static Vector3f createRotationVector(Vector3f v) {
        Vector3f rotation = new Vector3f();
        rotation.y = (float) -Math.toDegrees(Math.atan(v.x / v.z));
        if (v.z > 0) rotation.y -= 180;
        return rotation;
    }

    public static Vector4f clipSpaceToEyeSpace(Vector4f clipCoords, Matrix4f projectionMatrix) {
        Matrix4f inverted = Matrix4f.invert(projectionMatrix, null);
        Vector4f eyeCoords = Matrix4f.transform(inverted, clipCoords, null);
        eyeCoords.z = -1f;
        eyeCoords.w = 0f;
        return eyeCoords;
    }

    public static Vector4f eyeSpaceToWorldSpace(Vector4f eyeCoords, Matrix4f viewMatrix) {
        Matrix4f inverted = Matrix4f.invert(viewMatrix, null);
        return Matrix4f.transform(inverted, eyeCoords, null);
    }

    public static Vector2f mouseCoordsToNormalisedDeviceCoords(Vector2f mouseCoords) {
        Vector2f glCoords = new Vector2f();
        glCoords.x = (2f * mouseCoords.x) / Display.getWidth() - 1f;
        glCoords.y = (2f * mouseCoords.y) / Display.getHeight() - 1f;

        return glCoords;
    }

    public static Vector3f screenCoordsToRay(Vector2f screenCoords, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        Vector2f normalisedDeviceCoords = Maths.mouseCoordsToNormalisedDeviceCoords(new Vector2f(screenCoords.x, screenCoords.y));
        Vector4f clipCoords = new Vector4f(normalisedDeviceCoords.x, normalisedDeviceCoords.y, -1f, -1);
        Vector4f eyeCoords = Maths.clipSpaceToEyeSpace(clipCoords, projectionMatrix);
        Vector4f worldCoords = Maths.eyeSpaceToWorldSpace(eyeCoords, viewMatrix);
        Vector3f ray = new Vector3f(worldCoords.x, worldCoords.y, worldCoords.z);
        return ray.normalise(null);
    }

    public static Vector2f screenCoordsToGLCoords(Vector2f screenCoords) {
        Vector2f glCoords = new Vector2f(screenCoords);
        glCoords.y = -(glCoords.y * 2 - 1);
        glCoords.x = glCoords.x * 2 - 1;

        return  glCoords;
    }
}
