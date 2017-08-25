package mapGenerator;

import Framework.GameObject;

import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {
    public LinkedList<GameObject> object = new LinkedList();
    private GameObject tempObject;

    public void tick() {
        for (int i = 0; i < this.object.size(); i++) {
            this.tempObject = ((GameObject) this.object.get(i));

            this.tempObject.tick(this.object);
        }
    }

    public void render(Graphics g) {
        for (int i = 0; i < this.object.size(); i++) {
            this.tempObject = ((GameObject) this.object.get(i));
            this.tempObject.render(g);
        }
    }

    public void addObject(GameObject object) {
        this.object.add(object);
    }

    public void removeObject(GameObject object) {
        this.object.remove(object);
    }
}