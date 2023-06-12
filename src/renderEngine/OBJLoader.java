package renderEngine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import models.RawModel;

import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class OBJLoader {

    private static void parseVertices(List<Vector3f> vertices, List<Vector2f> textures, List<Vector3f> normals, String fPath) throws IOException {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Couldn't find " + fPath);
        }

        String line;
        while ((line = br.readLine()) != null) {
            String[] currentLine = line.split(" ");

            switch (currentLine[0]) {
                case "v" -> {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                }

                case "vt" -> {
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                }

                case "vn" -> {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                }
            }
        }

        br.close();
    }

    private static void parseFaces(List<Vector2f> textures, List<Vector3f> normals, List<Integer> indices,
                                   float[] textureArray, float[] normalArray, String fPath) throws IOException {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Couldn't find " + fPath);
        }

        String line;
        while ((line = br.readLine()) != null) {
            String[] currentLine = line.split(" ");
            if (!currentLine[0].equals("f")) continue;

            for (int i = 1; i < currentLine.length; i++)
                processVertex(currentLine[i].split("/"), indices, textures, normals, textureArray, normalArray);
        }

        br.close();
    }



    public static RawModel loadObjModel(String fPath, Loader loader) {
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        float[] textureArray, normalArray;

        try {
            parseVertices(vertices, textures, normals, fPath);
            textureArray = new float[vertices.size() * 2];
            normalArray = new float[vertices.size() * 3];

            parseFaces(textures, normals, indices, textureArray, normalArray, fPath);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading " + fPath + ": " + e.getMessage());
        }

        float[] verticesArray = new float[vertices.size() * 3];
        int[] indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++)
            indicesArray[i] = indices.get(i);

        return loader.loadToVAO(verticesArray, textureArray, normalArray, indicesArray);
    }

    private static void processVertex(String[] vertexData, List<Integer> indices,
                                      List<Vector2f> textures, List<Vector3f> normals, float[] textureArray,
                                      float[] normalsArray) throws IndexOutOfBoundsException {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);
        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currentVertexPointer * 2] = currentTex.x;
        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertexPointer * 3] = currentNorm.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
    }

}
