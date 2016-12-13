package fr.hadriel.hgl.opengl;

/**
 * Created by HaDriel setOn 08/12/2016.
 */
public class TextureRegion {

    public final Texture2D texture;
    public final float[] u;
    public final float[] v;

    public TextureRegion(Texture2D texture2D, float x, float y, float width, float height) {
        this.texture = texture2D;
        u = new float[]{
                x,
                x + width,
                x + width,
                x
        };
        v = new float[]{
                y,
                y,
                y + height,
                y + height
        };
    }
}