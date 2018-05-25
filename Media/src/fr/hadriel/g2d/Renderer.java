package fr.hadriel.g2d;

import fr.hadriel.g2d.commandbuffer.Command;
import fr.hadriel.g2d.commandbuffer.CommandBatch;
import fr.hadriel.g2d.commandbuffer.CommandBuffer;
import fr.hadriel.opengl.*;
import fr.hadriel.opengl.shader.Shader;
import fr.hadriel.opengl.shader.UniformBuffer;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;
import java.util.List;

public final class Renderer {
    public static final int DEFAULT_VERTEX_CAPACITY = 60_000;

    public static final VertexAttribute[] VERTEX_LAYOUT = {
            new VertexAttribute("position", GLType.FLOAT, 2),
            new VertexAttribute("color", GLType.FLOAT, 4),
            new VertexAttribute("uv", GLType.FLOAT, 2)
    };

    private CommandBuffer commandBuffer;
    private VertexArray vertexArray;

    public Renderer() {
        this(DEFAULT_VERTEX_CAPACITY);
    }

    public Renderer(int maxVertexCapacity) {
        this.vertexArray = new VertexArray(maxVertexCapacity, VERTEX_LAYOUT);
        this.commandBuffer = new CommandBuffer();
    }

    //Staging functions
    public void begin() {
        commandBuffer.clear();
    }

    public void submit(Shader shader, UniformBuffer uniformBuffer, List<Command> commands) {
        commandBuffer.submit(shader, uniformBuffer, commands);
    }

    public void submit(Shader shader, UniformBuffer uniformBuffer, Command... commands) {
        commandBuffer.submit(shader, uniformBuffer, commands);
    }

    public void submit(Shader shader, CommandBatch batch) {
        commandBuffer.submit(shader, batch);
    }

    public void end() {
        vertexArray.bind(); // bind VAO
        commandBuffer.forEach(batchList -> {
            Shader shader = batchList.getShader();
            shader.bind(); // bin Shader
            batchList.forEach(batch -> {
                GLBuffer buffer = vertexArray.getBuffer().bind(); // bind VBO
                Iterator<Command> commands = batch.iterator();
                while (commands.hasNext()) {
                    int elementCount = 0;

                    //Edit VBO
                    buffer.map();
                    while (commands.hasNext() && elementCount + 6 < vertexArray.getMaxElementCount()) {
                        Command c = commands.next();

                        //Triangle 1
                        buffer.write(c.transform.multiply(c.position.x, c.position.y))
                                .write(c.color)
                                .write(c.uv[0]);
                        buffer.write(c.transform.multiply(c.position.x + c.size.x, c.position.y))
                                .write(c.color)
                                .write(c.uv[1]);
                        buffer.write(c.transform.multiply(c.position.x + c.size.x, c.position.y + c.size.y))
                                .write(c.color)
                                .write(c.uv[2]);

                        //Triangle 2
                        buffer.write(c.transform.multiply(c.position.x + c.size.x, c.position.y + c.size.y))
                                .write(c.color)
                                .write(c.uv[2]);
                        buffer.write(c.transform.multiply(c.position.x, c.position.y + c.size.y))
                                .write(c.color)
                                .write(c.uv[3]);
                        buffer.write(c.transform.multiply(c.position.x, c.position.y))
                                .write(c.color)
                                .write(c.uv[0]);

                        elementCount += 6;
                    }
                    buffer.unmap();
                    batch.setUniforms(shader); // setup Uniforms
                    GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, elementCount); // Issue Draw Call
                }
            });
        });
    }
}