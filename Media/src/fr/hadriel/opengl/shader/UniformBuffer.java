package fr.hadriel.opengl.shader;

import fr.hadriel.math.*;

import java.util.HashMap;
import java.util.Map;

public class UniformBuffer {
    private final Map<String, Object> values;

    public UniformBuffer() {
        this.values = new HashMap<>();
    }

    public void uniform(String name, Object value) {
        values.put(name, value);
    }

    public void apply(Shader shader) {
        values.forEach((name, value) -> {
            if (value instanceof Integer) shader.uniform(name, (int) value);
            if (value instanceof Float) shader.uniform(name, (float) value);

            if (value instanceof float[]) shader.uniform(name, (float[]) value);
            if (value instanceof int[]) shader.uniform(name, (int[]) value);

            if (value instanceof Vec2) shader.uniform(name, (Vec2) value);
            if (value instanceof Vec3) shader.uniform(name, (Vec3) value);
            if (value instanceof Vec4) shader.uniform(name, (Vec4) value);

            if (value instanceof Matrix3) shader.uniform(name, (Matrix3) value);
            if (value instanceof Matrix4) shader.uniform(name, (Matrix4) value);
        });
    }

    //Compare the maps values
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof UniformBuffer && ((UniformBuffer) obj).values.equals(values);
    }
}