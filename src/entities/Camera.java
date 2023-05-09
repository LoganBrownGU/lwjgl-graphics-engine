package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position;
	private Vector3f rotation;
	
	public void move(){
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			position.z-=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			position.x+=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			position.x-=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			position.y+=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			position.y-=0.2f;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}
}
