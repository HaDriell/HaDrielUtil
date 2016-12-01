package fr.hadriel.math;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

/**
 * Created by glathuiliere on 13/07/2016.
 */
public class Matrix4f {

    /*
    * Matrix Layout
    * [M00 .... M03]
    * [M10 .... M13]
    * [M20 .... M23]
    * [M30 .... M33]
    * */

    //First Row
    public static final byte M00 = 0;
    public static final byte M01 = 1;
    public static final byte M02 = 2;
    public static final byte M03 = 3;

    //Second Row
    public static final byte M10 = 4;
    public static final byte M12 = 6;
    public static final byte M13 = 7;
    public static final byte M11 = 5;

    //Third Row
    public static final byte M20 = 8;
    public static final byte M21 = 9;
    public static final byte M22 = 10;
    public static final byte M23 = 11;

    //Third Row
    public static final byte M30 = 12;
    public static final byte M31 = 13;
    public static final byte M32 = 14;
    public static final byte M33 = 15;

    private static final float[] IDENTITY = new float[] {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
    };

    public final float[] elements = new float[4 * 4];

    public Matrix4f(float[] flatMatrix) {
        set(flatMatrix);
    }

    public Matrix4f(Matrix4f m) {
        this(m.elements);
    }

    public Matrix4f() {
        this(IDENTITY);
    }

    public Matrix4f set(float[] flatMatrix) {
        if(flatMatrix.length != 16)
            throw new IllegalArgumentException("Invalid flat Matrix sizeProperty");
        System.arraycopy(flatMatrix, 0, elements, 0, flatMatrix.length);
        return this;
    }

    public Matrix4f setIdentity() {
        set(IDENTITY);
        return this;
    }

    public Matrix4f translate(float x, float y, float z) {
        elements[M03] += x;
        elements[M13] += y;
        elements[M23] += z;
        return this;
    }

    public Matrix4f scale(float x, float y, float z) {
        elements[M00] *= x;
        elements[M11] *= y;
        elements[M22] *= z;
        return this;
    }

    public Matrix4f rotate(float angle, Vec3 axis) {
        multiply(Matrix4f.Rotation(angle, axis));
        return this;
    }

    public Matrix4f setToOrthographic(float left, float right, float top, float bottom, float near, float far) {
        setIdentity();

        elements[M00] = 2f / (right - left);
        elements[M11] = 2f / (top - bottom);
        elements[M22] = -2f / (far - near);

        elements[M03] = -(right + left) / (right - left);
        elements[M13] = -(top + bottom) / (top - bottom);
        elements[M23] = -(far + near) / (far - near);
        return this;
    }

    public Matrix4f setToScale(float sx, float sy, float sz) {
        setIdentity();
        scale(sx, sy, sz);
        return this;
    }

    public Matrix4f setToRotation(float angle, Vec3 axis) {
        setIdentity();
        float r = Mathf.toRadians(angle);
        float c = Mathf.cos(r);
        float s = Mathf.sin(r);
        float omc = 1 - c;
        float x = axis.x;
        float y = axis.y;
        float z = axis.z;
        elements[M00] = x * omc + c;
        elements[M01] = y * x * omc + z * s;
        elements[M02] = x * z * omc - y * s;

        elements[M10] = x * y * omc - z * s;
        elements[M11] = y * omc + c;
        elements[M12] = y * z * omc + x * s;

        elements[M20] = x * z * omc + y * s;
        elements[M21] = y * z * omc - x * s;
        elements[M22] = z * omc + c;
        return this;
    }

    public Matrix4f setToTranslation(float x, float y, float z) {
        setIdentity();
        elements[M02] = x;
        elements[M12] = y;
        elements[M22] = y;
        return this;
    }

    public Matrix4f multiply(Matrix4f matrix) {
        float[] data = new float[16];
        for(int row = 0; row < 4; row++) {
            for(int col = 0; col < 4; col++) {
                float sum = 0;
                for(int e = 0; e < 4; e++) {
                    sum += elements[e + row * 4] * matrix.elements[col + e * 4];
                }
                data[col + row * 4] = sum;
            }
        }
        set(data);
        return this;
    }

    public Matrix4f copy() {
        return new Matrix4f(this);
    }

    public static Matrix4f Orthographic(float left, float right, float top, float bottom, float near, float far) {
        return new Matrix4f().setToOrthographic(left, right, top, bottom, near, far);
    }

    public static Matrix4f Scale(float sx, float sy, float sz) {
        return new Matrix4f().setToScale(sx, sy, sz);
    }

    public static Matrix4f Rotation(float angle, Vec3 axis) {
        return new Matrix4f().setToRotation(angle, axis);
    }

    public static Matrix4f Translation(float x, float y, float z) {
        return new Matrix4f().setToTranslation(x, y, z);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%.2f %.2f %.2f %.2f]", elements[M00], elements[M01], elements[M02], elements[M03])).append('\n');
        sb.append(String.format("[%.2f %.2f %.2f %.2f]", elements[M10], elements[M11], elements[M12], elements[M13])).append('\n');
        sb.append(String.format("[%.2f %.2f %.2f %.2f]", elements[M20], elements[M21], elements[M22], elements[M23])).append('\n');
        sb.append(String.format("[%.2f %.2f %.2f %.2f]", elements[M30], elements[M31], elements[M32], elements[M33])).append('\n');
        return sb.toString();
    }

    public FloatBuffer toFloatBuffer() {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4 * 4);
        buffer.put(elements);
        buffer.flip();
        return buffer;
    }
}