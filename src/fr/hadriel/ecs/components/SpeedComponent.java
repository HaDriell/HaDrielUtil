package fr.hadriel.ecs.components;

import fr.hadriel.serialization.struct.StObject;

/**
 * Created by HaDriel setOn 09/12/2016.
 */
public class SpeedComponent implements NetworkedComponent {

    public float x;
    public float y;
    public float rotation;

    public void serialize(StObject object) {
        object.put("x", x);
        object.put("y", y);
        object.put("rotation", rotation);
    }

    public void deserialize(StObject object) {
        try {
            float x = object.get("x").asFloat();
            float y = object.get("y").asFloat();
            float r = object.get("rotation").asFloat();
            this.x = x;
            this.y = y;
            this.rotation = r;
        } catch (Exception discard) {}
    }
}
