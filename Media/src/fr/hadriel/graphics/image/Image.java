package fr.hadriel.graphics.image;

import fr.hadriel.asset.Asset;
import fr.hadriel.asset.AssetManager;
import fr.hadriel.math.Vec2;
import fr.hadriel.opengl.Texture2D;

public class Image extends Asset {

    private final String filename;

    private Texture2D texture;

    public Image(String filename) {
        this.filename = filename;
    }

    protected void onLoad(AssetManager manager) {
        ImageFile image = new ImageFile(filename);
        texture = new Texture2D(image.width, image.height, image.pixels);
    }

    public Sprite getSprite() {
        return getSprite(0, 0, texture.width, texture.height);
    }

    public Sprite getSprite(int x, int y, int width, int height) {
        float left = x / texture.width;
        float bottom = (y + height) / texture.height;
        float right = (x + width) / texture.width;
        float top = y / texture.height;
        return getSprite(new Vec2(left, top), new Vec2(right, top), new Vec2(right, bottom), new Vec2(left, bottom));
    }

    public Sprite getSprite(Vec2 uv0, Vec2 uv1, Vec2 uv2, Vec2 uv3) {
        return new Sprite(texture, uv0, uv1, uv2, uv3);
    }

    protected void onUnload(AssetManager manager) {
        texture.destroy();
    }
}