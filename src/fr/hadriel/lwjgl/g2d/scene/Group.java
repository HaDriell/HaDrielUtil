package fr.hadriel.lwjgl.g2d.scene;

import fr.hadriel.lwjgl.g2d.BatchGraphics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 06/02/2017.
 */
public class Group extends Widget {

    private List<Widget> children;

    public Group() {
        this.children = new ArrayList<>();
    }

    public void add(Widget widget) {
        if(children.contains(widget)) return;
        children.add(widget);
        widget.setParent(this);
    }

    public void remove(Widget widget) {
        children.remove(widget);
    }

    public void onRender(BatchGraphics g) {
        for(Widget widget : children) {
            widget.render(g);
        }
    }
}