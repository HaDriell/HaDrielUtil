package fr.hadriel.graphics.ui;

import fr.hadriel.events.MouseMovedEvent;
import fr.hadriel.events.MousePressedEvent;
import fr.hadriel.events.MouseReleasedEvent;
import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.Text;
import fr.hadriel.graphics.Texture;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 10/08/2016.
 */
public class Button extends Widget {

    private boolean pressed;

    private Text text;
    private Texture idleTexture;
    private Texture onHoveredTexture;
    private Texture onPressedTexture;

    public Button(String text, Texture idleTexture, Texture onHoveredTexture, Texture onPressedTexture) {
        this.text = new Text(text);
        this.idleTexture = idleTexture;
        this.onHoveredTexture = onHoveredTexture;
        this.onPressedTexture = onPressedTexture;
        setSize(idleTexture.getWidth(), idleTexture.getHeight());
    }

    public Button(String text) {
        this(text, UIDefaults.DEFAULT_IDLE_TEXTURE, UIDefaults.DEFAULT_HOVERED_TEXTURE, UIDefaults.DEFAULT_PRESSED_TEXTURE);
        Vec2 textSize = this.text.getSize();
        setSize(textSize.x, textSize.y);
    }

    public Text getText() {
        return text;
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
        else if(idleTexture != null) texture = idleTexture;

        if(texture != null) {
            texture.renderStretched(g, (int) size.x, (int) size.y);
        }

        Vec2 tr = size.copy().sub(text.getSize()).scale(.5f, .5f);

        g.translate(tr.x, tr.y);
        text.render(g);
        g.translate(-tr.x, -tr.y);
    }
}