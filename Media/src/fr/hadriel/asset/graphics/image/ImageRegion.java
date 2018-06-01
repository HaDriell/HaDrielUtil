package fr.hadriel.asset.graphics.image;

import fr.hadriel.opengl.texture.Texture2D;

public class ImageRegion {

    public final Texture2D texture;
    public final int x, y, width, height;

    public ImageRegion(Texture2D texture, int x, int y, int width, int height) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String toString() {
        return String.format("ImageRegion[tid=%d, x=%d, y=%d, width=%d, height=%d]", texture.handle, x, y, width, height);
    }
}