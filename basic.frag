#version 330 core

out vec4 color;

uniform sampler2D tex;

in vec3 o_normal;
in vec2 o_uv;

void main()
{
    color = texture(tex, o_uv);
}