#shader vertex
#version 330 core

layout (location = 0) in vec2 i_position;
layout (location = 1) in vec4 i_color;
layout (location = 2) in vec2 i_uv;
layout (location = 3) in int i_tid;


uniform mat4 u_projection;

out vec4 o_color;
out vec2 o_uv;
out int o_tid;


void main()
{
    gl_Position = u_projection * vec4(i_position, 0, 1);
    o_color = i_color;
    o_uv = i_uv;
    o_tid = i_tid;
}


#shader fragment
#version 330 core

out vec4 color;

uniform sampler2D u_page[32];
uniform float u_smoothing = 1.0 / 16.0;

in vec4 o_color;
in vec2 o_uv;
in int o_tid;

void main()
{
    float distance = texture2D(u_page[o_tid], o_uv).a;
    float alpha = smoothstep(0.5 - u_smoothing, 0.5 + u_smoothing, distance);
    color = vec4(o_color.rgb, o_color.a * alpha);
}