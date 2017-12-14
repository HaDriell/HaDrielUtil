package fr.hadriel.core.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public class GUITaskBatch implements GUITask {

    private List<GUITask> batch;

    public GUITaskBatch(GUITask... tasks) {
        this.batch = new ArrayList<>(Arrays.asList(tasks));
    }

    public boolean execute(float dt) {
        batch.removeIf(task -> task.execute(dt));
        return batch.isEmpty();
    }
}
