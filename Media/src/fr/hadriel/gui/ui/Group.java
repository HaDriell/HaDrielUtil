package fr.hadriel.gui.ui;


import fr.hadriel.graphics.IGraphics;
import fr.hadriel.gui.event.UIEvent;

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

    protected void onRender(IGraphics g, float width, float height, UIContext context) {
        for(Widget w : widgets)
            w.draw(g);
    }

    public void invalidate() {
        super.invalidate();
        widgets.forEach(Widget::invalidate); // must forward the invalidation
    }

    public void onEventCapture(UIEvent event) {
        super.onEventCapture(event);

        //When Capturing, fire event to the children while not captured
        if(event.isCapturing()) {
            for (int i = widgets.size() - 1; i >= 0; i--) {
                if (!event.isCapturing())
                    break;
                widgets.get(i).onEvent(event);
            }
        }
    }
}