package fr.hadriel.g2d.commandbuffer;

import fr.hadriel.opengl.shader.Shader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandBatchList implements Iterable<CommandBatch> {
    private final Shader shader;
    private final List<CommandBatch> batches;

    public CommandBatchList(Shader shader) {
        this.shader = shader;
        this.batches = new ArrayList<>();
    }

    public void add(CommandBatch batch) {
        //Try to merge first
        for (CommandBatch b : this)
            if (b.merge(batch))
                return;
        //If merge failed, add the Batch to the list
        batches.add(batch);
    }

    public Shader getShader() {
        return shader;
    }

    public Iterator<CommandBatch> iterator() {
        return batches.iterator();
    }
}
