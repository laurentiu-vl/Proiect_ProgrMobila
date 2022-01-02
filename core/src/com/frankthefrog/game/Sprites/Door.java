package com.frankthefrog.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Screens.PlayScreen;

public class Door extends InteractiveTileObject{
    public Door(PlayScreen screen, MapObject object, Body body, FixtureDef fdef) {
        super(screen, object, body, fdef);
        fixture.setUserData(this);
        setCategoryFilter(Frank.DOOR_BIT);
    }

    @Override
    public void onHit() {
        //Frank.manager.get("Sounds/power-up.mp3", Sound.class).play();
        this.screen.nextLevel();
    }
}
