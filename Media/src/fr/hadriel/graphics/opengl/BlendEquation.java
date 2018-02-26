package fr.hadriel.graphics.opengl;

import org.lwjgl.opengl.GL14;

public enum BlendEquation {
    GL_FUNC_ADD(GL14.GL_FUNC_ADD),
    GL_FUNC_SUBSTRACT(GL14.GL_FUNC_SUBTRACT),
    GL_FUNC_REVERSE_SUBTRACT(GL14.GL_FUNC_REVERSE_SUBTRACT),
    GL_MIN(GL14.GL_MIN),
    GL_MAX(GL14.GL_MAX)
    ;

    public final int value;
    BlendEquation(int value) {
        this.value = value;
    }
}