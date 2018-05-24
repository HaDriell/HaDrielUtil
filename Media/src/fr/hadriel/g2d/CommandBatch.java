package fr.hadriel.g2d;

import fr.hadriel.opengl.shader.Shader;
import fr.hadriel.opengl.shader.UniformBuffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandBatch implements Iterable<Command> {

    public final Shader shader;
    public final UniformBuffer uniformBuffer;
    private final List<Command> queue;

    public CommandBatch(Shader shader, UniformBuffer uniformBuffer) {
        this.shader = shader;
        this.uniformBuffer = uniformBuffer;
        this.queue = new ArrayList<>();
    }

    public boolean accept(UniformBuffer uniformBuffer) {
        return this.uniformBuffer.equals(uniformBuffer);
    }

    public void submit(Command command) {
        queue.add(command);
    }

    public Iterator<Command> iterator() {
        return queue.iterator();
    }
}