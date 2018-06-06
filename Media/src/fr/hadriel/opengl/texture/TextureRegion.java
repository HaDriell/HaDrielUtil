package fr.hadriel.opengl.texture;

public class TextureRegion {

    public final Texture2D texture;
    public final int x, y, width, height;

    public TextureRegion(Texture2D texture, int x, int y, int width, int height) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String toString() {
        return String.format("TextureRegion[tid=%d, x=%d, y=%d, width=%d, height=%d]", texture.handle, x, y, width, height);
    }
}