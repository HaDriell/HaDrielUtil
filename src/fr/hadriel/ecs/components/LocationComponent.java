package fr.hadriel.ecs.components;

import fr.hadriel.ecs.Component;
import fr.hadriel.serialization.struct.StObject;

/**
 * Created by HaDriel setOn 09/12/2016.
 */
public class LocationComponent extends Component {

    public float x; // x
    public float y; // y
    public float angle; // angle

    public LocationComponent() {
        super(true);
    }

    public void save(StObject object) {
        object.put("x", x);
        object.put("y", y);
        object.put("angle", angle);
    }

    public void load(StObject object) {
        try {
            float x = object.get("x").asFloat();
            float y = object.get("y").asFloat();
            float angle = object.get("angle").asFloat();
            this.x = x;
            this.y = y;
            this.angle = angle;
        } catch (Exception discard) {}
    }

    public String toString() {
        return String.format("( x : %.2f, y : %.2f, angle : %.2f)", x, y, angle);
    }
}