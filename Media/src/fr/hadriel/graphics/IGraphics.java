package fr.hadriel.graphics;

import fr.hadriel.graphics.opengl.Matrix3fStack;
import fr.hadriel.graphics.opengl.TextureRegion;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec4;
import fr.hadriel.math.geometry.Polygon;

/**
 *
 * @author glathuiliere
 */
public interface IGraphics {

    public static final class Settings {
        public float strokeWidth;
        public float bzFlatnessFactor;
        public int bzCubicSubdivisionCount;
        private Vec4 color;

        public Settings() {
            reset();
        }

        public void reset() {
            this.strokeWidth = 1f;
            this.bzFlatnessFactor = 1f;
            this.bzCubicSubdivisionCount = 25;
            this.color = new Vec4(1, 1, 1, 1);
        }

        public void color(Vec4 v) {
            color(v.x, v.y, v.z, v.w);
        }

        public void color(int rgba) {
            float r = ((rgba >> 24) & 0xFF) / 255f;
            float g = ((rgba >> 16) & 0xFF) / 255f;
            float b = ((rgba >> 8) & 0xFF) / 255f;
            float a = ((rgba) & 0xFF) / 255f;
            color(r, g, b, a);
        }

        public void color(float r, float g, float b, float a) {
            color = new Vec4(r, g, b, a);
        }

        public Vec4 color() {
            return color;
        }
    }

    public Settings settings();

    public void push(Matrix3f matrix);
    public void pop();

    public void draw(IDrawable drawable);

    public void draw(Polygon polygon);
    public void fill(Polygon polygon);

    public void drawLine(float ax, float ay, float bx, float by);
    public void drawCurve(float ax, float ay, float cx, float cy, float bx, float by);
    public void drawCurve(float ax, float ay, float c1x, float c1y, float c2x, float c2y, float bx, float by);

    public void drawRect(float x, float y, float width, float height);
    public void fillRect(float x, float y, float width, float height);

    public void drawTexture(float x, float y, TextureRegion region);
    public void drawTexture(float x, float y, TextureRegion region, Vec4 mask);
    public void drawTexture(float x, float y, float width, float height, TextureRegion region);
    public void drawTexture(float x, float y, float width, float height, TextureRegion region, Vec4 mask);
}