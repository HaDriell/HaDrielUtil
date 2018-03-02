package fr.hadriel.graphics.texture;

import fr.hadriel.math.Vec2;

public class TextureRegion {

    public final Texture2D texture2D;
    public final float width;
    public final float height;
    public final Vec2[] uvs;

    public TextureRegion(Texture2D texture2D, float x, float y, float width, float height) {
        this.texture2D = texture2D;
        this.width = width;
        this.height = height;
        this.uvs = new Vec2[4];

        float u0 = x / texture2D.width;
        float u1 = (x + width) / texture2D.width;
        float v0 = y / texture2D.height;
        float v1 = (y + height) / texture2D.height;

        uvs[0] = new Vec2(u0, v0);
        uvs[1] = new Vec2(u1, v0);
        uvs[2] = new Vec2(u1, v1);
        uvs[3] = new Vec2(u0, v1);
    }
}