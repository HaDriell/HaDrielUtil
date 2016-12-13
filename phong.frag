#version 330 core

out vec4 color;

in vec3 o_normal;
in vec3 o_toLight;
in vec3 o_toCamera;
in vec2 o_uv;

//textures
uniform sampler2D tex;

//Light setup
uniform vec3 ambientLight;
uniform vec3 diffuseLight;
uniform vec3 specularLight;

//Material setup
uniform vec3 ambientReflection;
uniform vec3 diffuseReflection;
uniform vec3 specularReflection;
uniform float shininess;

vec3 ambientLighting()
{
    return ambientReflection * ambientLight;
}

vec3 diffuseLighting(in vec3 N, in vec3 L)
{
    float diffuseTerm = clamp(dot(N, L), 0, 1);
    return diffuseReflection * diffuseLight * diffuseTerm;
}

vec3 specularLighting(in vec3 N, in vec3 L, in vec3 V)
{
    float specularTerm = 0;
    if(dot(N, L) > 0)
    {
        vec3 H = normalize(L + V);
        specularTerm = pow(dot(N, H), shininess);
    }
    return specularReflection * specularLight * specularTerm;
}

void main()
{
    vec3 Iamb = ambientLighting();
    vec3 Idif = diffuseLighting(o_normal, o_toLight);
    vec3 Ispe = specularLighting(o_normal, o_toLight, o_toCamera);
    vec4 textureColor = texture(tex, o_uv);
    color = textureColor * vec4((Iamb + Idif + Ispe), 1);
    color.a = 1;
}