package entities;

import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;

public class Terrain {
    private static final float SIZE = 800;

    private float x, z;
    private RawModel model;
    private final ModelTexture texture;

    public Terrain(int x, int z, ModelTexture texture, RawModel model) {
        this.texture = texture;
        this.model = model;
        this.x = x * SIZE;
        this.z = z * SIZE;
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
}
