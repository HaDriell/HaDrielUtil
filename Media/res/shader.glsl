#shader vertex
#version 330 core

 layout (location = 0) in vec2 i_position;
 layout (location = 1) in vec4 i_color;
 layout (location = 2) in vec2 i_uv;

 out vec4 o_color;
 out vec2 o_uv;

 void main()
 {
 	gl_Position = vec4(i_position, 0, 1);
 	o_color = i_color;
 	o_uv = i_uv;
 }

#shader fragment
#version 330 core

in vec4 o_color;
in vec2 o_uv;

uniform sampler2D u_texture;

out vec4 color;

void main()
{
    color = texture(u_texture, o_uv) * o_color;
}