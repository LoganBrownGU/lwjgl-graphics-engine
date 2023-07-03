package entities;

import models.RawModel;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import textures.ModelTexture;
import toolbox.Maths;

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

    // from the nth face to (from + length)th face
    public void recalculateNormals(int from, int length) {
        // reset normals that need to be recalculated
        for (int i = 3 * from; i < 3 * from + 3 * length; i++) {
            normals[indices[i] * 3] = 0;
            normals[indices[i] * 3 + 1] = 0;
            normals[indices[i] * 3 + 1] = 0;
        }

        // go through each face and recalculate
        for (int i = 3 * from; i < 3 * from + 3 * length; i += 3) {
            int idx = i;
            Vector3f v1 = new Vector3f(vertices[indices[idx] * 3], vertices[indices[idx] * 3 + 1], vertices[indices[idx] * 3 + 2]);
            idx++;
            Vector3f v2 = new Vector3f(vertices[indices[idx] * 3], vertices[indices[idx] * 3 + 1], vertices[indices[idx] * 3 + 2]);
            idx++;
            Vector3f v3 = new Vector3f(vertices[indices[idx] * 3], vertices[indices[idx] * 3 + 1], vertices[indices[idx] * 3 + 2]);

            Vector3f normal = Maths.normalToTriangle(v1, v2, v3);

            for (int j = 0; j < 3; j++) {
                normals[indices[i+j] * 3] += normal.x;
                normals[indices[i+j] * 3 + 1] += normal.y;
                normals[indices[i+j] * 3 + 2] += normal.z;
            }
        }
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
