package fr.hadriel;

import fr.hadriel.core.asset.Assets;
import fr.hadriel.graphics.font.Font;

/**
 * Created by gauti on 28/02/2018.
 */
public class TestFont {

    public static void main(String[] args) {
        Font font = new Font("Media/Arial.fnt");
        Assets.load("Arial", font, Font.class);
    }
}