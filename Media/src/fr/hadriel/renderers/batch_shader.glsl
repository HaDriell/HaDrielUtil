#shader vertex
#version 330 core

layout (location = 0) in vec2 i_position;
layout (location = 1) in vec4 i_color;
layout (location = 2) in vec2 i_uv;
layout (location = 3) in int i_texture;
layout (location = 4) in int i_mode;

uniform mat4 u_projection = mat4(1);

out struct Vertex
{
    vec2 position;
    vec4 color;
    vec2 uv;
} v;

flat out int tid;
flat out int mode;

void main()
{
    //GL projection
    gl_Position = u_projection * vec4(i_position, 0, 1);

    //Vertex data setup
    v.position  = i_position;
    v.color     = i_color;
    v.uv        = i_uv;
    //texture index setup
    tid         = i_texture;
    mode        = i_mode;
}

#shader fragment
#version 330 core

const int MAX_TEXTURES = 32;

out vec4 color;

uniform sampler2D u_texture[MAX_TEXTURES]; // list of textures activated while batching geometry
uniform float u_weight;
uniform float u_edge;

in struct Vertex
{
    vec2 position;
    vec4 color;
    vec2 uv;
} v;

flat in int tid;
flat in int mode;

//void mode_sprite();
//void mode_font();

void main()
{
    if (mode == 0)
    {
        color = v.color;

        if (tid != -1) {
            color *= texture(u_texture[tid], v.uv);
        }
    }

    if (mode != 0)
    {
        if (tid < 0 || tid >= MAX_TEXTURES)
            discard;
        float distance = 1.0 - texture(u_texture[tid], v.uv).a;
        float alpha = 1.0 - smoothstep(u_weight, u_weight + u_edge, distance);
        color = vec4(v.color.rgb, v.color.a * alpha);
    }
}