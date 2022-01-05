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

        Player player;
        if(fixA.getFilterData().categoryBits == Frank.PLAYER_BIT || fixA.getFilterData().categoryBits == Frank.POWER_UP_BIT || fixA.getFilterData().categoryBits == Frank.HEAD_BIT)
            player = (Player)(fixA.getUserData());
        else
            player = (Player)(fixB.getUserData());


        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch(cDef) {
            case Frank.PLAYER_BIT | Frank.DOOR_BIT:
                Gdx.app.log("Contact", "Touched door");
                if(fixA.getFilterData().categoryBits == Frank.DOOR_BIT)
                    ((Door) fixA.getUserData()).onHit();
                else
                    ((Door) fixB.getUserData()).onHit();
                break;
            case Frank.POWER_UP_BIT | Frank.STAR_BIT:
                if(fixA.getFilterData().categoryBits == Frank.STAR_BIT)
                    ((Star) fixA.getUserData()).onHit();
                else
                    ((Star) fixB.getUserData()).onHit();
                break;
            case Frank.PLAYER_BIT | Frank.SPIKE_BIT:
                Gdx.app.log("Contact", "Touched spikes");
                if(fixA.getFilterData().categoryBits == Frank.SPIKE_BIT)
                    ((Spike) fixA.getUserData()).onHit();
                else
                    ((Spike) fixB.getUserData()).onHit();
                player.b2body.setLinearVelocity(player.b2body.getLinearVelocity().x, 0);
                player.b2body.applyLinearImpulse(new Vector2(0.f, 4.f), player.b2body.getWorldCenter(), true);
                break;
            case Frank.POWER_UP_BIT | Frank.HEART_BIT:
                Gdx.app.log("Contact", "Touched heart");
                if(fixA.getFilterData().categoryBits == Frank.HEART_BIT)
                    ((Heart) fixA.getUserData()).onHit();
                else
                    ((Heart) fixB.getUserData()).onHit();
                break;
            case Frank.POWER_UP_BIT | Frank.LIGHTNING_BIT:
                Gdx.app.log("Contact", "Touched lightning");
                if(fixA.getFilterData().categoryBits == Frank.LIGHTNING_BIT)
                    ((Lightning) fixA.getUserData()).onHit();
                else
                    ((Lightning) fixB.getUserData()).onHit();
                break;
            case Frank.POWER_UP_BIT | Frank.COIN_BIT:
                Gdx.app.log("Contact", "Touched coins");
                if(fixA.getFilterData().categoryBits == Frank.COIN_BIT)
                    ((Coin) fixA.getUserData()).onHit();
                else
                    ((Coin) fixB.getUserData()).onHit();
                break;
            case Frank.PLAYER_BIT | Frank.TRAMPOLINE_BIT:
                Gdx.app.log("Contact", "Touched trampolines");
                Trampoline tramp;
                if(fixA.getFilterData().categoryBits == Frank.TRAMPOLINE_BIT)
                    tramp = (Trampoline) (fixA.getUserData());
                else
                    tramp = (Trampoline) (fixB.getUserData());

                float playerPos  = player.b2body.getPosition().x ;
                float trampPos = tramp.body.getPosition().x;

                if(player.b2body.getPosition().y >= tramp.body.getPosition().y + 50.f / Frank.PPM ||
                        (player.b2body.getLinearVelocity().y < 0 && playerPos <= trampPos  + 64.f / Frank.PPM && playerPos + 56.f / Frank.PPM >=  trampPos )) {
                    player.b2body.setLinearVelocity(player.b2body.getLinearVelocity().x, 0.f);
                    if(tramp.object.getProperties().containsKey("half"))
                        player.b2body.applyLinearImpulse(new Vector2(0f,6.5f), player.b2body.getWorldCenter(), true);
                    else
                        player.b2body.applyLinearImpulse(new Vector2(0f,8.5f), player.b2body.getWorldCenter(), true);
                }
                break;
            case Frank.POWER_UP_BIT | Frank.KEY_BIT:
                Gdx.app.log("Contact", "Touched key");
                if(fixA.getFilterData().categoryBits == Frank.KEY_BIT)
                    ((Key) fixA.getUserData()).onHit();
                else
                    ((Key) fixB.getUserData()).onHit();
                break;
            case Frank.HEAD_BIT | Frank.GROUND_BIT:
                Gdx.app.log("Contact", "Touched with head");
                player.isHeadTouched = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if(cDef == (Frank.HEAD_BIT | Frank.GROUND_BIT)) {
            Player player;
            if (fixA.getFilterData().categoryBits == Frank.HEAD_BIT)
                player = (Player) (fixA.getUserData());
            else
                player = (Player) (fixB.getUserData());
            player.isHeadTouched = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
