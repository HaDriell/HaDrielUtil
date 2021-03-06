package fr.hadriel.opengl.shader;

import fr.hadriel.math.*;
import fr.hadriel.opengl.VertexAttribute;
import fr.hadriel.util.IOUtils;
import fr.hadriel.util.logging.Log;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by glathuiliere on 29/11/2016.
 */
public class Shader {
    private static final Logger logger = Log.getLogger(Shader.class);

    private static int CompileShader(int shaderType, String source) {
        int shader = glCreateShader(shaderType);
        glShaderSource(shader, source);
        glCompileShader(shader);
        if(glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Error in Shader compilation: " + glGetShaderInfoLog(shader));
            glDeleteShader(shader);
        }
        return shader;
    }

    private static int CreateProgram(String vertexSource, String fragmentSource) {
        int program = glCreateProgram();
        int vertex = CompileShader(GL_VERTEX_SHADER, vertexSource);
        int fragment = CompileShader(GL_FRAGMENT_SHADER, fragmentSource);

        //Ensure that both Shaders are deleted
        if(!glIsShader(vertex)) {
            System.err.println("Vertex Shader Failed to compile");
            glDeleteShader(fragment);
            glDeleteProgram(program);
            return -1;
        }

        //Ensure that both Shaders are deleted
        if(!glIsShader(fragment)) {
            System.err.println("Fragment Shader Failed to compile");
            glDeleteShader(vertex);
            glDeleteProgram(program);
            return -1;
        }

        glAttachShader(program, vertex);
        glAttachShader(program, fragment);

        //Link
        glLinkProgram(program);
        if(glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE) {
            System.err.println("Program Linking failed: " + glGetProgramInfoLog(program));
            glDeleteProgram(program);
            return -1;
        }

        //Validate
        glValidateProgram(program); // well. fuck it if linking / validation fails, i'll handle that later
        if(glGetProgrami(program, GL_VALIDATE_STATUS) != GL_TRUE) {
            System.err.println("Program Validation failed: " + glGetProgramInfoLog(program));
            glDeleteProgram(program);
            return -1;
        }

        //Clean-up
        glDetachShader(program, vertex);
        glDetachShader(program, fragment);
        glDeleteShader(vertex);
        glDeleteShader(fragment);
        return program;
    }

    public static Shader GLSL(String filename) {
        return GLSL(new File(filename));
    }

