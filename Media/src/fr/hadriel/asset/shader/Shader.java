package fr.hadriel.asset.shader;

import fr.hadriel.asset.Asset;
import fr.hadriel.asset.AssetManager;
import fr.hadriel.math.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by gauti on 02/03/2018.
 */
public class Shader extends Asset {

    private final String filename;
    private Map<String, Integer> locations;

    private int program;

    public Shader(String filename) {
        this.filename = filename;
        this.locations = new HashMap<>();
    }

    protected void onLoad(AssetManager manager) {
        StringBuilder vs = new StringBuilder();
        StringBuilder fs = new StringBuilder();
        StringBuilder buffer = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)))) {
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
        } catch (IOException e) {
            throw new RuntimeException("Unable to load Shader : ", e);
        }
        program = CreateProgram(vs.toString(), fs.toString());
    }

    protected void onUnload(AssetManager manager) {
        glDeleteProgram(program);
    }

    private static int CompileShader(int shaderType, String source) {
        int shader = glCreateShader(shaderType);
        glShaderSource(shader, source);
        glCompileShader(shader);
        if(glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            glDeleteShader(shader);
            shader = -1;
            System.err.println("Shader Compilation Failed");
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

    public void bind() {
        glUseProgram(program);
    }

    public void unbind() {
        glUseProgram(0);
    }

    private int getLocation(String name) {
        return -1;
    }

    public void setUniform(String name, int a) {
        glUniform1i(getLocation(name), a);
    }

    public void setUniform(String name, int a, int b) {
        glUniform2i(getLocation(name), a, b);
    }

    public void setUniform(String name, int a, int b, int c) {
        glUniform3i(getLocation(name), a, b, c);
    }

    public void setUniform(String name, int a, int b, int c, int d) {
        glUniform4i(getLocation(name), a, b, c, d);
    }

    public void setUniform(String name, float a) {
        glUniform1f(getLocation(name), a);
    }

    public void setUniform(String name, float a, float b) {
        glUniform2f(getLocation(name), a, b);
    }

    public void setUniform(String name, float a, float b, float c) {
        glUniform3f(getLocation(name), a, b, c);
    }

    public void setUniform(String name, float a, float b, float c, float d) {
        glUniform4f(getLocation(name), a, b, c, d);
    }

    public void setUniform(String name, Vec2 vec2) {
        setUniform(name, vec2.x, vec2.y);
    }

    public void setUniform(String name, Vec3 vec3) {
        setUniform(name, vec3.x, vec3.y, vec3.z);
    }

    public void setUniform(String name, Vec4 vec4) {
        setUniform(name, vec4.x, vec4.y, vec4.z, vec4.w);
    }

    public void setUniform(String name, Matrix matrix) {
        glUniformMatrix3fv(getLocation(name), false, matrix.elements());
    }

    public void setUniform(String name, int[] values) {
        glUniform1iv(getLocation(name), values);
    }

    public void setUniform(String name, Matrix4f matrix) {
        glUniformMatrix4fv(getLocation("name"), false, matrix.elements);
    }

}