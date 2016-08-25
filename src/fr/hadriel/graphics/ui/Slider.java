package fr.hadriel.graphics.ui;

import fr.hadriel.events.input.MouseMovedEvent;
import fr.hadriel.events.input.MousePressedEvent;
import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.NinePatch;
import fr.hadriel.graphics.Texture;
import fr.hadriel.math.Mathf;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;
import fr.hadriel.util.Property;

/**
 * Created by glathuiliere on 12/08/2016.
 */
public class Slider extends ProgressBar {


    private Property<NinePatch> buttonPatchProperty;

    private Property<Float> buttonWidthProperty;

    private Texture buttonTexture;

    public Slider(int width, int height) {
        this(0f, width, height);
    }

    public Slider(float value, int width, int height) {
        super(value, width, height);
        this.buttonWidthProperty = new Property<>(0.2f); // relative to the slider's full size

        this.buttonPatchProperty = new Property<>(null, UIDefaults.DEFAULT_SLIDER_BUTTON_PATCH);

        this.buttonPatchProperty.addCallback((patch) -> buttonTexture = patch.createTexture(getButtonWidth(), getButtonHeight()));

        super.sizeProperty.addCallback((size) -> buttonTexture = buttonPatchProperty.get().createTexture(getButtonWidth(), getButtonHeight()));
        setSize(width, height);
    }

    private void changeValue(float x) {
        float value = Mathf.sqrt(x * x) / (sizeProperty.get().x - buttonTexture.getWidth() / 2);
        System.out.println(value);
        setValue(value);
    }

    public boolean onMousePressed(MousePressedEvent event) {
        Vec2 point = new Vec2(event.x, event.y);
        transform.transform(point);
        changeValue(point.x);
        return true;
    }

    public boolean onMouseMoved(MouseMovedEvent event) {
        if(event.dragged){
            Vec2 point = new Vec2(event.x, event.y);
            transform.transform(point);
            changeValue(point.x);
        }
        return true;
    }

    private int getButtonWidth() {
        return (int) (sizeProperty.get().x * buttonWidthProperty.get());
    }

    private int getButtonHeight() {
        return (int) sizeProperty.get().y;
    }

    public void onRender(HLGraphics graphics) {
        super.onRender(graphics);
        graphics.pushTransform(Matrix3f.Translation((sizeProperty.get().x - buttonTexture.getWidth()) * getValue(), 0));
        buttonTexture.render(graphics);
        graphics.popTransform();
    }
}