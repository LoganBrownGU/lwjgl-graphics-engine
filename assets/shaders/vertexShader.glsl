#version 400 core

const float density = 0.007;
const float gradient = 1.5;
const int max_lights = 10;

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec2 pass_textureCoordinates;
out vec3 surfaceNormal;
out vec3 toLightVectors[max_lights];
out vec3 toCameraVector;
out float visibility;
out int max_lights_out;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPositions[max_lights];
uniform bool fogEnabled;

void main(void){
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    vec4 distanceToCamera = viewMatrix * worldPosition;

    gl_Position = projectionMatrix * distanceToCamera;
    pass_textureCoordinates = textureCoordinates;

    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    for (int i = 0; i < max_lights; i++)
        toLightVectors[i] = lightPositions[i] - worldPosition.xyz;

    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

    if (fogEnabled) {
        visibility = exp(-pow(length(distanceToCamera.xyz) * density, gradient));
        visibility = clamp(visibility, 0, 1);
    } else visibility = 1;

    max_lights_out = max_lights;
}