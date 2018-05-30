package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.graphics.font.Font;
import fr.hadriel.asset.graphics.image.Image;
import fr.hadriel.g2d.Renderer;
import fr.hadriel.g2d.commandbuffer.Command;
import fr.hadriel.g2d.commandbuffer.CommandBatch;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Matrix4;
import fr.hadriel.opengl.shader.Shader;

public class TestG2D extends Application {

    private Font arial;
    private Image teron;
    private Renderer renderer;
    private Shader spriteShader;
    private Shader sdfShader;

    protected void start(String[] args) {
        teron = manager.load("Media/res/Teron Fielsang.png", Image.class);
        arial = manager.load("Media/res/Arial.fnt", Font.class);
        renderer = new Renderer();
        spriteShader = Shader.GLSL(Renderer.class.getResourceAsStream("defaults/sprite_shader.glsl"));
        sdfShader = Shader.GLSL(Renderer.class.getResourceAsStream("defaults/sdf_shader.glsl"));
    }

    protected void update(float delta) {
        renderer.begin();
        //Batch filling
        int size = 16;
        CommandBatch spriteBatch = new CommandBatch();
        spriteBatch.setUniform("u_projection", Matrix4.Orthographic(0, 800, 0, 450, 1000, 0));
        spriteBatch.setUniform("u_texture", teron.texture());
        for (int x = 0; x < 800; x += size) {
            for (int y = 0; y < 450; y += size) {
                spriteBatch.add(new Command(Matrix3.Translation(x, y), 0, 0, size, size));
            }
        }
        logger.info("Sprite Batch size: " + spriteBatch.size());
        renderer.submit(spriteShader, spriteBatch);


//        CommandBatch[] batches = new CommandBatch[arial.common().pages];
//        for (int i = 0; i < batches.length; i++) {
//            batches[i] = new CommandBatch(sdfShader);
//            batches[i].setUniform("u_projection", Matrix4.Orthographic(0, 800, 0, 450, 1000, 0));
//            batches[i].setUniform("u_buffer", 0.5f);
//            batches[i].setUniform("u_gamma", 0.1f);
//            batches[i].setUniform("u_texture", arial.page(i).texture());
//        }
//
//        String text = "Hello, world !";
//        float scale = 32f / arial.info().size;
//        int xadvance = 0;
//        byte[] chars = text.getBytes();
//        for(int i = 0; i < chars.length; i++) {
//            FontChar fc = arial.character(chars[i]);
//            CommandBatch batch = batches[fc.page];
//            int kerning = arial.kerning(chars[i], (i + 1 < chars.length) ? chars[i + 1] : 0);
//            batch.add(new Command(Matrix3.Identity,
//                    (fc.xoffset + xadvance + kerning) * scale,
//                    (fc.yoffset) * scale,
//                    fc.width * scale,
//                    fc.height * scale,
//                    Vec4.XYZW));
//            xadvance += fc.xadvance + kerning - arial.info().paddingLeft - arial.info().paddingRight - arial.info().spacingH;
//        }
//
//        for (CommandBatch batch : batches) {
//            if (batch.size() > 0)
//                renderer.submit(sdfShader, batch);
//        }
        renderer.end();
    }

    protected void terminate() {

    }

    public static void main(String... args) {
        launch(new TestG2D());
    }
}
