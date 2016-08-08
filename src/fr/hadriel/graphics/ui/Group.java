package fr.hadriel.graphics.ui;

import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventListener;
import fr.hadriel.events.*;
import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.math.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class Group extends Widget implements IEventListener {

    private List<Widget> widgets;

    public Group() {
        this.widgets = new ArrayList<>();
    }

    public void add(Widget widget) {
        widgets.add(widget);
    }

    public void remove(Widget widget) {
        widgets.remove(widget);
    }

    public void clear() {
        widgets.clear();
    }

    public void onEvent(IEvent event) {
        IEvent.dispatch(event, MousePressedEvent.class, this::onMousePressed);
        IEvent.dispatch(event, MouseReleasedEvent.class, this::onMouseReleased);
        IEvent.dispatch(event, MouseMovedEvent.class, this::onMouseMoved);
        IEvent.dispatch(event, KeyPressedEvent.class, this::onKeyPressed);
        IEvent.dispatch(event, KeyReleasedEvent.class, this::onKeyReleased);
    }

    public boolean onKeyPressed(KeyPressedEvent event) {
        for(Widget widget : widgets)
            if(widget.enabled && widget.onKeyPressed(event))
                return true;
        return false;
    }

    public boolean onKeyReleased(KeyReleasedEvent event) {
        for(Widget widget : widgets)
            if(widget.enabled && widget.onKeyReleased(event))
                return true;
        return false;
    }

    public boolean onMouseMoved(MouseMovedEvent event) {
        Vec2 mouse = transformInverse.transform(new Vec2(event.x, event.y));
        for(Widget widget : widgets)
            if(widget.enabled && widget.hit(mouse) && widget.onMouseMoved(event))
                return true;
        return false;
    }

    public boolean onMousePressed(MousePressedEvent event) {
        Vec2 mouse = transformInverse.transform(new Vec2(event.x, event.y));
        for(Widget widget : widgets)
            if(widget.enabled && widget.hit(mouse) && widget.onMousePressed(event))
                return true;
        return false;
    }
    
    public boolean onMouseReleased(MouseReleasedEvent event) {
        Vec2 mouse = transformInverse.transform(new Vec2(event.x, event.y));
        for(Widget widget : widgets)
            if(widget.enabled && widget.hit(mouse) && widget.onMouseReleased(event))
                return true;
        return false;
    }

    public void onUpdate(float delta) {
        for(Widget widget : widgets) {
            if(widget.enabled)
                widget.onUpdate(delta);
        }
    }

    public void onRender(HLGraphics graphics) {
        for(Widget widget : widgets) {
            if(widget.enabled)
                widget.render(graphics);
        }
    }
}