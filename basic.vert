#version 330 core

layout (location = 0) in vec3 i_position;
layout (location = 1) in vec3 i_normal;
layout (location = 2) in vec2 i_uv;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix;
uniform mat4 ml_matrix;

out vec3 o_normal;
out vec2 o_uv;

void main()
{
	gl_Position = pr_matrix * vw_matrix * ml_matrix * vec4(i_position, 1);
	o_normal = i_normal;
	o_uv = i_uv;
}