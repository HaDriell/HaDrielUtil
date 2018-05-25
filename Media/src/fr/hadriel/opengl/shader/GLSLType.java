package fr.hadriel.opengl.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public enum GLSLType {
    GL_INT(GL11.GL_INT, 4),
    GL_FLOAT(GL11.GL_FLOAT, 4),
    GL_UNSIGNED_INT(GL11.GL_UNSIGNED_INT, 4),

    GL_FLOAT_VEC2(GL20.GL_FLOAT_VEC2, 4 * 2),
    GL_FLOAT_VEC3(GL20.GL_FLOAT_VEC3, 4 * 3),
    GL_FLOAT_VEC4(GL20.GL_FLOAT_VEC4, 4 * 4),

    GL_FLOAT_MAT2(GL20.GL_FLOAT_MAT2, 4 * 2 * 2),
    GL_FLOAT_MAT3(GL20.GL_FLOAT_MAT3, 4 * 3 * 3),
    GL_FLOAT_MAT4(GL20.GL_FLOAT_MAT4, 4 * 4 * 4),

    GL_INT_VEC2(GL20.GL_INT_VEC2, 4 * 2),
    GL_INT_VEC3(GL20.GL_INT_VEC3, 4 * 3),
    GL_INT_VEC4(GL20.GL_INT_VEC4, 4 * 4),

    GL_UNSIGNED_INT_VEC2(GL30.GL_UNSIGNED_INT_VEC2, 4 * 2),
    GL_UNSIGNED_INT_VEC3(GL30.GL_UNSIGNED_INT_VEC3, 4 * 3),
    GL_UNSIGNED_INT_VEC4(GL30.GL_UNSIGNED_INT_VEC4, 4 * 4),

    //opaque types
    GL_SAMPLER_1D(GL20.GL_SAMPLER_1D, 4),
    GL_SAMPLER_2D(GL20.GL_SAMPLER_2D, 4),
    GL_SAMPLER_3D(GL20.GL_SAMPLER_3D, 4),
    GL_SAMPLER_1D_SHADOW(GL20.GL_SAMPLER_1D_SHADOW, 4),
    GL_SAMPLER_2D_SHADOW(GL20.GL_SAMPLER_2D_SHADOW, 4),



    ;

    public final int type;
    public final int size;

    private GLSLType(int type, int size) {
        this.type = type;
        this.size = size;
    }

    public static GLSLType findByType(int type) {
        for(GLSLType glslType : GLSLType.values())
            if(glslType.type == type)
                return glslType;
        return null;
    }
}