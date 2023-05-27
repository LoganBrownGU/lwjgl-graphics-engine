#version 400 core

const int max_lights = 28;
const int cel_num = 5;

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVectors[max_lights];
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform vec3 lightColours[max_lights];
uniform vec3 attenuations[max_lights];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;
uniform float num_lights;
uniform bool isEmissive;

float cel_shade(float brightness) {
    brightness *= cel_num;
    brightness = round(brightness);
    brightness /= cel_num;

    return brightness;
}

void main(void){

    vec4 textureColour = texture(modelTexture, pass_textureCoordinates);
    if (textureColour[3] < 0.5) discard;

    if (isEmissive) {
        out_Color = mix(vec4(skyColour, 1), textureColour, visibility);
        return;
    }

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0f);
    vec3 totalSpec = vec3(0f);

    for (int i = 0; i < max_lights && i < num_lights; i++) {
        float distanceToLight = length(toLightVectors[i]);
        float attenuation = attenuations[i].x + attenuations[i].y * distanceToLight + attenuations[i].z * pow(distanceToLight, 2);
        vec3 unitLightVector = normalize(toLightVectors[i]);

        float nDotl = dot(unitNormal, unitLightVector);
        float brightness = max(nDotl, 0.0);
        //brightness = cel_shade(brightness);

        vec3 lightDirection = -unitLightVector;
        vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

        float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
        specularFactor = max(specularFactor, 0.0);
        float dampedFactor = pow(specularFactor, shineDamper);

        totalDiffuse += (brightness * lightColours[i]) / attenuation;
        totalSpec += (dampedFactor * reflectivity * lightColours[i]) / attenuation;
    }

    totalDiffuse = max(totalDiffuse, 0.2);

    out_Color =  vec4(totalDiffuse, 1.0) * textureColour + vec4(totalSpec, 1.0);
    out_Color = mix(vec4(skyColour, 1), out_Color, visibility);
}