package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.math.Vec2;
import fr.hadriel.opengl.GLType;
import fr.hadriel.opengl.VertexArray;
import fr.hadriel.opengl.VertexAttribute;
import fr.hadriel.opengl.VertexBuffer;
import fr.hadriel.opengl.shader.Shader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.InputStream;

public class TestShader extends Application {

    private static final VertexAttribute[] LAYOUT = {
            new VertexAttribute("i_position", GLType.FLOAT, 2)
    };

    Shader shader;
    VertexArray vertexArray;

    protected void start(String[] args) {
        InputStream shaderSource = TestShader.class.getResourceAsStream("testShader.glsl");
        shader = Shader.GLSL(shaderSource);
        vertexArray = new VertexArray(6, LAYOUT);
        System.out.println("Shader ~ VAO validation : " + shader.validate(LAYOUT));

        VertexBuffer vbo = vertexArray.getBuffer();
        vbo.bind().map();
        vbo.write(new Vec2(0, 0));
        vbo.write(new Vec2(1, 0));
        vbo.write(new Vec2(1, 1));

        vbo.write(new Vec2(1, 1));
        vbo.write(new Vec2(0, 1));
        vbo.write(new Vec2(0, 0));
        vbo.unmap().unbind();
    }

    protected void update(float delta) {
        shader.bind();
        vertexArray.bind();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexArray.getMaxElementCount());
    }

    protected void terminate() {

    }

    public static void main(String[] args) {
        launch(new TestShader());
    }
}