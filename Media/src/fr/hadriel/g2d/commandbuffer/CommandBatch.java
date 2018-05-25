package fr.hadriel.g2d.commandbuffer;

import fr.hadriel.opengl.shader.Shader;
import fr.hadriel.opengl.shader.UniformBuffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommandBatch implements Iterable<Command> {

    private final UniformBuffer uniformBuffer;
    private final List<Command> commands;

    public CommandBatch(Shader shader, Command... commands) {
        this(shader.createUniformBuffer(), commands);
    }

    public CommandBatch(UniformBuffer uniformBuffer, Command... commands) {
        this(uniformBuffer, Arrays.asList(commands));
    }

    public CommandBatch(Shader shader, List<Command> commands) {
        this(shader.createUniformBuffer(), commands);
    }

    public CommandBatch(UniformBuffer uniformBuffer, List<Command> commands) {
        this.uniformBuffer = uniformBuffer;
        this.commands = new ArrayList<>(commands);
    }

    public void add(Command command) {
        commands.add(command);
    }

    public boolean isCompatible(CommandBatch batch) {
        return this != batch && batch.uniformBuffer.equals(uniformBuffer);
    }

    public boolean merge(CommandBatch batch) {
        if (!isCompatible(batch)) return false;
        commands.addAll(batch.commands);
        return true;
    }

    public void uniform(String name, Object value) {
        uniformBuffer.uniform(name, value);
    }

    public void setUniforms(Shader shader) {
        uniformBuffer.setupUniforms(shader);
    }

    public Iterator<Command> iterator() {
        return commands.iterator();
    }
}