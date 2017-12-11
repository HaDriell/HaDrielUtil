package fr.hadriel;

import fr.hadriel.core.media.MediaTask;
import fr.hadriel.core.media.Medias;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public class TestMedias {

    private static class DummyAction extends MediaTask {
        public boolean done;

        public DummyAction(boolean done) {
            this.done = done;
        }

        public void onBegin() {
            System.out.println("\tbegin action");
        }

        public boolean onExecute(float dt) {
            return done;
        }

        public void onEnd() {
            System.out.println("\tend action");
        }
    }

    public static void main(String[] args) {
        System.out.println("Submitting 10 DummyActions that execute immediately");
        for(int i = 0; i < 10; i++)
            Medias.execute(new DummyAction(true));

        System.out.println("Waiting 1 second");
        try { Thread.sleep(1000); } catch (InterruptedException ignore) {}

        System.out.println("Submitting 1 DummyAction that is persistent");
        DummyAction persistent = new DummyAction(false);
        Medias.execute(persistent);

        System.out.println("Waiting 3 seconds");
        try { Thread.sleep(3000); } catch (InterruptedException ignore) {}
        System.out.println("Allowing persistent MediaTask to finish");
        persistent.done = true;
        System.out.println("Waiting 1 second");
        try { Thread.sleep(1000); } catch (InterruptedException ignore) {}
    }
}
