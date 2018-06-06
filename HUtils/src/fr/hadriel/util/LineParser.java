package fr.hadriel.util;

import java.util.HashMap;
import java.util.Map;

public final class LineParser {
    private String prefix;
    private final Map<String, String> values;

    public LineParser() {
        this.values = new HashMap<>();
    }

    public void parse(String line) {
        int first_space = line.indexOf(" ");
        prefix = line.substring(0, first_space);
        line = line.substring(first_space + 1);
        byte[] buffer = line.getBytes();
        boolean quoted = false;
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] == '"')
                quoted = !quoted;
            if (buffer[i] == ' ' && !quoted)
                buffer[i] = '\n';
        }
        line = new String(buffer);
        for (String pair : line.split("\n")) {
            if (pair.contains("=")) {
                String[] kv = pair.split("=");
                if (kv[1].startsWith("\""))
                    kv[1] = kv[1].substring(1, kv[1].length() - 1);
                values.put(kv[0], kv[1]);
            }
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public String getString(String key) {
        return values.get(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(values.get(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(values.get(key));
    }

    public int[] getInt2(String key) {
        String[] array = values.get(key).split(",");
        return new int[] {
                Integer.parseInt(array[0]),
                Integer.parseInt(array[1])
        };
    }

    public int[] getInt4(String key) {
        String[] array = values.get(key).split(",");
        return new int[] {
                Integer.parseInt(array[0]),
                Integer.parseInt(array[1]),
                Integer.parseInt(array[2]),
                Integer.parseInt(array[3])
        };
    }
}