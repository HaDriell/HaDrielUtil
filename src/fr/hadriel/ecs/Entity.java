package fr.hadriel.ecs;


import fr.hadriel.serialization.struct.StObject;
import fr.hadriel.serialization.struct.StPrimitive;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere setOn 21/11/2016.
 */
public final class Entity {

    private Map<Class, Component> components;
    public final long id;

    public Entity(long id) {
        this.id = id;
        this.components = new HashMap<>();
    }

    public Component getComponent(String className) {
        try {
            return components.get(Class.forName(className));
        } catch (Exception e) {
            throw new RuntimeException("Unable to find class " + className);
        }
    }

    public <T extends Component> T getComponent(Class<T> type) {
        return type.cast(components.get(type));
    }

    public <T extends Component> T createComponent(Class<T> type) {
        try {
            T c = type.newInstance();
            components.put(type, c); //deletes any old version of this component
            return c;
        } catch (Exception e) {
            throw new RuntimeException("Unable to create Component " + type.getSimpleName());
        }
    }

    public <T extends Component> void destroyComponent(Class<T> type) {
        components.remove(type);
    }

    public void save(StObject serial) {
        //put Entity identifier
        serial.put("id", id);

        //put all relevant Components (given options and delta)
        for(Map.Entry<Class, Component> e : components.entrySet()) {
            Class type = e.getKey();
            Component component = e.getValue();
            if(!component.synchronizable) continue; // abort serializing this component (doesn't support serialization)

            //Serialize Component
            StObject componentSerial = new StObject();
            component.save(componentSerial);
            serial.put(type.getName(), componentSerial);
        }
    }

    public void load(StObject serial) {
        if(serial.get("id").asLong() != id) throw new RuntimeException("Invalid ID provided, cannot Update Entity");
        for(Map.Entry<String, StPrimitive> m : serial) {
            if(m.getKey().equals("id")) continue;
            String componentName = m.getKey();
            StObject componentSerial = m.getValue().asStObject();

        }
    }
}