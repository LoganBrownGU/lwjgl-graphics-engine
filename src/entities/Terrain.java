package entities;

import models.RawModel;
import textures.ModelTexture;

public class Terrain {
    private static final float SIZE = 800;

    private float x, z;
    private final float spacing;
    private final RawModel model;
    private final ModelTexture texture;
    private final float[][] heights;

    public Terrain(int x, int z, ModelTexture texture, RawModel model, float spacing, float[][] heights) {
        this.texture = texture;
        this.model = model;
        this.x = x * SIZE;
        this.z = z * SIZE;
        this.spacing = spacing;
        this.heights = heights;
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

    public float getSpacing() {
        return spacing;
    }

    public float[][] getHeights() {
        return heights;
    }
}
