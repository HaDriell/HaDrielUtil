package fr.hadriel.ecs;

import fr.hadriel.serialization.struct.StObject;

/**
 * Created by HaDriel on 23/11/2016.
 */
public interface SerializableComponent extends Component {
    public void deserialize(StObject primitive);
    public StObject serialize();
}