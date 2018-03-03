#shader vertex
#version 330 core

 layout (location = 0) in vec2 i_position;
 layout (location = 1) in vec4 i_color;
 layout (location = 2) in vec2 i_uv;
 layout (location = 3) in float i_tid;

 uniform mat4 projection;

 out vec4 o_color;
 out vec2 o_uv;
 out float o_tid;

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

uniform sampler2D textures[32]; // 32 is the max array length of version 3.3 (apparently)

in vec4 o_color;
in vec2 o_uv;
in float o_tid;

void main()
{
    if(o_tid < 0.0) {
        color = o_color;
    } else {
        color = texture(textures[int(o_tid)], o_uv) * o_color;
    }
}