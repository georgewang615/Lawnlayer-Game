package lawnlayer;

import java.util.ArrayList;
import processing.core.PImage;
import java.lang.*;

/**
 * Path class for the each path created by ball
*/
public class Path {
    private ArrayList<MyPoint> path;
    private ArrayList<Enemy> enemies;
    private int currentIndex;
    private boolean struck;
    private int spreadCount;
    private PImage redImage;
    private PImage dirtImage;
    private Map map;

    /**
     * Constructor of path
     * @param enemies list of enemies for current map
     * @param redImage image of red tile
     * @param dirtImage image of dirt tile
     * @param map map for current level
    */
    public Path(ArrayList<Enemy> enemies, PImage redImage, PImage dirtImage, Map map) {
        this.spreadCount = 0;
        this.path = new ArrayList<MyPoint>();
        this.struck = false;
        this.enemies = enemies;
        this.redImage = redImage;
        this.dirtImage = dirtImage;
        this.map = map;
    }

    /**
     * Returns points of the path
     * @return path
    */
    public ArrayList<MyPoint> getPoints() {
        return this.path;
    }

    /**
     * Returns number of points in path
     * @return size of path
    */
    public int size() {
        return this.path.size();
    }

    /**
     * Adds a point to the path
     * @param p point to be added
    */
    public void add(MyPoint p) {
        this.path.add(p);
    }

    /**
     * Checks if path contains a point
     * @param x x coordinate to be checked
     * @param y y coordinate to be checked
     * @return index of point, if not exists, returns -1
    */
    public int exists(int x, int y) {
        for (int count = 0; count < this.path.size(); count++) {
            MyPoint p = this.path.get(count);
            if (x == p.getX() && y == p.getY()) {
                return count;
            }
        }
        return -1;
    }

    /**
     * Returns x coordinate of current red tile
     * @return x coordinate of current red tile
    */
    public int getCurrentX() {
        return this.path.get(currentIndex).getX();
    }

    /**
     * Returns y coordinate of current red tile
     * @return y coordinate of current red tile
    */
    public int getCurrentY() {
        return this.path.get(currentIndex).getY();
    }

    /**
     * Finds a point within an enclosed shape for BFS
     * @param x_max upper x bound of path
     * @param x_min lower x bound of path
     * @param y_max upper y bound of path
     * @param y_min lower y bound of path
     * @return point within the enclosed shape
    */
    public MyPoint findPoint(int x_max, int x_min, int y_max, int y_min) {
        MyPoint p = this.path.get(0);
        int x = p.getX();
        int y = p.getY();

        if (x_max == x_min) {
            for (int y_count = 0; y_count < 32; y_count++) {
                if (exists(x, y_count) != -1) {
                    if (!(this.map.getTile(x - 1, y_count) instanceof Collidable)) {
                        return new MyPoint(x - 1, y_count);
                    }
                    else if (!(this.map.getTile(x + 1, y_count) instanceof Collidable)) {
                        return new MyPoint(x + 1, y_count);
                    }
                }
            }
            return null;
        }
        if (y_max == y_min) {
            for (int x_count = 0; x_count < 64; x_count++) {
                if (exists(x_count, y) != -1) {
                    if (!(this.map.getTile(x_count, y - 1) instanceof Collidable)) {
                        return new MyPoint(x_count, y - 1);
                    }
                    else if (!(this.map.getTile(x_count, y + 1) instanceof Collidable)) {
                        return new MyPoint(x_count, y + 1);
                    }
                }
            }
            return null;
        }
        if (x < x_max) {
            for (int x1 = x + 1; x1 <= 63; x1++) {
                if (exists(x1, y) != -1 || this.map.getTile(x1, y) instanceof Collidable) {
                    if (exists(x1 - 1, y) == -1 && !(this.map.getTile(x1 - 1, y) instanceof Collidable)) {
                        return new MyPoint(x1 - 1, y);
                    }
                    else {
                        break;
                    }
                }
            }
        }
        if (x > x_min){
            for (int x2 = x - 1; x2 >= 0; x2--) {
                if (exists(x2, y) != -1 || this.map.getTile(x2, y) instanceof Collidable) {
                    if (exists(x2 + 1, y) == -1 && !(this.map.getTile(x2 + 1, y) instanceof Collidable)) {
                        return new MyPoint(x2 + 1, y);
                    }
                    else {
                        break;
                    }
                }
            }
        }
        if (y < y_max) {
            for (int y1 = y + 1; y1 <= 31; y1++) {
                if (exists(x, y1) != -1 || this.map.getTile(x, y1) instanceof Collidable) {
                    if (exists(x, y1 - 1) == -1 && !(this.map.getTile(x, y1 - 1) instanceof Collidable)) {
                        return new MyPoint(x, y1 - 1);
                    }
                }
            }
        }
        if (y > y_min) {
            for (int y2 = y - 1; y2 >= 0; y2--) {
                if (exists(x, y2) != -1 || this.map.getTile(x, y2) instanceof Collidable) {
                    if (exists(x, y2 + 1) == -1 && !(this.map.getTile(x, y2 + 1) instanceof Collidable)) {
                        return new MyPoint(x, y2 + 1);
                    }
                    else {
                        break;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Handles collision between path and enemy, as well as propagation of red path
     * @return whether the path reached the player
    */
    public boolean handleCollision() {
        if (!this.struck) {
            for (int count = 0; count < this.path.size(); count++) {
                int x1 = this.path.get(count).getX();
                int y1 = this.path.get(count).getY();
                for (Enemy enemy : enemies) {
                    int x2 = enemy.getX();
                    int y2 = enemy.getY();

                    if ((Math.abs(x2 - x1) + Math.abs(y2 - y1) == 1) ||
                        (enemy.getDir() == Direction.UP_LEFT && x2 - x1 == 1 && y2 - y1 == 1) || 
                        (enemy.getDir() == Direction.UP_RIGHT && x2 - x1 == -1 && y2 - y1 == 1) || 
                        (enemy.getDir() == Direction.DOWN_RIGHT && x2 - x1 == -1 && y2 - y1 == -1) || 
                        (enemy.getDir() == Direction.DOWN_LEFT && x2 - x1 == 1 && y2 - y1 == -1))
                        
                    {
                        this.currentIndex = count;
                        this.struck = true;
                        this.spreadCount = 0;
                    }
                }
            }
            if (this.struck) {
                this.map.setTile(getCurrentX(), getCurrentY(), new Red(this.redImage));
            }
        }
        else {
            if (this.spreadCount == 2) {
                this.spreadCount = 0;
                if (this.currentIndex >= this.path.size() - 1) {
                    this.struck = false;
                    return true;
                }
                this.currentIndex += 1;
                this.map.setTile(getCurrentX(), getCurrentY(), new Red(this.redImage));
            }
            else {
                this.spreadCount += 1;
            } 
        }
        return false;
    }
}