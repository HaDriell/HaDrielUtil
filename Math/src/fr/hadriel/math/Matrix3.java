package fr.hadriel.math;

/**
 * Created by glathuiliere on 13/07/2016.
 * Inert version of the Matrix3f matrix
 */
public strictfp class Matrix3 {
    private static final float[] IDENTITY = new float[] {
            1, 0, 0,
            0, 1, 0,
            0, 0, 1
    };

    public static final Matrix3 Identity = new Matrix3();

    //First Row
    public static final byte M00 = 0;
    public static final byte M01 = 1;
    public static final byte M02 = 2;

    //Second Row
    public static final byte M10 = 3;
    public static final byte M11 = 4;
    public static final byte M12 = 5;

    //Third Row
    public static final byte M20 = 6;
    public static final byte M21 = 7;
    public static final byte M22 = 8;


    public final float m00, m01, m02;
    public final float m10, m11, m12;
    public final float m20, m21, m22;

    public Matrix3(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
        this.m20 = m20; this.m21 = m21; this.m22 = m22;
    }

    public Matrix3(float[] matrix) {
        this(
                matrix[M00], matrix[M01], matrix[M02],
                matrix[M10], matrix[M11], matrix[M12],
                matrix[M20], matrix[M21], matrix[M22]);
    }

    public Matrix3(Matrix3 m) {
        this(
                m.m00, m.m01, m.m02,
                m.m10, m.m11, m.m12,
                m.m20, m.m21, m.m22);
    }

    public Matrix3() {
        this(IDENTITY);
    }

    public static Matrix3 Transform(float sx, float sy, float r, float px, float py) {
        float cos = Mathf.cos(Mathf.toRadians(r));
        float sin = Mathf.sin(Mathf.toRadians(r));
        //manual
        return new Matrix3(
                cos * sx,   sin,        px,
                -sin,       cos * sy,   py,
                0,          0,          1
        );
    }

    public static Matrix3 Scale(float sx, float sy) {
        return new Matrix3(
                sx, 0,  0,
                0,  sy, 0,
                0,  0,  1
        );
    }

    //assumed RotationZ
    public static Matrix3 Rotation(float angle) {
        float c = Mathf.cos(Mathf.toRadians(angle));
        float s = Mathf.sin(Mathf.toRadians(angle));
        float[] elements = new float[9];
        elements[M00] = c;
        elements[M01] = s;
        elements[M10] = -s;
        elements[M11] = c;
        elements[M22] = 1;
        return new Matrix3(elements);
    }

    public static Matrix3 Translation(float x, float y) {
        float[] elements = new float[9];
        elements[M00] = 1f;
        elements[M11] = 1f;
        elements[M22] = 1f;

        elements[M20] = x;
        elements[M21] = y;
        return new Matrix3(elements);
    }

    public float[] elements() {
        return new float[] {
                m00, m01, m02,
                m10, m11, m12,
                m20, m21, m22
        };
    }

    public Matrix3 multiply(Matrix3 m) {
        float[] elements = new float[9];
        //ROW 0
        elements[M00] = m.m00 * m00 + m.m01 * m10 + m.m02 * m20;
        elements[M01] = m.m00 * m01 + m.m01 * m11 + m.m02 * m21;
        elements[M02] = m.m00 * m02 + m.m01 * m12 + m.m02 * m22;

        //ROW 1
        elements[M10] = m.m10 * m00 + m.m11 * m10 + m.m12 * m20;
        elements[M11] = m.m10 * m01 + m.m11 * m11 + m.m12 * m21;
        elements[M12] = m.m10 * m02 + m.m11 * m12 + m.m12 * m22;

        //ROW 2
        elements[M20] = m.m20 * m00 + m.m21 * m10 + m.m22 * m20;
        elements[M21] = m.m20 * m01 + m.m21 * m11 + m.m22 * m21;
        elements[M22] = m.m20 * m02 + m.m21 * m12 + m.m22 * m22;

        return new Matrix3(elements);
    }


    public Vec2 multiply(Vec2 v) { return multiply(v.x, v.y); }
    public Vec3 multiply(Vec3 v) { return multiply(v.x, v.y, v.z); }
    public Vec2 multiplyInverse(Vec2 v) { return multiplyInverse(v.x, v.y); }

    // assumed Vec3(x, y, 0)
    public Vec2 multiply(float x, float y) {
        float dx = m00 * x + m10 * y + m20;
        float dy = m01 * x + m11 * y + m21;
        return new Vec2(dx, dy);
    }

    public Vec3 multiply(float x, float y, float z) {
        float dx = m00 * x + m10 * y + m20 * z;
        float dy = m01 * x + m11 * y + m21 * z;
        float dz = m02 * x + m12 * y + m22 * z;
        return new Vec3(dx, dy, dz);
    }

    //Credits to Thomas Lathuilière for his awesome work around inversion optimizations
    public Vec2 multiplyInverse(float x, float y) {
        x -= m02;
        y -= m12;
        float det22_inv = 1f / (m00 * m11 - m01 * m10);
        float mm00 =  det22_inv * m11;
        float mm01 =  det22_inv * -m01;
        float mm10 =  det22_inv * -m10;
        float mm11 =  det22_inv * m00;
        float dx = x * mm00 + y * mm01;
        float dy = x * mm10 + y * mm11;
        return new Vec2(dx, dy);
    }

    public Vec2 getTranslation() {
        return new Vec2(m02, m12);
    }

    public float getRotation() {
        Vec2 scale = getScale();
        float cos = m00 / scale.x;
        float sin = -m10 / scale.x;
        return Mathf.toDegrees(Mathf.atan2(cos, sin));
    }

    public Vec2 getScale() {
        float sx = Mathf.sqrt(m00*m00 + m10*m10);
        float sy = Mathf.sqrt(m01*m01 + m11*m11);
        return new Vec2(sx, sy);
    }

    public float determinant() {
        float b01 = m22 * m11 - m12 * m21;
        float b11 = -m22 * m10 + m12 * m20;
        float b21 = m21 * m10 - m11 * m20;
        return  m00 * b01 + m01 * b11 + m02 * b21;
    }

    public Matrix3 invert() {
        float[] elements = new float[9];
        float b01 = m22 * m11 - m12 * m21;
        float b11 = -m22 * m10 + m12 * m20;
        float b21 = m21 * m10 - m11 * m20;

        // Calculate the determinant
        float det = m00 * b01 + m01 * b11 + m02 * b21;

        if (det == 0) return null; // singular Matrix
        det = 1f / det;

        elements[0] = b01 * det;
        elements[1] = (-m22 * m01 + m02 * m21) * det;
        elements[2] = (m12 * m01 - m02 * m11) * det;
        elements[3] = b11 * det;
        elements[4] = (m22 * m00 - m02 * m20) * det;
        elements[5] = (-m12 * m00 + m02 * m10) * det;
        elements[6] = b21 * det;
        elements[7] = (-m21 * m00 + m01 * m20) * det;
        elements[8] = (m11 * m00 - m01 * m10) * det;
        return new Matrix3(elements);
    }

    public String toString() {
        return
                "[ " + m00 + ", " + m01 + ", " + m02 + "]" +
                "[ " + m10 + ", " + m11 + ", " + m12 + "]" +
                "[ " + m20 + ", " + m21 + ", " + m22 + "]";
    }
}