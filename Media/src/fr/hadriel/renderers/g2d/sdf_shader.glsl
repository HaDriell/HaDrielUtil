#shader vertex
#version 330 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec4 color;
layout (location = 2) in vec2 uv;
layout (location = 3) in int texture;

uniform mat4 u_projection = mat4(1);

out struct Vertex
{
    vec2 position;
    vec4 color;
    vec2 uv;
} v;

flat out int tid;

void main()
{
    //GL projection
    gl_Position = u_projection * vec4(position, 0, 1);

    //Vertex data setup
    v.position  = position;
    v.color     = color;
    v.uv        = uv;
    //texture index setup
    tid         = texture;
}

#shader fragment
#version 330 core

const int MAX_TEXTURES = 32;



in struct Vertex
{
    vec2 position;
    vec4 color;
    vec2 uv;
} v;

flat in int tid;

uniform sampler2D u_texture[MAX_TEXTURES]; // list of textures activated while batching geometry
uniform float u_buffer = 0.5;
uniform float u_gamma = 0.1;

out vec4 color;

void main()
{
    float distance = texture(u_texture[tid], v.uv).a;
    float alpha = smoothstep(u_buffer - u_gamma, u_buffer + u_gamma, distance);
    color = vec4(v.color.rgb, v.color.a * alpha);
}