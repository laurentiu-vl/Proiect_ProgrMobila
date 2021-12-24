package com.frankthefrog.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Screens.PlayScreen;

public class Spike extends InteractiveTileObject{
    public Spike(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Frank.COIN_BIT);
    }

    @Override
    public void onHit(Player player) {
        Frank.manager.get("audio/sound/spike.wav", Sound.class).play();
        // subtract 1 life
        getCell().setTile(null);
    }
}
