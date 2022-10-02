package lawnlayer;

import processing.core.PImage;
import processing.core.PApplet;

/**
 * Tile parent class
*/
public class Tile {
    protected PImage image;
    protected String name;

    /**
     * Constructor of tile
     * @param image image of tile
    */
    public Tile(PImage image) {
        this.image = image;
    }

    /**
     * Gets the image of the tile
     * @return image of the tile
    */
    public PImage getImage() {
        return this.image;
    }

    /**
     * @param app main papplet app
     * @param x x coordinate to be drawn
     * @param y y coordinate to be drawn
    */
    public void draw(PApplet app, int x, int y) {
        app.image(this.image, x * 20, y * 20 + 80);
    }
}