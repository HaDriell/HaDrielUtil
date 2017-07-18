#version 330 core

out vec4 color;

uniform sampler2D textures[32]; // support for OpenGL 3.X (48 textures max) should be 80 right now

in vec4 o_color;
in vec2 o_uv;
in float o_tid;

void main()
{
    if(o_tid < 0.0) {
        color = o_color;
    } else {
        color = texture(textures[int(o_tid)], o_uv) * o_color;
    }
}