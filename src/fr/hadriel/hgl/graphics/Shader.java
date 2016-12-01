package fr.hadriel.hgl.graphics;

import fr.hadriel.math.Matrix4f;
import fr.hadriel.util.IOUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere on 29/11/2016.
 */
public class Shader {

    private final int handle;
    private Map<String, Integer> locationCache;

    public Shader(String vertexSource, String fragmentSource) {
        locationCache = new HashMap<>();
        handle = glCreateProgram();
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

        glAttachShader(handle, vertex);
        glAttachShader(handle, fragment);
        glLinkProgram(handle);
        glValidateProgram(handle);

        glDeleteShader(vertex);
        glDeleteShader(fragment);
    }

    public Shader(File vertexStream, File fragmentStream) throws IOException {
        this(IOUtils.readFileAsString(vertexStream),
                IOUtils.readFileAsString(fragmentStream));
    }

    public Shader(InputStream vertexStream, InputStream fragmentStream) throws IOException {
        this(IOUtils.readStreamAsString(vertexStream),
                IOUtils.readStreamAsString(fragmentStream));
    }

    public void bind() {
        glUseProgram(handle);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public int getUniform(String name) {
        Integer location = locationCache.get(name);
        if(location != null) return location;
        int result = glGetUniformLocation(handle, name);
        if(result == -1) System.err.println("Could not find uniform variable '" + name + "'");
        else locationCache.put(name, result);
        return result;
    }

    public void setUniform1i(String name, int value) {
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1f(String name, float value) {
        glUniform1f(getUniform(name), value);
    }

    public void setUniform2f(String name, float x, float y) {
        glUniform2f(getUniform(name), x, y);
    }

    public void setUniform3f(String name, float x, float y, float z) {
        glUniform3f(getUniform(name), x, y, z);
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {
        glUniformMatrix4fv(getUniform(name), true, matrix.toFloatBuffer());
    }
}