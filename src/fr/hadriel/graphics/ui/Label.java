package fr.hadriel.graphics.ui;

import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.Text;
import fr.hadriel.math.Vec2;
import fr.hadriel.util.Property;

/**
 * Created by glathuiliere on 10/08/2016.
 */
public class Label extends Widget {

    private Property<String> textProperty;
    private Text text;

    public Label(String label) {
        this.textProperty = new Property<>(label);
        this.text = new Text(label);
        this.textProperty.addCallback(text::setText);
    }

    private void syncSize() {
        Vec2 size = text.getSize();
        setSize(size.x, size.y);
    }

    public void onRender(HLGraphics graphics) {
        text.render(graphics);
    }

    public String getText() {
        return textProperty.get();
    }

    public void setText(String text) {
        textProperty.set(text);
    }
}