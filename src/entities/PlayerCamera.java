package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class PlayerCamera extends Camera {

    private final float height = 4f;

    public void move(Matrix4f projectionMatrix, Terrain terrain) {
        super.move(projectionMatrix);

        Vector3f relative = Vector3f.sub(this.position, new Vector3f(terrain.getX(), 0, terrain.getZ()), null);
        relative.scale(1 / terrain.getSpacing());
        float height = terrain.getHeights()[Math.round(relative.z)][Math.round(relative.x)];
        this.setPosition(new Vector3f(this.position.x, height + this.height, this.position.z));

        /*Vector3f move = new Vector3f();
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
            move = new Vector3f(0, 0, -1);
        else if (Keyboard.isKeyDown(Keyboard.KEY_S))
            move = new Vector3f(0, 0, 1);
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
            move = new Vector3f(-1, 0, 0);
        else if (Keyboard.isKeyDown(Keyboard.KEY_D))
            move = new Vector3f(1, 0, 0);

        Vector3f.add(this.position, move, this.position);*/
    }

    public PlayerCamera(Vector3f position, Vector3f rotation, float fov) {
        super(position, rotation, fov);
    }
}
