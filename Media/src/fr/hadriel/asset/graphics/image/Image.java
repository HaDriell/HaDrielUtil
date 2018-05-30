package fr.hadriel.asset.graphics.image;

import fr.hadriel.asset.Asset;
import fr.hadriel.asset.AssetManager;
import fr.hadriel.math.Vec2;
import fr.hadriel.opengl.Texture2D;

import java.nio.ByteBuffer;
import java.nio.file.Path;

public class Image extends Asset {

    private Texture2D texture;

    protected void onLoad(AssetManager manager, Path path, ByteBuffer fileContent) {
        ImageFile image = new ImageFile(fileContent);
        texture = new Texture2D(image.width, image.height, image.pixels);
    }

    public Texture2D texture() {
        return texture;
    }

    public int width() {
        return texture.width;
    }

    public int height() {
        return texture.height;
    }

    public ImageRegion getRegion() {
        return getRegion(0, 0, texture.width, texture.height);
    }

    public ImageRegion getRegion(int x, int y, int width, int height) {
        return new ImageRegion(texture, x, y, width, height);
    }

    protected void onUnload(AssetManager manager) {
        texture.destroy();
    }
}