#shader vertex
#version 330 core

layout (location = 0) in vec2 i_position;
layout (location = 1) in vec4 i_color;
layout (location = 2) in vec2 i_uv;
layout (location = 3) in int i_tid;


uniform mat4 projection;

out vec4 o_color;
out vec2 o_uv;
out int o_tid;


void main()
{
    gl_Position = projection * vec4(i_position, 0, 1);
    o_color = i_color;
    o_uv = i_uv;
    o_tid = i_tid;
}


#shader fragment
#version 330 core

out vec4 color;

uniform sampler2D textures[32];
uniform float smoothing = 1.0 / 16.0;

in vec4 o_color;
in vec2 o_uv;
in int o_tid;

void main()
{
    float distance = texture2D(textures[o_tid], o_uv).a;
    float alpha = smoothstep(0.5 - smoothing, 0.5 + smoothing, distance);
    color = vec4(o_color.rgb, o_color.a * alpha);
}