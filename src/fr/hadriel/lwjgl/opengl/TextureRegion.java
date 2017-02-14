package fr.hadriel.lwjgl.opengl;

/**
 * Created by HaDriel on 08/12/2016.
 */
public class TextureRegion {

    public final Texture texture;
    public final float width;
    public final float height;
    public final float[] u;
    public final float[] v;

    public TextureRegion(Texture texture, float x, float y, float width, float height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
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

        //Discrete UV [0 : 1]
        for(int i = 0; i < 4; i++) {
            u[i] /= texture.width;
            v[i] /= texture.height;
        }
    }
}