package com.frankthefrog.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Scenes.HUD;
import com.frankthefrog.game.Screens.PlayScreen;

public class Heart extends InteractiveTileObject {
    public Heart(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Frank.COIN_BIT);
    }

    @Override
    public void onHit(Player player) {
        Frank.manager.get("audio/sound/heart.wav", Sound.class).play();
        // add a life
        getCell().setTile(null);
    }
}