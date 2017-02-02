package fr.hadriel.util;

import java.util.*;

/**
 * Created by HaDriel on 19/10/2016.
 */
public class ArrayMap<K, V> extends AbstractMap<K, V> {

    private List<Map.Entry<K, V>> entries = new ArrayList<>();

    static class Node<Key, Value> implements Map.Entry<Key, Value> {
        private final Key key;
        private Value value;

        public Node(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        public Key getKey() {
            return key;
        }

        public Value getValue() {
            return value;
        }

        public Value setValue(Value nvalue) {
            Value old = value;
            value = nvalue;
            return old;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }

    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<Entry<K, V>>() {
            public Iterator<Entry<K, V>> iterator() {
                return entries.iterator();
            }

            public int size() {
                return entries.size();
            }
        };
    }

    private Map.Entry<K, V> getEntry(K key) {
        for(Map.Entry<K, V> entry : entries) {
            if(entry.getKey() != null && entry.getKey().equals(key)) {
                return entry;
            }
        }
        return null;
    }

    public V put(K key, V value) {
        V old = null;
        Map.Entry<K, V> entry = getEntry(key);
        if(entry != null) {
            old = entry.setValue(value);
        } else {
            entries.add(new Node<>(key, value));
        }
        return old;
    }
}