package com.frankthefrog.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Screens.PlayScreen;

public class Cage extends Sprite {
    public Cage(PlayScreen screen) {
        super(screen.getAtlas().findRegion("moving"));
        setBounds(0, 0, 160/ Frank.PPM, 160 / Frank.PPM);
        setRegion(new TextureRegion(screen.getAtlas().findRegion("cage"), 0, 0, 160, 160));
        setPosition(10160.f / Frank.PPM, 640.f / Frank.PPM);
    }
}