package lawnlayer;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest extends App {

    @Test
    //constructing app object
    public void appConstructorTest() {
        App app = new App();
        PApplet.runSketch(new String[] {"App"}, app);
        assertNotNull(app);
    }

    @Test
    //calling setup, draw, and key pressed
    public void setupTest() {
        App app = new App();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        app.draw();
        app.keyPressed();
        app.keyReleased();
        assertNotNull(app);
    }

    @Test
    //testing losing the game when life is 0
    public void livesTest() {
        App app = new App();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        app.draw();
        
        while (app.getMap().getLives() > 0) {
            app.getMap().loseLife();
        }
    }

    @Test
    //testing progress and switching maps
    public void progressTest() {
        App app = new App();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        while (app.getMap().getProgress() < app.getMap().getMaxProgress()) {
            app.getMap().increaseProgress();
        }
        app.redraw();
        // app.increaseLevel();
        // app.redraw();
    }

    @Test
    //testing winning the game
    public void winTest() {
        App app = new App();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        while (app.getMap().getProgress() < app.getMap().getMaxProgress()) {
            app.getMap().increaseProgress();
        }
        app.increaseLevel();
        app.redraw();
    }




    @Test
    //testing powerups
    public void powerUpTest() {
        App app = new App();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();

        app.getBall().setDir(Direction.DOWN);
        
        app.spawnPowerUp();
        app.getPowerUpPoint().setX(0);
        app.getPowerUpPoint().setY(0);

        app.delay(1000);
        app.redraw();
    
    }



}
