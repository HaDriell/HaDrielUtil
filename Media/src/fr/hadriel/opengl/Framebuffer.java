package fr.hadriel.opengl;


import fr.hadriel.math.Vec2;
import fr.hadriel.opengl.texture.Texture2D;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class Framebuffer {

    private int framebuffer;
    private int width;
    private int height;

    private Texture2D colorBuffer;
    private Texture2D depthBuffer;
    private Texture2D stencilBuffer;

    public Framebuffer(int width, int height) {
        this.framebuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        resize(width, height);
    }

    public void destroy() {
        glDeleteFramebuffers(framebuffer);
        colorBuffer.destroy();
        depthBuffer.destroy();
        stencilBuffer.destroy();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public Vec2 size() {
        return new Vec2(width, height);
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        if(colorBuffer != null) colorBuffer.destroy();
        if(depthBuffer != null) depthBuffer.destroy();
        if(stencilBuffer != null) stencilBuffer.destroy();

        colorBuffer = new Texture2D(width, height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorBuffer.handle, 0);

        depthBuffer = new Texture2D(width, height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthBuffer.handle, 0);

        stencilBuffer = new Texture2D(width, height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_STENCIL_ATTACHMENT, GL_TEXTURE_2D, stencilBuffer.handle, 0);

    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        colorBuffer.bind();
    }

    public void clearColor() {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void clearDepth() {
        glClear(GL_DEPTH_BUFFER_BIT);
    }

    public void clearStencil() {
        glClear(GL_STENCIL_BUFFER_BIT);
    }

    public void unbind() {
        colorBuffer.unbind();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
}