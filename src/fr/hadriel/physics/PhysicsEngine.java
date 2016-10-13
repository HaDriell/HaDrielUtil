package fr.hadriel.physics;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by glathuiliere on 05/09/2016.
 */
public class PhysicsEngine {

    private List<Body> bodies;
    private Lock lock = new ReentrantLock();

    private CollisionDetector collisionDetector;
    private CollisionResolver collisionResolver;

    public PhysicsEngine() {
        this.collisionDetector = new DefaultCollisionDetector();
        this.bodies = new ArrayList<>();
    }

    public void setCollisionDetector(CollisionDetector collisionDetector) {
        this.collisionDetector = collisionDetector;
    }

    public void setCollisionResolver(CollisionResolver collisionResolver) {
        this.collisionResolver = collisionResolver;
    }

    public Body[] getBodies() {
        lock.lock();
        Body[] array = new Body[bodies.size()];
        bodies.toArray(array);
        lock.unlock();
        return array;
    }

    public void addBody(Body body) {
        lock.lock();
        if(!bodies.contains(body))
            bodies.add(body);
        lock.unlock();
    }

    public void removeBody(Body body) {
        lock.lock();
        bodies.remove(body);
        lock.unlock();
    }

    private void resolveCollisions() {
        for(Body a : bodies) {
            for(Body b : bodies) {
                if(a != b && collisionDetector.collides(a, b)) {
                    //TODO
                }
            }
        }
    }

    private void updateBodies(float delta) {
        for(Body body : bodies) {
            BodyProperties p = body.properties;
            //forces update
            p.applyForces(delta);
            //linear update
            p.position.x += p.velocity.x * delta;
            p.position.y += p.velocity.y * delta;
            p.velocity.x += p.acceleration.x * delta;
            p.velocity.y += p.acceleration.y * delta;

            //rotation update
            p.rotation += p.angularVelocity * delta;
            p.angularVelocity += p.torque * delta;
        }
    }

    public void update(float delta) {
        lock.lock();
        resolveCollisions();
        updateBodies(delta);
        lock.unlock();
    }
}