package fr.hadriel.graphics.ui;

import fr.hadriel.events.MouseMovedEvent;
import fr.hadriel.events.MousePressedEvent;
import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.Texture;
import fr.hadriel.math.Mathf;
import fr.hadriel.math.Matrix3f;

/**
 * Created by glathuiliere on 12/08/2016.
 */
public class Slider extends Widget {

    private float value;
    private Texture background;
    private Texture button;


    public Slider(float value, Texture background, Texture button) {
        this.value = value;
        this.background = background;
        this.button = button;
        setSize(background.getWidth(), background.getHeight());
    }

    public Slider(float value) {
        this(value, UIDefaults.DEFAULT_SLIDER_BACKGROUND, UIDefaults.DEFAULT_SLIDER_BUTTON);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = Mathf.clamp(0, 1, value);
    }

    private void changeValue(float x, float y) {
        float buttonWidth = getButtonScale() * button.getWidth();
        float distance = Mathf.sqrt(x * x + y * y) - buttonWidth / 2;
        float internalWidth = size.x - buttonWidth;
        setValue(distance / internalWidth);
    }

    public boolean onMouseMoved(MouseMovedEvent event) {
        if(event.dragged) {
            changeValue(event.x, event.y);
        }
        return true;
    }

    public boolean onMousePressed(MousePressedEvent event) {
        changeValue(event.x, event.y);
        return true;
    }

    private float getButtonScale() {
        return size.y / button.getHeight();
    }

    private float getInternalWidth() {
        return size.x - button.getWidth() * getButtonScale();
    }

    public void onRender(HLGraphics g) {
        background.renderStretched(g, (int) size.x, (int) size.y);
        float scale = getButtonScale();
        float scaledHeight = button.getHeight() * scale;
        float scaledWidth = button.getWidth() * scale;

        float trX = value * (size.x - scaledWidth);
        g.pushTransform(Matrix3f.Translation(trX, 0));
        button.renderStretched(g, (int) scaledWidth, (int) scaledHeight);
        g.popTransform();
    }
}