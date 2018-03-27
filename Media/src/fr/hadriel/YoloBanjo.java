package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.math.Mathf;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import fr.hadriel.opengl.*;
import fr.hadriel.opengl.shader.Shader;
import fr.hadriel.renderers.RenderUtil;
import org.lwjgl.opengl.GL11;

public class YoloBanjo extends Application{
    private static int VERTEX_COUNT = 6;

    static VertexAttribute[] LAYOUT = {
            new VertexAttribute("position", GLType.FLOAT, 2)
    };

    private Shader shader;
    private VertexArray vao;

    protected void start(String[] args) {
        vao = new VertexArray(VERTEX_COUNT * 2, LAYOUT);
        shader = Shader.GLSL(YoloBanjo.class.getResourceAsStream("yolobanjo.glsl"));
        VertexBuffer vbo = vao.getBuffer();

        vbo.bind().map();
//        for(int i = 0; i < VERTEX_COUNT; i++) {
//            Vec2 v = new Vec2(1, 0).rotate((360 * i) / VERTEX_COUNT);
//            vbo.write(v);
//            System.out.println("[" + i + "] " + v);
//        }

        vbo.write(0f).write(0f);
        vbo.write(1f).write(0f);
        vbo.write(1f).write(1f);
        vbo.write(0f).write(1f);

        vbo.bind().unmap();
    }

    protected void update(float delta) {
        RenderUtil.Clear(0, 0, 0, 1);
        shader.uniform("u_color", new Vec4(1, 0, 0, 1));
        RenderUtil.Draw(GL11.GL_TRIANGLE_FAN, shader, vao, null, null, VERTEX_COUNT);
    }

    protected void terminate() {
    }

    public static void main(String... args) {
        launch(new YoloBanjo());
    }
}