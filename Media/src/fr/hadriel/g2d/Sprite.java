package fr.hadriel.g2d;

import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import fr.hadriel.opengl.Texture2D;

public class Sprite {
    public final float depth;
    public final Vec2 size;
    public final Vec4 color;
    public final Texture2D texture;
    public final Vec2 uv0;
    public final Vec2 uv1;
    public final Vec2 uv2;
    public final Vec2 uv3;

    //Textureless
    public Sprite(float depth, Vec2 size, Vec4 color) {
        this.depth = depth;
        this.size = size;
        this.color = color;
        this.texture = null;
        this.uv0 = Vec2.ZERO;
        this.uv1 = Vec2.ZERO;
        this.uv2 = Vec2.ZERO;
        this.uv3 = Vec2.ZERO;
    }

    public Sprite(Vec2 size, Vec4 color, Texture2D texture, int xoffset, int yoffset, int width, int height) {
        this(0, size, color, texture, xoffset, yoffset, width, height);
    }

    public Sprite(float depth, Vec2 size, Vec4 color, Texture2D texture, int xoffset, int yoffset, int width, int height) {
        this.depth = depth;
        this.size = size;
        this.color = color;
        this.texture = texture;
        this.uv0 = new Vec2(xoffset / (float) texture.width,           yoffset / (float) texture.height);
        this.uv1 = new Vec2((xoffset + width) / (float) texture.width, yoffset / (float) texture.height);
        this.uv2 = new Vec2((xoffset + width) / (float) texture.width, (yoffset + height) / (float) texture.height);
        this.uv3 = new Vec2(xoffset / (float) texture.width,           (yoffset + height) / (float) texture.height);
    }
}