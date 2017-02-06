package fr.hadriel.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gauti on 05/02/2017.
 */
public class Node {

    private DeferredEventFilter filters;
    private DeferredEventListener handlers;

    private Node parent;
    private List<Node> children;

    private boolean active;

    public Node() {
        this.filters = new DeferredEventFilter();
        this.handlers = new DeferredEventListener();
        this.children = new ArrayList<>();
        this.parent = null;
        this.active = true;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setParent(Node parent) {
        if(parent == this) throw new IllegalArgumentException("Tried to add this Node as it's own Parent");
        if(isCycling(parent, this)) throw new IllegalArgumentException("Tried to make a cycle in the Node Tree");
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public void add(Node child) {
        Node oldParent = child.getParent();
        if(oldParent != null) oldParent.remove(child);
        children.add(child);
        child.setParent(this);
    }

    public void remove(Node child) {
        if(children.remove(child))
            child.setParent(null);
    }

    public <T extends Event> void addEventListener(Class<T> type, IEventHandler<T> handler) {
        handlers.addEventHandler(type, handler);
    }

    public <T extends Event> void removeEventListener(Class<T> type, IEventHandler<T> handler) {
        handlers.removeEventHandler(type, handler);
    }

    public <T extends Event> void addEventFilter(Class<T> type, IEventFilter<T> filter) {
        filters.addEventFilter(type, filter);
    }

    public <T extends Event> void removeEventFilter(Class<T> type, IEventFilter<T> filter) {
        filters.removeEventFilter(type, filter);
    }

    public boolean onEvent(Event event) {
        if(isActive()) {
            System.out.println("capturing: " + this);
            if (filters.accept(event)) {
                for (int i = children.size() - 1; i >= 0; i--) {
                    if (children.get(i).onEvent(event)) // break on the first event accepted
                        break;
                }
                System.out.println("listening: " + this);
                handlers.onEvent(event);
            }
        }
        return false;
    }

    //Try to find the child instance as a parent of the future parent. If true then the graph is cycling
    public static boolean isCycling(Node parent, Node child) {
        Node node = parent;
        while(node != null) {
            node = node.parent; // shift through parents
            if(node == child) return true; // one of the Parents of the parent is the child.
        }
        return false;
    }
}
