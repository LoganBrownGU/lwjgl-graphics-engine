package renderEngine;

import de.matthiasmann.twl.utils.PNGDecoder;
import entities.Terrain;
import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import textures.ModelTexture;
import textures.TextureData;
import toolbox.FileHandler;
import toolbox.Maths;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class Loader {

    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();

    public int loadCubeMap(String[] textureFiles) {
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

        for (int i = 0; i < textureFiles.length; i++) {
            TextureData data = decodeTextureFile(textureFiles[i]);
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

        textures.add(texID);
        return texID;
    }

    private TextureData decodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ", didn't work");
            System.exit(-1);
        }
        return new TextureData(width, height, buffer);
    }

    public int loadToVAO(float[] positions, float[] textureCoords) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, 2, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        unbindVAO();
        return vaoID;
    }

    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public RawModel loadToVAO(float[] positions, int nDimensions) {
        int vaoID = createVAO();
        this.storeDataInAttributeList(0, nDimensions, positions);
        unbindVAO();
        return new RawModel(vaoID, positions.length / 2);
    }

    public int loadTexture(String fPath) {
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("PNG",
                    new FileInputStream(fPath));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

            if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
                float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
            } else
                System.out.println("anisotropic filtering not supported");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fPath + ", didn't work");
            System.exit(-1);
        }
        textures.add(texture.getTextureID());
        return texture.getTextureID();
    }

    public void cleanUp() {
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        for (int texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public Terrain loadHeightMap(String path, String texturePath, float size, float maxHeight, float textureScale) {
        float[][] data = FileHandler.readPixels(path);
        int height = data.length;
        int width = data[0].length;
        float spacing = size / Math.max(width, height);

        // storing vertices as vectors as well as array makes it easier to calculate normals
        ArrayList<Vector3f> vectorVertices = new ArrayList<>();
        float[] vertices = new float[width * height * 3];
        float[] textures = new float[width * height * 2];
        ArrayList<Integer> indices = new ArrayList<>();
        Vector3f[] normals = new Vector3f[vertices.length / 3];
        Arrays.fill(textures, 1);
        for (int i = 0; i < normals.length; i++) normals[i] = new Vector3f();

        // put vertices into array
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int idx = (width * i + j) * 3;
                Vector3f vertex = new Vector3f(j * spacing, data[i][j] * maxHeight - maxHeight / 2, i * spacing);
                vectorVertices.add(vertex);
                vertices[idx] = vertex.x;
                vertices[idx + 1] = vertex.y;
                vertices[idx + 2] = vertex.z;

                idx = (width * i + j) * 2;
                textures[idx] = j * spacing * .005f * textureScale;
                textures[idx+1] = i * spacing * .005f * textureScale;
            }
        }

        // create indices and calculate normals for each plane
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                int[] face = {i * width + j, (i+1) * width + j, i * width + j + 1};
                indices.add(face[0]);
                indices.add(face[1]);
                indices.add(face[2]);

                Vector3f normal = Maths.normalToTriangle(vectorVertices.get(face[0]), vectorVertices.get(face[1]), vectorVertices.get(face[2]));
                Vector3f.add(normals[face[0]], normal, normals[face[0]]);
                Vector3f.add(normals[face[1]], normal, normals[face[1]]);
                Vector3f.add(normals[face[2]], normal, normals[face[2]]);

                face = new int[] {i * width + j + 1, (i + 1) * width + j, (i + 1) * width + j + 1};
                indices.add(face[0]);
                indices.add(face[1]);
                indices.add(face[2]);
                normal = Maths.normalToTriangle(vectorVertices.get(face[0]), vectorVertices.get(face[1]), vectorVertices.get(face[2]));
                Vector3f.add(normals[face[0]], normal, normals[face[0]]);
                Vector3f.add(normals[face[1]], normal, normals[face[1]]);
                Vector3f.add(normals[face[2]], normal, normals[face[2]]);
            }
        }

        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indicesArray.length; i++) indicesArray[i] = indices.get(i);

        for (Vector3f v : normals)
            v.normalise(null);
        float[] normalsArray = new float[normals.length * 3];
        for (int i = 0; i < normals.length; i++) {
            normalsArray[i*3] = normals[i].x;
            normalsArray[i*3 + 1] = normals[i].y;
            normalsArray[i*3 + 2] = normals[i].z;
        }

        Terrain terrain = new Terrain(0, 0, new ModelTexture(loadTexture(texturePath), false), loadToVAO(vertices, textures, normalsArray, indicesArray), 1);

        terrain.setX(-width * spacing / 2);
        terrain.setZ(-height * spacing / 2);
        return terrain;
    }
}
