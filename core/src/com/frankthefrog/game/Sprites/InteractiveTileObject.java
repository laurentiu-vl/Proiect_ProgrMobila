package com.frankthefrog.game.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Screens.PlayScreen;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected Rectangle bounds;
    public Body body;
    protected Fixture fixture;
    protected PlayScreen screen;
    public MapObject object;

    public InteractiveTileObject(PlayScreen screen, MapObject object, Body body, FixtureDef fdef) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.object = object;
        this.bounds = ((RectangleMapObject) object).getRectangle();
        this.body = body;
        this.fixture = body.createFixture(fdef);
    }

    public abstract void onHit();

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public void removeTile() {
        TiledMapTileLayer layer =  (TiledMapTileLayer)map.getLayers().get(1);
        layer.getCell((int)(body.getPosition().x * Frank.PPM / 80), (int)(body.getPosition().y * Frank.PPM / 80)).setTile(null);
    }
}
