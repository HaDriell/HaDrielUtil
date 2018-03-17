#shader vertex
#version 330 core

layout (location = 0) in vec2 i_position;
layout (location = 1) in vec4 i_color;
layout (location = 2) in vec2 i_uv;
layout (location = 3) in uint i_texture;

uniform mat4 u_projection = mat4(1);

out struct Vertex
{
    vec2 position;
    vec4 color;
    vec2 uv;
} v;

flat out uint tid;


void main()
{
    //GL projection
    gl_Position = u_projection * vec4(i_position, 0, 1);

    //Vertex data setup
    v.position  = i_position;
    v.color     = i_color;
    v.uv        = i_uv;
    //texture index setup
    tid = i_texture;
}


#shader fragment
#version 330 core

layout (location = 0) out vec4 color;

uniform sampler2D u_page[32]; // list of glyph pages (texture2D) of the Font
uniform float u_weight; // width of the characters [0.0 - 1.0]
uniform float u_edge;   // strength of the bluring between character & background

in struct Vertex
{
    vec2 position;
    vec4 color;
    vec2 uv;
} v;

flat in uint tid;

void main()
{
    float distance = 1.0 - texture(u_page[tid], v.uv).a;
    float alpha = 1.0 - smoothstep(u_weight, u_weight + u_edge, distance);
    color = vec4(v.color.rgb, v.color.a * alpha);
}