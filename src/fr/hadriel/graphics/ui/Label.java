package fr.hadriel.graphics.ui;

import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.Text;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 10/08/2016.
 */
public class Label extends Widget {

    private Text text;

    public Label(String text) {
        this.text = new Text(text);
    }

    private void syncSize() {
        Vec2 size = text.getSize();
        setSize(size.x, size.y);
    }

    public void onRender(HLGraphics graphics) {
        text.render(graphics);
    }

    public Text getText() {
        return text;
    }
}