package fr.hadriel.asset.graphics.gui;

import fr.hadriel.asset.graphics.font.Font;
import fr.hadriel.event.IEvent;
import fr.hadriel.math.Vec4;
import fr.hadriel.renderers.BatchGraphics;

public class Label extends Component {

    public String text;
    public Font font;
    public float size;
    public Vec4 color;

    @Override
    public IEvent onEvent(IEvent event) {
        return event;
    }

    @Override
    public void onRender(BatchGraphics graphics) {
        if (text == null || font == null || size <= 0 || color == null) return;
        graphics.draw(0, 0, text, font, size, color);
    }
}