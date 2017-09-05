package Object;

import Framework.GameObject;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import mapGenerator.ObjectId;

public class Enemy
        extends GameObject {
    public Enemy(float x, float y, ObjectId id, String imgPath) {
        super(x, y, id, imgPath);
    }

    public void render(Graphics g) {
    }

    public void tick(LinkedList<GameObject> object, double gametick) {
    }

    public Rectangle getBounds() {
        return null;
    }
}
