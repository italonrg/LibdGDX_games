package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;


import java.awt.*;
import java.sql.Time;
import java.util.Iterator;
import java.util.Random;


/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image, tNave, tMissile, tEnemy;// isso cria as imagens que serão convertidas em sprites;
    private Sprite nave, missile;// isso cria o sprite;
    private float posX, posY, velocity, xMissile, yMissile;// estas variaveis servem para controlar o movimento da nave;
    private boolean attack, gameOver;//abstração para controle do tiro da nave;
    private Array<Rectangle> enemies;//Os inigos serãm "alocados" dentro de um Array e "dentro"de triangulos;
    private long lastEnemyTime;
    private int score, power, numEnemies;// pontuação do jogo & a vida da nave;
    private FreeTypeFontGenerator generator;
    private  FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont bitmapFont;



    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("bg01X.jpg");//fundo
        tNave = new Texture("F16x.png");//nave
        tMissile = new Texture("missile.png");// missil
        posX = 255;
        posY = 125;
        velocity = 10;
        attack = false;

        nave = new Sprite(tNave);
        missile = new Sprite(tMissile);
        xMissile = posX;
        yMissile = posY;

        tEnemy = new Texture("enemy.png");
        enemies = new Array<Rectangle>();
        lastEnemyTime = 0;
        score = 0;
        power = 4;
        numEnemies = 999999999;
        gameOver = false;

        //está e a forma de usar fontes customizadas no projeto;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 30;
        parameter.borderWidth = 1;
        parameter.color = Color.CORAL;
        parameter.borderColor = Color.BLUE;
        bitmapFont = generator.generateFont(parameter);


    }

    @Override
    public void render() {
        this.moveNave();
        this.moveMissile();
        this.moveEnemy();

        int larguraTela = Gdx.graphics.getWidth();
        int alturaTela = Gdx.graphics.getHeight();
        int imgageLargura = image.getWidth();
        int imageAltura = image.getHeight();
        float x = (larguraTela - imgageLargura ) /2 ;
        float y = (alturaTela - imageAltura ) /2 ;

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, x , y );

        if (!gameOver){// isso e a abstração de gameover sou seja o jogo deve parar ao a pontuação chegar a 0;
            if (attack) {
                batch.draw(missile, xMissile + nave.getWidth() / 2, yMissile + nave.getHeight() / 2 - 12);
            }

            batch.draw(nave, posX, posY);
            for (Rectangle enemy : enemies) {
                batch.draw(tEnemy, enemy.x, enemy.y);
            }
            bitmapFont.draw(batch,"Score : " + score, 20, Gdx.graphics.getHeight() - 20);// nisso aqui do Gdx.graphics eu estou pegando a altura e diminuindo por 20;
            bitmapFont.draw(batch,"POWER : " + power, Gdx.graphics.getWidth() -150, Gdx.graphics.getHeight() - 20);// Desenhando o power;
        }else {
            bitmapFont.draw(batch,"Score : " + score, 20, Gdx.graphics.getHeight() - 20);// nisso aqui do Gdx.graphics eu estou pegando a altura e diminuindo por 20;
            bitmapFont.draw(batch,"GAME OVER " + power, Gdx.graphics.getWidth() -150, Gdx.graphics.getHeight() - 20);// Desenhando o power;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            //isso e a logica de recomeço no caso tem que zerar os valores pra que o jogo recomeçe;
            score = 0;
            power= 3;
            posX = 0;
            posY = 0;
            gameOver = false;
        }



        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        tNave.dispose();
    }

    private void moveNave() {

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (posX < Gdx.graphics.getWidth() - nave.getWidth()) {
                posX += velocity;
            }

        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (posX > 0) {
                posX -= velocity;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (posY < Gdx.graphics.getHeight() - nave.getHeight()) {
                posY += velocity;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (posY > 0) {
                posY -= velocity;
            }
        }
    }

    private void moveMissile() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !attack) {
            attack = true; //logica: se espaço for pressonado a variavel attack recebe true e o missil tem o incremento no valor, se não ele fica onde está;
            yMissile = posY;
        }
        if (attack) {
            if (xMissile < Gdx.graphics.getWidth()) {
                xMissile += 30;// isso aqui diz que : quando attack fo true  a posição do eixo Y do xMissile recebe um incremento nisso o missil se move;
            } else {
                xMissile = posX;
                attack = false;
            }
        } else {
            xMissile = posX;
            yMissile = posY;
        }

    }

    private void spanwEnemyes() {
        Rectangle enemy = new Rectangle(Gdx.graphics.getWidth(), MathUtils.random(0, Gdx.graphics.getHeight() - tEnemy.getHeight()), tEnemy.getWidth(), tEnemy.getHeight());
        enemies.add(enemy);//nisso aqui eu coloquei o inimigo criado acima, dentro do Array vazio;
        lastEnemyTime = TimeUtils.nanoTime();
    }

    private void moveEnemy() {//controla o movimento/aparição dos inimigos e a logica de pontuação;

        if (TimeUtils.nanoTime() - lastEnemyTime > numEnemies && enemies.size < Math.random()/2) {
            this.spanwEnemyes();
        }
        for (Iterator<Rectangle> iter = enemies.iterator(); iter.hasNext(); ) {
            Rectangle enemy = iter.next();
            enemy.x -= 400 * Gdx.graphics.getDeltaTime();//isso controla a aparição dos inimigos;

            //Colisão com o missil
            if (collide(enemy.x, enemy.y, enemy.width, enemy.height, xMissile, yMissile, missile.getWidth(), missile.getHeight()) && attack){
             ++score;
                attack = false;
                iter.remove();
                if (score % 10 == 0 ){// esse if aqui e para almentar o numero de inimigos conforme a ponutuação;
                    numEnemies -=100;
                }
                //Colisão com a nave
            }else if (collide(enemy.x, enemy.y, enemy.width, enemy.height,posX, posY, nave.getWidth(),nave.getHeight()) && !gameOver){
                power --;
                if (power <= 0){
                    gameOver = true;
                }
                iter.remove();// iter, aqui e o iterador que contem os retangulos que são os nossos inimigos;
            }

            if (enemy.x + tEnemy.getWidth() < 0) {
                iter.remove();
            }
        }
    }

    private boolean collide(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2 ) {
        if (x1 + w1 > x2 && x1 < x2 + w2 && y1 + h1 > y2 && y1 < y2 + h2) {
            return true;
        }
        return false;

    }

}
