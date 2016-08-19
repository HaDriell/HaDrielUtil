package fr.hadriel.serialization.json;

import fr.hadriel.serialization.JsPrimitive;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by glathuiliere on 19/08/2016.
 */
public class JsObject implements JsonType, Iterable<Map.Entry<String, JsonType>> {

    private Map<String, JsonType> members;

    public JsObject() {
        this.members = new HashMap<>();
    }

    public void put(String name, Object obj) {
        if(name == null)
            throw new IllegalArgumentException("null member key not allowed");
        Json.checkJsonValidType(obj);
        members.put(name, JsonType.class.isInstance(obj) ? (JsonType) obj : new JsPrimitive(obj));
    }

    public void remove(String name) {
        members.remove(name);
    }

    public JsonType get(String name) {
        return members.get(name);
    }

    public Iterator<Map.Entry<String, JsonType>> iterator() {
        return members.entrySet().iterator();
    }

    public String toString() {
        String out = "{";
        boolean firstStatement = true;
        for(Map.Entry<String, JsonType> member : members.entrySet()) {
            if(firstStatement)
                firstStatement = false;
            else
                out +=",";
            out += '"' + member.getKey() + "\":" + member.getValue().toString();
        }
        out += "}";
        return out;
    }
}
