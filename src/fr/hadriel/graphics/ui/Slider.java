package fr.hadriel.graphics.ui;

import fr.hadriel.events.MouseMovedEvent;
import fr.hadriel.events.MousePressedEvent;
import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.NinePatch;
import fr.hadriel.graphics.Texture;
import fr.hadriel.math.Mathf;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.util.Property;

/**
 * Created by glathuiliere on 12/08/2016.
 */
public class Slider extends Widget {

    /*
    TODO : rework Slider so there is way more customisability:
    TODO : background
    TODO : button (independent in size !)
    TODO : left "fill"
    TODO : right "fill"
    TODO : render method
    */

    private Property<Float> valueProperty;
    private Property<NinePatch> buttonPatchProperty;
    private Property<NinePatch> backgroundPatchProperty;

    private Texture buttonTexture;
    private Texture backgroundTexture;

    public Slider(float value, int width, int height) {
        this.valueProperty = new Property<>(value);
        setSize(width, height);
        this.buttonPatchProperty = new Property<>(null, UIDefaults.DEFAULT_SLIDER_BUTTON_PATCH);
        this.backgroundPatchProperty = new Property<>(null, UIDefaults.DEFAULT_SLIDER_BACKGROUND_PATCH);

        this.buttonPatchProperty.addCallback((patch) -> buttonTexture = patch.createTexture(sizeProperty.get()));
        this.backgroundPatchProperty.addCallback((patch) -> buttonTexture = patch.createTexture(sizeProperty.get()));
        super.sizeProperty.addCallback((size) -> backgroundTexture = backgroundPatchProperty.get().createTexture(size));

        this.buttonTexture = buttonPatchProperty.get().createTexture((int) sizeProperty.get().y, (int) sizeProperty.get().y);
        this.backgroundTexture = backgroundPatchProperty.get().createTexture(sizeProperty.get());
    }

    public Slider(int width, int height) {
        this(0.5f, width, height);
    }

    public float getValue() {
        return valueProperty.get();
    }

    public void setValue(float value) {
        valueProperty.set(Mathf.clamp(0, 1, value));
    }

    private void changeValue(float positionFromLeft) {
        float buttonWidth = getButtonScale() * buttonTexture.getWidth();
        float distance = Mathf.sqrt(positionFromLeft * positionFromLeft) - buttonWidth / 2;
        float internalWidth = sizeProperty.get().x - buttonWidth;
        setValue(distance / internalWidth);
    }

    public boolean onMouseMoved(MouseMovedEvent event) {
        if(event.dragged) {
            changeValue(event.x);
        }
        return true;
    }

    public boolean onMousePressed(MousePressedEvent event) {
        changeValue(event.x);
        return true;
    }

    private float getButtonScale() {
        return sizeProperty.get().y / buttonTexture.getHeight();
    }

    private float getInternalWidth() {
        return sizeProperty.get().x - buttonTexture.getWidth() * getButtonScale();
    }

    public void onRender(HLGraphics g) {
        backgroundTexture.renderStretched(g, sizeProperty.get());
        float scale = getButtonScale();
        float scaledHeight = buttonTexture.getHeight() * scale;
        float scaledWidth = buttonTexture.getWidth() * scale;

        float trX = valueProperty.get() * (sizeProperty.get().x - scaledWidth);
        g.pushTransform(Matrix3f.Translation(trX, 0));
        buttonTexture.renderStretched(g, (int) scaledWidth, (int) scaledHeight);
        g.popTransform();
    }
}