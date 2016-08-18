package fr.hadriel.graphics.ui;

import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.NinePatch;
import fr.hadriel.graphics.Texture;
import fr.hadriel.math.Mathf;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.util.Property;

/**
 * Created by glathuiliere on 18/08/2016.
 */
public class ProgressBar extends Widget {

    private Property<Float> valueProperty;
    private Property<NinePatch> filledPatchProperty;
    private Property<NinePatch> backgroundPatchProperty;

    private Texture filledTexture;
    private Texture backgroundTexture;

    public ProgressBar(int width, int height) {
        this(0f, width, height);
    }

    public ProgressBar(float value, int width, int height) {
        this.valueProperty = new Property<>(value, 0f);
        this.backgroundPatchProperty = new Property<>(null, UIDefaults.DEFAULT_PROGRESSBAR_BACKGROUND_PATCH);
        this.filledPatchProperty = new Property<>(null, UIDefaults.DEFAULT_PROGRESSBAR_FILLED_PATCH);

        this.backgroundPatchProperty.addCallback((patch) -> backgroundTexture = patch.createTexture(sizeProperty.get()));
        this.filledPatchProperty.addCallback((patch) -> filledTexture = patch.createTexture(sizeProperty.get()));

        super.sizeProperty.addCallback((size) -> backgroundTexture = backgroundPatchProperty.get().createTexture(size));
        super.sizeProperty.addCallback((size) -> filledTexture = filledPatchProperty.get().createTexture(size));
        setSize(width, height);
    }

    public float getValue() {
        return valueProperty.get();
    }

    public void setValue(float value) {
        valueProperty.set(Mathf.clamp(0f, 1f, value));
    }

    protected float getInnerWidth() {
        return sizeProperty.get().x - backgroundPatchProperty.get().getLeft() - backgroundPatchProperty.get().getRight();
    }

    protected float getInnerHeight() {
        return sizeProperty.get().y - backgroundPatchProperty.get().getTop() - backgroundPatchProperty.get().getBottom();
    }

    public void onRender(HLGraphics graphics) {
        backgroundTexture.renderStretched(graphics, sizeProperty.get());
        graphics.pushTransform(Matrix3f.Translation(backgroundPatchProperty.get().getLeft(), backgroundPatchProperty.get().getTop()));
        filledTexture.renderRegion(graphics, 0, 0, (int) (getInnerWidth() * valueProperty.get()), (int) getInnerHeight());
        graphics.popTransform();
    }
}