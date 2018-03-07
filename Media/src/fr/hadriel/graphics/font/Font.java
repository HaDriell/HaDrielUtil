package fr.hadriel.graphics.font;

import fr.hadriel.asset.Asset;
import fr.hadriel.asset.AssetManager;
import fr.hadriel.math.Vec2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Font extends Asset {

    //Used to load the Asset
    private final String filename;

    //Font data
    private final FontInfo info;
    private final FontCommon common;
    private final List<FontPage> pages;
    private final List<FontChar> characters;
    private final List<FontKerning> kernings;

    public Font(String filename) {
        this.filename = filename;
        this.info = new FontInfo();
        this.common = new FontCommon();
        this.characters = new ArrayList<>();
        this.kernings = new ArrayList<>();
        this.pages = new ArrayList<>();
    }

    protected void onLoad(AssetManager manager) {
        characters.clear();
        kernings.clear();
        pages.clear();

        //Parse the Font File Descriptor
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.lines().forEach(this::parseLine);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the Font file", e);
        }
    }

    protected void onUnload(AssetManager manager) {
        pages.forEach(FontPage::unload);
    }

    // PUBLIC API

    public int kerning(int first, int second) {
        for(FontKerning kerning : kernings)
            if(kerning.first == first && kerning.second == second)
                return kerning.amount;
        return 0;
    }

    public FontPage page(int id) {
        for(FontPage page : pages)
            if(page.id == id)
                return page;
        return null;
    }

    public FontChar character(int id) {
        for(FontChar c : characters) {
            if(c.id == id)
                return c;
        }
        return null;
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
        for(String kvpair : args) {
            int i = kvpair.indexOf("=");
            String key = kvpair.substring(0, i);
            String value = kvpair.substring(i + 1, kvpair.length());
            if("face".equals(key))      info.face = value;
            if("size".equals(key))      info.size = Integer.parseInt(value);
            if("bold".equals(key))      info.bold = "1".equals(value);
            if("italic".equals(key))    info.italic = "1".equals(value);
            if("unicode".equals(key))   info.unicode = "1".equals(value);
            if("smooth".equals(key))    info.smooth = "1".equals(value);
            if("stretchH".equals(key))  info.stretchH = Integer.parseInt(value);
            if("aa".equals(key))        info.aa = Integer.parseInt(value);
            if("padding".equals(key))   info.padding = new int[] {
                    Integer.parseInt(value.split(",")[0]),
                    Integer.parseInt(value.split(",")[1]),
                    Integer.parseInt(value.split(",")[2]),
                    Integer.parseInt(value.split(",")[3])
            };
            if("spacing".equals(key))   info.spacing = new int[] {
                    Integer.parseInt(value.split(",")[0]),
                    Integer.parseInt(value.split(",")[1])
            };
        }
        System.out.println("Parsed " + info);
    }

    private void parseCommon(String[] args) {
        for(String kvpair : args) {
            int i = kvpair.indexOf("=");
            String key = kvpair.substring(0, i);
            String value = kvpair.substring(i + 1, kvpair.length());
            if("lineHeight".equals(key))    common.lineHeight = Integer.parseInt(value);
            if("base".equals(key))          common.base = Integer.parseInt(value);
            if("scaleW".equals(key))        common.scaleW = Integer.parseInt(value);
            if("scaleH".equals(key))        common.scaleH = Integer.parseInt(value);
            if("pages".equals(key))         common.pages = Integer.parseInt(value);
        }
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
        pages.add(new FontPage(id, file));
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
        characters.add(new FontChar(id, page, x, y, width, height, xoffset, yoffset, xadvance));
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
        kernings.add(new FontKerning(first, second, amount));
    }
}