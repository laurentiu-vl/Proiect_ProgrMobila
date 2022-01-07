package com.frankthefrog.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ButtonManager {
    static float width = Gdx.graphics.getWidth();
    static float height = Gdx.graphics.getHeight();

    public static ImageButton skipButton() {
        if(height >= 800 && width >= 1600)
            return new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/skip.png"))));
        return new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/skip-small.png"))));
    }

    public static ImageButton closeButton() {
        if(height >= 800 && width >= 1600)
            return new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/close.png"))));
        return new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/close-small.png"))));
    }

    public static ImageButton upButton() {
        if(height >= 800 && width >= 1600)
            return new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/arrow-up.png"))));
        return new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/arrow-up-small.png"))));
    }

    public static ImageButton leftButton() {
        if(height >= 800 && width >= 1600)
            return new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/arrow-left.png"))));
        return new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/arrow-left-small.png"))));
    }

    public static ImageButton rightButton() {
        if(height >= 800 && width >= 1600)
            return new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/arrow-right.png"))));
        return new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/arrow-right-small.png"))));
    }

    public static ImageButton replayButton() {
        if(height >= 800 && width >= 1600)
            return new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/replay.png"))));
        return new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("UI/replay-small.png"))));
    }
}
