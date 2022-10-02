package lawnlayer;

import processing.core.PImage;

/**
 * Concrete tile that is collidable by objects
*/
public class Concrete extends Tile implements Collidable {

    /**
     * Constructor of concrete
     * @param concrete image of concrete
    */
    public Concrete(PImage concrete) {
        super(concrete);
        this.name = "CONCRETE";
    }
    
}