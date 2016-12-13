package fr.hadriel.serialization.json;

import fr.hadriel.serialization.SerialException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by HaDriel setOn 19/10/2016.
 */
public class JsArray implements JsPrimitive2, Iterable<JsPrimitive2> {

    private List<JsPrimitive2> elements;

    public JsArray() {
        this(null);
    }

    public JsArray(Collection<JsPrimitive2> list) {
        this.elements = new ArrayList<>();
        if(list != null)
            this.elements.addAll(list);
    }

    public Iterator<JsPrimitive2> iterator() {
        return elements.iterator();
    }

    public void clear() {
        elements.clear();
    }

    public JsPrimitive2 get(int index) {
        return elements.get(index);
    }

    public void add(JsPrimitive2 primitive) {
        elements.add(primitive);
    }

    public void add(double value) {
        add(new JsNumber(value));
    }

    public void add(String value) {
        add(new JsString(value));
    }

    public void remove(JsPrimitive2 primitive) {
        elements.remove(primitive);
    }

    public void remove(int index) {
        elements.remove(index);
    }

    public byte asByte() {
        throw new UnsupportedOperationException();
    }

    public boolean asBoolean() {
        throw new UnsupportedOperationException();
    }

    public short asShort() {
        throw new UnsupportedOperationException();
    }

    public char asChar() {
        throw new UnsupportedOperationException();
    }

    public int asInt() {
        throw new UnsupportedOperationException();
    }

    public long asLong() {
        throw new UnsupportedOperationException();
    }

    public float asFloat() {
        throw new UnsupportedOperationException();
    }

    public double asDouble() {
        throw new UnsupportedOperationException();
    }

    public String asString() {
        StringBuilder sb = new StringBuilder();
        boolean firstStatement = true;
        sb.append('[');
        for(JsPrimitive2 element : elements) {
            if(firstStatement) firstStatement = false;
            else sb.append(',');
            sb.append(element.toString());
        }
        sb.append(']');
        return sb.toString();
    }

    public String toString() {
        return asString();
    }

    public static JsArray deserialize(String input) throws SerialException {
        List<String> elements = Json.splitJson(input);
        List<JsPrimitive2> list = new ArrayList<>();
        for(String element : elements) {
            list.add(Json.deserialize(element));
        }
        return new JsArray(list);
    }
}
