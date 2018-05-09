package fr.hadriel.asset.graphics.ui;

import java.util.Iterator;
import java.util.List;

public class UIElementIterator implements Iterator<UIElement> {
    private List<UIElement> elements;
    private int end;
    private int index;
    private int step;

    public UIElementIterator(List<UIElement> elements, int begin, int end, int step) {
        this.elements = elements;
        this.index = begin;
        this.end = end;
        this.step = step;
    }

    public boolean hasNext() {
        return index != end;
    }

    public UIElement next() {
        UIElement element = elements.get(index);
        index += step;
        return element;
    }
}
