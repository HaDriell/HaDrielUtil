package fr.hadriel.g2d;

import fr.hadriel.asset.AssetManager;
import fr.hadriel.asset.graphics.font.Font;
import fr.hadriel.asset.graphics.image.ImageRegion;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec4;
import fr.hadriel.opengl.shader.Shader;

import static org.lwjgl.opengl.GL11.*;

public class Graphics {

    public static final String DEFAULT_FONT = "Graphics.default.Font";
    public static final String DEFAULT_SPRITE_SHADER = "Graphics.default.SpriteShader";
    public static final String DEFAULT_SDF_SHADER = "Graphics.default.SDFShader";

    //Default Shaders
    private final Font defaultFont;
    private final Shader defaultSpriteShader;
    private final Shader defaultSDFShader;

    private Renderer renderer;
    private MatrixStack matrixStack;

    public Graphics(AssetManager manager) {
        this.renderer = new Renderer();
        this.defaultFont = new Font();
        this.defaultSpriteShader = Shader.GLSL(getClass().getResourceAsStream("defaults/sprite_shader.glsl"));
        this.defaultSDFShader = Shader.GLSL(getClass().getResourceAsStream("defaults/sdf_shader.glsl"));
        this.matrixStack = new MatrixStack();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public void begin() {
        renderer.begin();
        matrixStack.clear();
    }

    public void push(Matrix3 matrix) {
        matrixStack.push(matrix);
    }

    public void pop() {
        matrixStack.pop();
    }

    public void drawLine(float xa, float ya, float xb, float yb, float thickness) {

    }

    public void drawImage(ImageRegion region, Vec4 color, float x, float y, float width, float height) {
        
    }

    public void end() {
        renderer.end();
    }
}