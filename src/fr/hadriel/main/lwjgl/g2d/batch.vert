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