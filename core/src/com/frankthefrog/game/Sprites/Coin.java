package com.frankthefrog.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Scenes.HUD;
import com.frankthefrog.game.Screens.PlayScreen;

public class Coin extends InteractiveTileObject{
    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Frank.COIN_BIT);
    }

    @Override
    public void onHit(Player player) {
        Frank.manager.get("audio/sound/coin.wav", Sound.class).play();
        if(object.getProperties().containsKey("bronze")) {
            HUD.addScore(10);
        } else if(object.getProperties().containsKey("silver")){
            HUD.addScore(50);
        } else {
            HUD.addScore(100);
        }
        getCell().setTile(null);
    }
}
