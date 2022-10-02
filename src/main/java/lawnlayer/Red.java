package lawnlayer;

import processing.core.PImage;

/**
 * Red tile of Path
*/
public class Red extends Tile implements Colour {

    /**
     * Constructor of red
     * @param red image of red
    */
    public Red(PImage red) {
        super(red);
        this.name = "RED";
    }

}