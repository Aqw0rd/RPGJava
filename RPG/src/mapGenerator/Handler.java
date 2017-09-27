package mapGenerator;

import Framework.GameObject;

import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {
    public LinkedList<GameObject> object = new LinkedList<GameObject>();
    private GameObject tempObject;

    public void tick(double gametick) {
        for (int i = 0; i < this.object.size(); i++) {
            this.tempObject = ((GameObject) this.object.get(i));

            this.tempObject.tick(this.object, gametick);
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

    public GameObject getObject(ObjectId id) {
        for (int i = 0; i < this.object.size(); i++) {
            if(this.object.get(i).getId() == id)
                return this.object.get(i);
        }
        return null;
    }

    public void removeObject(GameObject object) {
        this.object.remove(object);
    }
}
