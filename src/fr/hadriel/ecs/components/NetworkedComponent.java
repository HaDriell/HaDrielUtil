package fr.hadriel.ecs.components;

import fr.hadriel.ecs.Component;
import fr.hadriel.serialization.struct.StObject;

/**
 * Created by HaDriel setOn 09/12/2016.
 */
public interface NetworkedComponent extends Component {
    public void serialize(StObject object);
    public void deserialize(StObject object);
}