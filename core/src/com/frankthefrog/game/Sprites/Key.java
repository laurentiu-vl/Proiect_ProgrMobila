package com.frankthefrog.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Screens.PlayScreen;
import com.frankthefrog.game.Tools.B2WorldCreator;

public class Key extends InteractiveTileObject {
    public Key(PlayScreen screen, MapObject object, Body body, FixtureDef fdef) {
        super(screen, object, body, fdef);
        fixture.setUserData(this);
        setCategoryFilter(Frank.KEY_BIT);
    }

    @Override
    public void onHit() {
        Frank.manager.get("Sounds/power-up.mp3", Sound.class).play();
        setCategoryFilter(Frank.NOTHING_BIT);
        removeTile();

        /* Open the doors */
        for(Wall wall: B2WorldCreator.walls) {
            wall.setCategoryFilter(Frank.NOTHING_BIT);
            wall.removeTile();
        }
    }
}
