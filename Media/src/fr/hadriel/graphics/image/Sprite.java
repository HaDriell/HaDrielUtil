package fr.hadriel.graphics.image;

import fr.hadriel.math.Vec2;
import fr.hadriel.opengl.Texture2D;

public class Sprite {

    public final Texture2D texture;
    public final Vec2 uv0;
    public final Vec2 uv1;
    public final Vec2 uv2;
    public final Vec2 uv3;

    public Sprite(Texture2D texture, Vec2 uv0, Vec2 uv1, Vec2 uv2, Vec2 uv3) {
        this.texture = texture;
        this.uv0 = uv0;
        this.uv1 = uv1;
        this.uv2 = uv2;
        this.uv3 = uv3;
    }

    public Sprite(Texture2D texture, int x, int y, int width, int height) {
        this(texture,
                new Vec2(x / texture.width,             y / texture.height),
                new Vec2((x + width) / texture.width,   y / texture.height),
                new Vec2((x + width) / texture.width,   (y + height) / texture.height),
                new Vec2(x / texture.width,             (y + height) / texture.height)
        );
    }
}