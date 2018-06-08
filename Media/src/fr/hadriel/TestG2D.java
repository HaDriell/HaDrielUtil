package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.application.Graphic2D;
import fr.hadriel.io.ImageFile;
import fr.hadriel.g2d.commandbuffer.Renderer;
import fr.hadriel.g2d.commandbuffer.CommandBatch;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Matrix4;
import fr.hadriel.opengl.Framebuffer;
import fr.hadriel.opengl.shader.Shader;
import fr.hadriel.opengl.texture.Texture2D;
import fr.hadriel.opengl.texture.TextureFilter;
import fr.hadriel.opengl.texture.TextureFormat;

public class TestG2D extends Application {

    private Shader textureShader;
    private Texture2D texture;

    private Framebuffer framebuffer;

    protected void start(String[] args) {
        textureShader = Graphic2D.getDefaultTextureShader();

        ImageFile arialImg = new ImageFile("Media/res/Arial1.png");
        ImageFile teronImg = new ImageFile("Media/res/Teron Fielsang.png");

        texture = new Texture2D();
        texture.bind();
        texture.setData(arialImg.width, arialImg.height, arialImg.pixels);
        texture.write(0, 0, teronImg.width, teronImg.height, teronImg.pixels);
        framebuffer = new Framebuffer();
        framebuffer.bind();

        //Color buffer setup
        Texture2D color = new Texture2D();
        color.bind();
        color.setData(128, 128, TextureFormat.RGBA8);
        color.setFilter(TextureFilter.LINEAR, TextureFilter.LINEAR);
        framebuffer.setColorTexture(color);

        Texture2D depth = new Texture2D(); // already bound
        depth.bind();
        depth.setData(128, 128, TextureFormat.DEPTH24);
        depth.setFilter(TextureFilter.LINEAR, TextureFilter.LINEAR);
        framebuffer.setDepthTexture(depth);

        framebuffer.validate();
        framebuffer.unbind();
    }

    protected void update(Renderer renderer, float delta) {

        //Generate data
        int size = 100;
        CommandBatch spriteBatch = new CommandBatch();
        spriteBatch.setUniform("u_projection", Matrix4.Orthographic(0, 800, 0, 450, 1000, 0));
        spriteBatch.setUniform("u_texture", texture);
        for (int x = 0; x < 800; x += size) {
            for (int y = 0; y < 450; y += size) {
                spriteBatch.add(Matrix3.Translation(x, y), 0, 0, size, size);
            }
        }

        //Render pass to Framebuffer
        {
            framebuffer.bind();
            framebuffer.clear();

            //Batch filling
//        logger.info("Sprite Batch size: " + spriteBatch.size());

            renderer.begin();
            renderer.submit(textureShader, spriteBatch);
            renderer.end();

            framebuffer.unbind();
        }

        //Render pass framebuffer to screen
        {
            CommandBatch frameBatch = new CommandBatch();
            frameBatch.setUniform("u_projection", Matrix4.Orthographic(0, 800, 0, 450, 1000, 0));
            frameBatch.setUniform("u_texture", framebuffer.getColorTexture());
            frameBatch.add(Matrix3.Identity, 0, 0, 800, 450);

            renderer.begin();
            renderer.submit(textureShader, frameBatch);
            renderer.end();
        }

        //Render pass directly to screen
        {

        }
    }

    protected void terminate() {

    }

    public static void main(String... args) {
        launch(new TestG2D());
    }
}
