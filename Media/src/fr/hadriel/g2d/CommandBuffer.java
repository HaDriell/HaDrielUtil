package fr.hadriel.g2d;

import fr.hadriel.math.Matrix3;
import fr.hadriel.opengl.shader.Shader;
import fr.hadriel.opengl.shader.UniformBuffer;

import java.util.ArrayList;
import java.util.List;

public class CommandBuffer {

    private final List<CommandBatch> batches;

    public CommandBuffer() {
        batches = new ArrayList<>();
    }

    public void clear() {
        batches.clear();
    }

    private CommandBatch getOrCreateBatch(Shader shader, UniformBuffer uniformBuffer) {
        CommandBatch batch = batches.stream()
                .filter(b -> b.shader.equals(shader))
                .filter(b -> b.uniformBuffer.equals(uniformBuffer))
                .findFirst()
                .orElse(null);

        if (batch == null) {
            batch = new CommandBatch(shader, uniformBuffer);
            batches.add(batch);
        }

        return batch;
    }

    public void submit(Matrix3 transform, Sprite sprite, Shader shader, UniformBuffer uniformBuffer) {
        getOrCreateBatch(shader, uniformBuffer).submit(new Command(transform, sprite));
    }
}