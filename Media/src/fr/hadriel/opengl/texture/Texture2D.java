package fr.hadriel.opengl.texture;



import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by glathuiliere on 29/11/2016.
 */
public class Texture2D {

    public final int handle;
    public final int width;
    public final int height;

    public Texture2D(int width, int height) {
        this(width, height, null);
    }

    public Texture2D(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        this.handle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, handle);

        //Texture2D io setupUniforms (migth be empty)
        if(pixels == null)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
        else
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
        glGenerateMipmap(GL_TEXTURE_2D); // always Generate mipmaps
    }

    public void setWrapping(TextureWrapper xWrapper, TextureWrapper yWrapper) {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, xWrapper.value);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, yWrapper.value);
    }

    public void setFilter(TextureFilter min, TextureFilter mag) {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, min.value);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mag.value);
    }

    public void write(int x, int y, int width, int  height, int[] pixels) {
        glTexSubImage2D(GL_TEXTURE_2D, 0, x, y, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
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

    public boolean equals(Object obj) {
        return obj instanceof Texture2D && ((Texture2D) obj).handle == handle;
    }
}