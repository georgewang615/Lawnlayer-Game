package lawnlayer;

import processing.core.PImage;

/**
 * Green tile of Path
*/
public class Green extends Tile implements Colour {

    /**
     * Constructor of green
     * @param green image of green
    */
    public Green(PImage green) {
        super(green);
        this.name = "GREEN";
    }

}