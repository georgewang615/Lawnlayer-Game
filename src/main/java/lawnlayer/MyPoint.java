package lawnlayer;

import java.util.ArrayList;

/**
 * MyPoint class for points on path and powerups
*/
public class MyPoint {
    private int x;
    private int y;

    /**
     * Constructor of MyPoint
     * @param x x coordinate of point
     * @param y y coordinate of point
    */
    public MyPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Returns x coordinate of point
     * @return x coordinate of point
    */
    public int getX() {
        return this.x;
    }

    /**
     * Returns y coordinate of point
     * @return y coordinate of point
    */
    public int getY() {
        return this.y;
    }

    /**
     * Sets x coordinate of point
     * @param x coordinate to be set
    */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets y coordinate of point
     * @param y coordinate to be set
    */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Checks if two points have identical coordinates
     * @param other other point to be checked
     * @return boolean - if points have same coordinates
    */
    public boolean equals(MyPoint other) {
        return (this.x == other.getX() && this.y == other.getY());
    }

    /**
     * Checks if ArrayList of points includes any enemies
     * @param valid list of tiles to be checked
     * @param enemies list of enemies to be checked
     * @return boolean - if valid includes enemies
    */
    public static boolean includes(ArrayList<MyPoint> valid, ArrayList<Enemy> enemies) {
        for (MyPoint p : valid) {
            for (Enemy enemy : enemies){
                if (p.getX() == enemy.getX() && p.getY() == enemy.getY()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if point exists in ArrayList of points
     * @param pointList list of points to be checked
     * @return boolean - if point exists in pointlist
    */
    public boolean existsIn(ArrayList<MyPoint> pointList) {
        for (MyPoint s : pointList) {
            if (this.x == s.getX() && this.y == s.getY()) {
                return true;
            }
        }
        return false;
    }
}
