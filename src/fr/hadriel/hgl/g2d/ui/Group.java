package fr.hadriel.hgl.g2d.ui;

import fr.hadriel.event.IEvent;
import fr.hadriel.hgl.g2d.BatchGraphics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere setOn 13/12/2016.
 */
public class Group extends Node {

    private List<Node> children;

    public Group(Node... nodes) {
        this.children = new ArrayList<>();
        for(Node node : nodes) {
            children.add(node);
        }

        //Basic Group default function
        setOnDefault(this::onForward);
    }

    public void add(Node node) {
        if(children.contains(node)) return;
        children.add(node);
    }

    public void remove(Node node) {
        children.remove(node);
    }

    public void onRender(BatchGraphics g) {
        for(Node child : children) {
            child.onRender(g);
        }
    }

    protected boolean onForward(IEvent event) {
        for (int i = children.size() - 1; i >= 0; i--) {
            if (children.get(i).onEvent(event)) {
                return true;
            }
        }
        return false;
    }
}