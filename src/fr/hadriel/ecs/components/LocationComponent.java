package fr.hadriel.ecs.components;

import fr.hadriel.serialization.struct.StObject;

/**
 * Created by HaDriel on 09/12/2016.
 */
public class LocationComponent implements NetworkedComponent {
    public float x; // x
    public float y; // y
    public float angle; // angle

    public void serialize(StObject object) {
        object.put("x", x);
        object.put("y", y);
        object.put("angle", angle);
    }

    public void deserialize(StObject object) {
        try {
            float x = object.get("x").asFloat();
            float y = object.get("y").asFloat();
            float angle = object.get("angle").asFloat();
            this.x = x;
            this.y = y;
            this.angle = angle;
        } catch (Exception discard) {}
    }
}