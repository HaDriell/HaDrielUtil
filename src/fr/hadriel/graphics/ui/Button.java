package fr.hadriel.graphics.ui;

import fr.hadriel.events.MousePressedEvent;
import fr.hadriel.events.MouseReleasedEvent;
import fr.hadriel.graphics.HLGraphics;

/**
 * Created by glathuiliere on 10/08/2016.
 */
public class Button extends Widget {

    public float paddingTop;
    public float paddingLeft;
    public float paddingBottom;
    public float paddingRight;

    private boolean pressed;

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
    }
}