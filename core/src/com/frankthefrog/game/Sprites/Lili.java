package com.frankthefrog.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Screens.PlayScreen;

public class Lili extends Sprite {
    public Lili(PlayScreen screen) {
        super(screen.getAtlas().findRegion("moving"));
        setBounds(0, 0, 50/ Frank.PPM, 100 / Frank.PPM);
        setRegion(new TextureRegion(screen.getAtlas().findRegion("lili"), 0, 0, 50, 100));
        setPosition(10220.f / Frank.PPM, 640.f / Frank.PPM);
    }
}
