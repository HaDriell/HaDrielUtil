package fr.hadriel.opengl;

import org.lwjgl.opengl.GL11;

public enum WindingOrder {
    CLOCKWISE(GL11.GL_CW),
    COUNTER_CLOCKWISE(GL11.GL_CCW);

    public final int value;

    private WindingOrder(int value) {
        this.value = value;
    }
}
