package com.frankthefrog.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.math.Rectangle;
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
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.frankthefrog.game.Frank;
import com.frankthefrog.game.Scenes.HUD;
import com.frankthefrog.game.Sprites.Cage;
import com.frankthefrog.game.Sprites.Lili;
import com.frankthefrog.game.Sprites.Player;
import com.frankthefrog.game.Tools.B2WorldCreator;
import com.frankthefrog.game.Tools.WorldContactListener;

import java.util.Arrays;
import java.util.List;

public class PlayScreen implements Screen {
    public enum State { RUNNING, GAME_OVER, PROLOG, EPILOGUE}

    public State currentState;
    private final HUD hud;
    private final Player player;
    private final Box2DDebugRenderer b2dr;
    private final OrthographicCamera gameCam;
    private final Viewport gamePort;
    private final TextureAtlas atlas;
    private final World world;
    public final TiledMap map;
    private float timeCount;
    private final OrthogonalTiledMapRenderer renderer;
    private BitmapFont gameOverFont, introFont, endFont;
    private List<String> introText, endText;
    private final Batch gameBatch ;
    public static int currentLevel = 1;
    private final Cage cage;
    private final Lili lili;
    public static final List<Vector2> doors = Arrays.asList(
            new Vector2(85.f, 565.f), // 1st level
            new Vector2(2165.f, 805.f), // 2nd Level
            new Vector2(3765.f, 805.f), // 3rd level
            new Vector2(5365.f, 405.f),  // 4th level
            new Vector2(7205.f, 85.f) // 5th level
    );

    private Stage interactStage, skipStage, replayStage, gameOverStage;
    public static Music introMusic, backgroundMusic,finalLevelMusic, outroMusic ;
    private final Frank game;
    private Rectangle leftButtonRectangle, rightButtonRectangle, upButtonRectangle;

