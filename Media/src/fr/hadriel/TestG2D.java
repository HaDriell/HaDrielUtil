package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.graphics.image.ImageFile;
import fr.hadriel.g2d.Renderer;
import fr.hadriel.g2d.commandbuffer.CommandBatch;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Matrix4;
import fr.hadriel.opengl.shader.Shader;
import fr.hadriel.opengl.texture.Texture2D;

public class TestG2D extends Application {

    private Renderer renderer;
    private Shader spriteShader;

    private Texture2D texture2D;

    protected void start(String[] args) {
        renderer = new Renderer();
        spriteShader = Shader.GLSL(Renderer.class.getResourceAsStream("defaults/sprite_shader.glsl"));

        ImageFile arialImg = new ImageFile("Media/res/Arial1.png");

        ImageFile teronImg = new ImageFile("Media/res/Teron Fielsang.png");
        texture2D = new Texture2D(arialImg.width, arialImg.height, arialImg.pixels);
        texture2D.write(0, 0, teronImg.width, teronImg.height, teronImg.pixels);
    }

    protected void update(float delta) {
        renderer.begin();
        //Batch filling
        int size = 800;
        CommandBatch spriteBatch = new CommandBatch();
        spriteBatch.setUniform("u_projection", Matrix4.Orthographic(0, 800, 0, 450, 1000, 0));
        spriteBatch.setUniform("u_texture", texture2D);
        for (int x = 0; x < 800; x += size) {
            for (int y = 0; y < 450; y += size) {
                spriteBatch.add(Matrix3.Translation(x, y), 0, 0, size, size);
            }
        }
        logger.info("Sprite Batch size: " + spriteBatch.size());
        renderer.submit(spriteShader, spriteBatch);
        renderer.end();
    }

    protected void terminate() {

    }

    public static void main(String... args) {
        launch(new TestG2D());
    }
}
