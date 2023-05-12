package toolbox;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import org.lwjgl.util.vector.Vector4f;

public class Maths {

	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1), matrix, matrix);
		return matrix;
	}

	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
			float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
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
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}

	public static Vector4f clipSpaceToEyeSpace(Vector4f clipCoords, Matrix4f projectionMatrix) {
		Matrix4f inverted = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(inverted, clipCoords, null);
		eyeCoords.z = 0;
		eyeCoords.w = 0;
		return eyeCoords;
	}

	public static Vector3f eyeSpaceToWorldSpace(Vector4f eyeCoords, Matrix4f viewMatrix) {
		Matrix4f inverted = Matrix4f.invert(viewMatrix, null);
		Vector4f worldCoords = Matrix4f.transform(viewMatrix, eyeCoords, null);
		return new Vector3f(worldCoords.x, worldCoords.y, worldCoords.z);
	}

	public static Vector2f mouseCoordsToGLCoords(Vector2f mouseCoords) {
		Vector2f glCoords = new Vector2f();
		glCoords.x = (2 * mouseCoords.x) / Display.getWidth() - 1;
		glCoords.y = (2 * mouseCoords.y) / Display.getHeight() - 1;

		return glCoords;
	}

}
