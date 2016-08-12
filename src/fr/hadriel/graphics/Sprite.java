package fr.hadriel.graphics;

import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public abstract class Sprite implements HLRenderable {

    public final Vec2 anchor;
    public final Vec2 position;
    public final Vec2 scale;
    public float rotation;

    protected Sprite() {
        this.anchor = new Vec2(.5f, .5f);
        this.position = new Vec2();
        this.scale = new Vec2(1, 1);
        this.rotation = 0f;
    }

    public void render(HLGraphics g) {
        Vec2 anchorOffset = anchor.copy().scale(getWidth(), getHeight());
        Vec2 translation = position.copy().sub(anchorOffset);
        Matrix3f matrix = new Matrix3f();
        matrix.setToTransform(scale.x, scale.y, rotation, translation.x, translation.y);
        g.pushTransform(matrix);
        onRender(g);
        g.popTransform();
    }

    public abstract float getWidth();

    public abstract float getHeight();

    public abstract void onRender(HLGraphics g);
}