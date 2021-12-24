package com.frankthefrog.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Scenes.HUD;
import com.frankthefrog.game.Tools.B2WorldCreator;

public class PlayScreen implements Screen {
    private final HUD hud;
    public Frank game;
    private final OrthographicCamera gameCam;
    private final Viewport gamePort;
    private final TextureAtlas atlas;
    private final World world;
    private final B2WorldCreator creator;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    private final Array<Item> items;

    public PlayScreen(Frank game) {
        atlas = new TextureAtlas();// atlas location
        this.game = game;
        this.gameCam = new OrthographicCamera();
        this.gamePort = new FitViewport(Frank.V_WIDTH / Frank.PPM, Frank.V_HEIGHT / Frank.PPM, gameCam );
        hud = new HUD(game.batch);

        map = new TmxMapLoader().load(); // tmx file location
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Frank.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2.f, gamePort.getWorldHeight() / 2.f, 0);

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        cretor = new B2WorldCreator(this);
        player = new Player(this);

        world.setContactListener(new WorldContactListener());

        // Music
        items = new Array<>();
    }


    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }

    public TextureAtlas getAtlas() {
        return this.atlas;
    }

    public void handleInput() {

    }

    public void update(float dt) {
        handleInput();
        player.update(dt);
        for(Item item: items) {
            item.update(dt);
        }

        hud.update(dt);
        world.step(1 / 60.f, 6, 2,);
        gameCam.position.x = player.b2body.getPosition().x;
        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        // Draw Items

        player.draw(game.batch);
        b2dr.render(world, hud.stage.getCamera().combined);
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
