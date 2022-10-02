package lawnlayer;

import processing.core.PImage;

/**
 * Grass tile that is collidable by objects
*/
public class Grass extends Tile implements Collidable {

    /**
     * Constructor of grass
     * @param grass image of grass
    */
    public Grass(PImage grass) {
        super(grass);
        this.name = "GRASS";
    }

}