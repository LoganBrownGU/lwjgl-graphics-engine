#version 400 core

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

const int cel_num = 5;

float cel_shade(float brightness) {
    brightness *= cel_num;
    brightness = round(brightness);
    brightness /= cel_num;

    return brightness;
}

void main(void){

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    float nDotl = dot(unitNormal, unitLightVector);
    float brightness = max(nDotl, 0.1);
    //brightness = cel_shade(brightness);
    vec3 diffuse = brightness * lightColour;

    vec3 unitVectorToCamera = normalize(toCameraVector);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColour;

    vec4 textureColour = texture(modelTexture, pass_textureCoordinates);
    if (textureColour[3] < 0.5) discard;

    out_Color =  vec4(diffuse, 1.0) * textureColour + vec4(finalSpecular, 1.0);

    out_Color = mix(vec4(skyColour, 1), out_Color, visibility);
}