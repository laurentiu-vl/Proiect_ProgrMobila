package com.frankthefrog.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Scenes.HUD;
import com.frankthefrog.game.Screens.PlayScreen;

public class Star extends InteractiveTileObject{
    public Star(PlayScreen screen, MapObject object, Body body, FixtureDef fdef) {
        super(screen, object, body, fdef);
        fixture.setUserData(this);
        setCategoryFilter(Frank.STAR_BIT);
    }

    @Override
    public void onHit() {
        Frank.manager.get("Sounds/star.wav", Sound.class).play();
        HUD.addScore(100);
        setCategoryFilter(Frank.NOTHING_BIT);
        removeTile();
    }
}
