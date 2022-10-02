package lawnlayer;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.ArrayList;

/**
 * Enemy class for multiple enemies
*/
public class Enemy {
    private final int SPEED = 2;
    private int x;
    private int y;
    private Direction enemyDir;
    private PImage image;
    private PImage dirtImage;
    private Map map;
    private int type;

    /**
     * Constructor of enemy
     * @param image image of enemy
     * @param dirtImage image of dirt tile
     * @param x x coordinate of enemy
     * @param y y coordinate of enemy
     * @param dir direction of enemy
     * @param map map for each level
     * @param type type of enemy (0 for worm, 1 for beetle)
    */
    public Enemy(PImage image, PImage dirtImage, int x, int y, int dir, Map map, int type) {
        this.image = image;
        this.dirtImage = dirtImage;
        this.x = x;
        this.y = y;
        this.enemyDir = Direction.values()[dir];
        this.map = map;
        this.type = type;
    }
    /**
     * Handles logic for enemy at 60 FPS - moves diagonally, checks for collision and change directions accordingly, eats grass if enemy is beetle
    */
    public void tick() {
        float x_val = (float) this.x / 20;
        float y_val = (float) this.y / 20;
        int x_round = getX();
        int y_round = getY();

        switch (this.enemyDir) {
            case UP_LEFT:
                if (x_val == x_round && y_val == y_round) {
                    if (checkCollision(x_round - 1, y_round - 1)) {
                        if (checkCollision(x_round - 1, y_round) && checkCollision(x_round, y_round - 1)) {
                            eatGrass(x_round - 1, y_round - 1);
                            this.enemyDir = Direction.DOWN_RIGHT;

                        } else if (checkCollision(x_round - 1, y_round)) {
                            eatGrass(x_round - 1, y_round);
                            this.enemyDir = Direction.UP_RIGHT;

                        } else if (checkCollision(x_round, y_round - 1)) {
                            eatGrass(x_round, y_round - 1);
                            this.enemyDir = Direction.DOWN_LEFT;

                        } else {
                            eatGrass(x_round - 1, y_round - 1);
                            this.enemyDir = Direction.DOWN_RIGHT;
                        }
                        return;
                    }
                }
                this.x -= SPEED;
                this.y -= SPEED;
                break;

            case UP_RIGHT:
                if (x_val == x_round && y_val == y_round) {
                    if (checkCollision(x_round + 1, y_round - 1)) {
                        if (checkCollision(x_round + 1, y_round) && checkCollision(x_round, y_round - 1)) {
                            eatGrass(x_round + 1, y_round - 1);
                            this.enemyDir = Direction.DOWN_LEFT;

                        } else if (checkCollision(x_round + 1, y_round)) {
                            eatGrass(x_round + 1, y_round);
                            this.enemyDir = Direction.UP_LEFT;

                        } else if (checkCollision(x_round, y_round - 1)) {
                            eatGrass(x_round, y_round - 1);
                            this.enemyDir = Direction.DOWN_RIGHT;

                        } else {
                            eatGrass(x_round + 1, y_round - 1);
                            this.enemyDir = Direction.DOWN_LEFT;
                        }
                        return;
                    }
                }
                this.x += SPEED;
                this.y -= SPEED;
                break;
                
            case DOWN_RIGHT:
                if (x_val == x_round && y_val == y_round) {
                    if (checkCollision(x_round + 1, y_round + 1)) {
                        if (checkCollision(x_round + 1, y_round) && checkCollision(x_round, y_round + 1)) {
                            eatGrass(x_round + 1, y_round + 1);
                            this.enemyDir = Direction.UP_LEFT;

                        } else if (checkCollision(x_round + 1, y_round)) {
                            eatGrass(x_round + 1, y_round);
                            this.enemyDir = Direction.DOWN_LEFT;

                        } else if (checkCollision(x_round, y_round + 1)) {
                            eatGrass(x_round, y_round + 1);
                            this.enemyDir = Direction.UP_RIGHT;
                            
                        } else {
                            eatGrass(x_round + 1, y_round + 1);
                            this.enemyDir = Direction.UP_LEFT;
                        }
                        return;
                    }
                }
                this.x += SPEED;
                this.y += SPEED;
                break;
    
            case DOWN_LEFT:
                if (x_val == x_round && y_val == y_round) {
                    if (checkCollision(x_round - 1, y_round + 1)) {
                        if (checkCollision(x_round - 1, y_round) && checkCollision(x_round, y_round + 1)) {
                            eatGrass(x_round - 1, y_round + 1);
                            this.enemyDir = Direction.UP_RIGHT;

                        } else if (checkCollision(x_round - 1, y_round)) {
                            eatGrass(x_round - 1, y_round);
                            this.enemyDir = Direction.DOWN_RIGHT;

                        } else if (checkCollision(x_round, y_round + 1)) {
                            eatGrass(x_round, y_round + 1);
                            this.enemyDir = Direction.UP_LEFT;

                        } else {
                            eatGrass(x_round - 1, y_round + 1);
                            this.enemyDir = Direction.UP_RIGHT;
                        }
                        return;
                    }
                }
                this.x -= SPEED;
                this.y += SPEED;
                break;
        }
    }

    /**
     * Returns x coordinate (in tiles) of enemy
     * @return rounded x coordinate of enemy
    */
    public int getX() {
        return Math.round((float) this.x / 20);
    }

    /**
     * Returns y coordinate (in tiles) of enemy
     * @return rounded y coordinate of enemy
    */
    public int getY() {
        return Math.round((float) this.y / 20);
    }

    /**
     * Returns direction of enemy
     * @return direction that enemy is moving towards
    */
    public Direction getDir() {
        return this.enemyDir;
    }

    /**
     * Checks for collision with concrete, grass, green or red from Path
     * @param x x coordinate of enemy
     * @param y y coordinate of enemy
     * @return boolean - whether a collision has happened
    */
    public boolean checkCollision(int x, int y) {
        if (this.map.getTile(x, y) instanceof Collidable || this.map.getTile(x, y) instanceof Colour) {
            return true;
        }
        return false;
    }

    /**
     * Changes grass to dirt if beetle hits grass
     * @param x x coordinate of enemy
     * @param y y coordinate of enemy
    */
    public void eatGrass(int x, int y) {
        if (this.type == 1) {
            if (this.map.getName(x, y).equals("GRASS")) {
                this.map.setTile(x, y, new Dirt(this.dirtImage));
            }
        }
    }

    /**
     * Draws enemy on screen - with minor adjustments depending on size of enemy image
     * @param app main papplet app
    */
    public void draw(PApplet app) {
        if (this.type == 0) {
            app.image(this.image, this.x, this.y + 80);
        }
        else {
            app.image(this.image, this.x - 5, this.y + 77);
        }
    }
}