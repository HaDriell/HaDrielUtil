package fr.hadriel.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;
/**
 * Created by HaDriel on 08/12/2016.
 */
public class BlendMode {

    public int sourceFactor = GL_SRC_ALPHA;
    public int destinationFactor = GL_ONE_MINUS_SRC_ALPHA;

    public void enable() {
        glEnable(GL_BLEND);
        glBlendFunc(sourceFactor, destinationFactor);
    }

    public void disable() {
        glDisable(GL_BLEND);
    }
}
