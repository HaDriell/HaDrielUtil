package fr.hadriel.opengl.shader;


import fr.hadriel.math.*;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

final class Uniform {

    public final String name;
    public final int location;
    public final GLSLType type;
    public Object value;

    public Uniform(String name, int location, GLSLType type) {
        this(name, location, type, null);
    }

    public Uniform(String name, int location, GLSLType type, Object value) {
        this.name = name;
        this.location = location;
        this.type = type;
        this.value = value;
    }

    void setup() {
        if(value == null)
            return;

        else if(value instanceof Integer) setup((int) value);
        else if(value instanceof Float) setup((float) value);

        else if(value instanceof int[]) setup((int[]) value);
        else if(value instanceof float[]) setup((float[]) value);

        else if(value instanceof Vec2) setup((Vec2) value);
        else if(value instanceof Vec3) setup((Vec3) value);
        else if(value instanceof Vec4) setup((Vec4) value);

        else if(value instanceof Matrix3) setup((Matrix3) value);
        else if(value instanceof Matrix4f) setup((Matrix4f) value);
    }


    private void setup(int value) {
        glUniform1i(location, value);
    }

    private void setup(float value) {
        glUniform1f(location, value);
    }

    private void setup(int[] values) {
        glUniform1iv(location, values);
    }

    private void setup(float[] values) {
        glUniform1fv(location, values);
    }

    private void setup(Vec2 v) {
        glUniform2f(location, v.x, v.y);
    }

    private void setup(Vec3 v) {
        glUniform3f(location, v.x, v.y, v.z);
    }

    private void setup(Vec4 v) {
        glUniform4f(location, v.x, v.y, v.z, v.w);
    }

    private void setup(Matrix3 matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(9);
            buffer.put(matrix.elements());
            buffer.flip();
            glUniformMatrix3fv(location, false, buffer);
        }
    }

    private void setup(Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            buffer.put(matrix.elements);
            buffer.flip();
            glUniformMatrix4fv(location, false, buffer);
        }
    }
}