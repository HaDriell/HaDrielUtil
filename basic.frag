#version 330 core

layout (location = 0) out vec4 color;

uniform sampler2D u_Texture;

in DATA
{
    vec4 color;
	vec2 uv;
} fs_in;

void main()
{
	color = texture(u_Texture, fs_in.uv);
}