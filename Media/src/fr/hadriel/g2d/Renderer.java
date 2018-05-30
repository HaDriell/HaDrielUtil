package fr.hadriel.g2d;

import fr.hadriel.g2d.commandbuffer.Command;
import fr.hadriel.g2d.commandbuffer.CommandBatch;
import fr.hadriel.g2d.commandbuffer.CommandBuffer;
import fr.hadriel.opengl.*;
import fr.hadriel.opengl.shader.Shader;

import java.util.Iterator;

import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public final class Renderer {
    public static final int DEFAULT_VERTEX_CAPACITY = 60_000;
    public static final int DEFAULT_VERTEX_ARRAY_COUNT = 2;

    public static final VertexAttribute[] VERTEX_LAYOUT = {
            new VertexAttribute("position", GLType.FLOAT, 2),
            new VertexAttribute("color", GLType.FLOAT, 4),
            new VertexAttribute("uv", GLType.FLOAT, 2)
    };

    private CommandBuffer commandBuffer;
    private VertexArray[] vertexArrays;
    private int vertexArrayIndex;

    public Renderer() {
        this(DEFAULT_VERTEX_CAPACITY, DEFAULT_VERTEX_ARRAY_COUNT);
    }

    public Renderer(int maxVertexCapacity, int vertexArrayCount) {
        this.commandBuffer = new CommandBuffer();
        this.vertexArrays = new VertexArray[vertexArrayCount];
        for (int i = 0; i < vertexArrays.length; i++) {
            vertexArrays[i] = new VertexArray(maxVertexCapacity, VERTEX_LAYOUT);
        }
        this.vertexArrayIndex = 0;
    }

    private VertexArray nextVertexArray() {
        vertexArrayIndex = (vertexArrayIndex + 1) % vertexArrays.length;
        return vertexArrays[vertexArrayIndex];
    }

    //Staging functions
    public void begin() {
        commandBuffer.clear();
    }

    public void submit(Shader shader, CommandBatch batch) {
        commandBuffer.submit(shader, batch);
    }

    public void end() {
        commandBuffer.forEach(batchList -> {
            Shader shader = batchList.getShader();
            shader.bind(); // bin Shader
            batchList.forEach(batch -> {
                Iterator<Command> commands = batch.iterator();
                while (commands.hasNext()) {
                    VertexArray vertexArray = nextVertexArray();
                    vertexArray.bind(); // bind VAO
                    GLBuffer buffer = vertexArray.getBuffer().bind(); // bind VBO

                    //Map VBO for edition
                    buffer.map();
                    int elementCount = 0;
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
                    batch.setupUniforms(shader); // setup Uniforms
                    glDrawArrays(GL_TRIANGLES, 0, elementCount); // Issue Draw Call
                }
            });
        });
    }
}