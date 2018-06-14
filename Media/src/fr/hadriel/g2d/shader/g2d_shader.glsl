#shader vertex
#version 330 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec4 color;
layout (location = 2) in vec2 uv;

uniform mat4 g2d_projection = mat4(1);
uniform mat3 g2d_model = mat3(1);
uniform mat3 g2d_view = mat3(1);

out struct G2DVertex
{
    vec2 position;
    vec4 color;
    vec2 uv;
} v;

void main()
{
    //GL projection
    gl_Position = g2d_projection * vec4(g2d_view * g2d_model * vec3(position, 0), 0);

    //Vertex data
    v.position  = position;
    v.color     = color;
    v.uv        = uv;
}

#shader fragment
#version 330 core

in struct G2DVertex
{
    vec2 position;
    vec4 color;
    vec2 uv;
} v;

uniform sampler2D u_texture;

out vec4 color;

void main()
{
    color = v.color * texture(u_texture, v.uv);
}