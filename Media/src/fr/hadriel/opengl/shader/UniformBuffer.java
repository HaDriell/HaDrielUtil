package fr.hadriel.opengl.shader;

import java.util.Arrays;

public class UniformBuffer {

    private static final class Uniform {
        public final UniformDeclaration declaration;
        public Object value;

        private Uniform(UniformDeclaration declaration, Object value) {
            this.declaration = declaration;
            this.value = value;
        }

        public boolean equals(Object obj) {
            if (obj instanceof Uniform) {
                Uniform other = (Uniform) obj;
                return other.declaration.equals(declaration) && other.value.equals(value);
            }
            return false;
        }
    }

    private final Uniform[] uniforms;


    public UniformBuffer(UniformDeclaration[] declarations) {
        uniforms = new Uniform[declarations.length];
        for (int i = 0; i < uniforms.length; i++) {
            uniforms[i] = new Uniform(declarations[i], null);
        }
    }

    private Uniform find(String name) {
        for (Uniform u : uniforms)
            if (u.declaration.name.equals(name))
                return u;
        return null;
    }

    public void uniform(String name, Object value) {
        Uniform uniform = find(name);
        if (uniform != null) uniform.value = value;
    }

    public void setupUniforms(Shader shader) {
        for (Uniform uniform : uniforms) {
            shader.uniform(uniform.declaration, uniform.value);
        }
    }

    //Compare the maps values
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof UniformBuffer) {
            UniformBuffer other = (UniformBuffer) obj;
            if (other.uniforms.length != uniforms.length) return false;
            return Arrays.equals(uniforms, other.uniforms);
        }
        return false;
    }
}