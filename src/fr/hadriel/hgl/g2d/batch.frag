#version 330 core

out vec4 color;

uniform sampler2D textures[48]; // back support is for OpenGL 3.X (48 textures max)

in vec4 o_color;
in vec2 o_uv;
in float o_tid;

void main()
{
    if(o_tid < 0f) {
        color = o_color;
    } else {
        color = texture(textures[int(o_tid)], o_uv) * o_color;
    }
}