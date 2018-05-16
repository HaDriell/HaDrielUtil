package fr.hadriel.asset.graphics.font;

import fr.hadriel.asset.Asset;
import fr.hadriel.asset.AssetManager;
import fr.hadriel.asset.graphics.image.Image;
import fr.hadriel.asset.graphics.image.ImageRegion;
import fr.hadriel.math.Vec2;
import fr.hadriel.opengl.TextureFilter;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.*;

public class Font extends Asset {
    //According to Hiero generated Font
    public static final int SPACING_H = 0;
    public static final int SPACING_V = 1;

    //According to Hiero generated Font
    public static final int PADDING_TOP    = 0;
    public static final int PADDING_RIGHT  = 1;
    public static final int PADDING_BOTTOM = 2;
    public static final int PADDING_LEFT   = 3;

    //Font data
    private FontInfo info;
    private FontCommon common;
    private Map<Integer, Image> pages;
    private Map<Integer, FontChar> characters;
    private Map<Long, FontKerning> kernings;

    private FontChar unknownCharacter;


    protected void onLoad(AssetManager manager, Path path, ByteBuffer fileContent) {
        characters = new HashMap<>();
        pages = new HashMap<>();
        kernings = new HashMap<>();

        byte[] data = new byte[fileContent.remaining()];
        fileContent.get(data, 0, data.length);

        //Parse the Font File Descriptor
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)))) {
            in.lines().forEach(line -> {
                String[] words = line.trim().split("\\s+");
                String[] args = Arrays.copyOfRange(words, 1, words.length);
                switch (words[0]) {
                    case "info":
                        parseInfo(args);
                        break;
                    case "common":
                        parseCommon(args);
                        break;
                    case "char":
                        parseChar(args);
                        break;
                    case "kerning":
                        parseKerning(args);
                        break;
                    case "page":
                        parsePage(args, path, manager);
                        break;
                    case "chars":
                    case "kernings":
                        break; //ignore these lines
                    default:
                        System.err.println("WARNING : invalid line while parsing Font");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the Font file", e);
        }
    }

    protected void onUnload(AssetManager manager) {
        pages.forEach((k, v) -> manager.unload(v)); // unload all pages loaded by that Font
    }

    // PUBLIC API

    public FontInfo info() {
        return info;
    }

    public FontCommon common() {
        return common;
    }

    public int kerning(int first, int second) {
        FontKerning kerning = kernings.get(PAIR(first, second));
        return kerning != null ? kerning.amount : 0;
    }

    public ImageRegion sprite(FontChar fc) {
        Image page = pages.get(fc.page);
        return page == null ? null : page.getRegion(fc.x, fc.y, fc.width, fc.height);
    }

    public FontChar character(int id) {
        return characters.getOrDefault(id, getUnknownCharacter());
    }

    public FontChar getUnknownCharacter() {
        if(unknownCharacter == null) {
            unknownCharacter = characters.get(0); // looks like always present in the BMFont format
        }
        return unknownCharacter;
    }

    public Vec2 sizeof(String text, float size) {
        int[] padding = info.padding;
        int[] spacing = info.spacing;

        float scale = size / (float) info.size;
        float xoffset = 0;
        float yoffset = common.lineHeight - padding[PADDING_TOP] - padding[PADDING_BOTTOM] - spacing[SPACING_V];
        char previousCharacter = 0;
        for (char character : text.toCharArray()) {
            FontChar fc = character(character);
            xoffset += fc.xadvance + kerning(previousCharacter, character) - padding[PADDING_LEFT] - padding[PADDING_RIGHT] - spacing[SPACING_H];
            previousCharacter = character;
        }
        return new Vec2(xoffset * scale, yoffset * scale);
    }

    // PARSING FUNCTIONS BELOW
    private void parseInfo(String[] args) {
        String face = null;
        int aa = 0, size = 0, stretchH = 0;
        boolean bold = false, italic = false, unicode = false, smooth = false;
        int[] padding = new int[4];
        int[] spacing = new int[2];

        for(String kvpair : args) {
            int i = kvpair.indexOf("=");
            String key = kvpair.substring(0, i);
            String value = kvpair.substring(i + 1, kvpair.length());
            if("face".equals(key))      face = value;
            if("size".equals(key))      size = Integer.parseInt(value);
            if("bold".equals(key))      bold = "1".equals(value);
            if("italic".equals(key))    italic = "1".equals(value);
            if("unicode".equals(key))   unicode = "1".equals(value);
            if("smooth".equals(key))    smooth = "1".equals(value);
            if("stretchH".equals(key))  stretchH = Integer.parseInt(value);
            if("aa".equals(key))        aa = Integer.parseInt(value);
            if("padding".equals(key))   padding = new int[] {
                    Integer.parseInt(value.split(",")[0]),
                    Integer.parseInt(value.split(",")[1]),
                    Integer.parseInt(value.split(",")[2]),
                    Integer.parseInt(value.split(",")[3])
            };
            if("spacing".equals(key))   spacing = new int[] {
                    Integer.parseInt(value.split(",")[0]),
                    Integer.parseInt(value.split(",")[1])
            };
        }
        info = new FontInfo(face, size, bold, italic, null, unicode, stretchH, smooth, aa, padding, spacing, 1);
    }

    private void parseCommon(String[] args) {
        int lineHeight = 0, base = 0, scaleW = 0, scaleH = 0, pages = 0;
        for(String kvpair : args) {
            int i = kvpair.indexOf("=");
            String key = kvpair.substring(0, i);
            String value = kvpair.substring(i + 1, kvpair.length());
            if("lineHeight".equals(key))    lineHeight = Integer.parseInt(value);
            if("base".equals(key))          base = Integer.parseInt(value);
            if("scaleW".equals(key))        scaleW = Integer.parseInt(value);
            if("scaleH".equals(key))        scaleH = Integer.parseInt(value);
            if("pages".equals(key))         pages = Integer.parseInt(value);
        }
        common = new FontCommon(lineHeight, base, scaleW, scaleH, pages);
    }

    private void parsePage(String[] args, Path path, AssetManager manager) {
        int id = 0;
        String file = null;
        for(String kvpair : args) {
            int i = kvpair.indexOf("=");
            String key = kvpair.substring(0, i);
            String value = kvpair.substring(i + 1, kvpair.length());
            if("id".equals(key))    id = Integer.parseInt(value);
            if("file".equals(key))  file = value.substring(1, value.length() - 1);
        }
        Image image = manager.load(path.resolveSibling(file), Image.class);
        image.texture().setFilter(TextureFilter.LINEAR, TextureFilter.LINEAR);
        pages.put(id, image);
    }

    private void parseChar(String[] args) {
        int id = 0, page = 0, x = 0, y = 0, xoffset = 0, yoffset = 0, width = 0, height = 0, xadvance = 0;
        for(String kvpair : args) {
            int i = kvpair.indexOf("=");
            String key = kvpair.substring(0, i);
            String value = kvpair.substring(i + 1, kvpair.length());
            if("id".equals(key))        id = Integer.parseInt(value);
            if("page".equals(key))      page = Integer.parseInt(value);
            if("x".equals(key))         x = Integer.parseInt(value);
            if("y".equals(key))         y = Integer.parseInt(value);
            if("xoffset".equals(key))   xoffset = Integer.parseInt(value);
            if("yoffset".equals(key))   yoffset = Integer.parseInt(value);
            if("width".equals(key))     width = Integer.parseInt(value);
            if("height".equals(key))    height = Integer.parseInt(value);
            if("xadvance".equals(key))  xadvance = Integer.parseInt(value);
        }
        characters.put(id, new FontChar(id, page, x, y, width, height, xoffset, yoffset, xadvance));
    }

    private void parseKerning(String[] args) {
        int first = 0, second = 0, amount = 0;
        for(String kvpair : args) {
            int i = kvpair.indexOf("=");
            String key = kvpair.substring(0, i);
            String value = kvpair.substring(i + 1, kvpair.length());
            if("first".equals(key))     first = Integer.parseInt(value);
            if("second".equals(key))    second = Integer.parseInt(value);
            if("amount".equals(key))    amount = Integer.parseInt(value);
        }
        kernings.put(PAIR(first, second), new FontKerning(first, second, amount));
    }

    // UTILITY FUNCTIONS

    public static Vec2 getTextSize(String text, Font font, float size) {
        int height = font.common.lineHeight;
        int width = 0;
        char previous = 0;
        for(char c : text.toCharArray()) {
            width += font.character(c).xadvance;
            width += font.kerning(previous, c);
            previous = c;
        }
        float scale = size / font.info.size;
        return new Vec2(width * scale, height * scale);
    }

    public static long PAIR(int first, int second) {
        return ((first & 0xFFFFL) << 32) | (second & 0xFFFFL);
    }
}