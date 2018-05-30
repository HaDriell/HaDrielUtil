package fr.hadriel.g2d.commandbuffer;

import fr.hadriel.math.*;
import fr.hadriel.opengl.Texture2D;
import fr.hadriel.opengl.shader.Shader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandBatch implements Iterable<Command> {

    private final UniformBuffer uniforms;
    private final List<Command> commands;

    public CommandBatch() {
        this.uniforms = new UniformBuffer();
        this.commands = new ArrayList<>();
    }

    public void add(Command command) {
        commands.add(command);
    }

    public void add(Matrix3 transform, float x, float y, float width, float height) {
        add(transform, x, y, width, height, Vec4.XYZW);
    }

    public void add(Matrix3 transform, float x, float y, float width, float height, Vec4 color) {
        add(new Command(transform, x, y, width, height, color));
    }

    public void setupUniforms(Shader shader) { uniforms.setupUniforms(shader); }
    public void removeUniform(String name) { uniforms.removeUniform(name); }
    public void setUniform(String name, int value) { uniforms.setUniform(name, value); }
    public void setUniform(String name, float value) { uniforms.setUniform(name, value); }
    public void setUniform(String name, Vec2 value) { uniforms.setUniform(name, value); }
    public void setUniform(String name, Vec3 value) { uniforms.setUniform(name, value); }
    public void setUniform(String name, Vec4 value) { uniforms.setUniform(name, value); }
    public void setUniform(String name, Matrix3 value) { uniforms.setUniform(name, value); }
    public void setUniform(String name, Matrix4 value) { uniforms.setUniform(name, value); }
    public void setUniform(String name, Texture2D value) { uniforms.setUniform(name, value); }

    public boolean isCompatible(CommandBatch batch) {
        return this != batch && batch.uniforms.equals(uniforms);
    }

    public boolean merge(CommandBatch batch) {
        if (!isCompatible(batch)) return false;
        commands.addAll(batch.commands);
        return true;
    }

    public int size() {
        return commands.size();
    }

    public Iterator<Command> iterator() {
        return commands.iterator();
    }
}