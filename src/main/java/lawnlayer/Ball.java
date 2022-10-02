package lawnlayer;

import processing.core.PImage;
import processing.core.PApplet;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Random;

/**
 * Ball class for the one ball object during the entire game
*/
public class Ball {

    private int speed;
    private int x;
    private int y;
    private PImage ballImage;
    private PImage grassImage;
    private PImage greenImage;
    private PImage redImage;
    private PImage dirtImage;
    private Map map;
    private ArrayList<Enemy> enemies;
    private ArrayList<MyPoint> fullMap;
    private Path path;
    private boolean pressed;
    private Direction ballDir;

    /**
     * Constructor of ball
     * @param ballImage image of ball
     * @param greenImage image of green tile
     * @param redImage image of red tile
     * @param dirtImage image of dirt tile
     * @param map map for each level
     * @param enemies enemies for each level
    */
    public Ball(PImage ballImage, PImage grassImage, PImage greenImage, PImage redImage, PImage dirtImage, Map map, ArrayList<Enemy> enemies) {
        this.redImage = redImage;
        this.ballImage = ballImage;
        this.grassImage = grassImage;
        this.greenImage = greenImage;
        this.dirtImage = dirtImage;
        this.fullMap = new ArrayList<>();

        for (int y_count = 0; y_count < 32; y_count++) {
            for (int x_count = 0; x_count < 64; x_count++) {
                fullMap.add(new MyPoint(x_count, y_count));
            }
        }
        reset(map, enemies);
    }

    /**
     * Returns adjacent Returns up to 8 valid (dirt and not seen before) neighbours next to a given point
     * @param current current point being examined
     * @param seen points that have been examined already
     * @param fringe points waiting to be examined
     * @return arraylist of all neighbours of current point
    */
    public ArrayList<MyPoint> getNeighbours(MyPoint current, ArrayList<MyPoint> seen, ArrayList<MyPoint> fringe) {
        ArrayList<MyPoint> neighbours = new ArrayList<>();
        ArrayList<MyPoint> validNeighbours = new ArrayList<>();

        int x = current.getX();
        int y = current.getY();

        neighbours.add(new MyPoint(x - 1, y - 1));
        neighbours.add(new MyPoint(x, y - 1));
        neighbours.add(new MyPoint(x + 1, y - 1));
        neighbours.add(new MyPoint(x - 1, y));
        neighbours.add(new MyPoint(x + 1, y));
        neighbours.add(new MyPoint(x - 1, y + 1));
        neighbours.add(new MyPoint(x, y + 1));
        neighbours.add(new MyPoint(x + 1, y + 1));

        for (MyPoint p : neighbours) {
            if (p.getX() >= 0 && p.getX() <= 63 && p.getY() >= 0 && p.getY() <= 31 && !p.existsIn(seen) && !p.existsIn(fringe)) {
                validNeighbours.add(p);
            } 
        }
        return validNeighbours;
    }

    /**
     * Fills in grass shape upon completing a path. Uses breadth-first search by selecting a random point and repeatedly finding neighbours
    */
    public void fill() {
        int x_min = this.path.getPoints().get(0).getX();
        int x_max = x_min;
        int y_min = this.path.getPoints().get(0).getY();
        int y_max = y_min;

        for (MyPoint p : this.path.getPoints()) {
            if (p.getX() < x_min) {
                x_min = p.getX();
            }
            if (p.getX() > x_max) {
                x_max = p.getX();
            }
            if (p.getY() < y_min) {
                y_min = p.getY();
            }
            if (p.getY() > y_max) {
                y_max = p.getY();
            }
            this.map.setTile(p.getX(), p.getY(), new Grass(this.grassImage));
            this.map.increaseProgress();
        }

        Path pathCopy = this.path;
        this.path = new Path(this.enemies, this.redImage, this.dirtImage, this.map); 
        MyPoint validPoint = pathCopy.findPoint(x_max, x_min, y_max, y_min);

        if (validPoint == null) {
            return;
        }

        int pX = validPoint.getX();
        int pY = validPoint.getY();

        ArrayList<MyPoint> fringe = new ArrayList<>();
        ArrayList<MyPoint> seen = new ArrayList<>();
        ArrayList<MyPoint> valid = new ArrayList<>();

        fringe.add(new MyPoint(pX, pY));

        while (fringe.size() > 0) {
            MyPoint current = fringe.remove(0);
            int x = current.getX();
            int y = current.getY();
            seen.add(current);

            if (this.map.getTile(x, y) instanceof Collidable || this.map.getName(x, y).equals("GREEN")) {
                continue;
            } else {
                fringe.addAll(getNeighbours(current, seen, fringe));
                valid.add(current);
            }
        }
        if (!MyPoint.includes(valid, enemies)) {
            this.map.fillTiles(valid, this.grassImage);
        }

        else {
            ArrayList<MyPoint> otherValid = new ArrayList<>();
            for (MyPoint p : fullMap) {
                if (!p.existsIn(valid) && !(this.map.getTile(p.getX(), p.getY()) instanceof Collidable)) {
                    otherValid.add(p);            
                }
            }
            if (!MyPoint.includes(otherValid, enemies)) {
                this.map.fillTiles(otherValid, this.grassImage);
            }    
        } 
    }

