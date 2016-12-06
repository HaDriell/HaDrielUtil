package fr.hadriel;

import fr.hadriel.math.Matrix4f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec3;
import fr.hadriel.math.Vec4;

/**
 * Created by glathuiliere on 05/12/2016.
 */
public class TestMath {

    public static void main(String[] args) {
        for(int i = 0; i < 360; i+= 15) {
            Matrix4f r = Matrix4f.Rotation(i, new Vec3(0, 0, 1));
            System.out.println(r.multiply(new Vec4(1, 0, 0, 1)));
            System.out.println(r.multiply(new Vec3(1, 0, 1)));
            System.out.println(r.multiply(new Vec2(1, 0)));
        }
    }
}
