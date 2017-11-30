package fr.hadriel.graphics.opengl;

import fr.hadriel.math.*;
import fr.hadriel.util.IOUtils;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere on 29/11/2016.
 */
public class Shader {

    private static int CompileShader(int shaderType, String source) {
        int shader = glCreateShader(shaderType);
        glShaderSource(shader, source);
        glCompileShader(shader);
        if(glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            glDeleteShader(shader);
            shader = -1;
        }
        return shader;
    }

    private static int CreateProgram(String vertexSource, String fragmentSource) {
        int program = glCreateProgram();
        int vertex = CompileShader(GL_VERTEX_SHADER, vertexSource);
        int fragment = CompileShader(GL_FRAGMENT_SHADER, fragmentSource);

        //Safely delete both shaders in case of compilation failure of one of them
        if(vertex == -1 && fragment != -1) glDeleteShader(fragment);
        if(vertex != -1 && fragment == -1) glDeleteShader(vertex);

        if(vertex == -1 && fragment == -1) { // compilation failure
            glDeleteProgram(program);
            program = -1;
        } else { // compilation success
            glAttachShader(program, vertex);
            glAttachShader(program, fragment);
            glLinkProgram(program);
            glValidateProgram(program); // well. fuck it if linking / validation fails, i'll handle that later
            glDetachShader(program, vertex);
            glDetachShader(program, fragment);
            glDeleteShader(vertex);
            glDeleteShader(fragment);
        }
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

    private final int program;
    private Map<String, Integer> locationCache;

    public Shader(int program) {
        this.program = program;
        this.locationCache = new HashMap<>();
    }

    public Shader(String vertexSource, String fragmentSource) {
        this(CreateProgram(vertexSource, fragmentSource));
    }

    public Shader(InputStream vertexStream, InputStream fragmentStream) {
        this(IOUtils.readStreamAsString(vertexStream), IOUtils.readStreamAsString(fragmentStream));
    }

    public void bind() {
        glUseProgram(program);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public int getUniform(String name) {
        Integer location = locationCache.get(name);
        if(location != null) return location;
        int result = glGetUniformLocation(program, name);
        if(result == -1) System.err.println("Could not find uniform '" + name + "'");
        else locationCache.put(name, result);
        return result;
    }

    public void setUniform1iv(String name, int[] values) {
        glUniform1iv(getUniform(name), values);
    }

    public void setUniform1i(String name, int value) {
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1f(String name, float value) {
        glUniform1f(getUniform(name), value);
    }

    public void setUniform2f(String name, Vec2 v) {
        setUniform2f(name, v.x, v.y);
    }

    public void setUniform3f(String name, Vec3 v) {
        setUniform3f(name, v.x, v.y, v.z);
    }

    public void setUniform4f(String name, Vec4 v) {
        setUniform4f(name, v.x, v.y, v.z, v.w);
    }

    public void setUniform2f(String name, float x, float y) {
        glUniform2f(getUniform(name), x, y);
    }

    public void setUniform3f(String name, float x, float y, float z) {
        glUniform3f(getUniform(name), x, y, z);
    }

    public void setUniform4f(String name, float x, float y, float z, float w) {
        glUniform4f(getUniform(name), x, y, z, w);
    }

    public void setUniformMat3f(String name, Matrix3f matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(matrix.elements.length);
        buffer.put(matrix.elements);
        buffer.flip();
        glUniformMatrix3fv(getUniform(name), false, buffer);
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(matrix.elements.length);
        buffer.put(matrix.elements);
        buffer.flip();
        glUniformMatrix4fv(getUniform(name), false, buffer);
    }
}