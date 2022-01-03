package com.frankthefrog.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Scenes.HUD;
import com.frankthefrog.game.Sprites.Player;
import com.frankthefrog.game.Tools.B2WorldCreator;
import com.frankthefrog.game.Tools.WorldContactListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayScreen implements Screen {
    public enum State { RUNNING, GAME_OVER, NEXT}

    private State currentState;
    private HUD hud;
    private Player player;
    private Box2DDebugRenderer b2dr;
    private final OrthographicCamera gameCam;
    private final Viewport gamePort;
    private final TextureAtlas atlas;
    private World world;
    private B2WorldCreator creator;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private BitmapFont gameOverFont;
    private Batch gameBatch ;
    public static int currentLevel = 1;
    private final Frank game;
    public static final List<Vector2> doors = Arrays.asList(
            new Vector2(85.f, 565.f), // 1st level
            new Vector2(2165.f, 805.f), // 2nd Level
            new Vector2(3765.f, 805.f), // 3rd level
            new Vector2(5365.f, 405.f)  // 4th level
    );

    private Stage stage;

    public PlayScreen(Frank game, int currentLevel) {
        /* Sprites atlas */
        atlas = new TextureAtlas("Sprites/frank.atlas");


        /* Game */
        this.game = game;
        this.gameBatch = game.batch;
        this.gameCam = new OrthographicCamera();
        this.gamePort = new FitViewport(Frank.V_WIDTH / Frank.PPM, Frank.V_HEIGHT / Frank.PPM, gameCam );
        gameCam.position.set(gamePort.getWorldWidth() / 2.f, gamePort.getWorldHeight() / 2.f, 0);

        this.currentLevel = currentLevel;

        /* HUD */
        hud = new HUD(game.batch);

        /* Current State */
        this.currentState = State.RUNNING;

        /* Map renderer */
        map = new TmxMapLoader().load("Levels/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Frank.PPM);


        /* World */
        world = new World(new Vector2(0, -10.f), true);
        world.setContactListener(new WorldContactListener());

        /* Box2DDebugRenderer */
        b2dr = new Box2DDebugRenderer();
        b2dr.setDrawBodies(false);

        creator = new B2WorldCreator(this);
        player = new Player(this);

        initMusic();
        initButtons();
        initFont();
    }

    private void initFont() {
        this.gameOverFont = new BitmapFont();
        this.gameOverFont.getData().setScale(3.f);
    }

    private void initButtons() {
        ImageButton leftButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/arrow-left.png"))));
        ImageButton rightButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/arrow-right.png"))));
        ImageButton upButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/arrow-up.png"))));

        Table table = new Table();
        table.setFillParent(true);
        table.bottom();
        table.add(leftButton).colspan(1).expandX().padBottom(40).padLeft(10);
        table.add(rightButton).colspan(1).expandX().padBottom(40);
        table.add(upButton).colspan(15).expandX().align(Align.right).padRight(50).padBottom(10);

        stage = new Stage();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
        leftButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(Player.currentState != Player.State.DEAD && player.b2body.getLinearVelocity().x <= 2) {
                    player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
                    return true;
                }
                return false;
            }
        });

        rightButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(Player.currentState != Player.State.DEAD && player.b2body.getLinearVelocity().x >= -2) {
                    player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
                    return true;
                }
                return false;
            }
        });

        upButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(Player.currentState != Player.State.DEAD && player.b2body.getLinearVelocity().y == 0) {
                    player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
                    return true;
                }
                return false;
            }
        });
    }

    public void nextLevel() {
        currentLevel++;
        player.updateBody();
    }

    private void initMusic() {
        Music music = Frank.manager.get("Sounds/background.mp3", Music.class);
        music.setLooping(true);
        music.play();
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
        if(Player.currentState != Player.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().y == 0) {
                player.b2body.applyLinearImpulse(new Vector2(0, 4.5f), player.b2body.getWorldCenter(), true);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.b2body.getLinearVelocity().x <= 2) {
                player.b2body.applyLinearImpulse(new Vector2(0.3f, 0), player.b2body.getWorldCenter(), true);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().x >= -2) {
                player.b2body.applyLinearImpulse(new Vector2(-0.3f, 0), player.b2body.getWorldCenter(), true);
            }
        }
    }
    public void update(float dt) {
        handleInput();
        if(currentState != State.NEXT)
            player.update(dt);
        if(Player.currentState == Player.State.DEAD) {
            this.currentState = State.GAME_OVER;
        }
        hud.update(dt);
        world.step(1 / 60.f, 6, 2);
        gameCam.position.x = player.b2body.getPosition().x;
        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        switch(currentState) {
            case RUNNING:
                renderer.render();
                b2dr.render(world, gameCam.combined);
                gameBatch.setProjectionMatrix(gameCam.combined);
                gameBatch.begin();
                player.draw(gameBatch);
                gameBatch.end();

                gameBatch.setProjectionMatrix(hud.stage.getCamera().combined);
                hud.stage.draw();

                stage.act(Gdx.graphics.getDeltaTime());
                stage.draw();
                break;
            case GAME_OVER:
                gameBatch.begin();
                GlyphLayout layout = new GlyphLayout();
                layout.setText(gameOverFont, "GAME OVER");
                float width = layout.width;
                float height = layout.height;
                this.gameOverFont.draw(gameBatch, "GAME OVER", (Gdx.graphics.getWidth() - width) / 2.f, (Gdx.graphics.getHeight() - height)/2.f);
                gameBatch.end();
                break;
            default:
                break;
        }
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
        world.destroyBody(player.b2body);
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
