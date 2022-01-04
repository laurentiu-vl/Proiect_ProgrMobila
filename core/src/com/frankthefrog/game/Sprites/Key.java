package com.frankthefrog.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Screens.PlayScreen;
import com.frankthefrog.game.Tools.B2WorldCreator;
import static com.frankthefrog.game.Screens.PlayScreen.currentLevel;

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
        for(int i = 4 * (currentLevel - 1); i < 4 * currentLevel; i++) {
            B2WorldCreator.walls.get(i).setCategoryFilter(Frank.NOTHING_BIT);
            B2WorldCreator.walls.get(i).removeTile();
        }
    }
}
