package fr.hadriel.hgl.graphics;

import fr.hadriel.math.Matrix4f;
import fr.hadriel.util.IOUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere on 29/11/2016.
 */
public class Shader {

    private static Shader bound;

    private static synchronized void bind(Shader shader) {
        glUseProgram(shader == null ? 0 : shader.id);
        bound = shader;
    }

    public static synchronized Shader getBound() {
        return bound;
    }

    private final int id;
    private Map<String, Integer> locationCache;

    public Shader(String vertexSource, String fragmentSource) {
        locationCache = new HashMap<>();
        id = glCreateProgram();
        int vertex = glCreateShader(GL_VERTEX_SHADER);
        int fragment = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertex, vertexSource);
        glShaderSource(fragment, fragmentSource);

        glCompileShader(vertex);
        if(glGetShaderi(vertex, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile vertex Shader");
            System.err.println(glGetShaderInfoLog(vertex));
        }

        glCompileShader(fragment);
        if(glGetShaderi(fragment, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile fragment Shader");
            System.err.println(glGetShaderInfoLog(fragment));
        }

        glAttachShader(id, vertex);
        glAttachShader(id, fragment);
        glLinkProgram(id);
        glValidateProgram(id);

        glDeleteShader(vertex);
        glDeleteShader(fragment);
    }

    public Shader(File vertexFile, File fragmentFile) throws IOException {
        this(IOUtils.readFileAsString(vertexFile),
                IOUtils.readFileAsString(fragmentFile));
    }

    public void bind() {
        bind(this);
    }

    public void unbind() {
        bind(null);
    }

    public boolean bound() {
        return getBound() == this;
    }

    public int getUniform(String name) {
        Integer location = locationCache.get(name);
        if(location != null) return location;
        int result = glGetUniformLocation(id, name);
        if(result == -1) System.err.println("Could not find uniform variable '" + name + "'");
        else locationCache.put(name, result);
        return result;
    }

    public void setUniform1i(String name, int value) {
        if(!bound()) bind();
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1f(String name, float value) {
        if(!bound()) bind();
        glUniform1f(getUniform(name), value);
    }

    public void setUniform2f(String name, float x, float y) {
        if(!bound()) bind();
        glUniform2f(getUniform(name), x, y);
    }

    public void setUniform3f(String name, float x, float y, float z) {
        if(!bound()) bind();
        glUniform3f(getUniform(name), x, y, z);
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {
        if(!bound()) bind();
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }
}