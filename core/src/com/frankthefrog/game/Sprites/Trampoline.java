package com.frankthefrog.game.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Screens.PlayScreen;

public class Trampoline extends InteractiveTileObject{
    public Trampoline(PlayScreen screen, MapObject object, Body body, FixtureDef fdef) {
        super(screen, object, body, fdef);
        fixture.setUserData(this);
        setCategoryFilter(Frank.TRAMPOLINE_BIT);
    }

    @Override
    public void onHit() {
        //Frank.manager.get("Sounds/power-up.mp3", Sound.class).play();

    }
}
