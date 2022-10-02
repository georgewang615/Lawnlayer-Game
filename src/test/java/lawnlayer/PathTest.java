package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class PathTest extends App {

    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Map map;
    private PImage testImage;

    @Test
    //constructing path object
    public void pathConstructorTest() {
        Path path = new Path(enemies, testImage, testImage, map);
        assertNotNull(path);
    }

    @Test
    //testing paths of width 1
    public void thinTest() {
        map = new Map("level1.txt", 3, 80, testImage, testImage);
        Path p1 = new Path(enemies, testImage, testImage, map);
        for (int y_count = 1; y_count < 31; y_count++) {
            p1.add(new MyPoint(1, y_count));
        }
        assert(p1.findPoint(1, 1, 30, 1) != null);

        map = new Map("level1.txt", 3, 80, testImage, testImage);
        Path p2 = new Path(enemies, testImage, testImage, map);
        for (int y_count = 1; y_count < 31; y_count++) {
            p2.add(new MyPoint(62, y_count));
        }
        assert(p2.findPoint(62, 62, 30, 1) != null);

        map = new Map("level1.txt", 3, 80, testImage, testImage);
        Path p3 = new Path(enemies, testImage, testImage, map);
        for (int x_count = 1; x_count < 63; x_count++) {
            p3.add(new MyPoint(x_count, 1));
        }
        assert(p3.findPoint(62, 1, 1, 1) != null);

        map = new Map("level1.txt", 3, 80, testImage, testImage);
        Path p4 = new Path(enemies, testImage, testImage, map);
        for (int x_count = 1; x_count < 63; x_count++) {
            p4.add(new MyPoint(x_count, 30));
        }
        assert(p4.findPoint(62, 1, 30, 30) != null);
    }

    @Test
    //testing normal conditions
    public void normalTest() {
        map = new Map("level1.txt", 3, 80, testImage, testImage);
        Path p1 = new Path(enemies, testImage, testImage, map);
        p1.add(new MyPoint(1, 2));
        p1.add(new MyPoint(2, 2));
        p1.add(new MyPoint(2, 1));
        assert(p1.findPoint(2, 1, 2, 1) != null);

        Path p2 = new Path(enemies, testImage, testImage, map);
        p2.add(new MyPoint(62, 2));
        p2.add(new MyPoint(61, 2));
        p2.add(new MyPoint(61, 1));
        assert(p2.findPoint(62, 61, 2, 1) != null);

        Path p3 = new Path(enemies, testImage, testImage, map);
        p3.add(new MyPoint(61, 29));
        p3.add(new MyPoint(61, 30));
        p3.add(new MyPoint(62, 30));
        assert(p3.findPoint(62, 61, 30, 29) != null);

        map = new Map("level1.txt", 3, 80, testImage, testImage);
        Path p4 = new Path(enemies, testImage, testImage, map);
        p4.add(new MyPoint(2, 30));
        p4.add(new MyPoint(2, 29));
        p4.add(new MyPoint(1, 29));
        assert(p4.findPoint(2, 1, 30, 20) != null);
    }

    @Test
    //testing collision with enemy
    public void collisionTest() {
        map = new Map("level1.txt", 3, 80, testImage, testImage);
        enemies.add(new Enemy(testImage, testImage, 40, 60, 4, map, 0));
        Path p1 = new Path(enemies, testImage, testImage, map);
        p1.add(new MyPoint(1, 2));
        p1.add(new MyPoint(2, 2));
        p1.add(new MyPoint(2, 1));
        assert(p1.handleCollision() == false);

        while (p1.handleCollision() == false) {
            p1.handleCollision();
        }
    }
}
