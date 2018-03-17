package fr.hadriel.asset.font;

import fr.hadriel.asset.Asset;
import fr.hadriel.asset.AssetManager;
import fr.hadriel.graphics.image.Sprite;
import fr.hadriel.math.Vec2;
import fr.hadriel.opengl.Texture2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Font extends Asset {

    //Used to load the Asset
    private final String filename;

    //Font data
    private FontInfo info;
    private FontCommon common;
    private Map<Integer, FontPage> pages;
    private Map<Integer, FontChar> characters;
    private Map<Long, FontKerning> kernings;

    private FontChar unknownCharacter;

    public Font(String filename) {
        this.filename = filename;
    }

    protected void onLoad(AssetManager manager) {
        characters = new HashMap<>();
        pages = new HashMap<>();
        kernings = new HashMap<>();

        //Parse the Font File Descriptor
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.lines().forEach(this::parseLine);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the Font file", e);
        }
    }

    protected void onUnload(AssetManager manager) {
        pages.forEach((k, v) -> v.unload());
    }

    // PUBLIC API

    public FontInfo info() {
        return info;
    }

    public FontCommon common() {
        return common;
    }

    public Texture2D page(int id) {
        FontPage page = pages.get(id);
        return page != null ? page.texture : null;
    }

    public int kerning(int first, int second) {
        FontKerning kerning = kernings.get(PAIR(first, second));
        return kerning != null ? kerning.amount : 0;
    }

    public Sprite sprite(FontChar fc) {
        FontPage page = pages.get(fc.page);
        return page == null ? null : new Sprite(page.texture, fc.x, fc.y, fc.width, fc.height);
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

    // PARSING FUNCTIONS BELOW

    private void parseLine(String line) {
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
                parsePage(args);
                break;
            case "chars":
            case "kernings":
                break; //ignore these lines
            default:
                System.err.println("WARNING : ignored line while parsing");
        }
    }

    private void parseInfo(String[] args) {
        String face = null;
        int a = 0, aa = 0, size = 0, stretchH = 0;
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
        System.out.println("Parsed " + info);
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
        System.out.println("Parsed " + common);
    }

    private void parsePage(String[] args) {
        int id = 0;
        String file = null;
        for(String kvpair : args) {
            int i = kvpair.indexOf("=");
            String key = kvpair.substring(0, i);
            String value = kvpair.substring(i + 1, kvpair.length());
            if("id".equals(key))    id = Integer.parseInt(value);
            if("file".equals(key))  file = filename.substring(0, filename.lastIndexOf('/')) + '/' + value.substring(1, value.length() - 1);
        }
        System.out.println("Page File: " + file);
        pages.put(id, new FontPage(id, file));
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