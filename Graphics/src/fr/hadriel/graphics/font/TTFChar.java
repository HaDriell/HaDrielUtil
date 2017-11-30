package fr.hadriel.graphics.font;

import fr.hadriel.graphics.opengl.Texture;
import fr.hadriel.graphics.opengl.TextureRegion;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 13/02/2017.
 */
public class TTFChar {
    private final Vec2 textureOffset;
    public final Vec2 size;
    public final float advance;

    public TTFChar(float texOffsetX, float texOffsetY, float advance, float width, float height) {
        this.textureOffset = new Vec2(texOffsetX, texOffsetY);
        this.size = new Vec2(width, height);
        this.advance = advance;
    }

    public TextureRegion getRegion(Texture texture) {
        return texture.getRegion(textureOffset.x, textureOffset.y, size.x, size.y);
    }
}