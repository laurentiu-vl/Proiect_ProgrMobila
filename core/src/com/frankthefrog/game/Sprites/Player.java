package com.frankthefrog.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Screens.PlayScreen;

public class Player extends Sprite {
    public void hit() {

    }

    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD}
    public State currentState, previousState;
    public World world;
    public Body b2body;
    private TextureRegion frankIdle;
    private final Animation<TextureRegion> frankWalk;
    private final TextureRegion frankJump;
    private final TextureRegion frankDead;
    private boolean runningRight;
    private boolean isDead;
    private float stateTimer;

    public Frank(PlayScreen screen) {
        super(screen.getAtlas().findRegion("frank"));
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0.f;
        runningRight = true;

        // Add Textures

        defineFrank();
        frankIdle = new TextureRegion(getTexture(), 0, 0, 16, 16);
        setBounds(0, 0, 80/ Frank.PPM, 80 / Frank.PPM);
        setRegion(frankIdle);
    }

    private void defineFrank() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(80 / Frank.PPM, 80 / Frank.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        isDead = false;

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Frank.PPM);
        fdef.filter.categoryBits = Frank.PLAYER_BIT;
        fdef.filter.maskBits = Frank.GROUND_BIT |
                               Frank.COIN_BIT |
                               Frank.STAR_BIT |
                               Frank.HEART_BIT |
                               Frank.LIGHTNING_BIT |
                               Frank.SPIKE_BIT |
                               Frank.COIN_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch(currentState) {
            case DEAD:
                region = frankDead;
                break;
            case JUMPING:
                region = frankJump;
                break;
            case RUNNING:
                region = frankWalk.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
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

    public State getState() { \
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
