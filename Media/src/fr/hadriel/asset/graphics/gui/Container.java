package fr.hadriel.asset.graphics.gui;

import fr.hadriel.event.IEvent;
import fr.hadriel.math.Vec2;
import fr.hadriel.renderers.BatchGraphics;

import java.util.ArrayList;
import java.util.List;

public class Container extends Component {

    private List<Component> children;

    public Container() {
        this.children = new ArrayList<>();
    }

    public List<Component> getChildren() {
        return children;
    }

    @Override
    public Component hit(float x, float y) {
        Vec2 local = getTransform().multiplyInverse(x, y);
        for (int i = children.size() - 1; i > 0; i--) {
            Component hit = children.get(i).hit(local);
            if (hit != null) {
                return hit;
            }
        }
        return null;
    }

    @Override
    public IEvent onEvent(IEvent event) {
        if (event == null)
            return null;
        for (int i = children.size() - 1; i > 0; i--) {
            event = children.get(i).onEvent(event);
            if (event == null) {
                break;
            }
        }
        return event;
    }

    @Override
    public void onRender(BatchGraphics graphics) {
        for (Component child : children) {
            child.render(graphics);
        }
    }
}