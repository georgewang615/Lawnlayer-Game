package lawnlayer;

import java.util.ArrayList;
import processing.core.PImage;
import processing.core.PApplet;
import java.util.Scanner;
import java.io.*;
import java.lang.*;

/**
 * Map class for the each map in the config file
*/
public class Map {
    
    private Tile[][] map;
    private int lives;
    private int total;
    private int progress;
    private int maxProgress;

    /**
     * Constructor of map
     * @param mapFile name of map file
     * @param lives number of player lives
     * @param goal grass percentage needed to complete level
     * @param concreteImage image of concrete tile
     * @param dirtImage image of dirt tile
    */
    public Map(String mapFile, int lives, int goal, PImage concreteImage, PImage dirtImage) {
        this.map = new Tile[32][64];
        this.lives = 3;
        int countTotal = 0;
        try {
            File f = new File(mapFile);
            Scanner readme = new Scanner(f);
            for (int count1 = 0; count1 < 32; count1++) {
                String line = readme.nextLine();
                for (int count2 = 0; count2 < 64; count2++) {
                    if (line.charAt(count2) == 'X') {
                        this.setTile(count2, count1, new Concrete(concreteImage));
                    }
                    else if (line.charAt(count2) == ' ') {
                        this.setTile(count2, count1, new Dirt(dirtImage));
                        countTotal += 1;
                    }
                    else {
                        System.out.println("Error with input file");
                        System.exit(0);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        this.maxProgress = goal;
        this.total = countTotal;
    }

    /**
     * Returns name of tile on given coordinate on map
     * @param x x coordinate of map tile
     * @param y y coordinate of map tile
     * @return name of the tile at coordinate
    */
    public String getName(int x, int y) {
        return this.map[y][x].name;
    }

    /**
     * Returns tile on given coordinate on map
     * @param x x coordinate of map tile
     * @param y y coordinate of map tile
     * @return tile at the coordinate
    */
    public Tile getTile(int x, int y) {
        return this.map[y][x];
    }

    /**
     * Sets tile on given coordinate on map
     * @param x x coordinate of map tile
     * @param y y coordinate of map tile
     * @param tile tile to be set on map
    */
    public void setTile(int x, int y, Tile tile) {
        this.map[y][x] = tile;
    }

    /**
     * Draws each tile on the map
     * @param app main papplet app
    */
    public void display(PApplet app) {
        for (int y_count = 0; y_count < 32; y_count++) {
            for (int x_count = 0; x_count < 64; x_count++) {
                app.image(this.getTile(x_count, y_count).getImage(), x_count * 20, y_count * 20 + 80);
            }
        }
    }

    /**
     * Sets valid tiles as grass, method called when path is completed
     * @param valid valid tiles to fill
     * @param grassImage image of grass
    */
    public void fillTiles(ArrayList<MyPoint> valid, PImage grassImage) {
        for (MyPoint p : valid) {
            setTile(p.getX(), p.getY(), new Grass(grassImage));
            this.progress += 1;
        }
    }

    /**
     * Returns player's current lives
     * @return lives of the current level
    */
    public int getLives() {
        return this.lives;
    }

    /**
     * Decreases current lives by 1
    */
    public void loseLife() {
        this.lives -= 1;
    }

    /**
     * Increases grass progress by 1 percent
    */
    public void increaseProgress() {
        this.progress += 1;
    }

    /**
     * Returns current grass progress 
     * @return current progress of player
    */
    public int getProgress() {
        return Math.round((float) (this.progress * 100 / this.total));
    }

    /**
     * Returns required grass progress to complete map
     * @return max progress of level
    */
    public int getMaxProgress() {
        return this.maxProgress;
    }
}