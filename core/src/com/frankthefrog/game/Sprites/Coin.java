package com.frankthefrog.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Scenes.HUD;
import com.frankthefrog.game.Screens.PlayScreen;

public class Coin extends InteractiveTileObject{
    public Coin(PlayScreen screen, MapObject object, Body body, FixtureDef fdef) {
        super(screen, object, body, fdef);
        fixture.setUserData(this);
        setCategoryFilter(Frank.COIN_BIT);
    }

    @Override
    public void onHit() {
        Frank.manager.get("Sounds/power-up.mp3", Sound.class).play();
        if(object.getProperties().containsKey("bronze")) {
            HUD.addScore(10);
        } else if(object.getProperties().containsKey("silver")){
            HUD.addScore(50);
        } else {
            HUD.addScore(100);
        }
        setCategoryFilter(Frank.NOTHING_BIT);
        removeTile();
    }
}
