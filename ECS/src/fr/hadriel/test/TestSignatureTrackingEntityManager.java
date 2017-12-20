package fr.hadriel.test;

import fr.hadriel.ecs.DirectEntityManager;
import fr.hadriel.ecs.SignatureTrackingEntityManager;

public class TestSignatureTrackingEntityManager {

    //Example Components
    private static final class AnyComponent {}

    public static void main(String[] args) {
        SignatureTrackingEntityManager manager = new SignatureTrackingEntityManager(new DirectEntityManager());

        long id = manager.create();
        Assert.assertFalse(manager.getEditedEntities().isEmpty(), "Creation not detected");
        manager.clearEditedEntities();

        manager.setComponent(id, new AnyComponent());
        Assert.assertFalse(manager.getEditedEntities().isEmpty(), "Component Addition undetected");
        manager.clearEditedEntities();

        manager.setComponent(id, new AnyComponent());
        Assert.assertTrue(manager.getEditedEntities().isEmpty(), "Component Modification detected (should NOT !)");
        manager.clearEditedEntities();

        manager.removeComponent(id, AnyComponent.class);
        Assert.assertFalse(manager.getEditedEntities().isEmpty(), "Component Removal undetected");
        manager.clearEditedEntities();

        manager.destroy(id);
        Assert.assertFalse(manager.getEditedEntities().isEmpty(), "Destruction not detected");
        manager.clearEditedEntities();
    }
}