    /**
     * Changes the ball's location depending on the direction while creating its path. Also checks if self-intersection with path occurs
    */
    public void move() {
        float x_val = (float) this.x / 20;
        float y_val = (float) this.y / 20;
        int x_round = Math.round(x_val);
        int y_round = Math.round(y_val);
        float x_diff = x_round - x_val;
        float y_diff = y_round - y_val;

        switch (this.ballDir) {
            case LEFT:
                if (this.x > 0) {
                    if (this.x < 1240) {
                        if (!(this.map.getTile(x_round, y_round) instanceof Collidable) && x_diff > 0) {
                            drawGreen(x_round, y_round);
                            if (this.map.getName(x_round - 1, y_round).equals("GREEN")) {
                                this.loseLife();
                                break;
                            }
                        }
                    }
                    this.x -= speed;
                }
                break;

            case UP:
                if (this.y > 0) {
                    if (this.y < 600){
                        if (!(this.map.getTile(x_round, y_round) instanceof Collidable) && y_diff > 0) {
                            drawGreen(x_round, y_round);
                            if (this.map.getName(x_round, y_round - 1).equals("GREEN")) {
                                this.loseLife();
                                break;
                            }
                        }
                        
                    }
                    this.y -= speed;
                }
                break;
            
            case RIGHT:
                if (this.x < 1260) {
                    if (this.x > 20) {
                        if (!(this.map.getTile(x_round, y_round) instanceof Collidable) && x_diff < 0) {
                            drawGreen(x_round, y_round);
                            if (this.map.getName(x_round + 1, y_round).equals("GREEN")) {
                                this.loseLife();
                                break;
                            }
                        }
                        
                    }
                    this.x += speed;
                }
                break;

            case DOWN:
                if (this.y < 620) {
                    if (this.y > 20){
                        if (!(this.map.getTile(x_round, y_round) instanceof Collidable) && y_diff < 0) {
                            drawGreen(x_round, y_round);
                            if (this.map.getName(x_round, y_round + 1).equals("GREEN")) {
                                this.loseLife();
                                break;
                            }
                        }
                    }
                    this.y += speed;
                }
                break;
        }
    }
    


    /**
     * Handles ball logic at 60 FPS - moves the ball, fills the grass areas, checks for enemy-path collision
    */
    public void tick() {
        float x_val = (float) this.x / 20;
        float y_val = (float) this.y / 20;
        int x_round = Math.round(x_val);
        int y_round = Math.round(y_val);
        
        if (this.path.handleCollision()) {
            this.loseLife();
        }

        if (this.map.getTile(x_round, y_round) instanceof Collidable) {
            if (this.path.size() > 0) {
                fill();
            }

            if (this.pressed) {
                move();
            }

            else {
                if (this.ballDir == Direction.LEFT && x_round - x_val < 0 ||
                    this.ballDir == Direction.UP && y_round - y_val < 0 ||
                    this.ballDir == Direction.RIGHT && x_round - x_val > 0 ||
                    this.ballDir == Direction.DOWN && y_round - y_val > 0) 
                {
                    move();
                }
            }
        }
        else {
            move();
        }
    }

    /**
     * Returns x coordinate of ball
     * @return x coordinate of ball
    */
    public int getX() {
        return this.x;
    }

    /**
     * Returns y coordinate of ball
     * @return y coordinate of ball
    */
    public int getY() {
        return this.y;
    }

    /**
     * Sets x coordinate of ball
     * @param x x coordinate of ball
    */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets y coordinate of ball
     * @param y y coordinate of ball
    */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets pressed state of ball
     * @param state pressed state of ball
    */
    public void setPressed(boolean state) {
        this.pressed = state;
    }

    /**
     * Gets direction that the ball is moving towards
     * @return direction of ball
    */
    public Direction getDir() {
        return this.ballDir;
    }

    /**
     * Sets direction that the ball is moving towards
     * @param dir direction of ball
    */
    public void setDir(Direction dir) {
        this.ballDir = dir;
    }

    /**
     * Handles life lost event and resets ball path
    */
    public void loseLife() {
        this.map.loseLife();
        this.x = 0;
        this.y = 0;
        this.pressed = false;

        for (MyPoint p : this.path.getPoints()) {
            map.setTile(p.getX(), p.getY(), new Dirt(this.dirtImage));
        }
        this.path = new Path(this.enemies, this.redImage, this.dirtImage, this.map);
    }

    /**
     * Updates path of ball during movement
     * @param x x coordinate of ball
     * @param y y coordinate of ball
    */
    public void drawGreen(int x, int y) {
        MyPoint newPoint = new MyPoint(x, y);
        if (!newPoint.existsIn(this.path.getPoints())) {
            this.path.add(newPoint);
            this.map.setTile(x, y, new Green(this.greenImage));
        }
        
    }

    /**
     * Resets states at the beginning of each map
     * @param map map for level
     * @param enemies enemies for level
    */
    public void reset(Map map, ArrayList<Enemy> enemies) {
        this.speed = 2;
        this.x = 0;
        this.y = 0;
        this.pressed = false;
        this.map = map;
        this.enemies = enemies;
        this.path = new Path(this.enemies, this.redImage, this.dirtImage, this.map);
    }

    /**
     * Sets speed of ball
     * @param speed speed of ball
    */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Draws ball on screen
     * @param app main papplet app
    */
    public void draw(PApplet app) {
        app.image(this.ballImage, this.x, this.y + 80);
    }

}