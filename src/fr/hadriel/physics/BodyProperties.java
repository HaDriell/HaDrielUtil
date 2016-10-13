package fr.hadriel.physics;

import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.geom.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HaDriel on 09/09/2016.
 */
public class BodyProperties {

    //Transformation components
    public final Vec2 scale = new Vec2(1, 1);
    public float rotation = 0;
    public final Vec2 position = new Vec2();

    //Speed components
    public final Vec2 velocity = new Vec2();
    public float angularVelocity = 0;

    //Acceleration components
    public final Vec2 acceleration = new Vec2();
    public float torque = 0;

    public float mass = 1f;
    public float friction = 0.1f;
    public float bounce = 0f;

    private List<Vec2> forces = new ArrayList<>();

    public void addForce(Vec2 force) {
        forces.add(force);
    }

    public void removeForce(Vec2 force) {
        forces.remove(force);
    }

    public void applyForces(float delta) {
        Vec2 result = new Vec2();
        for(Vec2 force : forces) {
            result.add(force);
        }
        result.scale(delta, delta);
        acceleration.add(result);
    }

    public Matrix3f getMatrix() {
        return Matrix3f.Transform(scale.x, scale.y, rotation, position.x, position.y);
    }
}