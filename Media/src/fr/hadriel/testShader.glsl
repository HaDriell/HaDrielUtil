#shader vertex
#version 330 core

layout (location = 0) in vec2 i_position;
layout (location = 1) in vec4 i_color;

out vec4 o_color;

void main()
{
    gl_Position = vec4(i_position, 0, 1);
    o_color = i_color;
}

#shader fragment
#version 330 core

out vec4 color;

in vec4 o_color;

void main()
{
    color = o_color;
}