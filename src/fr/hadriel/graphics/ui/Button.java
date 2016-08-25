package fr.hadriel.graphics.ui;

import fr.hadriel.events.input.MousePressedEvent;
import fr.hadriel.events.input.MouseReleasedEvent;
import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.NinePatch;
import fr.hadriel.graphics.Text;
import fr.hadriel.graphics.Texture;
import fr.hadriel.math.Vec2;
import fr.hadriel.util.Property;

/**
 * Created by glathuiliere on 10/08/2016.
 */
public class Button extends Widget {

    private boolean pressed;

    private Text text;
    private Property<String> textProperty;

    private Property<NinePatch> onIdlePatchProperty;
    private Property<NinePatch> onHoveredPatchProperty;
    private Property<NinePatch> onPressedPatchProperty;

    private Texture onIdleTexture;
    private Texture onHoveredTexture;
    private Texture onPressedTexture;

    public Button(String text) {
        this.text = new Text(text);
        this.textProperty = new Property<>(text);
        this.textProperty.addCallback(this.text::setText);

        this.onIdlePatchProperty = new Property<>(null, UIDefaults.DEFAULT_IDLE_PATCH);
        this.onHoveredPatchProperty = new Property<>(null, UIDefaults.DEFAULT_HOVERED_PATCH);
        this.onPressedPatchProperty = new Property<>(null, UIDefaults.DEFAULT_PRESSED_PATCH);

        //on patch change, regenerate the texture
        this.onIdlePatchProperty.addCallback((patch) -> onIdleTexture = patch.createTexture(sizeProperty.get()));
        this.onHoveredPatchProperty.addCallback((patch) -> onHoveredTexture = patch.createTexture(sizeProperty.get()));
        this.onPressedPatchProperty.addCallback((patch) -> onPressedTexture = patch.createTexture(sizeProperty.get()));

        //on size change, regenerate the texture
        super.sizeProperty.addCallback((size) -> onIdleTexture = onIdlePatchProperty.get().createTexture(size));
        super.sizeProperty.addCallback((size) -> onHoveredTexture = onHoveredPatchProperty.get().createTexture(size));
        super.sizeProperty.addCallback((size) -> onPressedTexture = onPressedPatchProperty.get().createTexture(size));

        Vec2 textSize = this.text.getSize().copy().add(20, 20);
        sizeProperty.set(textSize);
    }

    public String getText() {
        return textProperty.get();
    }

    public void setText(String text) {
        textProperty.set(text);
    }

    public void setOnIdleTexture(Texture texture) {
        setOnIdleTexture(texture, 0, 0, 0, 0);
    }

    public void setOnIdleTexture(Texture texture, int top, int left, int bottom, int right) {
        onIdlePatchProperty.set(new NinePatch(texture, top, left, bottom, right));
    }

    public void setOnHoveredTexture(Texture texture) {
        setOnIdleTexture(texture, 0, 0, 0, 0);
    }

    public void setOnHoveredTexture(Texture texture, int top, int left, int bottom, int right) {
        onHoveredPatchProperty.set(new NinePatch(texture, top, left, bottom, right));
    }

    public void setOnPressedTexture(Texture texture) {
        setOnIdleTexture(texture, 0, 0, 0, 0);
    }

    public void setOnPressedTexture(Texture texture, int top, int left, int bottom, int right) {
        onPressedPatchProperty.set(new NinePatch(texture, top, left, bottom, right));
    }

    public void onMouseExited() {
        pressed = false;
    }

    public boolean onMousePressed(MousePressedEvent event) {
        pressed = true;
        return true;
    }

    public boolean onMouseReleased(MouseReleasedEvent event) {
        if(hovered && pressed) {
            onAction();
        }
        pressed = false;
        return true;
    }

    protected void onAction() {}

    public void onRender(HLGraphics g) {
        Texture texture = null;
        if(pressed && onPressedTexture != null) texture = onPressedTexture;
        else if(hovered && onHoveredTexture != null) texture = onHoveredTexture;
        else if(onIdleTexture != null) texture = onIdleTexture;

        if(texture != null) {
            texture.renderStretched(g, sizeProperty.get());
        }

        Vec2 tr = sizeProperty.get().copy().sub(text.getSize()).scale(.5f, .5f);

        g.translate(tr.x, tr.y);
        text.render(g);
        g.translate(-tr.x, -tr.y);
    }
}