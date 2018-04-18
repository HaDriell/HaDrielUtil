package fr.hadriel.graphics.animation;

public class AnimationFrame {
    public final int id;
    public final int x;
    public final int y;
    public final int width;
    public final int height;

    public AnimationFrame(int id, int x, int y, int width, int height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}