package fr.hadriel.asset.font;

import fr.hadriel.graphics.image.ImageFile;
import fr.hadriel.opengl.Texture2D;
import fr.hadriel.opengl.TextureFilter;

public class FontPage {
    public final int id;
    public final Texture2D texture;

    public FontPage(int id, String filename) {
        this.id = id;
        ImageFile image = new ImageFile(filename);
        this.texture = new Texture2D(image.width, image.height, image.pixels);
        texture.setFilter(TextureFilter.LINEAR, TextureFilter.LINEAR);
    }

    public void unload() {
        texture.destroy();
    }
}