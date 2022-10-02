package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class BallTest extends App {

    private Ball ball;
    private PImage testImage;
    private Map map;
    private ArrayList<Enemy> enemies = new ArrayList<>();

    @Test
    //constructing ball object
    public void ballConstructorTest() {
        map = new Map("level1.txt", 3, 80, testImage, testImage);
        ball = new Ball(testImage, testImage, testImage, testImage, testImage, map, enemies);
        assertNotNull(ball);
    }

    @Test
    //basic movement in 4 directions on concrete
    public void concreteMoveTest() {
        map = new Map("level1.txt", 3, 80, testImage, testImage);
        ball = new Ball(testImage, testImage, testImage, testImage, testImage, map, enemies);

        ball.setPressed(true);

        ball.setDir(Direction.DOWN);
        assert(ball.getDir() == Direction.DOWN);
        for (int count = 0; count < 320; count++) {
            ball.tick();
        }

        ball.setDir(Direction.RIGHT);
        for (int count = 0; count < 640; count++) {
            ball.tick();
        }

        ball.setDir(Direction.UP);
        for (int count = 0; count < 320; count++) {
            ball.tick();
        }

        ball.setDir(Direction.LEFT);
        for (int count = 0; count < 640; count++) {
            ball.tick();
        }

        assert(ball.getX() == 0);
        assert(ball.getY() == 0);
    }

    @Test
    //illegal movements along the edge of the map
    public void illegalMoveTest() {
        map = new Map("level1.txt", 3, 80, testImage, testImage);
        ball = new Ball(testImage, testImage, testImage, testImage, testImage, map, enemies);
        ball.setPressed(true);
        ball.setDir(Direction.UP);
        ball.tick();
        ball.setDir(Direction.LEFT);
        ball.tick();

        ball.setX(1260);
        ball.setY(620);

        ball.setDir(Direction.DOWN);
        ball.tick();

        ball.setDir(Direction.RIGHT);
        ball.tick();

    }

    @Test
    //destroying path by hitting it
    public void destroyPathTest() {
        map = new Map("level1.txt", 3, 80, testImage, testImage);
        ball = new Ball(testImage, testImage, testImage, testImage, testImage, map, enemies);
        

        ball.setX(20);
        ball.setPressed(true);
        ball.setDir(Direction.DOWN);
        for (int count = 0; count < 60; count++) {
            ball.tick();
        }
        ball.setDir(Direction.UP);
        for (int count = 0; count < 60; count++) {
            ball.tick();
        }
        
        ball.setX(20);
        ball.setY(320);
        ball.setPressed(true);
        ball.setDir(Direction.UP);
        for (int count = 0; count < 60; count++) {
            ball.tick();
        }
        ball.setDir(Direction.DOWN);
        for (int count = 0; count < 60; count++) {
            ball.tick();
        }
        
        ball.setX(0);
        ball.setY(20);
        ball.setPressed(true);
        ball.setDir(Direction.RIGHT);
        for (int count = 0; count < 60; count++) {
            ball.tick();
        }
        ball.setDir(Direction.LEFT);
        for (int count = 0; count < 60; count++) {
            ball.tick();
        }

        ball.setX(1260);
        ball.setY(20);
        ball.setPressed(true);
        ball.setDir(Direction.LEFT);
        for (int count = 0; count < 60; count++) {
            ball.tick();
        }
        ball.setDir(Direction.RIGHT);
        for (int count = 0; count < 60; count++) {
            ball.tick();
        }

    }


    @Test
    //drawing grass areas in different conditions
    public void ballFullDrawTest() {
        map = new Map("level1.txt", 3, 80, testImage, testImage);
        enemies.add(new Enemy(testImage, testImage, 20, 20, 7, map, 0));
        ball = new Ball(testImage, testImage, testImage, testImage, testImage, map, enemies);

        ball.setPressed(true);
        ball.setDir(Direction.DOWN);
        for (int count = 0; count < 100; count++) {
            ball.tick();
        }
        ball.setDir(Direction.RIGHT);
        for (int count = 0; count < 100; count++) {
            ball.tick();
        }
        ball.setPressed(false);
        ball.setDir(Direction.UP);
        for (int count = 0; count < 100; count++) {
            ball.tick();
        }
        assert(ball.getX() == 200);
        assert(ball.getY() == 0);


        ball.setX(1260);
        ball.setY(0);
        ball.setPressed(true);
        ball.setDir(Direction.LEFT);
        for (int count = 0; count < 100; count++) {
            ball.tick();
        }
        ball.setDir(Direction.DOWN);
        for (int count = 0; count < 100; count++) {
            ball.tick();
        }
        ball.setPressed(false);
        ball.setDir(Direction.RIGHT);
        for (int count = 0; count < 100; count++) {
            ball.tick();
        }


        ball.setX(1260);
        ball.setY(620);
        ball.setPressed(true);
        ball.setDir(Direction.UP);
        for (int count = 0; count < 100; count++) {
            ball.tick();
        }
        ball.setDir(Direction.LEFT);
        for (int count = 0; count < 100; count++) {
            ball.tick();
        }
        ball.setPressed(false);
        ball.setDir(Direction.DOWN);
        for (int count = 0; count < 100; count++) {
            ball.tick();
        }

        ball.setX(0);
        ball.setY(620);
        ball.setPressed(true);
        ball.setDir(Direction.RIGHT);
        for (int count = 0; count < 100; count++) {
            ball.tick();
        }

        ball.setDir(Direction.UP);
        for (int count = 0; count < 100; count++) {
            ball.tick();
        }
        ball.setPressed(false);

        ball.setDir(Direction.LEFT);
        for (int count = 0; count < 100; count++) {
            ball.tick();
        }
    
    }


    
}
