package fr.hadriel.hgl.opengl;



import fr.hadriel.hgl.resources.Image;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

/**
 * Created by glathuiliere setOn 29/11/2016.
 */
public class Texture2D {

    //TODO : make a beter TextureProperties support PLEASE
    public final int handle;
    public final int width;
    public final int height;

    public Texture2D(String filename) throws IOException {
        this(filename, true);
    }

    public Texture2D(String filename, boolean glNearest) throws IOException {
        this(new Image(filename), glNearest);
    }

    public Texture2D(Image image) {
        this(image, true);
    }

    public Texture2D(Image image, boolean glNearest) {
        this.width = image.width;
        this.height = image.height;
        this.handle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, handle);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, glNearest ? GL_NEAREST : GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, glNearest ? GL_NEAREST : GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.pixels);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public TextureRegion getRegion(float x, float y, float width, float height) {
        return new TextureRegion(this, x, y, width, height);
    }

    public void setData(Image image) {
        ByteBuffer data = BufferUtils.createByteBuffer(image.pixels.length * 4);
        for(int p : image.pixels) data.putInt(p);
        data.flip();
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, image.width, image.height, GL_RGBA, GL_UNSIGNED_BYTE, data);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, handle);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void destroy() {
        glDeleteTextures(handle);
    }
}