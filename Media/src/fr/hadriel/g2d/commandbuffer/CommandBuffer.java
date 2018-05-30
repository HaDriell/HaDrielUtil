package fr.hadriel.g2d.commandbuffer;

import fr.hadriel.opengl.shader.Shader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandBuffer implements Iterable<CommandBatchList> {

    private final List<CommandBatchList> commandBatchLists;

    public CommandBuffer() {
        this.commandBatchLists = new ArrayList<>();
    }

    private CommandBatchList getCommandBatchList(Shader shader) {
        for (CommandBatchList list : commandBatchLists)
            if (list.getShader().equals(shader))
                return list;
        CommandBatchList list = new CommandBatchList(shader);
        commandBatchLists.add(list);
        return list;
    }

    public void clear() {
        commandBatchLists.clear();
    }

    public void submit(Shader shader, CommandBatch batch) {
        getCommandBatchList(shader).add(batch);
    }

    @Override
    public Iterator<CommandBatchList> iterator() {
        return commandBatchLists.iterator();
    }
}