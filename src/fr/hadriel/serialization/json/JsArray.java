package fr.hadriel.serialization.json;

import fr.hadriel.serialization.JsPrimitive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by glathuiliere on 19/08/2016.
 */
public class JsArray implements JsonType, Iterable<JsonType> {

    private List<JsonType> array;

    public JsArray() {
        this.array = new ArrayList<>();
    }

    public void add(Object object) {
        Json.checkJsonValidType(object);
        array.add(JsonType.class.isInstance(object) ? (JsonType) object : new JsPrimitive(object));
    }

    public void remove(Object object) {
        if (!Json.isJsonValidType(object))
            return;
        if (JsonType.class.isInstance(object)) {
            array.remove(object);
        } else {
            for (int i = 0; i < array.size(); i++) {
                JsonType entry = array.get(i);
                if (JsPrimitive.class.isInstance(entry) && ((JsPrimitive) entry).get() == object) {
                    array.remove(entry);
                    return; // quick exit
                }
            }
        }
    }

    public Iterator<JsonType> iterator() {
        return array.iterator();
    }

    public String toString() {
        String s = "[";
        boolean firstStatement = true;
        for(JsonType entry : array) {
            if(firstStatement)
                firstStatement = false;
            else
                s += ",";
            s += entry.toString();
        }
        s += "]";
        return s;
    }
}