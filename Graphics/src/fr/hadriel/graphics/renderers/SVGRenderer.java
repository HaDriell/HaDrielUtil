package fr.hadriel.graphics.renderers;

import fr.hadriel.graphics.opengl.Matrix3fStack;

/**
 * @author glathuiliere
 */
public class SVGRenderer implements IRenderer {

    private Matrix3fStack stack;

    public SVGRenderer() {
        this.stack = new Matrix3fStack();
    }

    public void begin() {

    }

    public void end() {

    }
}