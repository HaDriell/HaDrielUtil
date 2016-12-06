package fr.hadriel.hgl.opengl;



import fr.hadriel.hgl.stb.Image;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

/**
 * Created by glathuiliere on 29/11/2016.
 */
public class Texture2D {

    //TODO : make a beter TextureProperties support PLEASE
    public final int handle;

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

        ByteBuffer data = BufferUtils.createByteBuffer(4 * image.width * image.height);
        for(int pixel : image.pixels) data.putInt(pixel);
        data.flip();

        handle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, handle);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, glNearest ? GL_NEAREST : GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, glNearest ? GL_NEAREST : GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        glBindTexture(GL_TEXTURE_2D, 0);
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