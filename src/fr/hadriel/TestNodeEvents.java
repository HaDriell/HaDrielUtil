package fr.hadriel;

import fr.hadriel.event.Node;
import fr.hadriel.lwjgl.g2d.events.MouseMovedEvent;

/**
 * Created by gauti on 05/02/2017.
 */
public class TestNodeEvents {

    public static final class NamedNode extends Node {
        public final String name;
        public NamedNode(String name) {
            this.name = name;
        }

        public String toString() {
            return "Node(" + name + ")";
        }
    }

    public static void main(String[] args) {
        Node root = new NamedNode("root");
        Node a = new NamedNode("a");
        Node b = new NamedNode("b");
        Node c = new NamedNode("c");
        Node d = new NamedNode("d");
        Node e = new NamedNode("e");
        e.setActive(false);


        root.add(a);
        root.add(b);

        a.add(c);

        b.add(d);
        b.add(e);
        root.onEvent(new MouseMovedEvent(0, 0, false));
    }
}