    public static Shader GLSL(File file) {
        try {
            return GLSL(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Shader GLSL(InputStream stream) {
        StringBuilder vs = new StringBuilder();
        StringBuilder fs = new StringBuilder();
        StringBuilder buffer = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while((line = in.readLine()) != null) {
                if(line.startsWith("#shader")) {
                    if(line.contains("vertex"))
                        buffer = vs;
                    else if(line.contains("fragment"))
                        buffer = fs;
                    else
                        buffer = null;
                } else if(buffer != null) {
                    buffer.append(line).append('\n');
                }
            }
        } catch (IOException ignore) {}
        int program = CreateProgram(vs.toString(), fs.toString());
        return program != -1 ? new Shader(program) : null;
    }

    public final int program;
    private final UniformDeclaration[] uniforms;
    private final GLSLAttribute[] attributes;

    private Shader(int program) {
        this.program = program;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer size = stack.mallocInt(1);
            IntBuffer type = stack.mallocInt(1);
            IntBuffer uniformCount = stack.mallocInt(1);
            IntBuffer attributeCount = stack.mallocInt(1);

            glGetProgramiv(program, GL_ACTIVE_UNIFORMS, uniformCount);
            glGetProgramiv(program, GL_ACTIVE_ATTRIBUTES, attributeCount);


            //UniformDeclaration initialization
            this.uniforms = new UniformDeclaration[uniformCount.get(0)];
            for (int i = 0; i < uniforms.length; i++) {
                size.clear();
                type.clear();
                String name = glGetActiveUniform(program, i, size, type);
                int location = glGetUniformLocation(program, name);
                GLSLType glslType = GLSLType.findByType(type.get(0));
                uniforms[i] = new UniformDeclaration(name, location, glslType);
                logger.config("Declared setUniform %s %s");
            }

            //Attributes initialization
            this.attributes = new GLSLAttribute[attributeCount.get(0)];
            for (int i = 0; i < attributes.length; i++) {
                size.clear();
                type.clear();
                String name = glGetActiveAttrib(program, i, size, type);
                GLSLType glslType = GLSLType.findByType(type.get(0));
                attributes[i] = new GLSLAttribute(name, glslType);
            }
        }
    }

    public Shader(String vertexSource, String fragmentSource) {
        this(CreateProgram(vertexSource, fragmentSource));
    }

    public Shader(InputStream vertexStream, InputStream fragmentStream) {
        this(IOUtils.readStreamAsString(vertexStream), IOUtils.readStreamAsString(fragmentStream));
    }

    public void destroy() {
        glDeleteProgram(program);
    }

    public void bind() {
        glUseProgram(program);
    }

    public void unbind() {
        glUseProgram(0);
    }

    private UniformDeclaration getUniformDeclaration(String name) {
        for (UniformDeclaration uniform : uniforms)
            if (uniform.name.equals(name))
                return uniform;
        return null;
    }

    public void setUniform(String name, int value) {
        __uniform(getUniformDeclaration(name), value);
    }

    public void setUniform(String name, float value) {
        __uniform(getUniformDeclaration(name), value);
    }

    public void setUniform(String name, int[] value) {
        __uniform(getUniformDeclaration(name), value);
    }

    public void setUniform(String name, float[] value) {
        __uniform(getUniformDeclaration(name), value);
    }

    public void setUniform(String name, Vec2 v) {
        __uniform(getUniformDeclaration(name), v);
    }

    public void setUniform(String name, Vec3 v) {
        __uniform(getUniformDeclaration(name), v);
    }

    public void setUniform(String name, Vec4 v) {
        __uniform(getUniformDeclaration(name), v);
    }

    public void setUniform(String name, Matrix3 matrix) {
        __uniform(getUniformDeclaration(name), matrix);
    }

    public void setUniform(String name, Matrix4 matrix) {
        __uniform(getUniformDeclaration(name), matrix);
    }

    private void __uniform(UniformDeclaration declaration, int value) {
        if (declaration == null) return;
        glUniform1i(declaration.location, value);
    }

    private void __uniform(UniformDeclaration declaration, int[] value) {
        if (declaration == null) return;
        glUniform1iv(declaration.location, value);
    }

    private void __uniform(UniformDeclaration declaration, float value) {
        if (declaration == null) return;
        glUniform1f(declaration.location, value);
    }

    private void __uniform(UniformDeclaration declaration, float[] value) {
        if (declaration == null) return;
        glUniform1fv(declaration.location, value);
    }

    private void __uniform(UniformDeclaration declaration, Vec2 value) {
        if (declaration == null) return;
        glUniform2f(declaration.location, value.x, value.y);
    }

    private void __uniform(UniformDeclaration declaration, Vec3 value) {
        if (declaration == null) return;
        glUniform3f(declaration.location, value.x, value.y, value.z);
    }

    private void __uniform(UniformDeclaration declaration, Vec4 value) {
        if (declaration == null) return;
        glUniform4f(declaration.location, value.x, value.y, value.z, value.w);
    }

    public void __uniform(UniformDeclaration declaration, Matrix3 matrix) {
        if (declaration == null) return;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(9);
            buffer.put(matrix.elements());
            buffer.flip();
            glUniformMatrix3fv(declaration.location, false, buffer);
        }
    }

    public void __uniform(UniformDeclaration declaration, Matrix4 matrix) {
        if (declaration == null) return;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            buffer.put(matrix.elements());
            buffer.flip();
            glUniformMatrix4fv(declaration.location, false, buffer);
        }
    }

    public boolean validate(VertexAttribute[] vertexAttributes) {
        if(vertexAttributes.length != attributes.length)
            return false;
        for(int i = 0; i < attributes.length; i++)
            if(!attributes[i].validate(vertexAttributes[i]))
                return false;
        return true;
    }

    public boolean equals(Object obj) {
        return obj instanceof Shader && this.program == ((Shader) obj).program;
    }
}