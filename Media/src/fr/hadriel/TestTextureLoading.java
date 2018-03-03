package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.application.WindowHint;
import fr.hadriel.asset.shader.Shader;
import fr.hadriel.asset.texture.Texture;
import fr.hadriel.opengl.*;
import fr.hadriel.renderers.IRenderer;

/**
 * Created by gauti on 02/03/2018.
 */
public class TestTextureLoading extends Application {

    Shader shader;
    Texture texture;
    IndexBuffer indexBuffer;
    SingleBufferVertexArray vertexArray;
    RenderState renderState;

    protected void start(String[] args) {
        texture = assets.loadTexture("texture", "Media/res/heart.jpg");
        shader = assets.loadShader("shader", "Media/res/shader.glsl");
        vertexArray = new SingleBufferVertexArray(400,
                new AttribPointer("i_position", GLType.FLOAT, 2),
                new AttribPointer("i_color", GLType.FLOAT, 4),
                new AttribPointer("i_uv", GLType.FLOAT, 2)
                );
        indexBuffer = new IndexBuffer(400, GLType.UINT);
        renderState = new RenderState();
    }

    protected void update(float delta) {
        VertexBuffer vertexBuffer = vertexArray.getBuffer(0);
        vertexBuffer.bind();
        vertexBuffer.map();
        vertexBuffer.write(-1f).write(+1f)    .write(1f).write(1f).write(1f).write(1f)    .write(0f).write(0f);
        vertexBuffer.write(+1f).write(+1f)    .write(1f).write(1f).write(1f).write(1f)    .write(1f).write(0f);
        vertexBuffer.write(+1f).write(-1f)    .write(1f).write(1f).write(1f).write(1f)    .write(1f).write(1f);
        vertexBuffer.write(-1f).write(-1f)    .write(1f).write(1f).write(1f).write(1f)    .write(0f).write(1f);
        vertexBuffer.unmap();

        indexBuffer.bind();
        indexBuffer.map();
        indexBuffer.write(0, 1, 2, 2, 3, 0);
        indexBuffer.unmap();

        shader.bind();
        texture.bind();
        shader.setUniform("u_texture", 0);
        renderState.setBlending(true);
        renderState.setCullingFrontFace();
        IRenderer.DrawCall(shader, vertexArray, indexBuffer, renderState);
    }

    protected void terminate() {
        assets.unload("texture");
        assets.unload("shader");
    }

    public static void main(String[] args) {
        WindowHint hint = new WindowHint();
        hint.visible = true;
        launch(new TestTextureLoading(), hint);
    }
}