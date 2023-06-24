package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f scale;
	private final Picker picker;

	public Entity(TexturedModel model, Vector3f position, Vector3f rotation,
				  float scale, Picker picker) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = new Vector3f(scale, scale, scale);
		this.picker = picker;
	}

	public Entity(TexturedModel model, Vector3f position, Vector3f rotation,
				  Vector3f scale, Picker picker) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.picker = picker;
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotation.x += dx;
		this.rotation.y += dy;
		this.rotation.z += dz;
	}

	public void increaseRotation(Vector3f increase) {
		this.rotation = Vector3f.add(increase, this.rotation, null);
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return this.rotation.x;
	}

	public void setRotX(float rotX) {
		this.rotation.x = rotX;
	}

	public float getRotY() {
		return this.rotation.y;
	}

	public void setRotY(float rotY) {
		this.rotation.y = rotY;
	}

	public float getRotZ() {
		return this.rotation.z;
	}

	public void setRotZ(float rotZ) {
		this.rotation.z = rotZ;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = new Vector3f(scale, scale, scale);
	}
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public void setRotation(float x, float y, float z) {
		this.rotation.x = x;
		this.rotation.y = y;
		this.rotation.z = z;
	}

	public Picker getPicker() {
		return picker;
	}

	@Override
	public String toString() {
		return "entity @ " + position;
	}
}