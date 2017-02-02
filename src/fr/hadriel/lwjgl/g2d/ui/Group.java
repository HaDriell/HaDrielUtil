package fr.hadriel.lwjgl.g2d.ui;

import fr.hadriel.lwjgl.g2d.BatchGraphics;

/**
 * Created by glathuiliere on 01/02/2017.
 */
public class Group extends Node {

    public Group() {
        super(false); //forward events even if they're not hitting the Group (no need to handle Size then)
    }

    //Group's purpose is to focus on Grouping nodes, not rendering things
    protected void onRender(BatchGraphics g) {}
}
