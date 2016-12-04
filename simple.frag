#version 330 core

layout (location = 0) out vec4 color;

uniform sampler2D tex[32];

in DATA
{
    vec4 color;
    vec2 uv;
    int tid;
} fs_in;

void main()
{
    color = texture(tex[fs_in.tid], fs_in.uv);
}