package lawnlayer;

import processing.core.PImage;

/**
 * Dirt tile
*/
public class Dirt extends Tile {

    /**
     * Constructor of dirt
     * @param dirt image of dirt
    */
    public Dirt(PImage dirt) {
        super(dirt);
        this.name = "DIRT";
    }

}