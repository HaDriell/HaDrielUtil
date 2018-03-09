#shader vertex
#version 330 core

layout (location = 0) in vec2 i_position;

void main()
{
    gl_Position = vec4(i_position, 0, 1);
}

#shader fragment
#version 330 core

out vec4 color;

uniform vec4 u_color;

void main()
{
    color = u_color;
}

