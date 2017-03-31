package fr.hadriel.serialization.js;

import fr.hadriel.serialization.SerialException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by HaDriel on 19/10/2016.
 */
public class JsObject implements JsPrimitive, Iterable<Map.Entry<String, JsPrimitive>>{

    private Map<String, JsPrimitive> members;

    public JsObject() {
        this.members = new HashMap<>();
    }

    public void clear() {
        members.clear();
    }

    public void put(String name, JsPrimitive value) {
        members.put(name, value == null ? new JsNull() : value);
    }

    public void put(String name, String value) {
        put(name, new JsString(value));
    }

    public void put(String name, double value) {
        put(name, new JsNumber(value));
    }

    public void remove(String name) {
        members.remove(name);
    }

    public Iterator<Map.Entry<String, JsPrimitive>> iterator() {
        return members.entrySet().iterator();
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
        sb.append('{');
        for(Map.Entry<String, JsPrimitive> member : members.entrySet()) {
            if(firstStatement) firstStatement = false;
            else sb.append(',');
            sb.append('"').append(member.getKey()).append('"').append(':').append(member.getValue().toString());
        }
        sb.append('}');
        return sb.toString();
    }

    public String toString() {
        return asString();
    }

    public static JsObject deserialize(String input) throws SerialException {
        JsObject object = new JsObject();
        List<String> elements = Json.splitJson(input);
        for(String pair : elements) {
            String[] member = Json.splitPair(pair);
            object.put(member[0], Json.deserialize(member[1]));
        }
        return object;
    }
}
