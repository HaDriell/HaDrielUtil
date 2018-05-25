package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.graphics.Graphic2D;
import fr.hadriel.asset.graphics.image.Image;
import fr.hadriel.g2d.Renderer;
import fr.hadriel.g2d.commandbuffer.Command;
import fr.hadriel.g2d.commandbuffer.CommandBatch;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Matrix4;
import fr.hadriel.opengl.shader.Shader;
import fr.hadriel.util.Timer;

public class TestG2D extends Application {

    private Image teron;
    private Renderer renderer;
    private Shader shader;

    private int fpsCount;
    private Timer timer;

    protected void start(String[] args) {
        teron = manager.load("Media/res/Teron Fielsang.png", Image.class);
        renderer = new Renderer();
        shader = Shader.GLSL(Renderer.class.getResourceAsStream("renderer/sprite_shader.glsl"));
        timer = new Timer();
    }

    protected void update(float delta) {
        CommandBatch batch = new CommandBatch(shader);
        batch.uniform("u_projection", Matrix4.Orthographic(0, 800, 0, 450, 1000, 0));
        for (int x = 0; x < 800; x += 40) {
            for (int y = 0; y < 450; y += 50) {
                batch.add(new Command(Matrix3.Translation(x, y), x, y, 40, 50));
            }
        }

        if (timer.elapsed() > 1) {
            timer.reset();
            Graphic2D.setTitle(String.format("Window (%d FPS)", fpsCount));
            fpsCount = 0;
        }
        renderer.begin();
        renderer.submit(shader, batch);
        renderer.end();
        fpsCount++;
    }

    protected void terminate() {

    }

    public static void main(String... args) {
        launch(new TestG2D());
    }
}
