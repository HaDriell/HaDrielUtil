package fr.hadriel.asset.graphics.animation;

import fr.hadriel.asset.Asset;
import fr.hadriel.asset.AssetManager;
import fr.hadriel.asset.graphics.image.Image;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Arrays;

public class Animation extends Asset {

    private Image image;
    private AnimationFrame[] frames;

    protected void onLoad(AssetManager manager, Path path, ByteBuffer fileContent) {
        image = null;
        frames = null;

        byte[] data = new byte[fileContent.remaining()];
        fileContent.get(data, 0, data.length);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)))) {
            in.lines().forEach(line -> {
                String[] words = line.trim().split("\\s+");
                String[] args = Arrays.copyOfRange(words, 1, words.length);
                switch (words[0]) {
                    case "info":
                        parseInfo(args, path, manager);
                        break;
                    case "frames":
                        parseFrames(args);
                        break;
                    case "frame":
                        parseFrame(args);
                        break;
                    default:
                        throw new RuntimeException("Invalid line (" + line + ")");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the Animation file", e);
        }
    }

    protected void onUnload(AssetManager manager) {
        manager.unload(image);
    }

    private void parseInfo(String[] args, Path path, AssetManager manager) {
        String file = null;
        for(String kvpair : args) {
            int i = kvpair.indexOf("=");
            String key = kvpair.substring(0, i);
            String value = kvpair.substring(i + 1, kvpair.length());
            if("texture".equals(key)) file = value.substring(1, value.length() - 1); // remove the double-quotes
        }
        image = manager.load(path.resolveSibling(file), Image.class);
    }

    private void parseFrames(String[] args) {
        int count = 0;
        for(String kvpair : args) {
            int i = kvpair.indexOf("=");
            String key = kvpair.substring(0, i);
            String value = kvpair.substring(i + 1, kvpair.length());
            if("count".equals(key)) count = Integer.parseInt(value);
        }
        frames = new AnimationFrame[count];
    }

    private void parseFrame(String[] args) {
        int id = 0;
        int x = 0, y = 0, width = 0, height = 0;

        for(String kvpair : args) {
            int i = kvpair.indexOf("=");
            String key = kvpair.substring(0, i);
            String value = kvpair.substring(i + 1, kvpair.length());
            if("id".equals(key))        id = Integer.parseInt(value);
            if("x".equals(key))         x = Integer.parseInt(value);
            if("y".equals(key))         y = Integer.parseInt(value);
            if("width".equals(key))     width = Integer.parseInt(value);
            if("height".equals(key))    height = Integer.parseInt(value);
        }
        frames[id] = new AnimationFrame(id, x, y, width, height);
    }
}