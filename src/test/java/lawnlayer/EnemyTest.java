package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnemyTest extends App {
    private PImage testImage;
    private Map map;

    @Test
    //constructing enemy object
    public void enemyConstructorTest() {
        Enemy enemy = new Enemy(testImage, testImage, 0, 0, 7, map, 0);
        assertNotNull(enemy);
    }

    @Test
    //checking enemy collision while moving up left
    public void enemyMoveUpLeftTest() {
        map = new Map("level2.txt", 3, 80, testImage, testImage);
        Enemy e1 = new Enemy(testImage, testImage, 40, 40, 4, map, 1);
        for (int count = 0; count < 20; count++) {
            e1.tick();
        }
        Enemy e2 = new Enemy(testImage, testImage, 80, 40, 4, map, 1);
        for (int count = 0; count < 20; count++) {
            e2.tick();
        }
        Enemy e3 = new Enemy(testImage, testImage, 40, 80, 4, map, 1);
        for (int count = 0; count < 20; count++) {
            e3.tick();
        }
        Enemy e4 = new Enemy(testImage, testImage, 740, 360, 4, map, 1);
        for (int count = 0; count < 20; count++) {
            e4.tick();
        }
    }

    @Test
    //checking enemy collision while moving up right
    public void enemyMoveUpRightTest() {
        map = new Map("level2.txt", 3, 80, testImage, testImage);
        Enemy e1 = new Enemy(testImage, testImage, 1220, 40, 5, map, 1);
        for (int count = 0; count < 20; count++) {
            e1.tick();
        }
        Enemy e2 = new Enemy(testImage, testImage, 1200, 40, 5, map, 1);
        for (int count = 0; count < 20; count++) {
            e2.tick();
        }
        Enemy e3 = new Enemy(testImage, testImage, 1240, 80, 5, map, 1);
        for (int count = 0; count < 20; count++) {
            e3.tick();
        }
        Enemy e4 = new Enemy(testImage, testImage, 540, 360, 5, map, 1);
        for (int count = 0; count < 20; count++) {
            e4.tick();
        }
    }

    @Test
    //checking enemy collision while moving down right
    public void enemyMoveDownRightTest() {
        map = new Map("level2.txt", 3, 80, testImage, testImage);
        Enemy e1 = new Enemy(testImage, testImage, 1240, 600, 6, map, 1);
        for (int count = 0; count < 20; count++) {
            e1.tick();
        }
        Enemy e2 = new Enemy(testImage, testImage, 1240, 580, 6, map, 1);
        for (int count = 0; count < 20; count++) {
            e2.tick();
        }
        Enemy e3 = new Enemy(testImage, testImage, 1180, 600, 6, map, 1);
        for (int count = 0; count < 20; count++) {
            e3.tick();
        }
        Enemy e4 = new Enemy(testImage, testImage, 540, 240, 6, map, 1);
        for (int count = 0; count < 20; count++) {
            e4.tick();
        }
    }

    @Test
    //checking enemy collision while moving down left
    public void enemyMoveDownLeftTest() {
        map = new Map("level2.txt", 3, 80, testImage, testImage);
        Enemy e1 = new Enemy(testImage, testImage, 40, 580, 7, map, 1);
        for (int count = 0; count < 20; count++) {
            e1.tick();
        }
        Enemy e2 = new Enemy(testImage, testImage, 40, 560, 7, map, 1);
        for (int count = 0; count < 20; count++) {
            e2.tick();
        }
        Enemy e3 = new Enemy(testImage, testImage, 60, 580, 7, map, 1);
        for (int count = 0; count < 20; count++) {
            e3.tick();
        }
        Enemy e4 = new Enemy(testImage, testImage, 740, 240, 7, map, 1);
        for (int count = 0; count < 20; count++) {
            e4.tick();
        }
    }

}
