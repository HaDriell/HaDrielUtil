package fr.hadriel.hgl.opengl;

import fr.hadriel.math.Matrix4f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec3;
import fr.hadriel.math.Vec4;
import fr.hadriel.util.ArrayMap;
import fr.hadriel.util.IOUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere setOn 29/11/2016.
 */
public class Shader {

    private final int handle;
    private Map<String, Integer> locationCache;

    public Shader(String vertexSource, String fragmentSource) {
        handle = compileShader(vertexSource, fragmentSource);
        locationCache = new HashMap<>();
    }

    public Shader(File vertexFile, File fragmentStream) throws IOException {
        this(IOUtils.readFileAsString(vertexFile), IOUtils.readFileAsString(fragmentStream));
    }

    public Shader(InputStream vertexStream, InputStream fragmentStream) {
        this(IOUtils.readStreamAsString(vertexStream), IOUtils.readStreamAsString(fragmentStream));
    }

    private int compileShader(String vertexSource, String fragmentSource) {
        locationCache = new HashMap<>();
        int handle = glCreateProgram();
        int vertex = glCreateShader(GL_VERTEX_SHADER);
        int fragment = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertex, vertexSource);
        glShaderSource(fragment, fragmentSource);

        glCompileShader(vertex);
        if(glGetShaderi(vertex, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile vertex Shader");
            System.err.println(glGetShaderInfoLog(vertex));
            System.out.println("OpenGL Version : " + glGetString(GL_VERSION));
            System.exit(1);
        }

        glCompileShader(fragment);
        if(glGetShaderi(fragment, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile fragment Shader");
            System.err.println(glGetShaderInfoLog(fragment));
            System.out.println("OpenGL Version : " + glGetString(GL_VERSION));
            System.exit(1);
        }

        glAttachShader(handle, vertex);
        glAttachShader(handle, fragment);
        glLinkProgram(handle);
        glValidateProgram(handle);

        glDeleteShader(vertex);
        glDeleteShader(fragment);
        return handle;
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

    public void setUniformMat4f(String name, Matrix4f matrix) {
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }
}