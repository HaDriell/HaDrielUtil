package fr.hadriel.test;

import fr.hadriel.ecs.managers.DirectEntityManager;
import fr.hadriel.ecs.IEntityManager;
import fr.hadriel.ecs.Profile;

public class TestProfileAPI {

    private static final class ComponentA {}
    private static final class ComponentB {}

    public static void main(String[] args) {
        Profile profile = Profile.include(ComponentA.class, ComponentB.class).build();

        IEntityManager manager = new DirectEntityManager();
        long id = manager.create();

        Assert.assertFalse(profile.validate(id, manager), "Invalid Profile validated");
        manager.setComponent(id, new ComponentA());
        Assert.assertFalse(profile.validate(id, manager), "Invalid Profile validated");
        manager.setComponent(id, new ComponentB());
        Assert.assertTrue(profile.validate(id, manager), "Valid Profile Invalidated");
    }
}