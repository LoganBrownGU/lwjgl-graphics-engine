package entities;

import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;

public class Terrain {
    private static final float SIZE = 800;

    private float x, z;
    private final float spacing;
    private final RawModel model;
    private final ModelTexture texture;
    private final float[][] heights;
    private final float[] vertices, normals, textures;
    private final int[] indices;

    public Terrain(int x, int z, ModelTexture texture, float spacing, float[][] heights,
                   float[] vertices, float[] normals, float[] textures, int[] indices, Loader loader) {
        this.texture = texture;
        this.model = loader.loadToVAO(vertices, textures, normals, indices);
        this.x = x * SIZE;
        this.z = z * SIZE;
        this.spacing = spacing;
        this.heights = heights;
        this.vertices = vertices;
        this.normals = normals;
        this.textures = textures;
        this.indices = indices;
    }

    public void recalculateNormals(float[] vertices, int from, int length) {

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

    public float[] getVertices() {
        return vertices;
    }

    public float[] getNormals() {
        return normals;
    }

    public float[] getTextures() {
        return textures;
    }

    public int[] getIndices() {
        return indices;
    }
}
