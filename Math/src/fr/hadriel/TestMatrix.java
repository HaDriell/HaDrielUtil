package fr.hadriel;

import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Matrix4;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec3;
import fr.hadriel.math.geometry.Epsilon;

public class TestMatrix {
    public static void main(String[] args) {
        test_matrix3();
        test_matrix4();
    }

    private static void test_matrix3() {
        Vec2 v = new Vec2(5, 3);
        Vec2 dv;
        Matrix3 m;
        System.out.println("Starting Matrix3 test");
        for(float angle = 0; angle < 360; angle += 30) {
            m = Matrix3.Translation(100, 30);
            m = m.multiply(Matrix3.Rotation(angle));
            m = m.multiply(Matrix3.Scale(1, 10));
            dv = m.multiply(v);
            dv = m.multiplyInverse(dv);

            if(v.len2() - dv.len2() < Epsilon.E)
                continue;


            System.out.println(String.format("angle: %3.1f    origin:%s    (m-1 * (m * origin)): %s", angle, v,dv));
        }
        System.out.println("Ending test");
    }

    private static void test_matrix4() {
        Vec2 v = new Vec2(5, 3);
        Vec2 dv;
        Matrix4 m, im, im2;
        System.out.println("Starting Matrix4 test");
        for(float angle = 0; angle < 360; angle += 30) {
            m = Matrix4.Translation(100, 30, 0);
            m = m.multiply(Matrix4.Rotation(angle, Vec3.Z));
            m = m.multiply(Matrix4.Scale(1, 10, 1));
            im = m.invert();


            dv = m.multiply(v);
            dv = im.multiply(dv);

            if(v.len2() - dv.len2() < Epsilon.E)
                continue;

            System.out.println(String.format("angle: %3.1f    origin:%s    (m-1 * (m * origin)): %s", angle, v,dv));
        }
    }
}