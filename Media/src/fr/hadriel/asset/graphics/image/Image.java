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
        float left = x / (float) texture.width;
        float bottom = (y + height) / (float) texture.height;
        float right = (x + width) / (float) texture.width;
        float top = y / (float) texture.height;
        return getRegion(new Vec2(left, top), new Vec2(right, top), new Vec2(right, bottom), new Vec2(left, bottom));
    }

    public ImageRegion getRegion(Vec2 uv0, Vec2 uv1, Vec2 uv2, Vec2 uv3) {
        return new ImageRegion(texture, uv0, uv1, uv2, uv3);
    }

    protected void onUnload(AssetManager manager) {
        texture.destroy();
    }
}