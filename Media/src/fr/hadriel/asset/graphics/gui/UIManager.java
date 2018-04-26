package fr.hadriel.asset.graphics.gui;


import fr.hadriel.application.event.FocusGainEvent;
import fr.hadriel.application.event.FocusLostEvent;
import fr.hadriel.application.event.MouseEvent;
import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventListener;
import fr.hadriel.renderers.BatchGraphics;

public class UIManager implements IEventListener {

    private Component root;
    private Component focus;

    public void setRoot(Component root) {
        this.root = root;
        this.focus = null; // Invalidate focus.
    }

    public Component getRoot() {
        return root;
    }

    public void requestFocus(Component focus) {
        if (this.focus == focus) return; // no changes
        if (this.focus != null) this.focus.onEvent(new FocusLostEvent());
        this.focus = focus;
        if (this.focus != null) this.focus.onEvent(new FocusGainEvent());
    }

    public Component getFocus() {
        return focus;
    }

    public IEvent onEvent(IEvent event) {
        //Focus may change
        if (root != null) {
            if (MouseEvent.class.isInstance(event)) {
                MouseEvent mouseEvent = (MouseEvent) event;
                requestFocus(root.hit(mouseEvent.x, mouseEvent.y));
            }
            Component target = focus != null ? focus : root;
            target.onEvent(event); // consume event anyway
        }
        return null;
    }

    public void render(BatchGraphics graphics) {
        if (root != null) root.render(graphics);
    }
}