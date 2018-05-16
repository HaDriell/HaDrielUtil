package fr.hadriel.asset.graphics.ui.containers;

import fr.hadriel.asset.graphics.ui.Align;
import fr.hadriel.asset.graphics.ui.UIContainer;
import fr.hadriel.asset.graphics.ui.UIElement;
import fr.hadriel.math.Mathf;
import fr.hadriel.math.Vec2;

import java.util.Iterator;

public class VBox extends UIContainer {

    private Align align;

    public VBox(UIElement... elements) {
        this.align = Align.TOP_LEFT;
        for (UIElement element : elements) {
            add(element);
        }
    }

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
        invalidate();
    }

    protected void onLayout() {
        float width = 0;
        float height = 0;

        Iterator<UIElement> it = children();
        while (it.hasNext()) {
            UIElement element = it.next();
            if (!element.isEnabled()) continue;
            Vec2 size = element.getSize();
            width = Mathf.max(width, size.x); // width is not growing. Just compute max
            height += size.y;
        }

        Vec2 position = getPosition();
        float xoffset = 0;
        float yoffset = 0;
        it = children(); // reset Iterator
        while (it.hasNext()) {
            UIElement element = it.next();
            Vec2 elementSize = element.getSize();

            switch (align) {
                case TOP_LEFT:
                case MID_LEFT:
                case BOT_LEFT:
                    xoffset = 0;
                    break;

                case TOP_RIGHT:
                case MID_RIGHT:
                case BOT_RIGHT:
                    xoffset = width - elementSize.x;
                    break;

                case TOP_CENTER:
                case MID_CENTER:
                case BOT_CENTER:
                    xoffset = (width - elementSize.x) / 2;
                    break;
            }

            element.setPosition(position.x + xoffset, position.y + yoffset);
            yoffset += elementSize.y;
        }
        setSize(width, height);
    }
}