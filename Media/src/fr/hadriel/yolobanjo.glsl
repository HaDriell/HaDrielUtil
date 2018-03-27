#shader vertex
#version 330 core

layout (location = 0) in vec2 position;

void main()
{
    gl_Position = vec4(position, 0, 0);
}

#shader fragment
#version 330 core

out vec4 color;

uniform vec4 u_color = vec4(1);

void main()
{
    color = u_color;
}