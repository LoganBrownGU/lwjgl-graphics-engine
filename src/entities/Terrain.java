package entities;

import models.RawModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import textures.ModelTexture;

public class Terrain {
    private static final float SIZE = 800;

    private float x, z;
    public Vector3f scale;
    private RawModel model;
    private final ModelTexture texture;

    public Terrain(int x, int z, ModelTexture texture, RawModel model, Vector3f scale) {
        this.texture = texture;
        this.model = model;
        this.x = x * SIZE;
        this.z = z * SIZE;
        this.scale = scale;
    }

    public Terrain(int x, int z, ModelTexture texture, RawModel model, float scale) {
        this.texture = texture;
        this.model = model;
        this.x = x * SIZE;
        this.z = z * SIZE;
        this.scale = new Vector3f(scale, scale, scale);
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public RawModel getModel() {
        return model;
    }

    public ModelTexture getTexture() {
        return texture;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }
}
