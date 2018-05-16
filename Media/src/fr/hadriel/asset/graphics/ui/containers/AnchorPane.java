package fr.hadriel.asset.graphics.ui.containers;

import fr.hadriel.asset.graphics.ui.UIContainer;
import fr.hadriel.asset.graphics.ui.UIElement;
import fr.hadriel.math.Vec2;

import java.util.Iterator;

public class AnchorPane extends UIContainer {

    protected void onLayout() {
        float width = 0;
        float height = 0;

        Iterator<UIElement> children = children();
        while (children.hasNext()) {
            Vec2 size = children.next().getSize();
            if (width < size.x) width = size.x;
            if (height < size.y) height = size.y;
        }
        setSize(width, height);
    }
}