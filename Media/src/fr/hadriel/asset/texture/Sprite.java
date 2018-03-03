package fr.hadriel.asset.texture;

import fr.hadriel.math.Vec2;

public class Sprite {

    public final Texture texture;
    public final float width;
    public final float height;
    public final Vec2[] uvs;

    public Sprite(Texture texture, float x, float y, float width, float height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.uvs = new Vec2[4];

        float u0 = x / texture.width();
        float u1 = (x + width) / texture.width();
        float v0 = y / texture.height();
        float v1 = (y + height) / texture.height();

        uvs[0] = new Vec2(u0, v0);
        uvs[1] = new Vec2(u1, v0);
        uvs[2] = new Vec2(u1, v1);
        uvs[3] = new Vec2(u0, v1);
    }
}