package com.frankthefrog.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Sprites.Coin;
import com.frankthefrog.game.Sprites.Door;
import com.frankthefrog.game.Sprites.Heart;
import com.frankthefrog.game.Sprites.Key;
import com.frankthefrog.game.Sprites.Lightning;
import com.frankthefrog.game.Sprites.Player;
import com.frankthefrog.game.Sprites.Spike;
import com.frankthefrog.game.Sprites.Star;
import com.frankthefrog.game.Sprites.Trampoline;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        Player player = (fixA.getFilterData().categoryBits == Frank.PLAYER_BIT ||
                         fixA.getFilterData().categoryBits == Frank.POWER_UP_BIT ||
                         fixA.getFilterData().categoryBits == Frank.BOTTOM_BIT) ? (Player)(fixA.getUserData()) : (Player)(fixB.getUserData());

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch(cDef) {
            case Frank.PLAYER_BIT | Frank.DOOR_BIT:
                Gdx.app.log("Contact", "Touched door");
                Door door = (fixA.getFilterData().categoryBits == Frank.DOOR_BIT) ? (Door) fixA.getUserData() : (Door) fixB.getUserData();
                door.onHit();
                break;
            case Frank.POWER_UP_BIT | Frank.STAR_BIT:
                Gdx.app.log("Contact", "Touched star");
                Star star = (fixA.getFilterData().categoryBits == Frank.STAR_BIT) ? (Star) fixA.getUserData() : (Star) fixB.getUserData();
                star.onHit();
                break;
            case Frank.PLAYER_BIT | Frank.SPIKE_BIT:
                Gdx.app.log("Contact", "Touched spikes");
                Spike spike = (fixA.getFilterData().categoryBits == Frank.SPIKE_BIT) ? (Spike) fixA.getUserData() : (Spike) fixB.getUserData();
                spike.onHit();
                player.b2body.setLinearVelocity(player.b2body.getLinearVelocity().x, 0);
                player.b2body.applyLinearImpulse(new Vector2(0.f, 4.f), player.b2body.getWorldCenter(), true);
                break;
            case Frank.POWER_UP_BIT | Frank.HEART_BIT:
                Gdx.app.log("Contact", "Touched heart");
                Heart heart = (fixA.getFilterData().categoryBits == Frank.HEART_BIT) ? (Heart) fixA.getUserData() : (Heart) fixB.getUserData();
                heart.onHit();
                break;
            case Frank.POWER_UP_BIT | Frank.LIGHTNING_BIT:
                Gdx.app.log("Contact", "Touched lightning");
                Lightning lightning = (fixA.getFilterData().categoryBits == Frank.LIGHTNING_BIT) ? (Lightning) fixA.getUserData() : (Lightning) fixB.getUserData();
                lightning.onHit();
                break;
            case Frank.POWER_UP_BIT | Frank.COIN_BIT:
                Gdx.app.log("Contact", "Touched coins");
                Coin coin = (fixA.getFilterData().categoryBits == Frank.COIN_BIT) ? (Coin) fixA.getUserData() : (Coin) fixB.getUserData();
                coin.onHit();
                break;
            case Frank.BOTTOM_BIT | Frank.TRAMPOLINE_BIT:
                Gdx.app.log("Contact", "Touched trampolines");
                Trampoline tramp = (fixA.getFilterData().categoryBits == Frank.TRAMPOLINE_BIT) ? (Trampoline) (fixA.getUserData()) : (Trampoline) (fixB.getUserData());
                player.b2body.setLinearVelocity(player.b2body.getLinearVelocity().x, 0.f);
                Player.isJumping = true;
                tramp.onHit();
                if(tramp.object.getProperties().containsKey("half"))
                    player.b2body.applyLinearImpulse(new Vector2(0f,6.5f), player.b2body.getWorldCenter(), true);
                else
                    player.b2body.applyLinearImpulse(new Vector2(0f,8.5f), player.b2body.getWorldCenter(), true);
                break;
            case Frank.POWER_UP_BIT | Frank.KEY_BIT:
                Gdx.app.log("Contact", "Touched key");
                Key key = (fixA.getFilterData().categoryBits == Frank.KEY_BIT) ? (Key) fixA.getUserData() : (Key) fixB.getUserData();
                key.onHit();
                break;
            case Frank.BOTTOM_BIT | Frank.GROUND_BIT:
                Gdx.app.log("Contact", "Touched ground");
                Player.isJumping = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
