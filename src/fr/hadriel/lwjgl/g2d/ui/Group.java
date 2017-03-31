package fr.hadriel.lwjgl.g2d.ui;


import fr.hadriel.event.IEvent;
import fr.hadriel.lwjgl.g2d.BatchGraphics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 07/02/2017.
 */
public class Group extends Widget {

    private List<Widget> widgets;

    public Group() {
//        this.interceptor.clear(); // clear the default events filtering (causes Group to forward events blindly)
        this.widgets = new ArrayList<>();
    }

    public void add(Widget widget) {
        widget.setParent(this);
    }

    public void remove(Widget widget) {
        if(widgets.contains(widget))
            widget.setParent(null);
    }

    public void clear() {
        widgets.clear();
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

    protected void onRender(BatchGraphics g) {
        for(Widget w : widgets)
            w.render(g);
    }

    public void onEvent(IEvent IEvent) {
        if(!isActive()) return;
//        if(IEvent.isConsumed()) return;
//        if(!interceptor.accept(IEvent)) return;
        for(int i = widgets.size() - 1; i >= 0; i--) {
//            if(IEvent.isConsumed()) return;
            widgets.get(i).onEvent(IEvent);
        }
//        if(IEvent.isConsumed()) return;
//        if(IEvent.isListenable()) listener.onEvent(IEvent);
    }
}