#version 330 core

layout (location = 0) in vec3 i_position;
layout (location = 1) in vec3 i_normal;
layout (location = 2) in vec2 i_uv; // uv

uniform mat4 projection;
uniform mat4 model;
uniform mat4 view;

uniform vec3 lightPosition;
uniform vec3 cameraPosition;

out vec3 o_normal;
out vec3 o_toLight;
out vec3 o_toCamera;
out vec2 o_uv;

void main(void)
{
    vec4 worldPosition = model * vec4(i_position, 1);
    o_normal = normalize(i_normal);
    o_toLight = normalize(lightPosition - worldPosition.xyz);
    o_toCamera = normalize(cameraPosition - worldPosition.xyz);
    o_uv = i_uv;
    gl_Position = projection * view * worldPosition;
}