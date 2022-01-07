package com.frankthefrog.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.frankthefrog.game.Sprites.Player;

public class HUD implements Disposable {
    public Stage stage;

    @Override
    public void dispose() {
        stage.dispose();
    }

    private Integer worldTimer;
    private float timeCount;
    private static Integer score, lives;
    private static Float energy;
    private static Label scoreLabel, energyLabel, livesLabel;
    private final Label timeLabel;

    @SuppressWarnings("DefaultLocale")
    public HUD(SpriteBatch sb) {
        worldTimer = score = 0;
        timeCount = 0;
        energy = 200.f;
        lives = 3;

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage =  new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);

        Table labelTable = new Table();
        labelTable.top();
        labelTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        BitmapFont font = new BitmapFont();
        font.getData().setScale(3);

        /* Time Label */
        Label timeTextLabel = new Label("TIME", new Label.LabelStyle(font, Color.WHITE));
        timeLabel = new Label(String.format("%04d", worldTimer), new Label.LabelStyle(font, Color.WHITE));

        /* Score Label  */
        scoreLabel = new Label(String.format("%04d", score), new Label.LabelStyle(font, Color.WHITE));
        Label scoreTextLabel = new Label("SCORE", new Label.LabelStyle(font, Color.WHITE));

        /* Energy Label */
        Label energyTextLabel = new Label("ENERGY", new Label.LabelStyle(font, Color.WHITE));
        energyLabel = new Label(String.format("%03d", Math.round(energy)), new Label.LabelStyle(font, Color.WHITE));

        /* Lives Label */
        Label livesTextLabel = new Label("LIVES", new Label.LabelStyle(font, Color.WHITE));
        livesLabel = new Label(String.format("%02d", lives), new Label.LabelStyle(font, Color.WHITE));

        labelTable.add(timeTextLabel).expandX().padTop(15);
        labelTable.add(scoreTextLabel).expandX().padTop(15);
        labelTable.add(energyTextLabel).expandX().padTop(15);
        labelTable.add(livesTextLabel).expandX().padTop(15);
        labelTable.row();
        labelTable.add(timeLabel).expandX();
        labelTable.add(scoreLabel).expandX();
        labelTable.add(energyLabel).expandX();
        labelTable.add(livesLabel).expandX();
        stage.addActor(labelTable);
    }


    @SuppressWarnings("DefaultLocale")
    public void update(float dt) {
        timeCount += dt;
        if(timeCount >= 1) {
            worldTimer++;
            timeLabel.setText(String.format("%04d", worldTimer));
            timeCount = 0;
        }

        if(lives <= 0) {  // Game Over
            Player.deathCause = "Out of lives";
            Player.isDead = true;
        }

        if(energy <= 0.f) {
            Player.deathCause = "Out of energy";
            Player.isDead = true;
        }
    }

    @SuppressWarnings("DefaultLocale")
    public static void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format("%04d", score));
    }


    @SuppressWarnings("DefaultLocale")
    public static void addEnergy(float energyValue) {
        energy += energyValue;
        energyLabel.setText(String.format("%04d", Math.round(energy)));
    }

    @SuppressWarnings("DefaultLocale")
    public static void addLife() {
        lives++;
        livesLabel.setText(String.format("%02d", Math.round(lives)));
    }

    @SuppressWarnings("DefaultLocale")
    public static void removeLife() {
        lives--;
        livesLabel.setText(String.format("%02d", Math.round(lives)));
    }


}
