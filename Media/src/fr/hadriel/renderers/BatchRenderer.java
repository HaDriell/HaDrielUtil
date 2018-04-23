package fr.hadriel.renderers;

public class BatchRenderer {
    private static final byte NOTHING = 0b00;
    private static final byte SPRITES = 0b01;
    private static final byte FONTS   = 0b10;

    private FontBatchRenderer fonts;
    private SpriteBatchRenderer sprites;
    private byte rendering;

    public BatchRenderer() {
        this.fonts = new FontBatchRenderer();
        this.sprites = new SpriteBatchRenderer();
        this.rendering = NOTHING;
    }

    public void setProjection(float left, float right, float top, float bottom) {
        fonts.setProjection(left, right, top, bottom);
        sprites.setProjection(left, right, top, bottom);
    }

    public void beginFonts() {
        if (rendering == FONTS) return;
        if (rendering == SPRITES) sprites.end(); // flush batch
        rendering = FONTS;
        fonts.begin();
    }

    public void beginSprites() {
        if (rendering == SPRITES) return;
        if (rendering == FONTS) fonts.end();
        rendering = SPRITES;
        sprites.begin();
    }

    public void end() {
        if (rendering == NOTHING) return;
        if (rendering == SPRITES) sprites.end();
        if (rendering == FONTS) fonts.end();
        rendering = NOTHING;
    }
}