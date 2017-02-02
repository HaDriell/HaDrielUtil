package fr.hadriel.lwjgl.opengl;

import static org.lwjgl.opengl.GL11.*;
/**
 * Created by HaDriel on 08/12/2016.
 */
public class BlendMode {

    public boolean enabled = true;
    public int sourceFactor = GL_SRC_ALPHA;
    public int destinationFactor = GL_ONE_MINUS_SRC_ALPHA;

    public void setup() {
        if(enabled) {
            glEnable(GL_BLEND);
            glBlendFunc(sourceFactor, destinationFactor);
        } else {
            glDisable(GL_BLEND);
        }
    }
}
