package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image,tNave;// isso cria a imagem que vai ter que ser convertida num sptite;
    private Sprite nave;// isso cria o spite;
    private float posX , posY;// estas variaveis servem para controlar o movimento da nave;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("bg.png");//fundo
        tNave = new Texture("spaceship.png");//nave
        nave= new Sprite(tNave);
        posX = 0;
        posY = 0;
    }

    @Override
    public void render() {
        this.moveNave();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 140, 210);
        batch.draw(nave ,posX,posY);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        tNave.dispose();
    }

    private void moveNave(){

        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            posX += 10;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            posX -= 10;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            posY += 10;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            posY -= 10;
        }
    }
}
