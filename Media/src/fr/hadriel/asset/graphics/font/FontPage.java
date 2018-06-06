package fr.hadriel.asset.graphics.font;

import fr.hadriel.io.ImageFile;
import fr.hadriel.opengl.texture.Texture2D;
import fr.hadriel.opengl.texture.TextureFilter;
import fr.hadriel.opengl.texture.TextureFormat;

public class FontPage {
    public final int id;
    public final Texture2D texture;

    public FontPage(int id, String filename) {
        this.id = id;
        ImageFile image = new ImageFile(filename);
        this.texture = new Texture2D();

        //setup texture
        texture.bind();
        texture.setData(image.width, image.height, image.pixels, TextureFormat.RGBA8);
        texture.setFilter(TextureFilter.LINEAR, TextureFilter.LINEAR);
    }

    public void unload() {
        texture.destroy();
    }
}