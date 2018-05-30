#shader vertex
#version 330 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec4 color;
layout (location = 2) in vec2 uv;

uniform mat4 u_projection = mat4(1);

out struct Vertex
{
    vec2 position;
    vec4 color;
    vec2 uv;
} v;

void main()
{
    //GL projection
    gl_Position = u_projection * vec4(position, 0, 1);

    //Vertex data setupUniforms
    v.position  = position;
    v.color     = color;
    v.uv        = uv;
}

#shader fragment
#version 330 core

in struct Vertex
{
    vec2 position;
    vec4 color;
    vec2 uv;
} v;

uniform sampler2D u_texture;
uniform float u_buffer = 0.5;
uniform float u_gamma = 0.1;

out vec4 color;

void main()
{
    float distance = texture(u_texture, v.uv).a;
    float alpha = smoothstep(u_buffer - u_gamma, u_buffer + u_gamma, distance);
    color = vec4(v.color.rgb, v.color.a * alpha);
}