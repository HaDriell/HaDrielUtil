package fr.hadriel.asset.graphics.image;

import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec2;
import fr.hadriel.opengl.Texture2D;

public class Sprite {

    public float rotation;
    public Vec2 position = Vec2.ZERO;
    public Vec2 anchor = new Vec2(0.5f, 0.5f);
    public Vec2 scale = new Vec2(1, 1);

    public final Texture2D texture;
    public final int x, y, width, height;

    public Sprite(Texture2D texture, int x, int y, int width, int height) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
