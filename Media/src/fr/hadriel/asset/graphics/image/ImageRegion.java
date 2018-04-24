package fr.hadriel.asset.graphics.image;

import fr.hadriel.math.Vec2;
import fr.hadriel.opengl.Texture2D;

public class ImageRegion {

    public final Texture2D texture;
    public final Vec2 uv0;
    public final Vec2 uv1;
    public final Vec2 uv2;
    public final Vec2 uv3;

    public ImageRegion(Texture2D texture, Vec2 uv0, Vec2 uv1, Vec2 uv2, Vec2 uv3) {
        this.texture = texture;
        this.uv0 = uv0;
        this.uv1 = uv1;
        this.uv2 = uv2;
        this.uv3 = uv3;
    }

    public ImageRegion(Texture2D texture, int x, int y, int width, int height) {
        this(texture,
                new Vec2(x / (float) texture.width,             y / (float) texture.height),
                new Vec2((x + width) / (float) texture.width,   y / (float) texture.height),
                new Vec2((x + width) / (float) texture.width,   (y + height) / (float) texture.height),
                new Vec2(x / (float) texture.width,             (y + height) / (float) texture.height)
        );
    }

    @Override
    public String toString() {
        return String.format("ImageRegion(%d) [%s, %s, %s, %s]", texture.handle, uv0, uv1, uv2, uv3);
    }
}