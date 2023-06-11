package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class Entity {

	private TexturedModel[] models;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private Vector3f scale;
	private final Picker picker;

	public Entity(TexturedModel[] models, Vector3f position, float rotX, float rotY, float rotZ,
				  float scale, Picker picker) {
		this.models = models;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = new Vector3f(scale, scale, scale);
		this.picker = picker;
	}

	public Entity(TexturedModel[] models, Vector3f position, float rotX, float rotY, float rotZ,
				  Vector3f scale, Picker picker) {
		this.models = models;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.picker = picker;
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public TexturedModel[] getModels() {
		return models;
	}

	public void setModels(TexturedModel[] models) {
		this.models = models;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
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

	public Picker getPicker() {
		return picker;
	}

	@Override
	public String toString() {
		return "entity @ " + position;
	}
}
