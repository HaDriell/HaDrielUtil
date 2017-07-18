package fr.hadriel.main.lwjgl.g2d.ui;


import fr.hadriel.main.lwjgl.g2d.BatchGraphics;
import fr.hadriel.main.lwjgl.g2d.event.UIEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 07/02/2017.
 */
public class Group extends Widget {

    private List<Widget> widgets;

    public Group() {
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

    protected void onRender(BatchGraphics g, float width, float height) {
        for(Widget w : widgets)
            w.render(g);
    }

    public void invalidate() {
        super.invalidate();
        widgets.forEach(Widget::invalidate); // must forward the invalidation
    }

    public void onEvent(UIEvent event) {
        super.onEvent(event);
        //When Capturing, fire event to the children while not captured
        for(Widget child : widgets) {
            if(!event.isCapturing())
                break;
            child.onEvent(event);
        }
    }
}