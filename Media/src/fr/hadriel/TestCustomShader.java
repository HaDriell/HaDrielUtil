package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.graphics.image.Image;
import fr.hadriel.g2d.Renderer;
import fr.hadriel.g2d.commandbuffer.Command;
import fr.hadriel.g2d.commandbuffer.CommandBatch;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Matrix4;
import fr.hadriel.opengl.shader.Shader;

public class TestCustomShader extends Application {

    private Shader shader;
    private Renderer renderer;
    private Image teron;

    private float u_time;

    protected void start(String[] args) {
        teron = manager.load("Media/res/Teron Fielsang.png", Image.class);
        renderer = new Renderer();
        shader = Shader.GLSL(getClass().getResourceAsStream("customShader.glsl"));
    }

    protected void update(float delta) {
        u_time += delta;
        teron.texture().bind(); // setupUniforms the defaults

        CommandBatch batch = new CommandBatch();
        batch.add(Matrix3.Translation(400 - 32, 225 - 32), 0, 0, 64, 64);

        batch.setUniform("u_projection", Matrix4.Orthographic(0, 800, 0, 450, 1000, 0));
        batch.setUniform("u_time", u_time);
        batch.setUniform("u_texture", 0);

        renderer.begin();
        renderer.submit(shader, batch);
        renderer.end();
    }

    protected void terminate() {

    }

    public static void main(String... args) {
        launch(new TestCustomShader());
    }
}