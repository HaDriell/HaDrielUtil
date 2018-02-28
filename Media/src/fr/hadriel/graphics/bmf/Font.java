package fr.hadriel.graphics.bmf;

import fr.hadriel.core.asset.Asset;
import fr.hadriel.core.asset.AssetManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Font extends Asset {

    //Used to load the Asset
    private String filename;

    //Font data
    private final FontInfo info = new FontInfo();
    private final FontCommon common = new FontCommon();
    private final List<FontPage> pages = new ArrayList<>();
    private final List<FontChar> characters = new ArrayList<>();

    //Basically a FNT parser
    protected void onLoad(AssetManager manager) {

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.lines().forEach(this::parseLine);
        } catch (IOException ignore) {
        }
    }

    protected void onUnload(AssetManager manager) {

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
            if("stretchH".equals(key))  info.stretch = Integer.parseInt(value);
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
    }

    private void parseCommon(String[] args) {
        for(String kvpair : args) {
            int i = kvpair.indexOf("=");
            String key = kvpair.substring(0, i);
            String value = kvpair.substring(i + 1, kvpair.length());
            if("lineHeight".equals(key)) common.lineHeight = Integer.parseInt(value);
            if("base".equals(key)) common.lineHeight = Integer.parseInt(value);
            if("scaleW".equals(key)) common.lineHeight = Integer.parseInt(value);
            if("scaleH".equals(key)) common.lineHeight = Integer.parseInt(value);
            if("pages".equals(key)) common.lineHeight = Integer.parseInt(value);
        }
    }

    private void parsePage(String[] args) {

    }

    private void parseChar(String[] args) {

    }

    private void parseKerning(String[] args) {

    }
}