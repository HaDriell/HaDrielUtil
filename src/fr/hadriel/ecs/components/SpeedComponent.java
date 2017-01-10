package fr.hadriel.ecs.components;

import fr.hadriel.ecs.Component;
import fr.hadriel.serialization.struct.StObject;

/**
 * Created by HaDriel setOn 09/12/2016.
 */
public class SpeedComponent extends Component {

    public float x;
    public float y;
    public float rotation;

    public SpeedComponent() {
        super(true);
    }

    public void save(StObject object) {
        object.put("x", x);
        object.put("y", y);
        object.put("rotation", rotation);
    }

    public void load(StObject object) {
        try {
            float x = object.get("x").asFloat();
            float y = object.get("y").asFloat();
            float r = object.get("rotation").asFloat();
            this.x = x;
            this.y = y;
            this.rotation = r;
        } catch (Exception discard) {}
    }

    public String toString() {
        return String.format("( x : %.2f, y : %.2f, rotation : %.2f)", x, y, rotation);
    }
}
