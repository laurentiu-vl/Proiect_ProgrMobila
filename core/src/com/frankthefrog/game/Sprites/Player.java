package com.frankthefrog.game.Sprites;

import static com.frankthefrog.game.Screens.PlayScreen.currentLevel;
import static com.frankthefrog.game.Screens.PlayScreen.doors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Scenes.HUD;
import com.frankthefrog.game.Screens.PlayScreen;

import java.util.ArrayList;
import java.util.List;

public class Player extends Sprite {
    public enum State { JUMPING, STANDING, RUNNING, DEAD}
    public static State currentState;
    public World world;
    public List<Body> b2bodies = new ArrayList<>();
    public Body b2body ;
    private final PlayScreen screen;
    private final TextureRegion frankIdle;
    private final TextureRegion frankJump;
    private boolean runningRight;
    public static boolean isDead, isJumping;
    public static String deathCause;

    public Player(PlayScreen screen) {
        super(screen.getAtlas().findRegion("moving"));
        this.world = screen.getWorld();
        currentState = State.STANDING;
        runningRight = true;
        this.screen = screen;
        isJumping = true;
        isDead = false;

        // Add Textures
        frankIdle = new TextureRegion(screen.getAtlas().findRegion("moving"), 0, 0, 60, 60);
        frankJump = new TextureRegion(screen.getAtlas().findRegion("jump"), 0, 0, 60, 60);
        defineFrank();
        setBounds(0, 0, 60/ Frank.PPM, 60 / Frank.PPM);
        setRegion(frankIdle);
    }

    public void defineFrank() {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        for(int i = 0; i < 5; i++) {
            bdef.position.set(doors.get(i).x / Frank.PPM, doors.get(i).y / Frank.PPM);
            this.b2bodies.add(world.createBody(bdef));
        }

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(28 / Frank.PPM);
        fdef.filter.categoryBits = Frank.PLAYER_BIT;
        fdef.filter.maskBits =  Frank.GROUND_BIT |
                                Frank.SPIKE_BIT |
                                Frank.WALL_BIT |
                                Frank.TRAMPOLINE_BIT |
                                Frank.DOOR_BIT;
        fdef.shape = shape;
        for(int i = 0; i < 5; i++)
            b2bodies.get(i).createFixture(fdef).setUserData(this);

        fdef.filter.categoryBits = Frank.POWER_UP_BIT;
        fdef.filter.maskBits = Frank.COIN_BIT | Frank.STAR_BIT | Frank.KEY_BIT | Frank.LIGHTNING_BIT | Frank.HEART_BIT;
        fdef.isSensor = true;
        for(int i = 0; i < 5; i++)
            b2bodies.get(i).createFixture(fdef).setUserData(this);

        EdgeShape bottom = new EdgeShape();
        bottom.set(new Vector2(-20.f / Frank.PPM, -28.f / Frank.PPM), new Vector2(20.f / Frank.PPM, -28.f / Frank.PPM));
        fdef.filter.categoryBits = Frank.BOTTOM_BIT;
        fdef.filter.maskBits = Frank.GROUND_BIT | Frank.TRAMPOLINE_BIT ;
        fdef.shape = bottom;
        fdef.isSensor = true;
        for(int i =0; i < 5; i++)
            b2bodies.get(i).createFixture(fdef).setUserData(this);
        b2body = b2bodies.get(0);
    }

    public void updateBody() {
        b2body = b2bodies.get(currentLevel - 1);
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch(currentState) {
            case JUMPING:
                HUD.addEnergy(-1.5f * dt);
                region = frankJump;
                break;
            case RUNNING:
                HUD.addEnergy(-1.5f * dt);
                region = frankIdle;
                break;
            case STANDING:
            case DEAD:
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
        return region;
    }

    public void update(float dt) {
        if(getBoundingRectangle().overlaps(screen.getCage())) {
            screen.currentState = PlayScreen.State.EPILOGUE;
            PlayScreen.finalLevelMusic.stop();
            PlayScreen.finalLevelMusic.setPosition(0.f);
            PlayScreen.outroMusic.play();
        }
        setPosition(b2body.getPosition().x - getWidth()/2.f, b2body.getPosition().y - getWidth() / 2.f );
        setRegion(getFrame(dt));
    }


    public State getState() {
        if(isDead) {
            return State.DEAD;
        } else if(b2body.getLinearVelocity().y != 0 || isJumping) {
            return State.JUMPING;
        }  else if(b2body.getLinearVelocity().x != 0 && b2body.getLinearVelocity().y == 0 && !isJumping) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

}
