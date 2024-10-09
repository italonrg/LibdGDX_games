package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.sql.Time;
import java.util.Iterator;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image,tNave,tMissile,tEnemy;// isso cria as imagens que serão convertidas em sprites;
    private Sprite nave,missile;// isso cria o sprite;
    private float posX , posY ,velocity,xMissile,yMissile;// estas variaveis servem para controlar o movimento da nave;
    private boolean attack;//abstração para controle do tiro da nave;
    private Array<Rectangle> enemies;//Os inigos serãm "alocados" dentro de um Array e "dentro"de triangulos;
    private long lastEnemyTime;


    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("bg.png");//fundo
        tNave = new Texture("spaceship.png");//nave
        tMissile = new Texture("missile.png");// missil
        posX = 255;
        posY = 125;
        velocity = 10;
        attack = false;

        nave= new Sprite(tNave);
        missile = new Sprite(tMissile);
        xMissile = posX;
        yMissile = posY;

        tEnemy = new Texture("enemy.png");
        enemies = new Array<Rectangle>();
        lastEnemyTime = 0;
    }

    @Override
    public void render() {
        this.moveNave();
        this.moveMissile();
        this.moveEnemy();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 140, 210);

        if (attack){
            batch.draw(missile ,xMissile + nave.getWidth() /2,yMissile + nave.getHeight() /2 - 12);
        }

        batch.draw(nave ,posX,posY);
        for (Rectangle enemy : enemies){
            batch.draw(tEnemy,enemy.x, enemy.y);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        tNave.dispose();
    }

    private void moveNave(){

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            if (posX < Gdx.graphics.getWidth() - nave.getWidth() ){
                posX += velocity;
            }

        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            if (posX > 0){
                posX -= velocity;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            if (posY < Gdx.graphics.getHeight() - nave.getHeight()){
                posY += velocity;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            if (posY > 0){
                posY -= velocity;
            }
        }
    }
    private void moveMissile(){
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !attack){
            attack = true; //logica: se espaço for pressonado a variavel attack recebe true e o missil tem o incremento no valor, se não ele fica onde está;
            yMissile = posY;
        }
        if (attack){
            if (xMissile < Gdx.graphics.getWidth()){
                xMissile += 30;
            }else {
                xMissile = posX;
                attack = false;
            }
        } else {
            xMissile = posX;
            yMissile = posY;
        }

    }
    private void spanwEnemyes(){
        Rectangle enemy = new Rectangle(Gdx.graphics.getWidth(), MathUtils.random(0,Gdx.graphics.getHeight() - tEnemy.getHeight()),tEnemy.getWidth(),tEnemy.getHeight());
        enemies.add(enemy);//nisso aqui eu coloquei o inimigo criado acima, dentro do Array vazio;
        lastEnemyTime = TimeUtils.nanoTime();
    }
    private void moveEnemy (){

        if (TimeUtils.nanoTime() - lastEnemyTime > 100000000 && enemies.size < 5){
            this.spanwEnemyes();
        }
       // this.spanwEnemyes();
        for (Iterator<Rectangle> iter = enemies.iterator(); iter.hasNext();){
            Rectangle enemy = iter.next();
            //enemy.x -= 3;
            enemy.x -= 400 * Gdx.graphics.getDeltaTime();
            if (enemy.x + tEnemy.getWidth() < 0){
                iter.remove();
            }
        }
    }
}
