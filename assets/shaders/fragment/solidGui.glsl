#version 140

in vec2 textureCoords;

out vec4 out_Color;

uniform vec3 colour;

void main(void){
    out_Color = vec4(colour, 1);
}