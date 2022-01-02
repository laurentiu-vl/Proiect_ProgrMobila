package com.frankthefrog.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Scenes.HUD;
import com.frankthefrog.game.Screens.PlayScreen;

public class Player extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD}
    public static State currentState, previousState;
    public World world;
    public Body b2body;
    private final TextureRegion frankIdle;
    /*
      private final Animation<TextureRegion> frankWalk;
      private final TextureRegion frankJump;
      private final TextureRegion frankDead;
   */

    private boolean runningRight;
    public static boolean isDead;
    private float stateTimer;

    public Player(PlayScreen screen) {

        super(screen.getAtlas().findRegion("Frank"));
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0.f;
        runningRight = true;

        // Add Textures
        frankIdle = new TextureRegion(screen.getAtlas().findRegion("Frank"), 0, 0, 80, 80);

        defineFrank();
        setBounds(0, 0, 80/ Frank.PPM, 80 / Frank.PPM);
        setRegion(frankIdle);
    }

    private void defineFrank() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(280 / Frank.PPM, 800 / Frank.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        isDead = false;

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(36 / Frank.PPM);
        fdef.filter.categoryBits = Frank.PLAYER_BIT;
        fdef.filter.maskBits =  Frank.GROUND_BIT |
                              //  Frank.STAR_BIT  |
                              //  Frank.HEART_BIT |
                              //  Frank.LIGHTNING_BIT |
                                Frank.SPIKE_BIT |
                              //  Frank.COIN_BIT |
                                Frank.TRAMPOLINE_BIT |
                                Frank.WALL_BIT |
                               // Frank.KEY_BIT |
                                Frank.DOOR_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        fdef.filter.categoryBits = Frank.POWER_UP_BIT;
        fdef.filter.maskBits = Frank.COIN_BIT |
                               Frank.STAR_BIT |
                               Frank.KEY_BIT |
                               Frank.LIGHTNING_BIT |
                               Frank.HEART_BIT;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch(currentState) {
            case DEAD:
                region = frankIdle;
                break;
            case JUMPING:
                HUD.addEnergy(-1.5f * dt);
                region = frankIdle;
                break;
            case RUNNING:
                HUD.addEnergy(-1.5f * dt);
                region = frankIdle;// frankWalk.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                HUD.addEnergy(-1.5f * dt);
            case STANDING:
            default:
                region = frankIdle;
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth()/2.f, b2body.getPosition().y - getWidth() / 2.f );
        setRegion(getFrame(dt));
    }

    public State getState() {
        if(isDead) {
            return State.DEAD;
        } else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.FALLING)) {
            return State.JUMPING;
        } else if(b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if(b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

}