    public PlayScreen(Frank game) {
        /* Sprites atlas */
        atlas = new TextureAtlas("Sprites/frank.atlas");

        /* Set Time Count*/
        timeCount = 0.f;

        /* Game */
        this.game = game;
        this.gameBatch = game.batch;
        this.gameCam = new OrthographicCamera();
        this.gamePort = new FillViewport(Frank.V_WIDTH / Frank.PPM, Frank.V_HEIGHT / Frank.PPM, gameCam );
        gameCam.position.set(gamePort.getWorldWidth() / 2.f, gamePort.getWorldHeight() / 2.f, 0);

        PlayScreen.currentLevel = 1;

        /* HUD */
        hud = new HUD(game.batch);

        /* Current State */
        this.currentState = State.PROLOG;
        //this.currentState = State.RUNNING;

        /* Map renderer */
        map = new TmxMapLoader().load("Levels/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Frank.PPM);

        /* World */
        world = new World(new Vector2(0, -10.f), true);
        world.setContactListener(new WorldContactListener());

        /* Box2DDebugRenderer */
        b2dr = new Box2DDebugRenderer();
        b2dr.setDrawBodies(false);

        new B2WorldCreator(this);
        lili = new Lili(this);
        cage = new Cage(this);
        player = new Player(this);

        initMusic();
        initInteractButtons();
        initReplayButton();
        initSkipButton();
        initFont();
        initIntro();
        initEnding();
        initRetryButton();
    }

    private void initFont() {
        this.gameOverFont = new BitmapFont();
        this.gameOverFont.getData().setScale(5.f);

        this.introFont = new BitmapFont();
        this.introFont.getData().setScale(3.f);

        this.endFont = new BitmapFont();
        this.endFont.getData().setScale(3.f);
    }

    private void initIntro() {
        this.introText = Arrays.asList(
                "INTRO",
                "It was a beautiful, sunny day",
                "Frank the frog and Lili the lizard were walking down the road",
                "Suddenly, when nobody was watching, LiLi disappeared into a sewer",
                "Frank called her out repeatedly, but he didn't get any response",
                "Now it's his duty to go to save her"
        );
    }

    private void initEnding() {
        this.endText = Arrays.asList(
                "END",
                "Frank was very happy to meet Lili and to set her free",
                "She told him, she was kidnapped by an unknown person and locked up in a cage",
                "The kidnapper told her that he would free her for a certain sum of money",
                "Soon after they found a way out of the sewer and continued their walk",
                "THANKS FOR PLAYING!"
        );
    }

    private Rectangle getBoundingRectangle(ImageButton button) {
        Vector2 coords = new Vector2(button.getX(), button.getY());
        return new Rectangle(coords.x, coords.y, button.getWidth(), button.getHeight());
    }

    private void initInteractButtons() {
        ImageButton leftButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/arrow-left.png"))));
        ImageButton rightButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/arrow-right.png"))));
        ImageButton upButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/arrow-up.png"))));

        leftButton.setPosition(100,  50);
        rightButton.setPosition(130 + leftButton.getWidth(), 50);
        upButton.setPosition(Gdx.graphics.getWidth() - upButton.getWidth() - 100,  50);

        interactStage = new Stage();
        interactStage.addActor(leftButton);
        interactStage.addActor(rightButton);
        interactStage.addActor(upButton);

        leftButtonRectangle = getBoundingRectangle(leftButton);
        rightButtonRectangle = getBoundingRectangle(rightButton);
        upButtonRectangle = getBoundingRectangle(upButton);
    }

    private void initReplayButton() {
        ImageButton button =  new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/replay.png"))));
        Table table = new Table();
        table.setFillParent(true);
        table.bottom().padBottom(40).add(button).align(Align.center);
        replayStage = new Stage();
        replayStage.addActor(table);
        button.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                // Restart game
                outroMusic.stop();
                game.reset();
                return true;
            }
        });
    }

    private void initSkipButton() {
        ImageButton button =  new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/skip.png"))));
        Table table = new Table();
        table.setFillParent(true);
        table.bottom().padBottom(40).add(button).align(Align.right).padRight(50);
        skipStage = new Stage();
        skipStage.addActor(table);
        Gdx.input.setInputProcessor(skipStage);
        button.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                resetTimeCount();
                introMusic.stop();
                introMusic.setPosition(0.f);
                backgroundMusic.play();
                currentState = State.RUNNING;
                Gdx.input.setInputProcessor(interactStage);
                return true;
            }
        });
    }

    private void initRetryButton() {
        ImageButton button =  new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/replay.png"))));
        Table table = new Table();
        table.setFillParent(true);
        table.bottom().padBottom(40).add(button).align(Align.center);
        gameOverStage = new Stage();
        gameOverStage.addActor(table);
        button.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                // Restart game
                outroMusic.stop();
                game.reset();
                return true;
            }
        });
    }

    public void nextLevel() {
        currentLevel++;
        player.updateBody();
        if(currentLevel == 5) {
            backgroundMusic.stop();
            backgroundMusic.setPosition(0.f);
            finalLevelMusic.play();
        }
    }

    private void initMusic() {
        introMusic = Frank.manager.get("Sounds/intro.mp3", Music.class);
        introMusic.setLooping(true);
        introMusic.play();

        backgroundMusic = Frank.manager.get("Sounds/background.mp3", Music.class);
        backgroundMusic.setLooping(true);

        finalLevelMusic = Frank.manager.get("Sounds/final-level.mp3", Music.class);
        finalLevelMusic.setLooping(true);

        outroMusic = Frank.manager.get("Sounds/final.mp3", Music.class);
        outroMusic.setLooping(true);
    }

    public Rectangle getCage() {
        return cage.getBoundingRectangle();
    }

    public World getWorld() {
        return world;
    }

    public TextureAtlas getAtlas() {
        return this.atlas;
    }

    public void resetTimeCount() {
        this.timeCount = 0.f;
    }

    public void handleInput() {
        if(Player.currentState != Player.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().y == 0) {
                Frank.manager.get("Sounds/jump.wav", Sound.class).play();
                player.b2body.applyLinearImpulse(new Vector2(0, 4.5f), player.b2body.getWorldCenter(), true);
                Player.isJumping = true;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.b2body.getLinearVelocity().x <= 2) {
                player.b2body.applyLinearImpulse(new Vector2(0.3f, 0), player.b2body.getWorldCenter(), true);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().x >= -2) {
                player.b2body.applyLinearImpulse(new Vector2(-0.3f, 0), player.b2body.getWorldCenter(), true);
            }

            if(Gdx.input.isTouched()) {
                Vector2 point = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
                // gameCam.unproject(point.set(Gdx.input.getX(), Gdx.input.getY(), 0));
                Gdx.app.log("Point position x", String.valueOf(point.x));
                Gdx.app.log("Point position y", String.valueOf(point.y));
                if(leftButtonRectangle.contains(point.x, point.y) && player.b2body.getLinearVelocity().x >= -2) {
                    player.b2body.applyLinearImpulse(new Vector2(-0.3f, 0), player.b2body.getWorldCenter(), true);
                }

                if(rightButtonRectangle.contains(point.x, point.y) && player.b2body.getLinearVelocity().x <= 2) {
                    player.b2body.applyLinearImpulse(new Vector2(0.3f, 0), player.b2body.getWorldCenter(), true);
                }

                if(upButtonRectangle.contains(point.x, point.y) && player.b2body.getLinearVelocity().y == 0) {
                    Frank.manager.get("Sounds/jump.wav", Sound.class).play();
                    player.b2body.applyLinearImpulse(new Vector2(0, 4.5f), player.b2body.getWorldCenter(), true);
                    Player.isJumping = true;
                }

            }
        }
    }

    public void update(float dt) {
        switch(currentState) {
            case RUNNING:
                handleInput();
                player.update(dt);
                if (Player.currentState == Player.State.DEAD) {
                    this.currentState = State.GAME_OVER;
                    PlayScreen.backgroundMusic.stop();
                    PlayScreen.backgroundMusic.setPosition(0.f);
                    PlayScreen.finalLevelMusic.stop();
                    PlayScreen.finalLevelMusic.setPosition(0.f);
                    Frank.manager.get("Sounds/dead.mp3", Sound.class).play();
                }
                hud.update(dt);
                world.step(1 / 60.f, 6, 2);
                if(player.b2body.getPosition().x + 28.f / Frank.PPM >= gamePort.getWorldWidth() / 2.f) {
                        gameCam.position.x = player.b2body.getPosition().x + 28.f / Frank.PPM;
                        gameCam.update();
                }
                renderer.setView(gameCam);
                break;
            case GAME_OVER:
                Gdx.input.setInputProcessor(gameOverStage);
                break;
            case PROLOG:
                timeCount += dt;
                Gdx.input.setInputProcessor(skipStage);
                break;
            case EPILOGUE:
                timeCount += dt;
                Gdx.input.setInputProcessor(replayStage);
                break;
            default:
                break;
        }
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
                lili.draw(gameBatch);
                cage.draw(gameBatch);
                gameBatch.end();

                gameBatch.setProjectionMatrix(hud.stage.getCamera().combined);
                hud.stage.draw();

                interactStage.act(Gdx.graphics.getDeltaTime());
                interactStage.draw();
                break;
            case GAME_OVER:
                gameBatch.begin();
                GlyphLayout layout = new GlyphLayout();
                layout.setText(gameOverFont, "GAME OVER");
                this.gameOverFont.draw(gameBatch, "GAME OVER", (Gdx.graphics.getWidth() - layout.width) / 2.f, (Gdx.graphics.getHeight() - layout.height)/2.f + 150);
                layout.setText(endFont, Player.deathCause);
                this.endFont.draw(gameBatch, Player.deathCause, (Gdx.graphics.getWidth() - layout.width) / 2.f, (Gdx.graphics.getHeight() - layout.height) / 2.f);
                gameBatch.end();
                gameOverStage.act(Gdx.graphics.getDeltaTime());
                gameOverStage.draw();
                break;
            case PROLOG:
                gameBatch.begin();
                layout = new GlyphLayout();
                float deviceWidth = Gdx.graphics.getWidth();
                float deviceHeight= Gdx.graphics.getHeight();

                layout.setText(gameOverFont, introText.get(0)); // INTRO
                this.gameOverFont.draw(gameBatch, introText.get(0), (deviceWidth -  layout.width) / 2, deviceHeight - layout.height - 15.f);
                if(timeCount >= 2) {
                    layout.setText(introFont, introText.get(1));
                    this.introFont.draw(gameBatch, introText.get(1), (deviceWidth - layout.width) / 2, (deviceHeight - layout.height) / 2 + 2 * 102);
                }

                if(timeCount >= 7) {
                    layout.setText(introFont, introText.get(2));
                    this.introFont.draw(gameBatch, introText.get(2), (deviceWidth - layout.width) / 2, (deviceHeight - layout.height) / 2 +  100);
                }

                if(timeCount >= 12) {
                    layout.setText(introFont, introText.get(3));
                    this.introFont.draw(gameBatch, introText.get(3), (deviceWidth - layout.width) / 2, (deviceHeight - layout.height) / 2 );
                }

                if(timeCount >= 17) {
                    layout.setText(introFont, introText.get(4));
                    this.introFont.draw(gameBatch, introText.get(4), (deviceWidth - layout.width) / 2, (deviceHeight - layout.height) / 2 - 100);
                }

                if(timeCount >= 22) {
                    layout.setText(introFont, introText.get(5));
                    this.introFont.draw(gameBatch, introText.get(5), (deviceWidth - layout.width) / 2, (deviceHeight - layout.height) / 2 - 2 * 100);
                }

                if(timeCount >= 27) {
                    currentState = State.RUNNING;
                    introMusic.stop();
                    introMusic.setPosition(0.f);
                    backgroundMusic.play();
                    resetTimeCount();
                }
                gameBatch.end();

                skipStage.act(Gdx.graphics.getDeltaTime());
                skipStage.draw();
                break;
            case EPILOGUE:
                gameBatch.begin();
                layout = new GlyphLayout();
                deviceWidth = Gdx.graphics.getWidth();
                deviceHeight= Gdx.graphics.getHeight();

                layout.setText(gameOverFont, endText.get(0)); // INTRO
                this.gameOverFont.draw(gameBatch, endText.get(0), (deviceWidth -  layout.width) / 2, deviceHeight - layout.height - 15.f);
                if(timeCount >= 2) {
                    layout.setText(endFont, endText.get(1));
                    this.endFont.draw(gameBatch, endText.get(1), (deviceWidth - layout.width) / 2, (deviceHeight - layout.height) / 2 + 2 * 100);
                }

                if(timeCount >= 7) {
                    layout.setText(endFont, endText.get(2));
                    this.endFont.draw(gameBatch, endText.get(2), (deviceWidth - layout.width) / 2, (deviceHeight - layout.height) / 2 +  100);
                }

                if(timeCount >= 12) {
                    layout.setText(endFont, endText.get(3));
                    this.endFont.draw(gameBatch, endText.get(3), (deviceWidth - layout.width) / 2, (deviceHeight - layout.height) / 2 );
                }

                if(timeCount >= 17) {
                    layout.setText(endFont, endText.get(4));
                    this.endFont.draw(gameBatch, endText.get(4), (deviceWidth - layout.width) / 2, (deviceHeight - layout.height) / 2 - 100);
                }

                if(timeCount >= 22) {
                    layout.setText(gameOverFont, endText.get(5));
                    this.gameOverFont.draw(gameBatch, endText.get(5), (deviceWidth - layout.width) / 2, (deviceHeight - layout.height) / 2 - 2 * 100);
                }
                gameBatch.end();

                replayStage.act(Gdx.graphics.getDeltaTime());
                replayStage.draw();
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
        introMusic.dispose();
        outroMusic.dispose();
        finalLevelMusic.dispose();
        backgroundMusic.dispose();
    }
}
