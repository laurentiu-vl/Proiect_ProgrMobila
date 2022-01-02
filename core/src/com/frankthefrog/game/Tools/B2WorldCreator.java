package com.frankthefrog.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Screens.PlayScreen;
import com.frankthefrog.game.Sprites.Coin;
import com.frankthefrog.game.Sprites.Door;
import com.frankthefrog.game.Sprites.Heart;
import com.frankthefrog.game.Sprites.InteractiveTileObject;
import com.frankthefrog.game.Sprites.Key;
import com.frankthefrog.game.Sprites.Lightning;
import com.frankthefrog.game.Sprites.Spike;
import com.frankthefrog.game.Sprites.Star;
import com.frankthefrog.game.Sprites.Trampoline;
import com.frankthefrog.game.Sprites.Wall;

import java.util.ArrayList;
import java.util.List;

public class B2WorldCreator {
    public static List<Wall> walls = new ArrayList<>();
    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape p_shape = new PolygonShape();
        CircleShape c_shape = new CircleShape();
        FixtureDef fdef = new FixtureDef();
        Body body;


        /* Add coins */
        for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2.f)/ Frank.PPM, (rect.getY() + rect.getHeight()/2.f)/Frank.PPM);
            body = world.createBody(bdef);

            c_shape.setRadius(22.f / Frank.PPM);
            fdef.shape = c_shape;
            new Coin(screen, object, body, fdef);
        }

        /* Add bricks */
        for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2.f)/ Frank.PPM, (rect.getY() + rect.getHeight()/2.f)/Frank.PPM);
            body = world.createBody(bdef);

            p_shape.setAsBox(rect.getWidth() / 2.f / Frank.PPM, rect.getHeight() / 2.f / Frank.PPM);
            fdef.shape = p_shape;
            fdef.filter.categoryBits = Frank.GROUND_BIT;
            body.createFixture(fdef);
        }

        /* Add Boxes */
        for(MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2.f)/Frank.PPM, (rect.getY() + rect.getHeight() /2.f - 4.f)/Frank.PPM);
            body = world.createBody(bdef);

            p_shape.setAsBox( 35.f / Frank.PPM,   36.f/ Frank.PPM);
            fdef.shape = p_shape;
            fdef.filter.categoryBits = Frank.GROUND_BIT;
            body.createFixture(fdef);
        }

        /* Add trampolines */
        for(MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() /2.f + 2.f)/Frank.PPM, (rect.getY() + rect.getHeight() /2.f - 10.f)/Frank.PPM);
            body = world.createBody(bdef);

            p_shape.setAsBox( 32.f/ Frank.PPM, 25.f / Frank.PPM);
            fdef.shape = p_shape;
            new Trampoline(screen, object, body, fdef);
        }

        /* Add Lightnings */
        for(MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() /2.f)/Frank.PPM, (rect.getY() + rect.getHeight() /2.f + 2.f)/Frank.PPM);
            body = world.createBody(bdef);

            p_shape.setAsBox( 26.f/ Frank.PPM, 30.f / Frank.PPM);
            fdef.shape = p_shape;
            new Lightning(screen, object, body, fdef);
        }

        /* Add Stars */
        for(MapObject object: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() /2.f)/Frank.PPM, (rect.getY() + rect.getHeight() /2.f)/Frank.PPM);
            body = world.createBody(bdef);

            c_shape.setRadius(33.f / Frank.PPM);
            fdef.shape = c_shape;
            new Star(screen, object, body, fdef);
        }

        /* Add Hearts */
        for(MapObject object: map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() /2.f)/Frank.PPM, (rect.getY() + rect.getHeight() /2.f)/Frank.PPM);
            body = world.createBody(bdef);

            c_shape.setRadius(27.f / Frank.PPM);
            fdef.shape = c_shape;
            new Heart(screen, object, body, fdef);
        }

        /* Add doors */
        for(MapObject object: map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2.f)/Frank.PPM, (rect.getY() + rect.getHeight()/2.f)/Frank.PPM);
            body = world.createBody(bdef);

            p_shape.setAsBox(rect.getWidth() / 2.f / Frank.PPM, rect.getHeight() / 2.f / Frank.PPM);
            fdef.shape = p_shape;
            new Door(screen, object, body, fdef);
        }

        /* Add Walls */
        for(MapObject object: map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2.f)/Frank.PPM, (rect.getY() + rect.getHeight()/2.f)/Frank.PPM);
            body = world.createBody(bdef);

            p_shape.setAsBox(rect.getWidth() / 4.f / Frank.PPM, rect.getHeight() / 2.f / Frank.PPM);
            fdef.shape = p_shape;
            walls.add(new Wall(screen, object, body, fdef));
        }

        /* Add Key */
        for(MapObject object: map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2.f)/ Frank.PPM, (rect.getY() + rect.getHeight()/2.f - 1.f)/Frank.PPM);
            body = world.createBody(bdef);

            p_shape.setAsBox( 24.f / Frank.PPM, 33.f / Frank.PPM);
            fdef.shape = p_shape;
            new Key(screen, object, body, fdef);
        }

        /* Add Spikes */
        for(MapObject object: map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2.f)/ Frank.PPM, (rect.getY() + rect.getHeight()/4.f)/Frank.PPM);
            body = world.createBody(bdef);

            p_shape.setAsBox(rect.getWidth() / 2.f / Frank.PPM, rect.getHeight() / 4.f / Frank.PPM);
            fdef.shape = p_shape;
            new Spike(screen, object, body, fdef);
        }

        /* Add Up-Bricks */
        for(MapObject object: map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2.f)/Frank.PPM, (rect.getY() + 3.f * rect.getHeight()/4.f)/Frank.PPM);
            body = world.createBody(bdef);

            p_shape.setAsBox(rect.getWidth() / 2.f / Frank.PPM, rect.getHeight() / 4.f / Frank.PPM);
            fdef.shape = p_shape;
            fdef.filter.categoryBits = Frank.GROUND_BIT;
            body.createFixture(fdef);
        }
    }
}
