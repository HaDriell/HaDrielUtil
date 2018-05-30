package fr.hadriel.g2d.commandbuffer;

import fr.hadriel.math.*;
import fr.hadriel.opengl.Texture2D;
import fr.hadriel.opengl.shader.GLSLType;
import fr.hadriel.opengl.shader.Shader;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL13.*;

public final class UniformBuffer {

    private final List<Uniform> uniforms;

    public UniformBuffer() {
        this.uniforms = new ArrayList<>();
    }

    private Uniform getUniform(String name) {
        for(Uniform uniform : uniforms)
            if (uniform.name.equals(name))
                return uniform;
        return null;
    }

    private void __uniform(String name, GLSLType type, Object value) {
        Uniform uniform = getUniform(name);

        if (value == null && uniform != null) {
            uniforms.remove(uniform);
        }

        if (value != null) {
            if (uniform == null) {
                uniforms.add(new Uniform(name, type, value));
            } else {
                uniform.type = type;
                uniform.value = value;
            }
        }
    }

    public void clear() {
        uniforms.clear();
    }

    public void removeUniform(String name) {
        __uniform(name, null, null);
    }

    public void setUniform(String name, int value) {
        __uniform(name, GLSLType.GL_INT, value);
    }

    public void setUniform(String name, float value) {
        __uniform(name, GLSLType.GL_FLOAT, value);
    }

    public void setUniform(String name, Vec2 v) {
        __uniform(name, GLSLType.GL_FLOAT_VEC2, v);
    }

    public void setUniform(String name, Vec3 v) {
        __uniform(name, GLSLType.GL_FLOAT_VEC3, v);
    }

    public void setUniform(String name, Vec4 v) {
        __uniform(name, GLSLType.GL_FLOAT_VEC4, v);
    }

    public void setUniform(String name, Matrix3 m) {
        __uniform(name, GLSLType.GL_FLOAT_MAT3, m);
    }

    public void setUniform(String name, Matrix4 m) {
        __uniform(name, GLSLType.GL_FLOAT_MAT4, m);
    }

    public void setUniform(String name, Texture2D t) {
        __uniform(name, GLSLType.GL_SAMPLER_2D, t);
    }

    public void setupUniforms(Shader shader) {
        int slot = GL_TEXTURE0;

        for (Uniform uniform : uniforms) {
            switch (uniform.type) {
                case GL_INT: shader.setUniform(uniform.name, (int) uniform.value); break;
                case GL_FLOAT: shader.setUniform(uniform.name, (float) uniform.value); break;
                case GL_FLOAT_VEC2: shader.setUniform(uniform.name, (Vec2) uniform.value); break;
                case GL_FLOAT_VEC3: shader.setUniform(uniform.name, (Vec3) uniform.value); break;
                case GL_FLOAT_VEC4: shader.setUniform(uniform.name, (Vec4) uniform.value); break;
                case GL_FLOAT_MAT3: shader.setUniform(uniform.name, (Matrix3) uniform.value); break;
                case GL_FLOAT_MAT4: shader.setUniform(uniform.name, (Matrix4) uniform.value); break;

                case GL_SAMPLER_2D:
                    int tid = slot++;
                    glActiveTexture(tid);
                    shader.setUniform(uniform.name, tid);
                    break;
            }
        }
    }

    public boolean equals(Object obj) {
        if (!UniformBuffer.class.isInstance(obj)) return false;
        UniformBuffer buffer = (UniformBuffer) obj;

        if (uniforms.size() != buffer.uniforms.size()) return false;

        for (Uniform u : buffer.uniforms) {
            if (!u.equals(getUniform(u.name)))
                return false;
        }
        return true;
    }

    // Uniform Value Container

    private static final class Uniform {
        private final String name;
        private GLSLType type;
        private Object value;

        public Uniform(String name, GLSLType type, Object value) {
            this.name = name;
            this.type = type;
            this.value = value;
        }

        public boolean equals(Object obj) {
            if (!Uniform.class.isInstance(obj)) return false;
            Uniform u = (Uniform) obj;
            return name.equals(u.name) && type.equals(u.type) && value.equals(u.value);
        }
    }
}