package fr.hadriel.math;

/**
 * Created by glathuiliere on 13/07/2016.
 * Inert version of the Matrix3f matrix
 */
public strictfp class Matrix {
    private static final float[] IDENTITY = new float[] {
            1, 0, 0,
            0, 1, 0,
            0, 0, 1
    };

    public static final Matrix Identity = new Matrix();

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

    public Matrix(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
        this.m20 = m20; this.m21 = m21; this.m22 = m22;
    }

    public Matrix(float[] matrix) {
        this(
                matrix[M00], matrix[M01], matrix[M02],
                matrix[M10], matrix[M11], matrix[M12],
                matrix[M20], matrix[M21], matrix[M22]);
    }

    public Matrix(Matrix m) {
        this(
                m.m00, m.m01, m.m02,
                m.m10, m.m11, m.m12,
                m.m20, m.m21, m.m22);
    }

    public Matrix() {
        this(IDENTITY);
    }

    public static Matrix Transform(float sx, float sy, float r, float px, float py) {
        float cos = Mathf.cos(Mathf.toRadians(r));
        float sin = Mathf.sin(Mathf.toRadians(r));
        //manual
        return new Matrix(
                cos * sx,   sin,        px,
                -sin,       cos * sy,   py,
                0,          0,          1
        );
    }

    public static Matrix Scale(float sx, float sy) {
        return new Matrix(
                sx, 0,  0,
                0,  sy, 0,
                0,  0,  1
        );
    }

    public static Matrix Rotation(float angle) {
        float cos = Mathf.cos(Mathf.toRadians(angle));
        float sin = Mathf.sin(Mathf.toRadians(angle));
        return new Matrix(
                cos,    sin,    0,
                -sin,   cos,    0,
                0,      0,      1
        );
    }

    public static Matrix Translation(float x, float y) {
        return new Matrix(
                1,  0,  x,
                0,  1,  y,
                0,  0,  1
        );
    }

    public float[] elements() {
        return new float[] {
                m00, m01, m02,
                m10, m11, m12,
                m20, m21, m22
        };
    }

    public Matrix multiply(Matrix m) {
        float[] result = new float[9];
        result[M00] = m00 * m.m00 + m01 * m.m10 + m02 * m.m20;  result[M01] = m00 * m.m01 + m01 * m.m11 + m02 * m.m21;  result[M02] = m00 * m.m02 + m01 * m.m12 + m02 * m.m22;
        result[M10] = m10 * m.m00 + m11 * m.m10 + m12 * m.m20;  result[M11] = m10 * m.m01 + m11 * m.m11 + m12 * m.m21;  result[M12] = m10 * m.m02 + m11 * m.m12 + m12 * m.m22;
        result[M20] = m20 * m.m00 + m21 * m.m10 + m22 * m.m20;  result[M21] = m20 * m.m01 + m21 * m.m11 + m22 * m.m21;  result[M22] = m20 * m.m02 + m21 * m.m12 + m22 * m.m22;
        return new Matrix(result);
    }


    public Vec2 multiply(Vec2 v) { return multiply(v.x, v.y); }
    public Vec3 multiply(Vec3 v) { return multiply(v.x, v.y, v.z); }
    public Vec2 multiplyInverse(Vec2 v) { return multiplyInverse(v.x, v.y); }

    public Vec2 multiply(float x, float y) {
        float dx = x * m00 + y * m01 + m02;
        float dy = x * m10 + y * m11 + m12;
        return new Vec2(dx, dy);
    }

    public Vec3 multiply(float x, float y, float z) {
        float dx = m00 * x + m01 * y + m02 * z;
        float dy = m10 * x + m11 * y + m12 * z;
        float dz = m20 * x + m21 * y + m22 * z;
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

    public float det() {
        return  m00 * m11 * m22 +
                m01 * m12 * m20 +
                m02 * m10 * m21 -
                m00 * m12 * m21 -
                m01 * m10 * m22 -
                m02 * m11 * m20;
    }

    public Matrix invert() {
        float det = det();
        if(det == 0)
            throw new RuntimeException("Can't Invert Matrix");

        float invDet = 1f / det;
        float[] matrix = new float[9];

        // --- //
        matrix[M00] = m11 * m22 - m12 * m21;
        matrix[M01] = m02 * m21 - m01 * m22;
        matrix[M02] = m01 * m12 - m02 * m11;
        // --- //
        matrix[M10] = m12 * m20 - m10 * m22;
        matrix[M11] = m00 * m22 - m02 * m20;
        matrix[M12] = m02 * m10 - m00 * m12;
        // --- //
        matrix[M20] = m10 * m21 - m11 * m20;
        matrix[M21] = m01 * m20 - m00 * m21;
        matrix[M22] = m00 * m11 - m01 * m10;

        for(int i = 0; i < matrix.length; i++)
            matrix[i] *= invDet;

        return new Matrix(matrix);
    }

    public String toString() {
        return
                "[ " + m00 + ", " + m01 + ", " + m02 + "]" +
                "[ " + m10 + ", " + m11 + ", " + m12 + "]" +
                "[ " + m20 + ", " + m21 + ", " + m22 + "]";
    }
}