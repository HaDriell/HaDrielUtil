package fr.hadriel.hgl.core;

import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by HaDriel on 30/11/2016.
 */
public class BufferLayout {

    public static class Element {
        public final String name;
        public final int type;
        public final int size;
        public final int count;
        public final int offset;
        public final boolean normalized;

        public Element(String name, int type, int size, int count, int offset, boolean normalized) {
            this.name = name;
            this.type = type;
            this.size = size;
            this.count = count;
            this.offset = offset;
            this.normalized = normalized;
        }
    }

    private int stride;
    private List<Element> layout;

    public BufferLayout() {
        this.layout = new ArrayList<>();
    }

    private void push(String name, int type, int size, int count, boolean normalized) {
        layout.add(new Element(name, type, size, count, size, normalized));
        stride += size * count;
    }

    public void pushByte(String name, int count, boolean normalized) {
        push(name, GL11.GL_UNSIGNED_BYTE, 1, count, normalized);
    }

    public void pushShort(String name, int count, boolean normalized) {
        push(name, GL11.GL_SHORT, 2, count, normalized);
    }

    public void pushUnsignedShort(String name, int count, boolean normalized) {
        push(name, GL11.GL_UNSIGNED_SHORT, 2, count, normalized);
    }

    public void pushInt(String name, int count, boolean normalized) {
        push(name, GL11.GL_INT, 4, count, normalized);
    }

    public void pushUnsignedInt(String name, int count, boolean normalized) {
        push(name, GL11.GL_UNSIGNED_INT, 4, count, normalized);
    }

    public void pushFloat(String name, int count, boolean normalized) {
        push(name, GL11.GL_FLOAT, 4, count, normalized);
    }

    public void pushVec2(String name, boolean normalized) {
        push(name, GL11.GL_FLOAT, 4, 2, normalized);
    }

    public void pushVec3(String name, boolean normalized) {
        push(name, GL11.GL_FLOAT, 4, 3, normalized);
    }

    public void pushVec4(String name, boolean normalized) {
        push(name, GL11.GL_FLOAT, 4, 4, normalized);
    }

    public int getStride() {
        return stride;
    }

    public void unbind(int offsetAttribPointer) {
        for(int i = 0; i < layout.size(); i++)
            glDisableVertexAttribArray(offsetAttribPointer + i);
    }

    public void bind(int offsetAttribPointer) {
        for(int i = 0; i < layout.size(); i++) {
            Element element = layout.get(i);
            glEnableVertexAttribArray(offsetAttribPointer + i);
            glVertexAttribPointer(offsetAttribPointer + i, element.count, element.type, element.normalized, stride, element.offset);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < layout.size(); i++) {
            Element element = layout.get(i);
            sb.append("glEnableVertexAttribArray(" + i + ");").append('\n');
            sb.append("glVertexAttribPointer(" + i + ", " + element.count + ", " + element.type + ", " + element.normalized + ", " + stride + ", " + element.offset + ");").append('\n');
        }
        return sb.toString();
    }
}