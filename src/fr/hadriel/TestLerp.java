package fr.hadriel;

import fr.hadriel.math.Mathf;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class TestLerp {
    public static void main(String[] args) {
        Vec2 start = new Vec2();
        Vec2 end = new Vec2(10, 5);
        System.out.println(Mathf.lerp(start, end, 0.5f));
    }
}