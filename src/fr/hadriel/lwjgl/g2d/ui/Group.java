package fr.hadriel.lwjgl.g2d.ui;

import fr.hadriel.events.MouseMovedEvent;
import fr.hadriel.events.MousePressedEvent;
import fr.hadriel.events.MouseReleasedEvent;
import fr.hadriel.lwjgl.g2d.BatchGraphics;
import fr.hadriel.math.Transform;
import fr.hadriel.math.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere setOn 13/12/2016.
 */
public class Group extends Node {

    private final Transform transform;
    private final List<Node> children;

    public Group(Node... nodes) {
        this.transform = new Transform();
        this.children = new ArrayList<>();

        //Init children
        for(Node node : nodes) {
            children.add(node);
        }

        //Forward to all children any event until handled
        setOnDefault((event) -> {
            for (int i = children.size() - 1; i >= 0; i--) {
                if (children.get(i).onEvent(event)) return true;
            }
            return false;
        });

        setOn(MousePressedEvent.class, (event) -> {
            Vec2 point = transform.transform(event.x, event.y);
            return onDefault(new MousePressedEvent(point, event.button));
        });

        setOn(MouseReleasedEvent.class, (event) -> {
            Vec2 point = transform.transform(event.x, event.y);
            return onDefault(new MouseReleasedEvent(point, event.button));
        });

        setOn(MouseMovedEvent.class, (event) -> {
            Vec2 point = transform.transform(event.x, event.y);
            return onDefault(new MouseMovedEvent(point, event.dragged));
        });
    }

    public Transform getTransform() {
        return transform;
    }

    public void add(Node node) {
        if(children.contains(node)) return;
        children.add(node);
    }

    public void remove(Node node) {
        children.remove(node);
    }

    public void clear() {
        children.clear();
    }

    public void onRender(BatchGraphics g) {
        g.push(transform.getMatrix());
        for(Node child : children) {
            child.onRender(g);
        }
        g.pop();
    }
}