package fr.hadriel.asset.graphics.font;

import fr.hadriel.asset.Asset;
import fr.hadriel.io.ImageFile;
import fr.hadriel.opengl.texture.TextureFormat;
import fr.hadriel.opengl.texture.TextureRegion;
import fr.hadriel.math.Vec2;
import fr.hadriel.opengl.texture.Texture2D;
import fr.hadriel.util.LineParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.*;

public class Font extends Asset {

    //Smallest prefix overhead
    public static final String PREFIX_INFO      = "info";
    public static final String PREFIX_COMMON    = "common";
    public static final String PREFIX_PAGE      = "page";
    public static final String PREFIX_CHARACTER = "char";
    public static final String PREFIX_KERNING   = "kerning";


    //FontFile data
    private FontInfo info;
    private FontCommon common;
    private Map<Integer, Texture2D> pages;
    private Map<Integer, FontChar> characters;
    private Map<Long, FontKerning> kernings;

    private FontChar unknownCharacter;


    protected void onLoad(Path path, ByteBuffer fileContent) {
        FontInfo info = new FontInfo();
        FontCommon common = new FontCommon();
        Map<Integer, Texture2D> pages = new HashMap<>();
        Map<Integer, FontChar> characters = new HashMap<>();
        Map<Long, FontKerning> kernings = new HashMap<>();

        byte[] buffer = new byte[fileContent.remaining()];
        fileContent.get(buffer, 0, buffer.length);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer)))) {
            LineParser parser = new LineParser();
            in.lines().forEach(line -> {
                if (line.startsWith("chars")) return; // unused line

                parser.parse(line);
                switch (parser.getPrefix()) {
                    case PREFIX_INFO:
                        info.face = parser.getString("face");
                        info.size = parser.getInt("size");
                        info.bold = parser.getBoolean("bold");
                        info.italic = parser.getBoolean("italic");
                        info.charset = parser.getString("charset");
                        info.smooth = parser.getBoolean("smooth");
                        info.unicode = parser.getBoolean("unicode");
                        info.stretchH = parser.getInt("stretchH");
                        info.aa   = parser.getInt("aa");
                        int[] spacing = parser.getInt2("spacing");
                        info.spacingH = spacing[0];
                        info.spacingV = spacing[1];
                        int[] padding = parser.getInt4("padding");
                        info.paddingUp    = padding[0];
                        info.paddingRight = padding[1];
                        info.paddingDown  = padding[2];
                        info.paddingLeft  = padding[3];
                        break;

                    case PREFIX_COMMON:
                        common.lineHeight = parser.getInt("lineHeight");
                        common.base = parser.getInt("base");
                        common.scaleW = parser.getInt("scaleW");
                        common.scaleH = parser.getInt("scaleH");
                        common.pages = parser.getInt("pages");
                        common.packed = parser.getInt("packed");
                        break;

                    case PREFIX_PAGE:
                        int id = parser.getInt("id");
                        ImageFile image = new ImageFile(path.resolveSibling(parser.getString("file")).toString());
                        Texture2D texture = new Texture2D();
                        texture.bind();
                        texture.setData(image.width, image.height, image.pixels, TextureFormat.RGBA8);
                        pages.put(id, texture);
                        break;

                    case PREFIX_CHARACTER:
                        FontChar fc = new FontChar();
                        fc.id = parser.getInt("id");
                        fc.x  = parser.getInt("x");
                        fc.y  = parser.getInt("y");
                        fc.width = parser.getInt("width");
                        fc.height = parser.getInt("height");
                        fc.xoffset = parser.getInt("xoffset");
                        fc.yoffset = parser.getInt("yoffset");
                        fc.xadvance = parser.getInt("xadvance");
                        fc.page = parser.getInt("page");
                        fc.channel = parser.getInt("chnl");
                        characters.put(fc.id, fc);
                        break;

                    case PREFIX_KERNING:
                        FontKerning fk = new FontKerning();
                        fk.first = parser.getInt("first");
                        fk.second = parser.getInt("second");
                        fk.amount = parser.getInt("amount");
                        kernings.put(COMBINE(fk.first, fk.second), fk);
                        break;
                }
            });
        } catch (IOException ignore) {}

        this.info = info;
        this.common = common;
        this.pages = pages;
        this.characters = characters;
        this.kernings = kernings;
    }

    protected void onUnload() {
        pages.forEach((k, v) -> v.destroy());
        pages.clear();
    }

    // PUBLIC API

    public FontInfo info() {
        return info;
    }

    public FontCommon common() {
        return common;
    }

    public int kerning(int first, int second) {
        FontKerning kerning = kernings.get(COMBINE(first, second));
        return kerning != null ? kerning.amount : 0;
    }

    public Texture2D page(int page) {
        return pages.get(page);
    }

    public TextureRegion sprite(FontChar fc) {
        Texture2D page = pages.get(fc.page);
        if (page == null) return null;
        return new TextureRegion(page, fc.x, fc.y, fc.width, fc.height);
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
        float scale = size / (float) info.size;
        float xoffset = 0;
        float yoffset = common.lineHeight - info.paddingUp - info.paddingDown - info.spacingV;
        char previousCharacter = 0;
        for (char character : text.toCharArray()) {
            FontChar fc = character(character);
            xoffset += fc.xadvance + kerning(previousCharacter, character) - info.paddingLeft - info.paddingRight - info.spacingH;
            previousCharacter = character;
        }
        return new Vec2(xoffset * scale, yoffset * scale);
    }

    private static long COMBINE(int first, int second) {
        return ((first & 0xFFFFL) << 32) | (second & 0xFFFFL);
    }
}