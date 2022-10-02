package lawnlayer;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.io.*;
import java.lang.Math;

import processing.core.PFont;

/**
 * App that draws at 60 FPS
*/
public class App extends PApplet {
    
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int TOPBAR = 80;
    public static final int FPS = 60;

    public String configPath;
    
    private PImage concreteImage;
    private PImage dirtImage;
    private PImage wormImage;
    private PImage beetleImage;
    private PImage iceImage;
    private PImage rocketImage;
    
    private Map map;
    private Random rand;
    private ArrayList<Enemy> enemies;
    private Ball ball;
    
    private PFont font;
    private int level;
    private int maxLevel;
    private int maxLives;

    private int timer;
    private String powerUp;
    private String activePowerUp;
    private boolean frozen;
    private MyPoint powerUpPoint;
    private JSONObject json;


    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT); 
        
    }


    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);
        this.rand = new Random();
        this.font = createFont("Arial", 32, true);
        this.json = loadJSONObject(this.configPath);
        this.level = 0;
        this.maxLevel = this.json.getJSONArray("levels").size();

        String mapFile = this.json.getJSONArray("levels").getJSONObject(this.level).getString("outlay");
        int goal = (int) (this.json.getJSONArray("levels").getJSONObject(this.level).getFloat("goal") * 100);

        this.maxLives = this.json.getInt("lives");
        
        // Load images during setup
        this.concreteImage = loadImage(this.getClass().getResource("concrete_tile.png").getPath());
        this.dirtImage = loadImage(this.getClass().getResource("dirt.png").getPath());
        this.wormImage = loadImage(this.getClass().getResource("worm.png").getPath());
        this.beetleImage = loadImage(this.getClass().getResource("beetle.png").getPath());
        this.iceImage = loadImage(this.getClass().getResource("ice.png").getPath());
        this.rocketImage = loadImage(this.getClass().getResource("rocket.png").getPath());

        PImage ballImage = loadImage(this.getClass().getResource("ball.png").getPath());
        PImage grassImage = loadImage(this.getClass().getResource("grass.png").getPath());
        PImage greenImage = loadImage(this.getClass().getResource("green.png").getPath());
        PImage redImage = loadImage(this.getClass().getResource("red.png").getPath());
    
        this.map = new Map(mapFile, this.maxLives, goal, this.concreteImage, this.dirtImage);
        
        spawnEnemies();
        
        ball = new Ball(ballImage, grassImage, greenImage, redImage, dirtImage, this.map, this.enemies);
    }
	
    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {
        background(94, 60, 33);
        textFont(this.font);
        text("Lives: " + this.map.getLives(), 100, 40);
        text(String.format("%d/%d%%", this.map.getProgress(), this.map.getMaxProgress()), 400, 40);
        text(this.activePowerUp + ": " + (int) Math.ceil((float)(this.timer / 60)), 800, 40);
        timer -= 1;
        if (timer == 0) {
            spawnPowerUp();
            timer = 600;
        }

        textFont(this.font, 24);

        text("Level " + (this.level + 1), 1180, 60);

        this.map.display(this);

        this.ball.tick();
        this.ball.draw(this);

        for (Enemy i : enemies) {
            if (!this.frozen) {
                i.tick();
            }
            i.draw(this);
        }

        if (!this.powerUp.equals("None")) {

            int powerUpX = this.powerUpPoint.getX();
            int powerUpY = this.powerUpPoint.getY();
            int x = Math.round((float) (this.ball.getX() / 20));
            int y = Math.round((float) (this.ball.getY() / 20));

            if (this.powerUp.equals("Freeze Enemies")) {
                this.image(this.iceImage, powerUpX * 20, powerUpY * 20 + 80);
            }
            else if (this.powerUp.equals("Double Speed")) {
                this.image(this.rocketImage, powerUpX * 20, powerUpY * 20 + 80);
            }

            
            if (!this.map.getName(powerUpX, powerUpY).equals("DIRT")) {
                this.activePowerUp = "No PowerUp";
                this.powerUp = "None";
                this.timer = 600;
            }

            else if (powerUpX == x && powerUpY == y) {
                this.activePowerUp = this.powerUp;
                this.powerUp = "None";
                if (this.activePowerUp.equals("Freeze Enemies")) {
                    this.frozen = true;
                    this.timer = 600;
                }
                else if (this.activePowerUp.equals("Double Speed")) {
                    this.ball.setSpeed(4);
                    this.timer = 600;
                }
            }
        }

        if (this.map.getLives() == 0) {
            textFont(this.font, 150);
            background(139, 0, 0);
            textAlign(CENTER);
            text("YOU LOSE!", 640, 400);
            noLoop();
        }

        if (this.map.getProgress() >= this.map.getMaxProgress()) {
            if (this.level == this.maxLevel - 1) {
                textFont(this.font, 150);
                background(0, 100, 0);
                textAlign(CENTER);
                text("YOU WIN!", 640, 400);
                noLoop();

            } else {
                increaseLevel();
                String mapFile = json.getJSONArray("levels").getJSONObject(this.level).getString("outlay");
                int goal = (int) (this.json.getJSONArray("levels").getJSONObject(this.level).getFloat("goal") * 100);
                this.map = new Map(mapFile, this.maxLives, goal, this.concreteImage, this.dirtImage);
                this.activePowerUp = "No PowerUp";
                spawnPowerUp();
                spawnEnemies();
                this.ball.reset(this.map, this.enemies);
            }
        }

    }

    /**
     * Spawns a random powerup and resets the effects of the active one
    */
    public void spawnPowerUp() {
        int x_rand = 0;
        int y_rand = 0;
        int random = this.rand.nextInt(2);
        this.activePowerUp = "No PowerUp";
        while (!map.getName(x_rand, y_rand).equals("DIRT")) {
            x_rand = rand.nextInt(62) + 1;
            y_rand = rand.nextInt(30) + 1;
        }

        this.powerUpPoint = new MyPoint(x_rand, y_rand);

        if (random == 0) {
            this.powerUp = "Freeze Enemies";
        }
        else {
            this.powerUp = "Double Speed";
        }

        this.frozen = false;
        this.ball.setSpeed(2);
    }


    /**
     * Spawns enemies upon reading a new map file
    */
    public void spawnEnemies() {
        this.timer = 600;
        this.powerUp = "None";
        this.frozen = false;
        this.activePowerUp = "No PowerUp";

        this.enemies = new ArrayList<Enemy>();
        JSONArray enemiesArray = this.json.getJSONArray("levels").getJSONObject(this.level).getJSONArray("enemies");
        for (int count = 0; count < enemiesArray.size(); count++) {
            String spawn = enemiesArray.getJSONObject(count).getString("spawn");
            int type = enemiesArray.getJSONObject(count).getInt("type");

            int x = 0;
            int y = 0;

            if (spawn.equals("random")) {
                while (map.getName(x, y).equals("CONCRETE")) {
                    y = rand.nextInt(30) + 1;
                    x = rand.nextInt(62) + 1;
                }
            }
            else {
                x = Integer.parseInt(spawn.split(",")[0]);
                y = Integer.parseInt(spawn.split(",")[1]);
            }

            if (type == 0) {
                this.enemies.add(new Enemy(this.wormImage, this.dirtImage, x * 20, y * 20, rand.nextInt(4) + 4, this.map, type));
            }
            else {
                this.enemies.add(new Enemy(this.beetleImage, this.dirtImage, x * 20, y * 20, rand.nextInt(4) + 4, this.map, type));
            }
        }
    }

    /**
     * Allowing arrow keys to trigger movement in direction
    */
    public void keyPressed() {
        if (key == CODED) {
            if (keyCode >= 37 && keyCode <= 40) {
                this.ball.setPressed(true);

                int x_round = Math.round((float) (this.ball.getX() / 20));
                int y_round = Math.round((float) (this.ball.getY() / 20));

                Direction[] directions = Direction.values();
                int index = keyCode - 37;

                if (directions[index] != this.ball.getDir()) {

                    this.ball.setX(x_round * 20);
                    this.ball.setY(y_round * 20);
                    this.ball.setDir(directions[index]);
                }
            }
        }
    }

    /**
     * Sets pressed state to false when arrow key is released
    */
    public void keyReleased() {
        if (key == CODED) {
            if (keyCode >= 37 && keyCode <= 40) {
                this.ball.setPressed(false);
            }
        }
    }

    /**
     * Increases the level by 1
    */
    public void increaseLevel() {
        this.level += 1;
    }

    /**
     * Returns the map for testing
     * @return map of current level
    */
    public Map getMap() {
        return this.map;
    }

    /**
     * Returns the ball for testing
     * @return ball object
    */
    public Ball getBall() {
        return this.ball;
    }

    /**
     * Returns the powerup point for testing
     * @return current powerup coordinate
    */
    public MyPoint getPowerUpPoint() {
        return this.powerUpPoint;
    }


    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
}
