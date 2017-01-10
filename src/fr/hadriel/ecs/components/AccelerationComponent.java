package fr.hadriel.ecs.components;

import fr.hadriel.ecs.Component;
import fr.hadriel.serialization.struct.StObject;

/**
 * Created by HaDriel setOn 09/12/2016.
 */
public class AccelerationComponent extends Component {

    public float x;
    public float y;
    public float torque;

    public AccelerationComponent() {
        super(true);
    }

    public void save(StObject object) {
        object.put("x", x);
        object.put("y", y);
        object.put("torque", torque);
    }

    public void load(StObject object) {
        try {
            float x = object.get("x").asFloat();
            float y = object.get("y").asFloat();
            float t = object.get("torque").asFloat();
            this.x = x;
            this.y = y;
            this.torque = t;
        } catch (Exception discard) {}
    }

    public String toString() {
        return String.format("( x : %.2f, y : %.2f, torque : %.2f)", x, y, torque);
    }
}
